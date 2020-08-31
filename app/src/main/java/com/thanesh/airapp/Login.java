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
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    // declare
    private EditText et_email, et_password;
    Button btn_login;
    TextView tv_registerHere, tv_pwdRec;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // call declared
        mAuth = FirebaseAuth.getInstance();
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        tv_registerHere = findViewById(R.id.tv_register_here);
        tv_pwdRec = findViewById(R.id.tv_pwd_recovery);

        // login auth
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    Toast.makeText(Login.this, "Successfully Logged In!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Login.this, Home.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(Login.this, "Please Login..", Toast.LENGTH_SHORT).show();
                }
            }
        };
        // login button on click listener
        btn_login.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(Login.this, "Fields are empty!", Toast.LENGTH_SHORT).show();
                } else if (!(email.isEmpty() && password.isEmpty())) {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(Login.this, "Login Error! Please try again..", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent i = new Intent(Login.this, Home.class);
                                startActivity(i);
                                finish();
                            }
                        }
                    });
                } else {
                    Toast.makeText(Login.this, "Error Occurred! Please Contact Dev..", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // reset pwd on click listener
        tv_pwdRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, ResetPassword.class);
                startActivity(i);
                finish();
            }
        });
        // regis screen on click listener
        tv_registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Registration.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }
}
