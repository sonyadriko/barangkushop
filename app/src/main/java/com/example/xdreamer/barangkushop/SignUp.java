package com.example.xdreamer.barangkushop;

import android.app.ProgressDialog;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xdreamer.barangkushop.Common.Common;
import com.example.xdreamer.barangkushop.Object.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {

    private EditText name, nomorhp, password, secureCode;
    private Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.etNameSignup);
        nomorhp = findViewById(R.id.etNomorhpSignup);
        password = findViewById(R.id.etPasswordSignUp);
        signup = findViewById(R.id.btnSignUp);
        secureCode = findViewById(R.id.etSecureCode);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference("User");

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isConnectedToInternet(getBaseContext())) {

                    final ProgressDialog dialog = new ProgressDialog(SignUp.this);
                    dialog.setMessage("Please waiting");
                    dialog.show();

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(nomorhp.getText().toString()).exists()) {
                                dialog.dismiss();
                                Toast.makeText(SignUp.this, "Phone number already regis", Toast.LENGTH_SHORT).show();
                            } else {
                                dialog.dismiss();
                                User user = new User(name.getText().toString(), password.getText().toString()
                                ,secureCode.getText().toString());
                                databaseReference.child(nomorhp.getText().toString()).setValue(user);
                                Toast.makeText(SignUp.this, "Sign up succes", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(SignUp.this, "Please check your connection...", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}
