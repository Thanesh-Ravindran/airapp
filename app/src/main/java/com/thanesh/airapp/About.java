/*
    THANESH RAVINDRAN
    012018022619
    FINAL YEAR PROJECT 2020
*/
package com.thanesh.airapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class About extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        // declare and call
        Button btn_back = findViewById(R.id.btn_back);
        Button btn_dwn = findViewById(R.id.btn_doc);
        // on click listener for back button
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(About.this, Home.class);
                startActivity(i);
                finish();
            }
        });
        // on click listener for doc download
        btn_dwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
            }
        });
    }

    // download auth function
    public void download() {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference mStorageRef = firebaseStorage.getReferenceFromUrl("gs://dummy-app-c377a.appspot.com/");
        StorageReference mref = mStorageRef.child("AirApp_User_Documentation_2020.pdf");

        mref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                downloadManual(About.this, "AirApp_User_Documentation_2020", ".pdf", DIRECTORY_DOWNLOADS, url);
                Toast.makeText(About.this, "Download Complete", Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(About.this, "Failed to download: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    // download manager function
    public void downloadManual(Context context, String filename, String fileExtension, String fileDestination, String url) {

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, fileDestination, filename + fileExtension);

        assert downloadManager != null;
        downloadManager.enqueue(request);

    }
}
