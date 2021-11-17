package com.example.geocode;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

//Adapter class for handling creation of the list
public class GeoListAdapter extends ArrayAdapter<GeoRecord>{
    private static final String TAG = "GeoRecordListAdapter";

    private Context mContext;
    int mResource;

    //Constructor
    public GeoListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<GeoRecord> objects) {
        super(context, resource, objects);
        mContext=context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //get values based on the position of the object in the arraylist
        int id = getItem(position).getId();
        String address = getItem(position).getAddress();
        String latitude = getItem(position).getLatitude();
        String longitude = getItem(position).getLongitude();

        //create new object of the georecord class
        GeoRecord geo = new GeoRecord(id, address, latitude, longitude);

        LayoutInflater inflater = LayoutInflater.from(mContext);

        convertView = inflater.inflate(mResource, parent, false);

        //get the textviews where the information will be displayed
        TextView address_view = (TextView) convertView.findViewById(R.id.placeholder_address_view);
        TextView latitude_view = (TextView) convertView.findViewById(R.id.placeholder_latitude_view);
        TextView longitude_view = (TextView) convertView.findViewById(R.id.placeholder_longitude_view);

        //set the address within the textview
        address_view.setText(address);

        //round lat and long to 5 decimal places maximum
        double lat = Double.parseDouble(latitude);
        lat = Math.round(lat*100000.0)/100000.0;

        double longit = Double.parseDouble(longitude);
        longit = Math.round(longit*100000.0)/100000.0;

        //set text for latitude and longitude based on the values in the arraylist
        latitude_view.setText("Lat: " + lat);
        longitude_view.setText("Long: " + longit);
        return convertView;

    }
}
