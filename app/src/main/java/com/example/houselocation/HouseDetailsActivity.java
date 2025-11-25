package com.example.houselocation;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HouseDetailsActivity extends AppCompatActivity {

    private ImageView img;
    private TextView tvTitle, tvPrice, tvDesc, tvAvail;
    private Button btnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_details);

        img = findViewById(R.id.imgHouseDetail);
        tvTitle = findViewById(R.id.tvTitleDetail);
        tvPrice = findViewById(R.id.tvPriceDetail);
        tvDesc = findViewById(R.id.tvDescriptionDetail);
        tvAvail = findViewById(R.id.tvAvailability);
        btnMap = findViewById(R.id.btnOpenMap);

        String imageUri = getIntent().getStringExtra("image"); // Use String instead of int
        String title = getIntent().getStringExtra("title");
        String desc = getIntent().getStringExtra("description");
        double price = getIntent().getDoubleExtra("price", 0);
        boolean available = getIntent().getBooleanExtra("available", true);

        // Safe image loading
        if (imageUri != null && !imageUri.isEmpty()) {
            try {
                img.setImageURI(Uri.parse(imageUri));
            } catch (Exception e) {
                img.setImageDrawable(null);
            }
        } else {
            img.setImageDrawable(null);
        }


        tvTitle.setText(title);
        tvDesc.setText(desc);
        tvPrice.setText(price + " TND");
        tvAvail.setText(available ? "Available" : "Not Available");

        // Example coordinates
        double lat = 36.8065;
        double lng = 10.1815;

        btnMap.setOnClickListener(v -> {
            String uri = "geo:" + lat + "," + lng + "?q=" + lat + "," + lng + "(" + title + ")";
            startActivity(new android.content.Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri))
                    .setPackage("com.google.android.apps.maps"));
        });
    }
}
