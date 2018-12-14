package com.example.ks.haushaltsmanager;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RezeptErstellenActivity extends AppCompatActivity {

    EditText et_rezeptname;
    String rezeptname;
    Button btn_rezepthinzufuegen;
    LinearLayout linearleayoutrezepterstellen;
    TextView zutat01, zutat02, zutat03, zutat04;
    FloatingActionButton fab_zutathinzufuegen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezept_erstellen);

        et_rezeptname = findViewById(R.id.et_rezeptname);
        btn_rezepthinzufuegen = findViewById(R.id.btn_rezepterstellen);
        linearleayoutrezepterstellen = findViewById(R.id.linearlayoutrezepterstellen);
        fab_zutathinzufuegen = findViewById(R.id.fab_zutathinzufuegen);
    }
}
