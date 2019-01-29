package com.example.ks.haushaltsmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class HaushaltsInformationenActivity extends AppCompatActivity {

    Button btn_verlassen;
    boolean verlassen;
    TextView tv_haushaltsnameinfo;
    int benutzerid, haushaltsid;
    String haushaltsname, name;
    LinearLayout ll_mitglieder;
    RequestQueue requestQueue, requestQueue2;
    TextView mitglied;
    String url = "http://10.0.2.2:3306/zeigemitglieder.php";
    String url2 = "http://10.0.2.2:3306/verlassehaushalt.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_haushalts_informationen);

        SharedPreferences prefs = getSharedPreferences("sharedprefs", MODE_PRIVATE);
        benutzerid = prefs.getInt("ID", -1);
        haushaltsname = prefs.getString("Haushaltsname", "Unbekannter Haushalt");
        haushaltsid = prefs.getInt("HaushaltsID", -1);

        btn_verlassen = findViewById(R.id.btn_haushaltverlassen);
        tv_haushaltsnameinfo = findViewById(R.id.tv_infohaushaltsname);

        ll_mitglieder = findViewById(R.id.ll_haushaltsinfo);

        tv_haushaltsnameinfo.setText(haushaltsname);

        zeigeHaushaltsMitglieder();

        btn_verlassen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verlasseHaushalt();
            }
        });
    }

    public void zeigeHaushaltsMitglieder() {

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response.toString());
                    JSONArray bname = obj.getJSONArray("name");

                    for (int z = 0; z < bname.length(); z++) {
                        final JSONObject name_json = bname.getJSONObject(z);

                        mitglied = new TextView(getApplicationContext());
                        mitglied.setText(name_json.getString("Name"));
                        ll_mitglieder.addView(mitglied);
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
                parameters.put("haushaltsid", ""+haushaltsid);

                return parameters;
            }
        };

        requestQueue.add(request);

    }

    public void verlasseHaushalt() {

        requestQueue2 = Volley.newRequestQueue(getApplicationContext());

        StringRequest vrequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    verlassen = obj.getBoolean("loeschenerfolgreich");

                    if (verlassen == true) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Haushalt erfolgreich verlassen!", Toast.LENGTH_SHORT);
                        toast.show();

                        SharedPreferences prefs = getSharedPreferences("sharedprefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor spe = prefs.edit();
                        spe.putInt("HaushaltsID", -1);
                        spe.putString("Haushaltsname", "Unbekannter Haushalt");
                        spe.apply();

                        Intent intent = new Intent(HaushaltsInformationenActivity.this, EinstellungenActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Fehler! Bitte probiere es noch einmal", Toast.LENGTH_SHORT);
                        toast.show();
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
                parameters.put("haushaltsid", ""+haushaltsid);
                parameters.put("benutzerid", ""+benutzerid);

                return parameters;
            }
        };

        requestQueue2.add(vrequest);
    }
}
