package com.example.houselocation;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddHouseActivity extends AppCompatActivity {

    EditText etTitle, etDesc, etPrice, etImageName;
    Switch swAvailable;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_house);

        etTitle = findViewById(R.id.etTitle);
        etDesc = findViewById(R.id.etDesc);
        etPrice = findViewById(R.id.etPrice);
        etImageName = findViewById(R.id.etImageName); // new EditText for drawable name
        swAvailable = findViewById(R.id.swAvailable);
        btnSave = findViewById(R.id.btnSave);

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

            // Convert drawable name to resource URI
            int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            if(imageResId == 0) {
                Toast.makeText(this, "Drawable not found", Toast.LENGTH_SHORT).show();
                return;
            }
            String imageUri = "android.resource://" + getPackageName() + "/" + imageResId;

            DBHelper db = new DBHelper(this);
            db.insertHouse(title, desc, price, swAvailable.isChecked(), imageUri);
            finish();
        });
    }
}
