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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Registration extends AppCompatActivity {
    // declare
    private EditText et_email, et_password;
    Button btn_register;
    TextView tv_loginHere;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        // call declared
        mAuth = FirebaseAuth.getInstance();
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_register = findViewById(R.id.bt_register);
        tv_loginHere = findViewById(R.id.tv_login_here);
        // regis button on click listener
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                if (email.isEmpty()) {
                    et_email.setError("Please enter Email");
                    et_email.requestFocus();
                } else if (password.isEmpty()) {
                    et_password.setError("Please enter Password");
                    et_password.requestFocus();
                } else if (email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(Registration.this, "Fields are empty!", Toast.LENGTH_SHORT).show();
                } else if (!(email.isEmpty() && password.isEmpty())) {
                    // regis auth
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(Registration.this, "Registration Unsuccessful, Please try again..", Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(Registration.this, Home.class));
                                finish();
                            }
                        }
                    });

                } else {
                    Toast.makeText(Registration.this, "Error Occurred! Please Contact Dev..", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // login screen on click listener
        tv_loginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Registration.this, Login.class);
                startActivity(i);
                finish();
            }
        });
    }
}
