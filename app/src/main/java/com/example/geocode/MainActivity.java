package com.example.geocode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.geocode.MyDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //initialization
    ListView myListView;
    MyDatabase db;
    ArrayList<GeoRecord> arr = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new MyDatabase(this);

        //list view variable that will contain list of records
        myListView = (ListView) findViewById(R.id.list_view_records);

        //handle user attempting to search items
        SearchView search = findViewById(R.id.search_box);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            //as the user types, search the database and show the information to the user
            public boolean onQueryTextChange(String newText) {
                displaySearchedList(newText);
                GeoListAdapter adapter = new GeoListAdapter(MainActivity.this, R.layout.adapter_view_layout, arr);
                myListView.setAdapter(adapter);
                return false;
            }
        });
        //display list of addresses and coordinates on the main screen. This runs if the user does not search anything. It is also the main screen that appears when the user opens the app
        displayList();




        //new instance of the adapter class which will display each record within the listview
        GeoListAdapter adapter = new GeoListAdapter(this, R.layout.adapter_view_layout, arr);
        myListView.setAdapter(adapter);

        //handles clicking on each element within the list. This will navigate the user to the update record view, and pass all the information to the next view as well
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String clickedAddress = arr.get(i).getAddress();
                String clickedLatitude = arr.get(i).getLatitude();
                String clickedLongitude = arr.get(i).getLongitude();
                int clickedId = arr.get(i).getId();

                Intent in = new Intent(MainActivity.this, UpdateRecordActivity.class);
                in.putExtra("address", clickedAddress);
                in.putExtra("latitude", clickedLatitude);
                in.putExtra("longitude", clickedLongitude);
                in.putExtra("id", clickedId);
                startActivity(in);

            }
        });



        FloatingActionButton fab = findViewById(R.id.button_add_record);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddRecordActivity.class);
                startActivity(i);
            }
        });




    }

    private  void displaySearchedList(String searched){
        System.out.println("im getting called");
        arr = new ArrayList<>();
        Cursor cursor = null;
        cursor = db.search(searched);

        if (cursor.getCount() != 0) {

            while (cursor.moveToNext()) {
                arr.add(new GeoRecord(cursor.getInt(0),
                        cursor.getString(1), cursor.getString(2), cursor.getString(3)));


            }
        }
    }

    private void displayList(){
        arr = new ArrayList<>();
        Cursor cursor = null;
        cursor = db.getRecords();



        if (cursor.getCount() != 0) {

            while (cursor.moveToNext()) {
                arr.add(new GeoRecord(cursor.getInt(0),
                        cursor.getString(1), cursor.getString(2), cursor.getString(3)));

            }
        }
    }


}