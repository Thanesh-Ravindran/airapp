/*
    THANESH RAVINDRAN
    012018022619
    FINAL YEAR PROJECT 2020
*/
package com.thanesh.airapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {
    // declare
    private EditText et_email;
    Button btn_send;
    TextView tv_loginHere;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        // call declared
        mAuth = FirebaseAuth.getInstance();
        et_email = findViewById(R.id.et_email);
        btn_send = findViewById(R.id.btn_send);
        tv_loginHere = findViewById(R.id.tv_login_here);
        // back button on click listener
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // firebase reset auth
                mAuth.sendPasswordResetEmail(et_email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPassword.this, "Sent! Please check your email..", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ResetPassword.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        // login screen on click listener
        tv_loginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResetPassword.this, Login.class);
                startActivity(i);
                finish();
            }
        });
    }
}
