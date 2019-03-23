package com.example.xdreamer.barangkushop.ViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xdreamer.barangkushop.Interface.ItemClickListener;
import com.example.xdreamer.barangkushop.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView product_name, product_price;
    public ImageView product_image,fav_image,quick_cart;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        product_name = itemView.findViewById(R.id.product_name);
        product_image = itemView.findViewById(R.id.product_image);
        product_price = itemView.findViewById(R.id.product_price);
        quick_cart = itemView.findViewById(R.id.btn_quick_cart);
        fav_image = itemView.findViewById(R.id.imageFAV);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.OnClick(v, getAdapterPosition(), false);
    }
}
