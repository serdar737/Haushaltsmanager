package com.example.ks.haushaltsmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

/**
 * Dieser Bildschirm zeigt den Haushaltsbestand des Haushaltes an
 */

public class HaushaltsbestandActivity extends AppCompatActivity {

    TextView tv_haushaltsname;
    Button btn_artikel;
    LinearLayout llhaushaltsbestand;
    int haushaltsid;
    String artikelname;
    String url = "http://10.0.2.2:3306/zeigehaushaltsbestand.php";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_haushaltsbestand);

        SharedPreferences prefs = getSharedPreferences("sharedprefs", MODE_PRIVATE);
        haushaltsid = prefs.getInt("HaushaltsID", -1);

        tv_haushaltsname = findViewById(R.id.tv_haushaltsnamehb);
        llhaushaltsbestand = findViewById(R.id.linearlayouthaushaltsbestand);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response.toString());
                    JSONArray bestandsliste = obj.getJSONArray("haushaltsbestand");

                    for (int z = 0; z < bestandsliste.length(); z++) {
                        JSONObject artikel = bestandsliste.getJSONObject(z);

                       btn_artikel = new Button(getApplicationContext());
                       btn_artikel.setText(artikel.getString("Artikelname"));
                       llhaushaltsbestand.addView(btn_artikel);
                       btn_artikel.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {

                               //Intent intent = new Intent(HaushaltsbestandActivity.this, RezeptErstellenActivity.class);
                               //startActivity(intent);
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

                return parameters;
            }
        };

        requestQueue.add(request);
    }

}
