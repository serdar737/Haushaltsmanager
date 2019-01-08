package com.example.ks.haushaltsmanager;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RezeptBearbeitenActivity extends AppCompatActivity {

    EditText et_zutat, et_beschreibung;
    String rezeptnametemp;
    Button btn_rezepthinzufuegen, btn_zutathinzufuegen;
    LinearLayout linearleayoutrezepterstellen;
    String zutat01 = "-1", zutat02 = "-1", zutat03 = "-1", zutat04 = "-1", zutat05 = "-1", zutat06 = "-1", zutat07 = "-1", zutat08 = "-1", zutat09 = "-1", zutat10 = "-1", zutat11 = "-1", zutat12 = "-1", zutat13 = "-1", zutat14 = "-1", zutat15 = "-1", zutat16 = "-1", zutat17 = "-1", zutat18 = "-1", zutat19 = "-1", zutat20 = "-1";
    FloatingActionButton fab_zutathinzufuegen;
    int haushaltsid, nutzerid;

    RequestQueue requestQueue;
    String insertUrl = "http://10.0.2.2:3306/erstellerezept.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_rezept_bearbeiten);

        SharedPreferences prefs = getSharedPreferences("sharedprefs", MODE_PRIVATE);
        nutzerid = prefs.getInt("ID", -1);
        haushaltsid = prefs.getInt("HaushaltsID", -1);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            rezeptnametemp = extras.getString("REZEPTNAME");
        }

        btn_rezepthinzufuegen = findViewById(R.id.btn_rezepterstellen);
        linearleayoutrezepterstellen = findViewById(R.id.linearlayoutrezepterstellen);
        fab_zutathinzufuegen = findViewById(R.id.fab_zutathinzufuegen);
        et_beschreibung = findViewById(R.id.et_beschreibung);

        fab_zutathinzufuegen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder popupbuilder2 = new AlertDialog.Builder(RezeptBearbeitenActivity.this);
                View popupviewrezepte = getLayoutInflater().inflate(R.layout.popup_zutathinzufuegen, null);

                et_zutat = popupviewrezepte.findViewById(R.id.et_zutat);
                btn_zutathinzufuegen = popupviewrezepte.findViewById(R.id.btn_zutathinzufuegenpopup);

                popupbuilder2.setView(popupviewrezepte);
                final AlertDialog dialogrezepte = popupbuilder2.create();

                btn_zutathinzufuegen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        zutathinzufugen();
                        dialogrezepte.hide();
                    }
                });

                dialogrezepte.show();
            }
        });

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        btn_rezepthinzufuegen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String beschreibung = et_beschreibung.getText().toString();
                final String rezeptname = rezeptnametemp;

                StringRequest srequest = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        //parameters.put("haushaltsid", haushaltsid);
                        parameters.put("rezeptname", rezeptname);
                        parameters.put("beschreibung", beschreibung);
                        //parameters.put("zutat01", zutat01);
                        //parameters.put("zutat02", zutat02);
                        //parameters.put("zutat03", zutat03);
                        //parameters.put("zutat04", zutat04);
                        //parameters.put("zutat05", zutat05);
                        //parameters.put("zutat06", zutat06);
                        //parameters.put("zutat07", zutat07);
                        //parameters.put("zutat08", zutat08);
                        //parameters.put("zutat09", zutat09);
                        //parameters.put("zutat10", zutat10);
                        //parameters.put("zutat11", zutat11);
                        //parameters.put("zutat12", zutat12);
                        //parameters.put("zutat13", zutat13);
                        //parameters.put("zutat14", zutat14);
                        //parameters.put("zutat15", zutat15);
                        //parameters.put("zutat16", zutat16);
                        //parameters.put("zutat17", zutat17);
                        //parameters.put("zutat18", zutat18);
                        //parameters.put("zutat19", zutat19);
                        //parameters.put("zutat20", zutat20);


                        return parameters;
                    }
                };

                //fuegt die Werte der RequestQueue zu, sodass diese in die php Datei uebergeben werden koennen
                requestQueue.add(srequest);

                Intent intent = new Intent(RezeptBearbeitenActivity.this, RezeptUebersichtActivity.class);
                startActivity(intent);

            }
        });

    }

    public void zutathinzufugen() {

        TextView zutat = new TextView(getApplicationContext());

        if(zutat01.equals("-1")) {
            zutat01 = et_zutat.getText().toString();
            zutat.setText(zutat01);
        }
        else if (zutat02.equals("-1")) {
            zutat02 = et_zutat.getText().toString();
            zutat.setText(zutat02);
        }
        else if (zutat03.equals("-1")) {
            zutat03 = et_zutat.getText().toString();
            zutat.setText(zutat03);
        }
        else if (zutat04.equals("-1")) {
            zutat04 = et_zutat.getText().toString();
            zutat.setText(zutat04);
        }
        else if (zutat05.equals("-1")) {
            zutat05 = et_zutat.getText().toString();
            zutat.setText(zutat05);
        }
        else if (zutat06.equals("-1")) {
            zutat06 = et_zutat.getText().toString();
            zutat.setText(zutat06);
        }
        else if (zutat07.equals("-1")) {
            zutat07 = et_zutat.getText().toString();
            zutat.setText(zutat07);
        }
        else if (zutat08.equals("-1")) {
            zutat08 = et_zutat.getText().toString();
            zutat.setText(zutat08);
        }
        else if (zutat09.equals("-1")) {
            zutat09 = et_zutat.getText().toString();
            zutat.setText(zutat09);
        }
        else if (zutat10.equals("-1")) {
            zutat10 = et_zutat.getText().toString();
            zutat.setText(zutat10);
        }
        else if (zutat11.equals("-1")) {
            zutat11 = et_zutat.getText().toString();
            zutat.setText(zutat11);
        }
        else if (zutat12.equals("-1")) {
            zutat12 = et_zutat.getText().toString();
            zutat.setText(zutat12);
        }
        else if (zutat13.equals("-1")) {
            zutat13 = et_zutat.getText().toString();
            zutat.setText(zutat13);
        }
        else if (zutat14.equals("-1")) {
            zutat14 = et_zutat.getText().toString();
            zutat.setText(zutat14);
        }
        else if (zutat15.equals("-1")) {
            zutat15 = et_zutat.getText().toString();
            zutat.setText(zutat15);
        }
        else if (zutat16.equals("-1")) {
            zutat16 = et_zutat.getText().toString();
            zutat.setText(zutat16);
        }
        else if (zutat17.equals("-1")) {
            zutat17 = et_zutat.getText().toString();
            zutat.setText(zutat17);
        }
        else if (zutat18.equals("-1")) {
            zutat18 = et_zutat.getText().toString();
            zutat.setText(zutat18);
        }
        else if (zutat19.equals("-1")) {
            zutat19 = et_zutat.getText().toString();
            zutat.setText(zutat19);
        }
        else if (zutat20.equals("-1")) {
            zutat20 = et_zutat.getText().toString();
            zutat.setText(zutat20);
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(), "Du hast bereits 20 Zutaten hinzugefügt", Toast.LENGTH_SHORT);
            toast.show();
        }

        linearleayoutrezepterstellen.addView(zutat);
    }

}
