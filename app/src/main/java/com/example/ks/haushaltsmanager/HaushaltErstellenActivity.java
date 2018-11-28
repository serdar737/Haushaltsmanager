package com.example.ks.haushaltsmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Objects;

public class HaushaltErstellenActivity extends AppCompatActivity {

    EditText et_haushalterstellenname, et_haushalterstellenanschrift, et_haushalterstellenbeschreibung, et_haushalterstellenpasswort;
    Button btn_haushalterstellen, btn_haushaltbeitreten;

    String haushaltname, haushaltanschrift, haushaltbeschreibung, haushaltpasswort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_haushalt_erstellen);

        et_haushalterstellenname = findViewById(R.id.et_haushalterstellenname);
        et_haushalterstellenanschrift = findViewById(R.id.et_haushalterstellenanschrift);
        et_haushalterstellenpasswort = findViewById(R.id.et_haushalterstellenpasswort);
        et_haushalterstellenbeschreibung = findViewById(R.id.editText3);
        btn_haushalterstellen = findViewById(R.id.btn_haushalterstellen);
        btn_haushaltbeitreten = findViewById(R.id.btn_haushalbeitreten);

        btn_haushalterstellen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                haushaltname = et_haushalterstellenname.getText().toString();
                haushaltanschrift = et_haushalterstellenanschrift.getText().toString();
                haushaltbeschreibung = et_haushalterstellenbeschreibung.getText().toString();
                haushaltpasswort = et_haushalterstellenpasswort.getText().toString();
            }
        });

        btn_haushaltbeitreten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HaushaltErstellenActivity.this, HaushaltBeitretenActivity.class);
                startActivity(intent);
            }
        });
    }
}
