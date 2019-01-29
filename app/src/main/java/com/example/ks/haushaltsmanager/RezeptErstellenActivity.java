package com.example.ks.haushaltsmanager;


import android.support.v7.app.AppCompatActivity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;



public class RezeptErstellenActivity extends AppCompatActivity {

    EditText et_rezeptname, et_personenanzahl;
    Button btn_weiter;
    String rezeptname;
    int personen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_rezept_erstellen);

        et_rezeptname = findViewById(R.id.et_rezeptbenennen);
        et_personenanzahl = findViewById(R.id.et_personenanzahl);
        btn_weiter = findViewById(R.id.btn_rezeptnameweiter);

        btn_weiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rezeptname = et_rezeptname.getText().toString();
                personen = Integer.parseInt(et_personenanzahl.getText().toString());

                if (rezeptname.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Bitte fülle alle Felder aus.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if (personen == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Bitte fülle alle Felder aus.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    Intent intent = new Intent(RezeptErstellenActivity.this, RezeptBearbeitenActivity.class);
                    intent.putExtra("REZEPTNAME", rezeptname);
                    intent.putExtra("PERSONEN", personen);
                    startActivity(intent);
                }
            }
        });
    }
}
