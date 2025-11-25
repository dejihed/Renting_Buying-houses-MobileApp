package com.example.houselocation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get coordinates passed from HouseDetailsActivity
        double latitude = getIntent().getDoubleExtra("lat", 0);
        double longitude = getIntent().getDoubleExtra("lng", 0);

        // Build Google Maps URI
        String uri = "geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude;

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        mapIntent.setPackage("com.google.android.apps.maps");

        startActivity(mapIntent);

        // Close activity after launching Maps
        finish();
    }
}
