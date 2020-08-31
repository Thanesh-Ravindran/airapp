/*
    THANESH RAVINDRAN
    012018022619
    FINAL YEAR PROJECT 2020
*/
package com.thanesh.airapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Readings extends AppCompatActivity {
    // declare
    private TextView co2, voc, temp, polLevel;
    private ProgressBar co2_progressBar, tvoc_progressBar, temp_progressBar;
    private LineChart lineChart;
    ArrayList<Entry> yData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readings);
        // call declared
        Button btn_back = findViewById(R.id.btn_back);
        co2 = findViewById(R.id.tv_co2);
        voc = findViewById(R.id.tv_tvoc);
        temp = findViewById(R.id.tv_temp);
        polLevel = findViewById(R.id.tv_polLevel);
        co2_progressBar = findViewById(R.id.progress_co2);
        tvoc_progressBar = findViewById(R.id.progress_tvoc);
        temp_progressBar = findViewById(R.id.progress_temp);

        lineChart = findViewById(R.id.line_chart);
        // chart character
        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.getDescription().setEnabled(false);
        // chart limit line
        LimitLine limitLine = new LimitLine(400f, "TVOC Limit");
        limitLine.setLineWidth(2f);
        limitLine.setLineColor(Color.RED);
        limitLine.setTextSize(10f);
        // chart yaxis character
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setDrawLabels(true);
        yAxis.addLimitLine(limitLine);
        yAxis.setAxisMinimum(0f);
        // chart xaxis character
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(true);
        // chart legend character
        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        // back button on click listener
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Readings.this, Home.class);
                startActivity(i);
                finish();
            }
        });
        // firebase ref 1 progress bar & text view
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("AirApp");
        mref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // get value
                String eco2 = Objects.requireNonNull(dataSnapshot.child("eCO2").getValue()).toString();
                String tvoc = Objects.requireNonNull(dataSnapshot.child("TVOC").getValue()).toString();
                String tempp = Objects.requireNonNull(dataSnapshot.child("Temperature").getValue()).toString();
                // parse float for progress bar
                float Ieco2 = Float.parseFloat(eco2);
                float Itvoc = Float.parseFloat(tvoc);
                float Itemp = Float.parseFloat(tempp);
                // set value
                co2.setText(eco2);
                co2_progressBar.setProgress((int) Ieco2, true);
                voc.setText(tvoc);
                tvoc_progressBar.setProgress((int) Itvoc, true);
                temp.setText(tempp);
                temp_progressBar.setProgress((int) Itemp, true);
                // if condition temp
                if (Itemp >= 100) tempNotification();
                // if condition eco2 & tvoc
                if ((Ieco2 >= 2000) || (Itvoc >= 325)) {
                    polLevel.setText("High");
                    polLevel.setTextColor(Color.RED);
                    if (Ieco2 >= 2000) co2Notification();
                    if (Itvoc >= 325) vocNotification();
                } else if ((Ieco2 >= 1000) || (Itvoc >= 162.5)) {
                    polLevel.setText("Medium");
                    polLevel.setTextColor(Color.YELLOW);
                } else {
                    polLevel.setText("Low");
                    polLevel.setTextColor(Color.GREEN);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Readings.this, "Error: " + databaseError, Toast.LENGTH_SHORT).show();
            }
        });
        // firebase ref 2 for graph
        DatabaseReference mref2 = FirebaseDatabase.getInstance().getReference().child("AirApp").child("Log");
        mref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // get values in array
                yData = new ArrayList<>();
                float i = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    i = i + 1;
                    String SV = Objects.requireNonNull(ds.child("TVOC").getValue()).toString();
                    float SensorValue = Float.parseFloat(SV);
                    yData.add(new Entry(i, SensorValue));
                }
                final LineDataSet lineDataSet = new LineDataSet(yData, "TVOC");
                lineDataSet.setColor(Color.BLACK);
                lineDataSet.setLineWidth(3f);
                lineDataSet.setCircleColor(Color.BLACK);
                LineData data = new LineData(lineDataSet);
                lineChart.setData(data);
                lineChart.notifyDataSetChanged();
                lineChart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Readings.this, "Error: " + databaseError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // noti method for the readings
    private void co2Notification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setContentTitle("CO2 Warning!")
                .setContentText("Current CO2 readings in waste are high. Please dispose the content of the waste bin.")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Current CO2 readings in waste are high. Please dispose the content of the waste bin."))
                .setAutoCancel(true);

        Intent notiIntent = new Intent(this, Readings.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notiIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.notify(0, builder.build());
    }

    private void vocNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setContentTitle("Total Volatile Organic Compound Warning!")
                .setContentText("Current TVOC readings in waste are high. Please be aware as it could be harmful.")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Current TVOC readings in waste are high. Please be aware as it could be harmful."))
                .setAutoCancel(true);

        Intent notiIntent = new Intent(this, Readings.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notiIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.notify(0, builder.build());
    }

    private void tempNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setContentTitle("Temperature Warning!")
                .setContentText("Board internal temperature is higher than usual. Please turn off device and let it cool down.")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Board internal temperature is higher than usual. Please turn off device and let it cool down."))
                .setAutoCancel(true);

        Intent notiIntent = new Intent(this, Readings.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notiIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.notify(0, builder.build());
    }
}
