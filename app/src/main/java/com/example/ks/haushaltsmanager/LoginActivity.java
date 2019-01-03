package com.example.ks.haushaltsmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.content.Context;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText et_benutzername, et_passwortlogin;
    Button btn_login, btn_neueskonto;
    String benutzername, passwort;
    int nutzerid, haushaltsid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_login);

        et_benutzername = findViewById(R.id.et_benutzername);
        et_passwortlogin = findViewById(R.id.et_passwortlogin);
        btn_login = findViewById(R.id.btn_login);
        btn_neueskonto = findViewById(R.id.btn_neueskonto);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                benutzername = et_benutzername.getText().toString();
                passwort = et_passwortlogin.getText().toString();

                nutzerid = 13;

                SharedPreferences idspeicher = getSharedPreferences("sharedprefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor spe = idspeicher.edit();
                spe.putInt("ID", (int)nutzerid);
                spe.commit();

                //TODO: NutzerID soll ueberprueft werden ob dazu mehrere haushalte vorliegen, wenn ja soll die Haushaltsauswahl geoeffnet werden
                //wenn nein, soll die HaushaltsID geholt werden und direkt ins Hauptmenue uebergegangen werden

                Intent intent = new Intent(LoginActivity.this, HauptmenueActivity.class);
                //intent.putExtra("BENUTZERNAME_UEBERGABE", nutzerid);
                startActivity(intent);
            }
        });

        btn_neueskonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, KontoErstellenActivity.class);
                startActivity(intent);
            }
        });
    }
}
