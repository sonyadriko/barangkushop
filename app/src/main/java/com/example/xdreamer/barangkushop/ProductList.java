package com.example.xdreamer.barangkushop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.xdreamer.barangkushop.Common.Common;
import com.example.xdreamer.barangkushop.Database.Database;
import com.example.xdreamer.barangkushop.Interface.ItemClickListener;
import com.example.xdreamer.barangkushop.Object.Products;
import com.example.xdreamer.barangkushop.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProductList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference productList;

    String categoryId = "";

    FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter;

    Database localDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        database = FirebaseDatabase.getInstance();
        productList = database.getReference("Products");

        localDB = new Database(this);

        recyclerView = findViewById(R.id.recycler_product);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if (!categoryId.isEmpty() && categoryId != null) {
            if (Common.isConnectedToInternet(getBaseContext())) {
                loadListFood(categoryId);
            } else {
                Toast.makeText(ProductList.this, "Please check your connection...", Toast.LENGTH_SHORT).show();
                return;
            }

        }
    }

    private void loadListFood(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(Products.class,
                R.layout.product_item,
                ProductViewHolder.class,
                productList.orderByChild("menuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(final ProductViewHolder viewHolder, final Products model, final int position) {
                viewHolder.product_name.setText(model.getName());
                Picasso.get()
                        .load(model.getImage())
                        .into(viewHolder.product_image);

                final Products local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void OnClick(View view, int position, boolean isLongClick) {
                        Intent details = new Intent(ProductList.this, ProductDetailsNew.class);
                        details.putExtra("productId", adapter.getRef(position).getKey());
                        startActivity(details);
                    }
                });

                if (localDB.HasisFavorite(adapter.getRef(position).getKey()))
                    viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);

                viewHolder.fav_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!localDB.HasisFavorite(adapter.getRef(position).getKey())) {
                            localDB.addToFavorites(adapter.getRef(position).getKey());
                            viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(ProductList.this, "" + model.getName() + " was added to Favorites", Toast.LENGTH_SHORT).show();
                        } else {
                            localDB.removeFromFavorites(adapter.getRef(position).getKey());
                            viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(ProductList.this, "" + model.getName() + " was removed from Favorites", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }
}
