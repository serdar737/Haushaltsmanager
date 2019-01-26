package com.example.ks.haushaltsmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class RezeptAnsichtActivity extends AppCompatActivity {

    TextView tv_rezeptname, tv_zutat;
    LinearLayout ll_zutaten, ll_beschreibung;
    RequestQueue requestQueue;
    String url = "http://10.0.2.2:3306/rezeptansicht.php";
    String rezeptname;
    int haushaltsid, zahl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_rezept_ansicht);

        SharedPreferences prefs = getSharedPreferences("sharedprefs", MODE_PRIVATE);
        haushaltsid = prefs.getInt("HaushaltsID", -1);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            rezeptname = extras.getString("REZEPTNAME");
        }

        tv_rezeptname = findViewById(R.id.tv_rezeptname_ru);
        ll_beschreibung = findViewById(R.id.ll_rezeptzutaten);
        ll_zutaten = findViewById(R.id.ll_rezeptbeschreibung);

        tv_rezeptname.setText(rezeptname);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject obj = new JSONObject(response.toString());
                    JSONArray rezept = obj.getJSONArray("rezeptzutaten");

                    zahl = 01;

                    for (int z = 0; z < rezept.length(); z++) {
                        final JSONObject zutatobj = rezept.getJSONObject(z);

                        tv_zutat = new TextView(getApplicationContext());
                        tv_zutat.setText(zutatobj.getString("Zutat01"));
                        zahl++;
                        ll_zutaten.addView(tv_zutat);
                    }

                }
                catch (JSONException e) {

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
                parameters.put("haushaltsid", ""+haushaltsid);
                parameters.put("rezeptname", rezeptname);

                return parameters;
            }
        };

        requestQueue.add(request);


    }
}
