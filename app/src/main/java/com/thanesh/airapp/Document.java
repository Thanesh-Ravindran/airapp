/*
    THANESH RAVINDRAN
    012018022619
    FINAL YEAR PROJECT 2020
*/
package com.thanesh.airapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;


import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;


public class Document extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        // declare and call
        Button btn_back = findViewById(R.id.btn_back);
        Button btn_dwn = findViewById(R.id.btn_download);

        // back button on click listener
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Document.this, Home.class);
                startActivity(i);
                finish();
            }
        });
        // export excel button on click listener
        btn_dwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // let user know file is being exported
                Toast.makeText(Document.this, "Exporting..", Toast.LENGTH_SHORT).show();
                // firebase fb ref
                DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("AirApp").child("Log");
                mref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // using stringbuilder for excel gen
                        final StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("TVOC, eCO2, I.Temp, Time");

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            // using firebase datasnapshot to loop through data
                            String tvoc = Objects.requireNonNull(ds.child("TVOC").getValue()).toString();
                            String eco2 = Objects.requireNonNull(ds.child("eCO2").getValue()).toString();
                            String temp = Objects.requireNonNull(ds.child("Int_temp").getValue()).toString();
                            String time = Objects.requireNonNull(ds.child("Time").getValue()).toString();
                            // write into stringbuilder
                            stringBuilder.append("\n" + tvoc + "," + eco2 + "," + temp + "," + time);
                            break;
                        }
                        // export complete msg
                        Toast.makeText(Document.this, "Export Complete", Toast.LENGTH_SHORT).show();

                        try {
                            // output gen file
                            FileOutputStream fileOutputStream = openFileOutput("AirApp.csv", Context.MODE_PRIVATE);
                            fileOutputStream.write((stringBuilder.toString().getBytes()));
                            fileOutputStream.close();

                            Context context = getApplicationContext();
                            File file = new File(getFilesDir(), "AirApp.csv");
                            Uri uri = FileProvider.getUriForFile(context, "com.thanesh.airapp.fileprovider", file);
                            // intent send action
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/csv");
                            intent.putExtra(Intent.EXTRA_SUBJECT, "AirApp");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.putExtra(Intent.EXTRA_STREAM, uri);
                            startActivity(Intent.createChooser(intent, "Export"));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Document.this, "Error: " + databaseError, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

}
