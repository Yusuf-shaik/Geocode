package com.example.geocode;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//this class handles a user adding a new record to the database
public class AddRecordActivity extends AppCompatActivity {

    //declare global variables for edittext inputs
    private EditText  latitudeInput, longitudeInput;//, addressInput
    MyDatabase db;
    String address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //assign layout file to this class
        setContentView(R.layout.activity_add_record);

        //new instance of my db class
        db = new MyDatabase(this);

        //get the coordinate values that the user entered
        latitudeInput = findViewById(R.id.edit_text_latitude_add);
        longitudeInput = findViewById(R.id.edit_text_longitude_add);

        //handle user clicking on the save button. Save the information, and navigate to the main activity using an intent
        FloatingActionButton saveButton = findViewById(R.id.button_insert_add);
        saveButton.setOnClickListener(view -> {
            boolean saved = saveAddress();
            if (saved) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }

        });


    }

    //determine address based on coordinates, and enter information into the database
    private boolean saveAddress() {
        boolean status = false;
        //error handling for empty input
        if (latitudeInput.getText().toString().trim().isEmpty() || longitudeInput.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show();
        } else {

            //initialize geocoder object
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            //convert coordinates to double, because the geocoder class treats them as the double data type
            Double latitude = Double.parseDouble(latitudeInput.getText().toString().trim());
            Double longitude = Double.parseDouble(longitudeInput.getText().toString().trim());

            //handle coordinates out of range
            if(latRange(latitude) && longRange(longitude)){

                //get address based on coordinates
                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    address = addresses.get(0).getAddressLine(0);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                //save all information into the database and notify the user
                boolean success = db.insertAddress(address, latitudeInput.getText().toString().trim(), longitudeInput.getText().toString().trim());
                if (success) {
                    Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                    status = true;
                }

            }
            else {
                Toast.makeText(this, "Coordinates Out of Range", Toast.LENGTH_SHORT).show();
            }



        }
        return status;
    }

    //return true if latitude input is within the allowed values for latitude. Return false otherwise
    private boolean latRange(double lat){
        return lat >= -90 && lat <=90;
    }

    //return true of longitude input is within the allowed values for longitude. Return false otherwise
    private boolean longRange(double longit){
        return longit >= -180 && longit <= 180;
    }

}
