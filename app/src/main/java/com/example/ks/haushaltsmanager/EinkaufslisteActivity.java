package com.example.ks.haushaltsmanager;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

public class EinkaufslisteActivity extends AppCompatActivity {

    TextView tv_benutzername, tv_haushaltsname;
    FloatingActionButton fab_artikelhinzufuegen;
    LinearLayout lleinkaufsliste;
    Button btn_weiter;
    EditText et_artikelname, et_menge, et_kaufhaeufigkeit;
    String artikelname, menge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_einkaufsliste);

        tv_benutzername = findViewById(R.id.tv_benutzernameeinkauf);
        tv_haushaltsname = findViewById(R.id.tv_haushalteinkauf);
        fab_artikelhinzufuegen = findViewById(R.id.fab_artikelhinzufuegen);
        lleinkaufsliste = findViewById(R.id.linearlayouteinkaufsliste);
        btn_weiter = findViewById(R.id.btn_weiter_artikel);
        et_artikelname = findViewById(R.id.et_artikelname);
        et_menge = findViewById(R.id.et_menge);
        et_kaufhaeufigkeit = findViewById(R.id.et_kaufhaeufigkeit);

        fab_artikelhinzufuegen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder popupbuilder = new AlertDialog.Builder(EinkaufslisteActivity.this);
                View popupview = getLayoutInflater().inflate(R.layout.layout_popup_artikelhinzufuegen, null);

                btn_weiter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        artikelname = et_artikelname.getText().toString();
                        menge = et_menge.getText().toString();
                        //TODO: CheckBoxen dem LinearLayout hinzufuegen udns chauen warum es abstuerzt

                    }
                });

                popupbuilder.setView(popupview);
                AlertDialog dialog = popupbuilder.create();
                dialog.show();
            }
        });
    }
}
