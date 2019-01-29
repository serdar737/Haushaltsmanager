package com.example.ks.haushaltsmanager;

import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    Button btn_artikel, btn_weiter;
    LinearLayout llhaushaltsbestand;
    int haushaltsid;
    String artikelname, haushaltsname;
    String url = "http://10.0.2.2:3306/zeigehaushaltsbestand.php";
    String gurl = "http://10.0.2.2:3306/artikelhaushaltupdaten.php";
    RequestQueue requestQueue, requestQueue2;
    EditText et_menge, et_verfallsdatum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_haushaltsbestand);

        SharedPreferences prefs = getSharedPreferences("sharedprefs", MODE_PRIVATE);
        haushaltsid = prefs.getInt("HaushaltsID", -1);
        haushaltsname = prefs.getString("Haushaltsname", "Unbekannter Haushalt");

        tv_haushaltsname = findViewById(R.id.tv_haushaltsnamehb);
        llhaushaltsbestand = findViewById(R.id.linearlayouthaushaltsbestand);

        tv_haushaltsname.setText(haushaltsname);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response.toString());
                    JSONArray bestandsliste = obj.getJSONArray("haushaltsbestandsliste");
                    JSONArray id = obj.getJSONArray("artikelids");

                    for (int z = 0; z < bestandsliste.length(); z++) {
                        JSONObject artikel = bestandsliste.getJSONObject(z);
                        final JSONObject artikelids = id.getJSONObject(z);

                        final String artikelid = artikelids.getString("ID");

                       btn_artikel = new Button(getApplicationContext());
                       btn_artikel.setText(artikel.getString("Artikelname"));
                       llhaushaltsbestand.addView(btn_artikel);
                       btn_artikel.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {

                               AlertDialog.Builder popupbuilder = new AlertDialog.Builder(HaushaltsbestandActivity.this);
                               View popupview = getLayoutInflater().inflate(R.layout.popup_artikelinfo, null);

                               et_menge = popupview.findViewById(R.id.et_mengebestand);
                               et_verfallsdatum = popupview.findViewById(R.id.et_verfallsdatum);
                               btn_weiter = popupview.findViewById(R.id.btn_weiter_bestand);

                               popupbuilder.setView(popupview);
                               final AlertDialog dialog = popupbuilder.create();

                               btn_weiter.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {

                                       requestQueue2 = Volley.newRequestQueue(getApplicationContext());

                                       StringRequest arequest = new StringRequest(Request.Method.POST, gurl, new Response.Listener<String>() {

                                           @Override
                                           public void onResponse(String response) {

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
                                               parameters.put("artikelid", ""+artikelid);

                                               return parameters;
                                           }
                                       };

                                       //fuegt die Werte der RequestQueue zu, sodass diese in die php Datei uebergeben werden koennen
                                       requestQueue2.add(arequest);
                                   }
                               });

                               dialog.show();
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
