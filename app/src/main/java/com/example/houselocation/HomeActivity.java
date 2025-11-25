package com.example.houselocation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.houselocation.models.House;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView rv;
    private EditText etSearch;
    private ImageButton btnLogout;
    private FloatingActionButton btnAddHouse;
    private HouseAdapter adapter;
    private List<House> houses = new ArrayList<>();
    private List<House> filtered = new ArrayList<>();
    private boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rv = findViewById(R.id.rvHouses);
        etSearch = findViewById(R.id.etSearch);
        btnLogout = findViewById(R.id.btnLogout);
        btnAddHouse = findViewById(R.id.btnAddHouse);

        String role = getIntent().getStringExtra("role");
        isAdmin = role != null && role.equals("admin");

        btnAddHouse.setVisibility(isAdmin ? View.VISIBLE : View.GONE);

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        btnAddHouse.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, AddHouseActivity.class)) );

        loadHouses();
        filtered.addAll(houses);
        adapter = new HouseAdapter(this, filtered, isAdmin, this::refreshHouses);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }
        });
    }

    private void loadHouses() {
        DBHelper db = new DBHelper(this);
        houses = db.getAllHouses();
        // Insert sample data only if DB is empty
        if (houses.isEmpty()) {
            db.insertHouse( "Modern Villa", "Beautiful villa with pool", 1500, true, "android.resource://" + getPackageName() + "/" + R.drawable.house1 );
            db.insertHouse( "City Apartment", "Near downtown", 900, false, "android.resource://" + getPackageName() + "/" + R.drawable.house2 );
            db.insertHouse( "Beach House", "5 min from the beach", 1200, true, "android.resource://" + getPackageName() + "/" + R.drawable.house3 );
            houses = db.getAllHouses(); // reload
        }
    }

    private void filterList(String text) {
        filtered.clear();
        for (House h : houses) {
            if (h.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filtered.add(h);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void refreshHouses() {
        DBHelper db = new DBHelper(this);
        houses = db.getAllHouses();
        filterList(etSearch.getText().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshHouses();
    }
}
