package com.example.ks.haushaltsmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class HauptmenueActivity extends AppCompatActivity {

    TextView tv_benutzername, tv_haushaltsname;
    Button btn_einkaufsliste, btn_rezeptuebersicht, btn_einstellungen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_hauptmenue);

        tv_benutzername = findViewById(R.id.tv_benutzernamehauptmenue);
        tv_haushaltsname = findViewById(R.id.tv_aktuellerhaushalthauptmenue);
        btn_einkaufsliste = findViewById(R.id.btn_einkaufsliste);
        btn_rezeptuebersicht = findViewById(R.id.btn_rezepte);
        btn_einstellungen = findViewById(R.id.btn_einstellungen);

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

        btn_einstellungen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HauptmenueActivity.this, EinstellungenActivity.class);
                startActivity(intent);
            }
        });

    }
}
