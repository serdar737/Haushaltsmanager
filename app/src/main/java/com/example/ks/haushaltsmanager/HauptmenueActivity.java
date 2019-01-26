package com.example.ks.haushaltsmanager;

import android.content.Intent;
import android.content.SharedPreferences;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;

import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class HauptmenueActivity extends AppCompatActivity {

    TextView tv_haushaltsname;
    Button btn_einkaufsliste, btn_rezeptuebersicht, btn_einstellungen, btn_haushaltsbestand;
    String haushaltsname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_hauptmenue);

        SharedPreferences prefs = getSharedPreferences("sharedprefs", MODE_PRIVATE);
        haushaltsname = prefs.getString("Haushaltsname", "Unbekannter Haushalt");

        tv_haushaltsname = findViewById(R.id.tv_haushalthauptmenue);
        btn_einkaufsliste = findViewById(R.id.btn_einkaufsliste);
        btn_rezeptuebersicht = findViewById(R.id.btn_rezepte);
        btn_haushaltsbestand = findViewById(R.id.btn_haushaltsbestand);
        btn_einstellungen = findViewById(R.id.btn_einstellungen);

        tv_haushaltsname.setText(haushaltsname);

        btn_einkaufsliste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HauptmenueActivity.this, EinkaufslisteActivity.class);
                startActivity(intent);
            }
        });

        btn_rezeptuebersicht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HauptmenueActivity.this, RezeptUebersichtActivity.class);
                startActivity(intent);
            }
        });

        btn_haushaltsbestand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HauptmenueActivity.this, RezeptUebersichtActivity.class);
                startActivity(intent);
            }
        });

        btn_einstellungen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HauptmenueActivity.this, EinstellungenActivity.class);
                startActivity(intent);
            }
        });

    }
}
