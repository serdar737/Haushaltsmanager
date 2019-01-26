package com.example.ks.haushaltsmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;

import android.support.v7.app.AppCompatActivity;

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

public class LoginActivity extends AppCompatActivity {

    EditText et_benutzername, et_passwortlogin;
    Button btn_login, btn_neueskonto;
    int haushaltsid, benutzerid;
    RequestQueue requestQueue;
    String insertUrl = "http://10.0.2.2:3306/login.php";
    String benutzername, passwort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_login);

        et_benutzername = findViewById(R.id.et_benutzername);
        et_passwortlogin = findViewById(R.id.et_passwortlogin);
        btn_login = findViewById(R.id.btn_login);
        btn_neueskonto = findViewById(R.id.btn_neueskonto);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        SharedPreferences prefs = getSharedPreferences("sharedprefs", MODE_PRIVATE);
        haushaltsid = prefs.getInt("HaushaltsID", -1);

        if (haushaltsid == -1) {
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    benutzername = et_benutzername.getText().toString();
                    passwort = et_passwortlogin.getText().toString();

                    StringRequest loginrequest = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject obj = new JSONObject(response);

                                JSONArray login = obj.getJSONArray("einloggen");

                                JSONObject loginerlaubnis_json = login.getJSONObject(0);

                                String loginerlaubnis = loginerlaubnis_json.getString("login");

                                if (loginerlaubnis.equals("true")) {

                                    JSONObject benutzerid_json = login.getJSONObject(1);

                                    benutzerid = benutzerid_json.getInt("ID");

                                    JSONObject haushalte_json = login.getJSONObject(2);

                                    String haushalte = haushalte_json.getString("mehrhaushalte");

                                    if (haushalte.equals("false")) {

                                        JSONObject haushaltsid_json = login.getJSONObject(3);

                                        haushaltsid = haushaltsid_json.getInt("HaushaltsID");

                                        SharedPreferences idspeicher = getSharedPreferences("sharedprefs", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor spe = idspeicher.edit();
                                        spe.putInt("ID", benutzerid);
                                        spe.putInt("HaushaltsID", haushaltsid);
                                        spe.putString("Haushaltsname", "Haushalt: "+haushaltsid);
                                        spe.commit();

                                        System.out.println("Nutzerhaushalte: "+haushaltsid+", "+benutzerid);

                                        Intent intent = new Intent(LoginActivity.this, HauptmenueActivity.class);
                                        startActivity(intent);
                                    }
                                    else {

                                        SharedPreferences idspeicher = getSharedPreferences("sharedprefs", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor spe = idspeicher.edit();
                                        spe.putInt("ID", benutzerid);
                                        spe.putInt("HaushaltsID", -1);
                                        spe.putString("Haushaltsname", "Unbekannter Haushalt");
                                        spe.commit();

                                        System.out.println("Nutzerhaushalte: "+haushaltsid+", "+benutzerid);

                                        Intent intent = new Intent(LoginActivity.this, NutzerhaushalteActivity.class);
                                        startActivity(intent);

                                    }

                                }
                                else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Benutzername oder Passwort falsch", Toast.LENGTH_SHORT);
                                    toast.show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
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
                            parameters.put("benutzername", benutzername);
                            parameters.put("passwort", passwort);

                            return parameters;
                        }
                    };

                    requestQueue.add(loginrequest);


                }
            });
        }
        else {
            Intent intent = new Intent(LoginActivity.this, HauptmenueActivity.class);
            startActivity(intent);
        }



        btn_neueskonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, KontoErstellenActivity.class);
                startActivity(intent);
            }
        });
    }
}
