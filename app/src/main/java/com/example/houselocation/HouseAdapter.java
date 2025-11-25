package com.example.houselocation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.houselocation.models.House;

import java.util.List;

public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.ViewHolder> {

    private Context context;
    private List<House> houseList;
    private boolean isAdmin;
    private Runnable refreshCallback;

    public HouseAdapter(Context context, List<House> houseList, boolean isAdmin, Runnable refreshCallback) {
        this.context = context;
        this.houseList = houseList;
        this.isAdmin = isAdmin;
        this.refreshCallback = refreshCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_house, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        House h = houseList.get(position);
        holder.title.setText(h.getTitle());
        holder.price.setText(h.getPrice() + " TND");

        // Safe image loading
        if (h.getImageUri() != null && !h.getImageUri().isEmpty()) {
            try {
                holder.image.setImageURI(Uri.parse(h.getImageUri()));
            } catch (Exception e) {
                holder.image.setImageDrawable(null);
            }
        } else {
            holder.image.setImageDrawable(null);
        }


        holder.btnEdit.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
        holder.btnDelete.setVisibility(isAdmin ? View.VISIBLE : View.GONE);

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditHouseActivity.class);
            intent.putExtra("id", h.getId());
            intent.putExtra("title", h.getTitle());
            intent.putExtra("description", h.getDescription());
            intent.putExtra("price", h.getPrice());
            intent.putExtra("available", h.isAvailable());
            intent.putExtra("image", h.getImageUri());
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            DBHelper db = new DBHelper(context);
            db.deleteHouse(h.getId());
            if (refreshCallback != null) refreshCallback.run();
        });

        holder.btnViewDetails.setOnClickListener(v -> {
            Intent intent = new Intent(context, HouseDetailsActivity.class);
            intent.putExtra("id", h.getId());
            intent.putExtra("title", h.getTitle());
            intent.putExtra("description", h.getDescription());
            intent.putExtra("price", h.getPrice());
            intent.putExtra("available", h.isAvailable());
            intent.putExtra("image", h.getImageUri()); // pass URI instead of int
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return houseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, price;
        Button btnViewDetails;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgHouse);
            title = itemView.findViewById(R.id.tvTitle);
            price = itemView.findViewById(R.id.tvPrice);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
