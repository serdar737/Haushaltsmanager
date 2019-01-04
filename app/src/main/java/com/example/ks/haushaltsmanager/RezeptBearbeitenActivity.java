package com.example.ks.haushaltsmanager;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    String rezeptname;
    Button btn_rezepthinzufuegen, btn_zutathinzufuegen;
    LinearLayout linearleayoutrezepterstellen;
    String zutat01, zutat02, zutat03, zutat04, zutat05, zutat06, zutat07, zutat08, zutat09, zutat10, zutat11, zutat12, zutat13, zutat14, zutat15, zutat16, zutat17, zutat18, zutat19, zutat20;
    FloatingActionButton fab_zutathinzufuegen;
    int haushaltsid;

    RequestQueue requestQueue;
    String insertUrl = "http://10.0.2.2:3306/htdocs/erstellerezept.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_rezept_bearbeiten);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            rezeptname = extras.getString("REZEPTNAME");
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

                        zutat01 = et_zutat.getText().toString();
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
                        //parameters.put("rezeptname", rezeptname);
                        //parameters.put("beschreibung", beschreibung);
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
        zutat.setText(zutat01);
        linearleayoutrezepterstellen.addView(zutat);
    }

}
