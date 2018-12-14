package com.example.ks.haushaltsmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RezeptErstellenActivity extends AppCompatActivity {

    EditText et_rezeptname;
    String rezeptname;
    Button btn_rezepthinzufuegen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezept_erstellen);

        et_rezeptname = findViewById(R.id.et_rezeptname);
        btn_rezepthinzufuegen = findViewById(R.id.btn_rezepterstellen);
    }
}
