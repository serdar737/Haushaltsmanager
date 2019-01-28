package com.example.ks.haushaltsmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class EinstellungenActivity extends AppCompatActivity {

    Button btn_haushaltwechseln, btn_hilfe, btn_ausloggen, btn_loeschen, btn_benutzerloeschen, btn_haushaltloeschen;
    int haushaltsid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_einstellungen);

        btn_haushaltwechseln = findViewById(R.id.btn_haushaltwechseln);
        btn_hilfe = findViewById(R.id.btn_hilfeaufrufen);
        btn_ausloggen = findViewById(R.id.btn_ausloggen);
        btn_loeschen = findViewById(R.id.btn_loeschen);

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

                SharedPreferences prefs = getSharedPreferences("sharedprefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor spe = prefs.edit();
                spe.putInt("ID", -1);
                spe.putInt("HaushaltsID", -1);
                spe.putString("Haushaltsname", "Unbekannter Haushalt");
                spe.apply();

                Intent intent = new Intent(EinstellungenActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_loeschen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder popupbuilder = new AlertDialog.Builder(EinstellungenActivity.this);
                View popupviewkontenloeschen = getLayoutInflater().inflate(R.layout.popup_kontenloeschen, null);

                btn_benutzerloeschen = popupviewkontenloeschen.findViewById(R.id.btn_benutzerloeschen);
                btn_haushaltloeschen = popupviewkontenloeschen.findViewById(R.id.btn_haushaltloeschen);

                popupbuilder.setView(popupviewkontenloeschen);
                final AlertDialog dialogloeschen = popupbuilder.create();

                btn_benutzerloeschen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialogloeschen.hide();
                    }
                });

                btn_haushaltloeschen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialogloeschen.hide();
                    }
                });

                dialogloeschen.show();
            }
        });
    }
}
