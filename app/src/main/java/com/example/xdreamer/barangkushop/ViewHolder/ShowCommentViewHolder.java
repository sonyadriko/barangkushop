package com.example.xdreamer.barangkushop.ViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.xdreamer.barangkushop.R;

public class ShowCommentViewHolder extends RecyclerView.ViewHolder{

    public TextView txtUserPhone, txtComment;
    public RatingBar ratingBar;

    public ShowCommentViewHolder(@NonNull View itemView) {
        super(itemView);
        txtUserPhone = itemView.findViewById(R.id.txtUserComment);
        txtComment = itemView.findViewById(R.id.txtComment);
        ratingBar = itemView.findViewById(R.id.ratingBarComment);

    }

}
