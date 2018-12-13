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
    LinearLayout linearlayoutartikel;
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
        linearlayoutartikel = findViewById(R.id.linearlayoutartikel);


        fab_artikelhinzufuegen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder popupbuilder = new AlertDialog.Builder(EinkaufslisteActivity.this);
                View popupview = getLayoutInflater().inflate(R.layout.popup_artikelhinzufuegen, null);
                btn_weiter = popupview.findViewById(R.id.btn_weiter_artikel);
                et_artikelname = popupview.findViewById(R.id.et_artikelname);
                et_menge = popupview.findViewById(R.id.et_menge);
                et_kaufhaeufigkeit = popupview.findViewById(R.id.et_kaufhaeufigkeit);

                popupbuilder.setView(popupview);
                final AlertDialog dialog = popupbuilder.create();

                btn_weiter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        artikelname = et_artikelname.getText().toString();
                        menge = et_menge.getText().toString();
                        artikelCheckBoxNeu();
                        dialog.hide();
                    }
                });


                dialog.show();
            }
        });
    }

    public void artikelCheckBoxNeu() {
        CheckBox checkbox = new CheckBox(getApplicationContext());
        checkbox.setText(artikelname);
        linearlayoutartikel.addView(checkbox);
    }
}
