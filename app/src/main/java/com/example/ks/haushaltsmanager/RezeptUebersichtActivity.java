package com.example.ks.haushaltsmanager;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Objects;

public class RezeptUebersichtActivity extends AppCompatActivity {

    LinearLayout linearlayoutrezepteuebersicht;
    FloatingActionButton fab_neuesrezept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_rezept_uebersicht);

        linearlayoutrezepteuebersicht = findViewById(R.id.linearlayoutartikel);
        fab_neuesrezept = findViewById(R.id.fab_artikelhinzufuegen);

        fab_neuesrezept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RezeptUebersichtActivity.this, RezeptErstellenActivity.class);
                //intent.putExtra("BENUTZERNAME_UEBERGABE", benutzername);
                startActivity(intent);
            }
        });
    }
}
