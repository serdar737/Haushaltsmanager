package com.example.ks.haushaltsmanager;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

public class EinkaufslisteActivity extends AppCompatActivity {

    TextView tv_benutzername, tv_haushaltsname;
    FloatingActionButton fab_artikelhinzufuegen;
    LinearLayout lleinkaufsliste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_einkaufsliste);

        tv_benutzername = findViewById(R.id.tv_benutzernameeinkauf);
        tv_haushaltsname = findViewById(R.id.tv_haushalteinkauf);
        fab_artikelhinzufuegen = findViewById(R.id.fab_artikelhinzufuegen);
        lleinkaufsliste = findViewById(R.id.linearlayouteinkaufsliste);

        fab_artikelhinzufuegen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
