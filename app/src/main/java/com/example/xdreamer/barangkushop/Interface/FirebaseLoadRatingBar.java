package com.example.xdreamer.barangkushop.Interface;

import com.example.xdreamer.barangkushop.Object.Rating;

import java.util.List;

public interface FirebaseLoadRatingBar {
    void onFirebaseLoadSucces(List<Rating> ratingList);
    void onFirebaseLoadFailed(String message);
}
