package com.example.xdreamer.barangkushop.Service;

import com.example.xdreamer.barangkushop.Common.Common;
import com.example.xdreamer.barangkushop.Object.Token;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String tokenRefreshed = FirebaseInstanceId.getInstance().getToken();
        if (Common.currentUser != null) {
            updateTokenFirebase(tokenRefreshed);
        }
    }

    private void updateTokenFirebase(String tokenRefreshed) {
        if (Common.currentUser != null) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");
        Token token = new Token(tokenRefreshed, false);
        tokens.child(Common.currentUser.getPhone()).setValue(token);
         }
    }
}
