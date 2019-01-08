package com.example.ks.haushaltsmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

/**
 * Dieser Bildschirm zeigt den Haushaltsbestand des Haushaltes an
 */

public class HaushaltsbestandActivity extends AppCompatActivity {

    TextView tv_haushaltsname;
    LinearLayout llhaushaltsbestand;
    String artikelname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_haushaltsbestand);

        tv_haushaltsname = findViewById(R.id.tv_haushaltsnamehb);
        llhaushaltsbestand = findViewById(R.id.linearlayouthaushaltsbestand);
    }

    public void artikelanzeigen() {
        CheckBox artikel = new CheckBox(getApplicationContext());
        artikel.setText(artikelname);
        llhaushaltsbestand.addView(artikel);
    }
}
