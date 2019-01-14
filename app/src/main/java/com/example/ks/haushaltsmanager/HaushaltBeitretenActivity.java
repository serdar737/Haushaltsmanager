package com.example.ks.haushaltsmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HaushaltBeitretenActivity extends AppCompatActivity {

    Button btn_beitreten, btn_abbruch, btn_hilfe, btn_neuenhaushalt;
    EditText et_haushaltsid, et_passwort;
    int nutzerid;

    RequestQueue requestQueue;
    String insertUrl = "http://10.0.2.2:3306/haushaltbeitreten.php";

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

        btn_beitreten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                requestQueue = Volley.newRequestQueue(getApplicationContext());

                StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //TODO: Bei richtiger Eingabe von Passwort und HaushaltsID wird die neue Activity geoeffnet
                        Intent intent = new Intent(HaushaltBeitretenActivity.this, HauptmenueActivity.class);
                        startActivity(intent);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("haushaltsid", et_haushaltsid.getText().toString());
                        parameters.put("passwort", et_passwort.getText().toString());
                        parameters.put("id", ""+nutzerid);

                        return parameters;
                    }
                };

                //fuegt die Werte der RequestQueue zu, sodass diese in die php Datei uebergeben werden koennen
                requestQueue.add(request);

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

            }
        });
    }
}
