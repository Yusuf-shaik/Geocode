package com.example.geocode;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class UpdateRecordActivity extends AppCompatActivity {

    //initialize variables
    MyDatabase db;

    String address;
    String latitude;
    String longitude;
    int id;

    private EditText latitudeInput, longitudeInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_record);

        db = new MyDatabase(this);

        //unpackage intent to get values that were passed when the user clicked on an element.
        Intent intent = getIntent();
        this.address = intent.getStringExtra("address");
        this.latitude = intent.getStringExtra("latitude");
        this.longitude = intent.getStringExtra("longitude");
        this.id = intent.getIntExtra("id", 0);

        //edit text input boxes for the 2 inputs on this page
        latitudeInput = findViewById(R.id.edit_text_latitude_update);
        longitudeInput = findViewById(R.id.edit_text_longitude_update);

        //set the coordinates that were previously in the database so the user has the ability to edit the values. This is very helpful since the coordinate information can sometimes be extremely long
        latitudeInput.setText(this.latitude);
        longitudeInput.setText(this.longitude);

        //handles a user clicking the save button. Update record, and navigate to main page
        FloatingActionButton saveButton = findViewById(R.id.button_insert_update);
        saveButton.setOnClickListener(view -> {
            boolean saved = update();
            if (saved) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }

        });

        //handles a user clicking the delete button
        FloatingActionButton deleteButton = findViewById(R.id.button_delete_record_update);
        deleteButton.setOnClickListener(view -> {

            //build confirmation dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Delete Record");
            builder.setMessage("Are you sure you want to delete this record?");
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        //delete record
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean deleted = deleteRecord();
                            if (deleted) {
                                //navigate to main page
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                            }
                        }
                    });
            //do nothing if user clicks cancel
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            //display confirmation dialog
            AlertDialog dialog = builder.create();
            dialog.show();



        });



    }

    //delete record and notify user if delete is successful
    private boolean deleteRecord() {

        boolean status = false;

        long success = db.deleteRecord(this.id);
        if (success > 0) {
            Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
            status = true;
        }
        return status;

    }

    //determine new address and update table
    private boolean update() {

        //handles invalid input
        boolean status = false;
        if (latitudeInput.getText().toString().trim().isEmpty() || longitudeInput.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show();
        } else {
            //create new instance of the geocoder class
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            //conver to double
            Double latitude = Double.parseDouble(latitudeInput.getText().toString().trim());
            Double longitude = Double.parseDouble(longitudeInput.getText().toString().trim());

            //convert coordinates to address
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                address = addresses.get(0).getAddressLine(0);

            } catch (IOException e) {
                e.printStackTrace();
            }


            //update information and notify user when complete
            boolean success = db.updateAddress(this.id, address, latitudeInput.getText().toString().trim(), longitudeInput.getText().toString().trim());
            if (success) {
                Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show();
                status = true;
            }

        }
        return status;
    }
}
