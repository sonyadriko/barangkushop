package com.example.xdreamer.barangkushop;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xdreamer.barangkushop.Common.Common;
import com.example.xdreamer.barangkushop.Object.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class SignIn extends AppCompatActivity {

    private EditText nomorhp, password;
    private Button signIn;
    private TextView forgotPWD;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        checkBox = findViewById(R.id.cbRemember);

        Paper.init(this);

        nomorhp = findViewById(R.id.etNomorhp);
        password = findViewById(R.id.etPassword);
        signIn = findViewById(R.id.btnSignIn);
        forgotPWD = findViewById(R.id.txtForgotPwd);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");

        forgotPWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotPwdDialog();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isConnectedToInternet(getBaseContext())) {

                    if (checkBox.isChecked()) {
                        Paper.book().write(Common.USER_KEY, nomorhp.getText().toString());
                        Paper.book().write(Common.PWD_KEY, password.getText().toString());
                    }

                    final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                    mDialog.setMessage("Please waiting");
                    mDialog.show();


                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(nomorhp.getText().toString()).exists()) {


                                mDialog.dismiss();
                                User user = dataSnapshot.child(nomorhp.getText().toString()).getValue(User.class);
                                user.setPhone(nomorhp.getText().toString());
                                if (user.getPassword().equals(password.getText().toString())) {
                                    Intent succes = new Intent(SignIn.this, Home.class);
                                    Common.currentUser = user;
                                    startActivity(succes);
                                    finish();

                                    databaseReference.removeEventListener(this);
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
                } else {
                    Toast.makeText(SignIn.this, "Please check your connection...", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void showForgotPwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Forgot Password");
        builder.setMessage("Enter your secure code");

        LayoutInflater inflater = this.getLayoutInflater();
        View forgot_view = inflater.inflate(R.layout.activity_forgot_password,null);

        builder.setView(forgot_view);
        builder.setIcon(R.drawable.ic_security_black_24dp);

        final EditText edtPhone = forgot_view.findViewById(R.id.etNomorForgot);
        final EditText edtSecure = forgot_view.findViewById(R.id.etSecureCodeForgot);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);

                        if (user.getSecureCode().equals(edtSecure.getText().toString()))
                            Toast.makeText(SignIn.this, "Your Password : "+user.getPassword(), Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(SignIn.this, "Wrong secure code", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }
}
