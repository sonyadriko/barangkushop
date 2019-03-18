package com.example.xdreamer.barangkushop;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xdreamer.barangkushop.Common.Common;
import com.example.xdreamer.barangkushop.Object.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button signin, signup;

    private TextView nameApp, sloganApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameApp = findViewById(R.id.nameappmain);
        sloganApp = findViewById(R.id.textSlogan);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/TaxiDriver.ttf");
        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/MarvelousSans.otf");
        sloganApp.setTypeface(typeface);
        nameApp.setTypeface(typeface1);

        signin = findViewById(R.id.buttonsignin);
        signup = findViewById(R.id.buttonsignup);

        Paper.init(this);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignIn.class));
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUp.class));
            }
        });

        String user = Paper.book().read(Common.USER_KEY);
        String pwd = Paper.book().read(Common.PWD_KEY);

        if (user != null && pwd != null) {
            if (!user.isEmpty() && !pwd.isEmpty())
                login(user, pwd);
        }
    }

    private void login(final String phone, final String pwd) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference("User");


        if (Common.isConnectedToInternet(getBaseContext())) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(phone).exists()) {

                        //mDialog.dismiss();
                        User user = dataSnapshot.child(phone).getValue(User.class);
                        user.setPhone(phone);
                        if (user.getPassword().equals(pwd)) {
                            Intent succes = new Intent(MainActivity.this, Home.class);
                            Common.currentUser = user;
                            startActivity(succes);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(MainActivity.this, "Please check your connection...", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
