package com.example.houselocation;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditHouseActivity extends AppCompatActivity {

    EditText etTitle, etDesc, etPrice, etImageName;
    Switch swAvailable;
    Button btnSave;
    int houseId;
    String currentImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_house);

        etTitle = findViewById(R.id.etTitle);
        etDesc = findViewById(R.id.etDesc);
        etPrice = findViewById(R.id.etPrice);
        etImageName = findViewById(R.id.etImageName);
        swAvailable = findViewById(R.id.swAvailable);
        btnSave = findViewById(R.id.btnSave);

        houseId = getIntent().getIntExtra("id", -1);
        etTitle.setText(getIntent().getStringExtra("title"));
        etDesc.setText(getIntent().getStringExtra("description"));
        etPrice.setText(String.valueOf(getIntent().getIntExtra("price", 0)));
        swAvailable.setChecked(getIntent().getBooleanExtra("available", false));
        currentImageUri = getIntent().getStringExtra("image");

        // Extract drawable name from URI for editing
        if(currentImageUri != null && currentImageUri.contains("/")) {
            String[] parts = currentImageUri.split("/");
            String resName = parts[parts.length - 1];
            etImageName.setText(resName);
        }

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            String imageName = etImageName.getText().toString().trim();

            if(title.isEmpty() || desc.isEmpty() || priceStr.isEmpty() || imageName.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int price;
            try {
                price = Integer.parseInt(priceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show();
                return;
            }

            int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            if(imageResId == 0) {
                Toast.makeText(this, "Drawable not found", Toast.LENGTH_SHORT).show();
                return;
            }
            currentImageUri = "android.resource://" + getPackageName() + "/" + imageResId;

            DBHelper db = new DBHelper(this);
            db.updateHouse(houseId, title, desc, price, swAvailable.isChecked(), currentImageUri);
            finish();
        });
    }
}
