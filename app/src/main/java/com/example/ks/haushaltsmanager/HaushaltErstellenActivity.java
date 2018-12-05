package com.example.ks.haushaltsmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class HaushaltErstellenActivity extends AppCompatActivity {

    EditText et_haushalterstellenname, et_haushalterstellenanschrift, et_haushalterstellenbeschreibung, et_haushalterstellenpasswort;
    Button btn_haushalterstellen, btn_haushaltbeitreten;

    RequestQueue requestQueue;
    String insertUrl = "http://10.0.2.2:8080/quercus-4.0.39/erstellehaushalt.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_haushalt_erstellen);

        et_haushalterstellenname = findViewById(R.id.et_haushalterstellenname);
        et_haushalterstellenanschrift = findViewById(R.id.et_haushalterstellenanschrift);
        et_haushalterstellenpasswort = findViewById(R.id.et_haushalterstellenpasswort);
        et_haushalterstellenbeschreibung = findViewById(R.id.editText3);
        btn_haushalterstellen = findViewById(R.id.btn_haushalterstellen);
        btn_haushaltbeitreten = findViewById(R.id.btn_haushalbeitreten);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        btn_haushalterstellen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String haushaltname = et_haushalterstellenname.getText().toString();
                final String haushaltanschrift = et_haushalterstellenanschrift.getText().toString();
                final String haushaltbeschreibung = et_haushalterstellenbeschreibung.getText().toString();
                final String haushaltpasswort = et_haushalterstellenpasswort.getText().toString();



                StringRequest srequest = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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

                        return parameters;
                    }
                };

                //fuegt die Werte der RequestQueue zu, sodass diese in die php Datei uebergeben werden koennen
                requestQueue.add(srequest);
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
}
