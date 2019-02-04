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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RezeptBearbeitenActivity extends AppCompatActivity {

    EditText et_zutat, et_beschreibung, et_menge, et_masseinheit;
    String rezeptnametemp;
    Button btn_rezepthinzufuegen, btn_zutathinzufuegen;
    LinearLayout linearleayoutrezepterstellen;
    String zutat, masseinheit;
    FloatingActionButton fab_zutathinzufuegen;
    int haushaltsid, benutzerid, rezeptidtemp;
    double menge;

    RequestQueue requestQueue, requestQueue2, requestQueue3;
    String insertUrl = "http://10.0.2.2:3306/updaterezept.php";
    String url = "http://10.0.2.2:3306/erstellezutat.php";
    String starturl = "http://10.0.2.2:3306/rezeptansicht.php";

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
            rezeptidtemp = extras.getInt("REZEPTID");
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
                et_masseinheit = popupviewrezepte.findViewById(R.id.et_masseinheit);
                btn_zutathinzufuegen = popupviewrezepte.findViewById(R.id.btn_zutathinzufuegenpopup);

                popupbuilder2.setView(popupviewrezepte);
                final AlertDialog dialogrezepte = popupbuilder2.create();

                btn_zutathinzufuegen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        zutat = et_zutat.getText().toString();
                        String temp_menge = et_menge.getText().toString();
                        menge = Double.parseDouble(temp_menge);
                        masseinheit = et_masseinheit.getText().toString();

                        zutatHinzufuegen(zutat, menge, masseinheit);
                        zutatZuRezept();
                        dialogrezepte.hide();
                    }
                });

                dialogrezepte.show();
            }
        });

        requestQueue3 = Volley.newRequestQueue(getApplicationContext());

        final int rezeptid2 = rezeptidtemp;

        StringRequest arequest = new StringRequest(Request.Method.POST, starturl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray j_zutat = obj.getJSONArray("zutat");
                    JSONArray j_menge = obj.getJSONArray("menge");
                    JSONArray j_masseinheit = obj.getJSONArray("masseinheit");
                    JSONArray j_beschreibung = obj.getJSONArray("beschreibung");

                    for (int z = 0; z < j_zutat.length(); z++) {
                        final JSONObject zutatobj = j_zutat.getJSONObject(z);
                        final JSONObject mengeobj = j_menge.getJSONObject(z);
                        final JSONObject masseinheitobj = j_masseinheit.getJSONObject(z);

                        zutat = zutatobj.getString("Zutat");
                        //String temp_menge = et_menge.getText().toString();
                        menge = mengeobj.getDouble("Menge");
                        masseinheit = masseinheitobj.getString("Masseinheit");

                        zutatHinzufuegen(zutat, menge, masseinheit);
                    }

                    final JSONObject beschreibungobj = j_beschreibung.getJSONObject(0);

                    et_beschreibung.setText(beschreibungobj.getString("Beschreibung"));

                }
                catch (JSONException e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ResponseError");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("rezeptid", ""+rezeptid2);

                return parameters;
            }
        };

        //fuegt die Werte der RequestQueue zu, sodass diese in die php Datei uebergeben werden koennen
        requestQueue3.add(arequest);


        requestQueue = Volley.newRequestQueue(getApplicationContext());

        btn_rezepthinzufuegen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String beschreibung = et_beschreibung.getText().toString();
                final int rezeptid = rezeptidtemp;

                StringRequest srequest = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());

                            boolean sicherheit = obj.getBoolean("erstellt");

                            if (sicherheit == false) {
                                Toast toast = Toast.makeText(getApplicationContext(), "Rezept konnte nicht bearbeitet werden. Bitte versuche es nochmal.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else {
                                Toast toast = Toast.makeText(getApplicationContext(), "Rezept erfolgreich bearbeitet!", Toast.LENGTH_SHORT);
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
                        System.out.println("ResponseError");
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("rezeptid", ""+rezeptid);
                        parameters.put("beschreibung", ""+beschreibung);

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


    public void zutatHinzufuegen(String z, double m, String ma) {

        TextView tv_zutat = new TextView(getApplicationContext());
        tv_zutat.setText(z+" - "+m+" "+ma);
        linearleayoutrezepterstellen.addView(tv_zutat);

    }

    public void zutatZuRezept() {

        requestQueue2 = Volley.newRequestQueue(getApplicationContext());

        final int rezeptid = rezeptidtemp;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ResponseError");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("rezeptid", ""+rezeptid);
                parameters.put("zutat", zutat);
                parameters.put("menge", ""+menge);
                parameters.put("masseinheit", masseinheit);

                return parameters;
            }
        };

        //fuegt die Werte der RequestQueue zu, sodass diese in die php Datei uebergeben werden koennen
        requestQueue2.add(request);
    }

    @Override
    public void onBackPressed() {
        //Nichts machen wenn die Zurueck taste des handys benutzt wird
    }

}
