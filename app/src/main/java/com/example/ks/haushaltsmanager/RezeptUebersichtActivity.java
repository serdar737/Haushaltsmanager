package com.example.ks.haushaltsmanager;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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
    int haushaltsid = 1;

    RequestQueue requestQueue;
    String insertUrl = "http://10.0.2.2:3306/htdocs/zeigerezepte.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_rezept_uebersicht);

        linearlayoutrezepteuebersicht = findViewById(R.id.linearlayoutartikel);
        fab_neuesrezept = findViewById(R.id.fab_artikelhinzufuegen);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        Map<String, Integer> params = new HashMap();
        params.put("haushaltsid", haushaltsid);

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, insertUrl, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray rezepte = response.getJSONArray("rezepte");
                    for (int i = 0; i < rezepte.length(); i++ ) {
                        JSONObject rezept = rezepte.getJSONObject(i);

                        String rezeptname = rezept.getString("Name");

                        btn_rezeptname.setText(rezeptname);
                        linearlayoutrezepteuebersicht.addView(btn_rezeptname);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonrequest);

        fab_neuesrezept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RezeptUebersichtActivity.this, RezeptErstellenActivity.class);
                startActivity(intent);
            }
        });
    }
}
