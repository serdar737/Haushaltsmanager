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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RezeptBearbeitenActivity extends AppCompatActivity {

    EditText et_zutat, et_beschreibung, et_menge;
    String rezeptnametemp;
    Button btn_rezepthinzufuegen, btn_zutathinzufuegen;
    LinearLayout linearleayoutrezepterstellen;
    String zutat;
    FloatingActionButton fab_zutathinzufuegen;
    int haushaltsid, benutzerid, personentemp, rezeptid;

    RequestQueue requestQueue, requestQueue2;
    String insertUrl = "http://10.0.2.2:3306/erstellerezept.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_rezept_bearbeiten);

        SharedPreferences prefs = getSharedPreferences("sharedprefs", MODE_PRIVATE);
        benutzerid = prefs.getInt("ID", -1);
        haushaltsid = prefs.getInt("HaushaltsID", -1);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            rezeptnametemp = extras.getString("REZEPTNAME");
            personentemp = extras.getInt("PERSONEN");
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
                et_menge = popupviewrezepte.findViewById(R.id.et_zutatmenge);
                btn_zutathinzufuegen = popupviewrezepte.findViewById(R.id.btn_zutathinzufuegenpopup);

                popupbuilder2.setView(popupviewrezepte);
                final AlertDialog dialogrezepte = popupbuilder2.create();

                btn_zutathinzufuegen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        zutatHinzufuegen();
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
                final int personen = personentemp;

                StringRequest srequest = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);

                            boolean sicherheit = obj.getBoolean("sicherheit");

                            if (sicherheit == false) {
                                Toast toast = Toast.makeText(getApplicationContext(), "Rezept konnte nicht erstellt werden. Bitte versuche es nochmal.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else {

                                rezeptid = obj.getInt("ID");
                                System.out.println(rezeptid);
                                //zutatZuRezeptHinzufugen();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("haushaltsid", ""+haushaltsid);
                        parameters.put("benutzerid", ""+benutzerid);
                        parameters.put("rezeptname", rezeptname);
                        parameters.put("beschreibung", beschreibung);
                        parameters.put("personen", ""+personen );


                        return parameters;
                    }
                };

                //fuegt die Werte der RequestQueue zu, sodass diese in die php Datei uebergeben werden koennen
                requestQueue.add(srequest);

            }
        });

    }

    public void zutatZuRezeptHinzufugen() {

        requestQueue2 = Volley.newRequestQueue(getApplicationContext());

        StringRequest srequest = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    boolean sicherheit = obj.getBoolean("sicherheit");

                    if (sicherheit == false) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Rezept konnte nicht erstellt werden. Bitte versuche es nochmal.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else {

                        Toast toast = Toast.makeText(getApplicationContext(), "Rezept erstellt!.", Toast.LENGTH_SHORT);
                        toast.show();
                        Intent intent = new Intent(RezeptBearbeitenActivity.this, RezeptUebersichtActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                //parameters.put("rezeptid", "" +rezeptid);

                return parameters;
            }

        };

        requestQueue2.add(srequest);
    }

    public void zutatHinzufuegen() {

    }

}
