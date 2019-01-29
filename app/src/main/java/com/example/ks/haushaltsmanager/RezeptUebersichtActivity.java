package com.example.ks.haushaltsmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

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

public class RezeptUebersichtActivity extends AppCompatActivity {

    LinearLayout linearlayoutrezepteuebersicht;
    FloatingActionButton fab_neuesrezept;
    Button btn_rezeptname;
    int haushaltsid, benutzerid;

    RequestQueue requestQueue;
    String insertUrl = "http://10.0.2.2:3306/zeigerezepte.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_rezept_uebersicht);

        SharedPreferences prefs = getSharedPreferences("sharedprefs", MODE_PRIVATE);
        haushaltsid = prefs.getInt("HaushaltsID", -1);
        benutzerid = prefs.getInt("ID", -1);

        linearlayoutrezepteuebersicht = findViewById(R.id.linearlayoutrezepte);
        fab_neuesrezept = findViewById(R.id.fab_artikelhinzufuegen);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray rezepte = jobj.getJSONArray("rezepte");
                    JSONArray rezeptids = jobj.getJSONArray("rezeptids");

                    for (int z = 0; z < rezepte.length(); z++) {
                        final JSONObject rezept = rezepte.getJSONObject(z);
                        final JSONObject id = rezeptids.getJSONObject(z);

                        btn_rezeptname = new Button(getApplicationContext());
                        btn_rezeptname.setText(id.getInt("ID")+" - "+rezept.getString("Rezeptname"));
                        linearlayoutrezepteuebersicht.addView(btn_rezeptname);

                        btn_rezeptname.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(RezeptUebersichtActivity.this, RezeptAnsichtActivity.class);
                                try {
                                    intent.putExtra("REZEPTNAME", rezept.getString("Rezeptname") );
                                    intent.putExtra("REZEPTID", id.getInt("ID") );
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                startActivity(intent);
                            }
                        });
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
                parameters.put("benutzerid", ""+benutzerid);

                return parameters;
            }
        };

        requestQueue.add(request);

        fab_neuesrezept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RezeptUebersichtActivity.this, RezeptErstellenActivity.class);
                startActivity(intent);
            }
        });

    }
}
