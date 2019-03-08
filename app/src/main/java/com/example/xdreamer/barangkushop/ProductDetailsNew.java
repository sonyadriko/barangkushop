package com.example.xdreamer.barangkushop;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.xdreamer.barangkushop.Common.Common;
import com.example.xdreamer.barangkushop.Database.Database;
import com.example.xdreamer.barangkushop.Object.Order;
import com.example.xdreamer.barangkushop.Object.Products;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductDetailsNew extends AppCompatActivity {

    TextView product_nama, product_price, product_desc;
    ImageView productimage;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton floatingActionButton;
    ElegantNumberButton elegantNumberButton;

    String productId = "";

    FirebaseDatabase database;
    DatabaseReference products;

    Products currentProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details_new);

        database = FirebaseDatabase.getInstance();
        products = database.getReference("Products");

        elegantNumberButton = findViewById(R.id.number_buttonnew);
        floatingActionButton = findViewById(R.id.btn_cartnew);

        product_nama = findViewById(R.id.product_namedetailnew);
        product_price = findViewById(R.id.product_pricedetailnew);
        product_desc = findViewById(R.id.product_descriptionnew);
        productimage = findViewById(R.id.img_productnew);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        productId,
                        currentProducts.getName(),
                        elegantNumberButton.getNumber(),
                        currentProducts.getPrice(),
                        currentProducts.getDiscount()
                ));

                Toast.makeText(ProductDetailsNew.this, "Added To Cart", Toast.LENGTH_SHORT).show();
            }
        });


        collapsingToolbarLayout = findViewById(R.id.collapsingnew);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        if (getIntent() != null)
            productId = getIntent().getStringExtra("productId");
        if (!productId.isEmpty()) {
            if (Common.isConnectedToInternet(getBaseContext())) {
                getDetailProduct(productId);
            } else {
                Toast.makeText(ProductDetailsNew.this, "Please check your connection...", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void getDetailProduct(String productId) {
        products.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentProducts = dataSnapshot.getValue(Products.class);
                Picasso.get()
                        .load(currentProducts.getImage())
                        .into(productimage);
                collapsingToolbarLayout.setTitle(currentProducts.getName());

                product_price.setText(currentProducts.getPrice());

                product_nama.setText(currentProducts.getName());

                product_desc.setText(currentProducts.getDescription());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
