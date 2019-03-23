package com.example.xdreamer.barangkushop;

import androidx.annotation.NonNull;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.xdreamer.barangkushop.Object.Products;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductDetail extends AppCompatActivity {

    TextView product_nama, product_price, product_desc;
    ImageView productimage;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton floatingActionButton;
    ElegantNumberButton elegantNumberButton;

    String productId="";

    FirebaseDatabase database;
    DatabaseReference products;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        database = FirebaseDatabase.getInstance();
        products = database.getReference("Products");

        elegantNumberButton = findViewById(R.id.number_button);
        floatingActionButton = findViewById(R.id.btn_cart);

        product_nama = findViewById(R.id.product_namedetail);
        product_price = findViewById(R.id.product_pricedetail);
        product_desc = findViewById(R.id.product_description);

        productimage = findViewById(R.id.img_product);

        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        if (getIntent() != null)
            productId = getIntent().getStringExtra("ProductId");
        if (!productId.isEmpty()){
            getDetailProduct(productId);
        }
    }

    private void getDetailProduct(String productId) {
        products.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Products model = dataSnapshot.getValue(Products.class);
                Picasso.get()
                        .load(model.getImage())
                        .into(productimage);
                collapsingToolbarLayout.setTitle(model.getName());
                product_price.setText(model.getPrice());
                product_nama.setText(model.getName());
                product_desc.setText(model.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
