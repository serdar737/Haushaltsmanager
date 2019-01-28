package com.example.ks.haushaltsmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class KontoErstellenActivity extends AppCompatActivity {

    EditText et_benutzername, et_name, et_email, et_passwort;
    Button btn_kontoeroeffnen;
    RequestQueue requestQueue;
    String insertUrl = "http://10.0.2.2:3306/erstellenutzerkonto.php";
    int benutzerid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_konto_erstellen);

        et_benutzername = findViewById(R.id.et_benutzernameerstellen);
        et_name = findViewById(R.id.et_nameerstellen);
        et_email = findViewById(R.id.et_maileingeben);
        et_passwort = findViewById(R.id.et_passworterstellen);
        btn_kontoeroeffnen = findViewById(R.id.btn_kontoeroeffnen);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        btn_kontoeroeffnen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                final String benutzername = et_benutzername.getText().toString();
                final String name = et_name.getText().toString();
                final String email = et_email.getText().toString();
                final String passwort = et_passwort.getText().toString();

                if (benutzername.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Bitte fülle alle Felder aus!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if (name.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Bitte fülle alle Felder aus!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if (email.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Bitte fülle alle Felder aus!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if (passwort.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Bitte fülle alle Felder aus!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {

                    StringRequest srequest = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject obj = new JSONObject(response);
                                benutzerid = obj.getInt("ID");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (benutzerid == -1) {
                                Toast toast = Toast.makeText(getApplicationContext(), "Benutzername bereits in Gebrauch!", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else {
                                SharedPreferences prefs = getSharedPreferences("sharedprefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor spe = prefs.edit();
                                spe.putInt("ID", benutzerid);
                                spe.putString("Benutzername", benutzername);
                                spe.putString("Name", name);
                                spe.apply();

                                Toast toast = Toast.makeText(getApplicationContext(), "Konto erfolgreich erstellt!", Toast.LENGTH_SHORT);
                                toast.show();

                                Intent intent = new Intent(KontoErstellenActivity.this, HaushaltBeitretenActivity.class);
                                startActivity(intent);
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
                            parameters.put("name", name);

                            parameters.put("email", email);
                            parameters.put("passwort", passwort);

                            return parameters;
                        }
                    };

                    //fuegt die Werte der RequestQueue zu, sodass diese in die php Datei uebergeben werden koennen
                    requestQueue.add(srequest);

                }
            }
        });
    }
}
