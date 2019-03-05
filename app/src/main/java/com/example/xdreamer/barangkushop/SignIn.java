package com.example.xdreamer.barangkushop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xdreamer.barangkushop.Common.Common;
import com.example.xdreamer.barangkushop.Object.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    private EditText nomorhp, password;
    private Button signIn;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        nomorhp = findViewById(R.id.etNomorhp);
        password = findViewById(R.id.etPassword);
        signIn = findViewById(R.id.btnSignIn);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference("User");

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                // mDialog.setMessage("Please waiting");
                // mDialog.show();

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(nomorhp.getText().toString()).exists()) {


                            //           mDialog.dismiss();
                            User user = dataSnapshot.child(nomorhp.getText().toString()).getValue(User.class);
                            user.setPhone(nomorhp.getText().toString());
                            if (user.getPassword().equals(password.getText().toString())) {
                                Intent succes = new Intent(SignIn.this, Home.class);
                                Common.currentUser = user;
                                startActivity(succes);
                                finish();
                            } else {
                                Toast.makeText(SignIn.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(SignIn.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
