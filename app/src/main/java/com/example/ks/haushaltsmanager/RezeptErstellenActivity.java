package com.example.ks.haushaltsmanager;


import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;

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



public class RezeptErstellenActivity extends AppCompatActivity {

    EditText et_rezeptname, et_personenanzahl;
    Button btn_weiter;
    String rezeptname;
    int personen, haushaltsid, benutzerid;
    RequestQueue requestQueue;
    String insertUrl = "http://10.0.2.2:3306/erstellerezept.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_rezept_erstellen);

        SharedPreferences prefs = getSharedPreferences("sharedprefs", MODE_PRIVATE);
        benutzerid = prefs.getInt("ID", -1);
        haushaltsid = prefs.getInt("HaushaltsID", -1);

        et_rezeptname = findViewById(R.id.et_rezeptbenennen);
        et_personenanzahl = findViewById(R.id.et_personenanzahl);
        btn_weiter = findViewById(R.id.btn_rezeptnameweiter);

        btn_weiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rezeptname = et_rezeptname.getText().toString();
                personen = Integer.parseInt(et_personenanzahl.getText().toString());

                if (rezeptname.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Bitte fülle alle Felder aus.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if (personen == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Bitte fülle alle Felder aus.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    erstelleRezept();
                }
            }
        });
    }

    public void erstelleRezept() {

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest srequest = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response.toString());
                    JSONArray id = obj.getJSONArray("array_id");
                    JSONArray sicher = obj.getJSONArray("array_sicherheit");

                    final JSONObject obj_sicherheit = sicher.getJSONObject(0);
                    final JSONObject obj_id = id.getJSONObject(0);

                    boolean sicherheit = obj_sicherheit.getBoolean("sicherheit");
                    int rezeptid = obj_id.getInt("ID");

                    if (sicherheit == false) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Rezept konnte nicht erstellt werden. Bitte versuche es nochmal.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else {
                        Intent intent = new Intent(RezeptErstellenActivity.this, RezeptBearbeitenActivity.class);
                        intent.putExtra("REZEPTNAME", rezeptname);
                        intent.putExtra("REZEPTID", rezeptid);
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
                parameters.put("haushaltsid", ""+haushaltsid);
                parameters.put("benutzerid", ""+benutzerid);
                parameters.put("rezeptname", rezeptname);
                parameters.put("personen", ""+personen );


                return parameters;
            }
        };

        //fuegt die Werte der RequestQueue zu, sodass diese in die php Datei uebergeben werden koennen
        requestQueue.add(srequest);

    }
}
