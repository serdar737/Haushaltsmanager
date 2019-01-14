package com.example.ks.haushaltsmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Objects;

public class NutzerhaushalteActivity extends AppCompatActivity {

    LinearLayout llnutzerhaushalte;
    Button btn_haushalt;
    String haushaltsname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_nutzerhaushalte);

        llnutzerhaushalte = findViewById(R.id.ll_nutzerhaushalte);

        //TODO: aus der Db alle dem Nutzer zugehoerigen Haushalte auslesen und in Buttons schreiben
    }

    public void haushalteinfuegen() {
        btn_haushalt = new Button(getApplicationContext());
        btn_haushalt.setText(haushaltsname);
        llnutzerhaushalte.addView(btn_haushalt);
    }
}
