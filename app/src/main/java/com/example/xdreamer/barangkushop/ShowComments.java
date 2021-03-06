package com.example.xdreamer.barangkushop;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xdreamer.barangkushop.Common.Common;
import com.example.xdreamer.barangkushop.Object.Rating;
import com.example.xdreamer.barangkushop.ViewHolder.ShowCommentViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowComments extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference ratingTbl;

    SwipeRefreshLayout swipeRefreshLayout;

    String productId="";

    FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder> adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter != null){
            adapter.stopListening();
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
        .setDefaultFontPath("fonts/TaxiDriver.ttf")
        .setFontAttrId(R.attr.fontPath)
        .build());
        setContentView(R.layout.activity_show_comments);

        database = FirebaseDatabase.getInstance();
        ratingTbl = database.getReference("Rating");

        recyclerView = findViewById(R.id.recyclerComment);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout = findViewById(R.id.swipe_layout_comments);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (getIntent() != null){
                    productId = getIntent().getStringExtra(Common.INTENT_PRODUCT_ID);
                } if (!productId.isEmpty() && productId != null){
                    Query query = ratingTbl.orderByChild("productId").equalTo(productId);

                    FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>()
                            .setQuery(query,Rating.class)
                            .build();
                    adapter = new FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull ShowCommentViewHolder holder, int i, @NonNull Rating model) {
                            holder.ratingBar.setRating(Float.parseFloat(model.getRateValue()));
                            holder.txtUserPhone.setText(model.getUserPhone());
                            holder.txtComment.setText(model.getComment());
                        }

                        @NonNull
                        @Override
                        public ShowCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.show_comment_layout,parent,false);
                            return new ShowCommentViewHolder(view);
                        }
                    };

                    loadComment(productId);
                }
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);

                if (getIntent() != null){
                    productId = getIntent().getStringExtra(Common.INTENT_PRODUCT_ID);
                } if (!productId.isEmpty() && productId != null){
                    Query query = ratingTbl.orderByChild("productId").equalTo(productId);

                    FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>()
                            .setQuery(query,Rating.class)
                            .build();
                    adapter = new FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull ShowCommentViewHolder holder, int i, @NonNull Rating model) {
                            holder.ratingBar.setRating(Float.parseFloat(model.getRateValue()));
                            holder.txtUserPhone.setText(model.getUserPhone());
                            holder.txtComment.setText(model.getComment());
                        }

                        @NonNull
                        @Override
                        public ShowCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.show_comment_layout,parent,false);
                            return new ShowCommentViewHolder(view);
                        }
                    };

                    loadComment(productId);
                }
            }
        });
    }

    private void loadComment(String productId) {
        adapter.startListening();

        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }

}
