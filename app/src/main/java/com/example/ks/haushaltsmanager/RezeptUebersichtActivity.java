package com.example.ks.haushaltsmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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
    int haushaltsid;

    RequestQueue requestQueue;
    String insertUrl = "http://10.0.2.2:3306/zeigerezepte.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_rezept_uebersicht);

        SharedPreferences prefs = getSharedPreferences("sharedprefs", MODE_PRIVATE);
        haushaltsid = prefs.getInt("HaushaltsID", -1);

        linearlayoutrezepteuebersicht = findViewById(R.id.linearlayoutrezepte);
        fab_neuesrezept = findViewById(R.id.fab_artikelhinzufuegen);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    System.out.println("Im onResponse");
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray rezepte = jobj.getJSONArray("rezepte");

                    for (int z = 0; z < rezepte.length(); z++) {
                        JSONObject rezept = rezepte.getJSONObject(z);

                        //String rezeptname = rezept.getString("Name");
                        //System.out.println(rezept.getString("Name"));
                        btn_rezeptname = new Button(getApplicationContext());
                        btn_rezeptname.setText(rezept.getString("Name"));
                        linearlayoutrezepteuebersicht.addView(btn_rezeptname);
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
