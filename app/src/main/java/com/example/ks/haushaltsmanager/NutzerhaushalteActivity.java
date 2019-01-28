package com.example.ks.haushaltsmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
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

public class NutzerhaushalteActivity extends AppCompatActivity {

    LinearLayout llnutzerhaushalte;
    Button btn_haushalt;
    TextView tv_benutzername;
    String name, haushaltsname;
    FloatingActionButton fab_erstellen;

    int benutzerid, haushaltsid;

    RequestQueue requestQueue;
    String insertUrl = "http://10.0.2.2:3306/zeigenutzerhaushalte.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_nutzerhaushalte);

        SharedPreferences prefs = getSharedPreferences("sharedprefs", MODE_PRIVATE);
        benutzerid = prefs.getInt("ID", -1);
        name = prefs.getString("Name", "Unbekannter Nutzer");

        fab_erstellen = findViewById(R.id.fab_haushalterstellen);
        llnutzerhaushalte = findViewById(R.id.ll_nutzerhaushalte);
        tv_benutzername = findViewById(R.id.tv_benutzernameuebersicht);
        tv_benutzername.setText(name);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response.toString());
                    JSONArray id = obj.getJSONArray("id");
                    JSONArray name = obj.getJSONArray("name");

                    for (int z = 0; z < id.length(); z++) {
                        final JSONObject haushaltsid_json = id.getJSONObject(z);
                        final JSONObject haushaltsname_json = name.getJSONObject(z);

                        haushaltsid = haushaltsid_json.getInt("HaushaltsID");
                        haushaltsname = haushaltsname_json.getString("Haushaltsname");

                        btn_haushalt = new Button(getApplicationContext());
                        btn_haushalt.setText(haushaltsname+" ["+haushaltsid+"]");
                        llnutzerhaushalte.addView(btn_haushalt);

                        btn_haushalt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                SharedPreferences prefs = getSharedPreferences("sharedprefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor spe = prefs.edit();
                                spe.putInt("ID", benutzerid);
                                try {
                                    spe.putInt("HaushaltsID", haushaltsid_json.getInt("HaushaltsID"));
                                    spe.putString("Haushaltsname", haushaltsname_json.getString("Haushaltsname"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                spe.apply();

                                Intent intent = new Intent(NutzerhaushalteActivity.this, HauptmenueActivity.class);
                                startActivity(intent);
                            }
                        });
                    }

                }
                catch (JSONException e) {
                    System.out.println("JsonException");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ResponseException");
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String, String> parameters = new HashMap<String, String>();
                parameters.put("benutzerid", ""+benutzerid);

                return parameters;
            }
        };

        requestQueue.add(request);

        fab_erstellen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(NutzerhaushalteActivity.this, HaushaltErstellenActivity.class);
                startActivity(intent);
            }
        });
    }
}
