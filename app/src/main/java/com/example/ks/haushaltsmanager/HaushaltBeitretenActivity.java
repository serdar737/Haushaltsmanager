package com.example.ks.haushaltsmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class HaushaltBeitretenActivity extends AppCompatActivity {

    Button btn_beitreten, btn_abbruch, btn_hilfe, btn_neuenhaushalt;
    EditText et_haushaltsid, et_passwort;
    int nutzerid, haushaltsid;

    RequestQueue requestQueue;
    String insertUrl = "http://10.0.2.2:3306/haushaltbeitreten.php";
    String haushaltsname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_haushalt_beitreten);

        btn_beitreten = findViewById(R.id.btn_beitreten);
        btn_abbruch = findViewById(R.id.btn_abbruch);
        btn_hilfe = findViewById(R.id.btn_hilfe);
        btn_neuenhaushalt = findViewById(R.id.btn_neuenhaushalt);
        et_haushaltsid = findViewById(R.id.et_haushaltsid);
        et_passwort = findViewById(R.id.et_passwortlogin);

        SharedPreferences prefs = getSharedPreferences("sharedprefs", MODE_PRIVATE);
        nutzerid = prefs.getInt("ID", -1);
        haushaltsid = prefs.getInt("HaushaltsID", -1);

        btn_beitreten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_haushaltsid.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Bitte fülle alle Felder aus", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if (et_passwort.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Bitte fülle alle Felder aus", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    String temp_haushaltsid = et_haushaltsid.getText().toString();

                    haushaltsname = temp_haushaltsid;

                    requestQueue = Volley.newRequestQueue(getApplicationContext());

                    StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject obj = new JSONObject(response.toString());

                                JSONArray login = obj.getJSONArray("beitreten");

                                JSONObject loginerlaubnis_json = login.getJSONObject(0);

                                String loginerlaubnis = loginerlaubnis_json.getString("login");

                                //das Json Objekt loginerlaubnis speichert true, wenn HaushaltsID und Passwort korrekt waren, false wenn nicht
                                if (loginerlaubnis.equals("true")) {

                                    //Wenn die Anmeldedaten korrekt waren, liest das JsonObject den Haushaltsnamen aus
                                    JSONObject haushaltsname_json = login.getJSONObject(1);

                                    haushaltsid = haushaltsname_json.getInt("ID");

                                    SharedPreferences prefs = getSharedPreferences("sharedprefs", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor spe = prefs.edit();
                                    spe.putInt("HaushaltsID", haushaltsid);
                                    spe.putString("Haushaltsname", haushaltsname);
                                    spe.apply();

                                    Intent intent = new Intent(HaushaltBeitretenActivity.this, HauptmenueActivity.class);
                                    startActivity(intent);

                                }
                                else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "HaushaltsID oder Passwort falsch", Toast.LENGTH_SHORT);
                                    toast.show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                System.out.println("Fehler im Haushalt erstellen bei der Json Abfrage");
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
                            parameters.put("haushaltsname", haushaltsname);
                            parameters.put("benutzerid", ""+nutzerid);
                            parameters.put("passwort", et_passwort.getText().toString());

                            return parameters;
                        }
                    };

                    //fuegt die Werte der RequestQueue zu, sodass diese in die php Datei uebergeben werden koennen
                    requestQueue.add(request);

                }

            }
        });

        btn_neuenhaushalt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HaushaltBeitretenActivity.this, HaushaltErstellenActivity.class);
                startActivity(intent);
            }
        });

        btn_hilfe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HaushaltBeitretenActivity.this, HilfeActivity.class);
                startActivity(intent);
            }
        });

        btn_abbruch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (haushaltsid == -1) {

                    Intent intent = new Intent(HaushaltBeitretenActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(HaushaltBeitretenActivity.this, HauptmenueActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //Nichts machen wenn die Zurueck taste des handys benutzt wird
    }
}
