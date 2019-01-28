package com.example.ks.haushaltsmanager;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class HaushaltsInformationenActivity extends AppCompatActivity {

    Button btn_verlassen;
    TextView tv_haushaltsnameinfo;
    int benutzerid, haushaltsid;
    String haushaltsname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_haushalts_informationen);

        SharedPreferences prefs = getSharedPreferences("sharedprefs", MODE_PRIVATE);
        benutzerid = prefs.getInt("ID", -1);
        haushaltsname = prefs.getString("Haushaltsname", "Unbekannter Haushalt");

        btn_verlassen = findViewById(R.id.btn_haushaltverlassen);
        tv_haushaltsnameinfo = findViewById(R.id.tv_infohaushaltsname);

        tv_haushaltsnameinfo.setText(haushaltsname);

        btn_verlassen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
