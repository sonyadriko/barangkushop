package com.example.xdreamer.barangkushop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class DaftarAuth extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    EditText nomorhp, passwordakun;
    Button btnSignIn;
    String VerifikasiID;
    String No_Telepon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_auth);


        firebaseAuth = FirebaseAuth.getInstance();

        nomorhp = findViewById(R.id.etNomorhpauth);

        btnSignIn = findViewById(R.id.btnSignAuth);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = nomorhp.getText().toString().trim();

                Intent intent = new Intent(DaftarAuth.this, PhoneAuth.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);

            }
        });

    }
}
