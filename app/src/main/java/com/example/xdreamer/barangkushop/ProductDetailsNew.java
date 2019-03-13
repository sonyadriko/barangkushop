package com.example.xdreamer.barangkushop;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.xdreamer.barangkushop.Common.Common;
import com.example.xdreamer.barangkushop.Database.Database;
import com.example.xdreamer.barangkushop.Object.Order;
import com.example.xdreamer.barangkushop.Object.Products;
import com.example.xdreamer.barangkushop.Object.Rating;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ProductDetailsNew extends AppCompatActivity implements RatingDialogListener {

    TextView product_nama, product_price, product_desc;
    ImageView productimage;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton floatingActionButton, btnRating;
    ElegantNumberButton elegantNumberButton;

    RatingBar ratingBar;

    String productId = "";

    FirebaseDatabase database;
    DatabaseReference products;
    DatabaseReference ratingTbl;

    Products currentProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details_new);

        database = FirebaseDatabase.getInstance();
        products = database.getReference("Products");
        ratingTbl = database.getReference("Rating");

        elegantNumberButton = findViewById(R.id.number_buttonnew);
        floatingActionButton = findViewById(R.id.btn_cartnew);
        btnRating = findViewById(R.id.btn_rating);

        product_nama = findViewById(R.id.product_namedetailnew);
        product_price = findViewById(R.id.product_pricedetailnew);
        product_desc = findViewById(R.id.product_descriptionnew);
        productimage = findViewById(R.id.img_productnew);

        ratingBar = findViewById(R.id.ratingBar);

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

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
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
                getRatingFood(productId);
            } else {
                Toast.makeText(ProductDetailsNew.this, "Please check your connection...", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void getRatingFood(String productId) {

        Query foodRating = ratingTbl.orderByChild("productId").equalTo(productId);

        foodRating.addValueEventListener(new ValueEventListener() {
            int count = 0, sum = 0;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Rating item = dataSnapshot.getValue(Rating.class);
                    sum += Integer.parseInt(item.getRateValue());
                    count++;
                }

                if (count != 0) {
                    float average = sum / count;
                    ratingBar.setRating(average);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not Good", "Standart", "Very Good", "Excellent"))
                .setDefaultRating(1)
                .setTitle("Rate this food")
                .setDescription("Please select some stars and give your feedback")
                .setTitleTextColor(R.color.colorAccent)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please write your comment here...")
                .setHintTextColor(R.color.colorAccent)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setCommentTextColor(android.R.color.white)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(ProductDetailsNew.this)
                .show();
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

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int i, @NotNull String s) {
        final Rating rating = new Rating(Common.currentUser.getPhone(),
                productId,
                String.valueOf(i),
                s);
        ratingTbl.child(Common.currentUser.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(Common.currentUser.getPhone()).exists()) {
                    //remove old value
                    ratingTbl.child(Common.currentUser.getPhone()).removeValue();
                    //Update new value
                    ratingTbl.child(Common.currentUser.getPhone()).setValue(rating);
                } else {
                    //Update new value
                    ratingTbl.child(Common.currentUser.getPhone()).setValue(rating);
                }
                Toast.makeText(ProductDetailsNew.this, "Thank you for submit rating ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
