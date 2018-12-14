package com.example.ks.haushaltsmanager;


import android.support.v7.app.AppCompatActivity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Objects;



public class RezeptErstellenActivity extends AppCompatActivity {

    EditText et_rezeptname;
    Button btn_weiter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_rezept_erstellen);

        et_rezeptname = findViewById(R.id.et_rezeptbenennen);
        btn_weiter = findViewById(R.id.btn_rezeptnameweiter);

        btn_weiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RezeptErstellenActivity.this, RezeptBearbeitenActivity.class);
                startActivity(intent);
            }
        });
    }
}
