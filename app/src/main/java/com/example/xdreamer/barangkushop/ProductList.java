package com.example.xdreamer.barangkushop;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.xdreamer.barangkushop.Common.Common;
import com.example.xdreamer.barangkushop.Database.Database;
import com.example.xdreamer.barangkushop.Interface.ItemClickListener;
import com.example.xdreamer.barangkushop.Object.Favorites;
import com.example.xdreamer.barangkushop.Object.Order;
import com.example.xdreamer.barangkushop.Object.Products;
import com.example.xdreamer.barangkushop.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class ProductList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference productList;

    String categoryId = "";

    SwipeRefreshLayout swipeRefreshLayout;

    FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter;

    Database localDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        swipeRefreshLayout = findViewById(R.id.swipe_layout_list);

        database = FirebaseDatabase.getInstance();
        productList = database.getReference("Products");

        localDB = new Database(this);

        recyclerView = findViewById(R.id.recycler_product);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
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
        });

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

        Query listProductById = productList.orderByChild("menuId").equalTo(categoryId);

        FirebaseRecyclerOptions<Products> Options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(listProductById,Products.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(Options) {
            @Override
            protected void onBindViewHolder(@NonNull final ProductViewHolder viewHolder, final int position, @NonNull final Products model) {
                viewHolder.product_name.setText(model.getName());
                viewHolder.product_price.setText(String.format("Rp. " + model.getPrice()));
                Picasso.get()
                        .load(model.getImage())
                        .into(viewHolder.product_image);

                //Fitur Favorite Product :)))
                if (localDB.isFavorite(adapter.getRef(position).getKey())) {
                    viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                }
                viewHolder.fav_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Favorites favorites = new Favorites();
                        favorites.setProductId(adapter.getRef(position).getKey());
                        favorites.setProductName(model.getName());
                        favorites.setProductPrice(model.getPrice());
                        favorites.setProductDescription(model.getDescription());
                        favorites.setProductDiscount(model.getDiscount());
                        favorites.setProductImage(model.getImage());
                        favorites.setProductMenuId(model.getMenuId());
                        favorites.setUserPhone(Common.currentUser.getPhone());

                        if (!localDB.isFavorite(adapter.getRef(position).getKey())) {
                            localDB.addToFavorites(favorites);
                            viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(ProductList.this, "" + model.getName() + " was added to Favorites", Toast.LENGTH_SHORT).show();
                        } else {
                            localDB.removeFromFavorites(adapter.getRef(position).getKey(),Common.currentUser.getPhone());
                            viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            Toast.makeText(ProductList.this, "" + model.getName() + " was removed from Favorites", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                final boolean isExists = new Database(getBaseContext()).checkProductExists(adapter.getRef(position).getKey(), Common.currentUser.getPhone());

                viewHolder.quick_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!isExists) {
                            new Database(getBaseContext()).addToCart(new Order(
                                    Common.currentUser.getPhone(),
                                    adapter.getRef(position).getKey(),
                                    model.getName(),
                                    "1",
                                    model.getPrice(),
                                    model.getDiscount(),
                                    model.getImage()
                            ));
                        } else {
                            new Database(getBaseContext()).increaseCart(Common.currentUser.getPhone(), adapter.getRef(position).getKey());
                        }

                        Toast.makeText(ProductList.this, "Added To Cart", Toast.LENGTH_SHORT).show();
                    }
                });

                final Products local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void OnClick(View view, int position, boolean isLongClick) {
                        Intent details = new Intent(ProductList.this, ProductDetailsNew.class);
                        details.putExtra("productId", adapter.getRef(position).getKey());
                        startActivity(details);
                    }
                });


            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.product_item, viewGroup, false);
                return new ProductViewHolder(itemView);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
