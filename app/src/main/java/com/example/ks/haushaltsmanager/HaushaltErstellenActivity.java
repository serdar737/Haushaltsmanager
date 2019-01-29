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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HaushaltErstellenActivity extends AppCompatActivity {

    EditText et_haushalterstellenname, et_haushalterstellenbeschreibung, et_haushalterstellenpasswort;
    Button btn_haushalterstellen, btn_haushaltbeitreten;

    RequestQueue requestQueue;
    String insertUrl = "http://10.0.2.2:3306/erstellehaushalt.php";
    int benutzerid, haushaltsid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_haushalt_erstellen);

        SharedPreferences prefs = getSharedPreferences("sharedprefs", MODE_PRIVATE);
        benutzerid = prefs.getInt("ID", -1);

        et_haushalterstellenname = findViewById(R.id.et_haushalterstellenname);
        et_haushalterstellenpasswort = findViewById(R.id.et_haushalterstellenpasswort);
        et_haushalterstellenbeschreibung = findViewById(R.id.editText3);
        btn_haushalterstellen = findViewById(R.id.btn_haushalterstellen);
        btn_haushaltbeitreten = findViewById(R.id.btn_haushalbeitreten);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        btn_haushalterstellen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String haushaltname = et_haushalterstellenname.getText().toString();
                final String haushaltbeschreibung = et_haushalterstellenbeschreibung.getText().toString();
                final String haushaltpasswort = et_haushalterstellenpasswort.getText().toString();

                if (haushaltname.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Bitte fülle alle Felder aus!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if (haushaltpasswort.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Bitte fülle alle Felder aus!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    StringRequest srequest = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject obj = new JSONObject(response);
                                haushaltsid = obj.getInt("haushaltsid");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (haushaltsid == -1) {
                                Toast toast = Toast.makeText(getApplicationContext(), "Haushaltsname bereits vergeben!", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else {
                                SharedPreferences prefs = getSharedPreferences("sharedprefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor spe = prefs.edit();
                                spe.putInt("HaushaltsID", haushaltsid);
                                spe.putString("Haushaltsname", haushaltname);
                                spe.apply();

                                Toast toast = Toast.makeText(getApplicationContext(), "Haushalt erfolgreich erstellt!", Toast.LENGTH_SHORT);
                                toast.show();

                                Intent intent = new Intent(HaushaltErstellenActivity.this, HauptmenueActivity.class);
                                startActivity(intent);
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map <String, String> parameters = new HashMap<String, String>();
                            parameters.put("haushaltsname", haushaltname);
                            parameters.put("passwort", haushaltpasswort);
                            parameters.put("beschreibung", haushaltbeschreibung);
                            parameters.put("benutzerid", ""+benutzerid);

                            return parameters;
                        }
                    };

                    //fuegt die Werte der RequestQueue zu, sodass diese in die php Datei uebergeben werden koennen
                    requestQueue.add(srequest);
                }


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

    @Override
    public void onBackPressed() {
        //Nichts machen wenn die Zurueck taste des handys benutzt wird
    }
}
