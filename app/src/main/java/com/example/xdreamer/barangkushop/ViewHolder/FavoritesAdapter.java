package com.example.xdreamer.barangkushop.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import com.example.xdreamer.barangkushop.ProductDetailsNew;
import com.example.xdreamer.barangkushop.ProductList;
import com.example.xdreamer.barangkushop.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesViewHolder> {

    private Context context;
    private List<Favorites> favoritesList;

    public FavoritesAdapter(Context context, List<Favorites> favoritesList) {
        this.context = context;
        this.favoritesList = favoritesList;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.favorites_item,viewGroup,false);
        return new FavoritesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder viewHolder, final int position) {
        viewHolder.product_name.setText(favoritesList.get(position).getProductName());
        viewHolder.product_price.setText(String.format("Rp. " + favoritesList.get(position).getProductPrice()));
        Picasso.get()
                .load(favoritesList.get(position).getProductImage())
                .into(viewHolder.product_image);



        viewHolder.quick_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final boolean isExists = new Database(context).checkProductExists(favoritesList.get(position).getProductId(), Common.currentUser.getPhone());

                if (!isExists) {
                    new Database(context).addToCart(new Order(
                            Common.currentUser.getPhone(),
                            favoritesList.get(position).getProductId(),
                            favoritesList.get(position).getProductName(),
                            "1",
                            favoritesList.get(position).getProductPrice(),
                            favoritesList.get(position).getProductDiscount(),
                            favoritesList.get(position).getProductImage()
                    ));
                } else {
                    new Database(context).increaseCart(Common.currentUser.getPhone(), favoritesList.get(position).getProductId());
                }

                Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show();
            }
        });

        final Favorites local = favoritesList.get(position);
        viewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void OnClick(View view, int position, boolean isLongClick) {
                Intent details = new Intent(context, ProductDetailsNew.class);
                details.putExtra("productId", favoritesList.get(position).getProductId());
                context.startActivity(details);
            }
        });

    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    public void removeItem(int position){
        favoritesList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Favorites item,int position){
        favoritesList.add(position,item);
        notifyItemInserted(position);
    }

    public Favorites getItem(int position){
        return favoritesList.get(position);
    }
}
