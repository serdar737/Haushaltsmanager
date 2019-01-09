package com.example.ks.haushaltsmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;

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

public class LoginActivity extends AppCompatActivity {

    EditText et_benutzername, et_passwortlogin;
    Button btn_login, btn_neueskonto;
    int nutzerid, haushaltsid;
    RequestQueue requestQueue;
    String insertUrl = "http://10.0.2.2:3306/htdocs/loginabfrage.php";
    String haushaltsname;
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

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                benutzername = et_benutzername.getText().toString();
                passwort = et_passwortlogin.getText().toString();

                nutzerid = 13;
                haushaltsid = 2;

                StringRequest loginrequest = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
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
                        parameters.put("benutzername", benutzername);
                        parameters.put("passwort", passwort);

                        return parameters;
                    }
                };

                //fuegt die Werte der RequestQueue zu, sodass diese in die php Datei uebergeben werden koennen
                //TODO: freischalten wenn Datenbankverbindung hergestellt werden soll
                //requestQueue.add(loginrequest);

                SharedPreferences idspeicher = getSharedPreferences("sharedprefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor spe = idspeicher.edit();
                spe.putInt("ID", (int)nutzerid);
                spe.putInt("HaushaltsID", (int)haushaltsid);
                spe.putString("Haushaltsname", haushaltsname);
                spe.commit();

                //TODO: NutzerID soll ueberprueft werden ob dazu mehrere haushalte vorliegen, wenn ja soll die Haushaltsauswahl geoeffnet werden
                //wenn nein, soll die HaushaltsID geholt werden und direkt ins Hauptmenue uebergegangen werden

                Intent intent = new Intent(LoginActivity.this, HauptmenueActivity.class);
                startActivity(intent);
            }
        });

        btn_neueskonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, KontoErstellenActivity.class);
                startActivity(intent);
            }
        });
    }
}
