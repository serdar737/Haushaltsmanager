package com.example.ks.haushaltsmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class EinstellungenActivity extends AppCompatActivity {

    Button btn_haushaltwechseln, btn_hilfe, btn_ausloggen;
    String haushaltsname;
    int haushaltsid, nutzerid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_einstellungen);

        btn_haushaltwechseln = findViewById(R.id.btn_haushaltwechseln);
        btn_hilfe = findViewById(R.id.btn_hilfeaufrufen);
        btn_ausloggen = findViewById(R.id.btn_ausloggen);

        btn_haushaltwechseln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EinstellungenActivity.this, NutzerhaushalteActivity.class);
                startActivity(intent);
            }
        });

        btn_hilfe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EinstellungenActivity.this, HilfeActivity.class);
                startActivity(intent);
            }
        });

        btn_ausloggen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nutzerid = -1;
                haushaltsid = -1;
                haushaltsname = "Unbekannter Haushalt";

                SharedPreferences prefs = getSharedPreferences("sharedprefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor spe = prefs.edit();
                spe.putInt("ID", (int)nutzerid);
                spe.putInt("HaushaltsID", (int)haushaltsid);
                spe.putString("Haushaltsname", haushaltsname);
                spe.apply();

                Intent intent = new Intent(EinstellungenActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
