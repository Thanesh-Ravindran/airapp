/*
    THANESH RAVINDRAN
    012018022619
    FINAL YEAR PROJECT 2020
*/
package com.thanesh.airapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {
    // declare cardviews
    CardView cv_tvoc, cv_logout, cv_about, cv_manual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // call declared cardviews
        cv_tvoc = findViewById(R.id.tvoc);
        cv_about = findViewById(R.id.about);
        cv_manual = findViewById(R.id.manual);
        cv_logout = findViewById(R.id.logout);

        // on click listeners for cardviews
        cv_tvoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, Readings.class);
                startActivity(i);
                finish();
            }
        });
        cv_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, About.class);
                startActivity(i);
                finish();
            }
        });
        cv_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, Document.class);
                startActivity(i);
                finish();
            }
        });
        cv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(Home.this, Login.class);
                startActivity(i);
                finish();
            }
        });
    }
}
