package com.example.xdreamer.barangkushop.ViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xdreamer.barangkushop.Interface.ItemClickListener;
import com.example.xdreamer.barangkushop.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_cartname, txt_cartprice;
    public ImageView img_cart_count, cart_image;

    public RelativeLayout view_background;
    public LinearLayout view_foreground;

    private ItemClickListener itemClickListener;

    public void setTxt_cartname(TextView txt_cartname) {
        this.txt_cartname = txt_cartname;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_cartname = itemView.findViewById(R.id.cart_item_name);
        txt_cartprice = itemView.findViewById(R.id.cart_item_price);
        img_cart_count = itemView.findViewById(R.id.cart_item_count);
        cart_image = itemView.findViewById(R.id.cart_image);

        view_background = itemView.findViewById(R.id.view_background);
        view_foreground = itemView.findViewById(R.id.view_foreground);

    }

    @Override
    public void onClick(View v) {

    }

}