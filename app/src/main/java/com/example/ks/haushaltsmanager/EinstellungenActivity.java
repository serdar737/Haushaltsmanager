package com.example.ks.haushaltsmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class EinstellungenActivity extends AppCompatActivity {

    Button btn_haushaltwechseln, btn_hilfe, btn_ausloggen, btn_loeschen, btn_benutzerloeschen, btn_haushaltloeschen, btn_ja, btn_nein;
    int haushaltsid;
    TextView tv_haushaltsname;
    String haushaltsname;

    RequestQueue requestQueue;
    String insertUrl = "http://10.0.2.2:3306/loeschehaushalt.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_einstellungen);

        SharedPreferences prefs = getSharedPreferences("sharedprefs", MODE_PRIVATE);
        haushaltsid = prefs.getInt("HaushaltsID", -1);
        haushaltsname = prefs.getString("Haushaltsname", "Unbekannter Haushalt");

        btn_haushaltwechseln = findViewById(R.id.btn_haushaltwechseln);
        btn_hilfe = findViewById(R.id.btn_hilfeaufrufen);
        btn_ausloggen = findViewById(R.id.btn_ausloggen);
        btn_loeschen = findViewById(R.id.btn_loeschen);

        btn_haushaltwechseln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EinstellungenActivity.this, NutzerhaushalteActivity.class);
                startActivity(intent);
            }
        });

        btn_hilfe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EinstellungenActivity.this, HilfeActivity.class);
                startActivity(intent);
            }
        });

        btn_ausloggen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences prefs = getSharedPreferences("sharedprefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor spe = prefs.edit();
                spe.putInt("ID", -1);
                spe.putInt("HaushaltsID", -1);
                spe.putString("Haushaltsname", "Unbekannter Haushalt");
                spe.apply();

                Intent intent = new Intent(EinstellungenActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_loeschen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder popupbuilder = new AlertDialog.Builder(EinstellungenActivity.this);
                View popupviewkontenloeschen = getLayoutInflater().inflate(R.layout.popup_kontenloeschen, null);

                btn_benutzerloeschen = popupviewkontenloeschen.findViewById(R.id.btn_benutzerloeschen);
                btn_haushaltloeschen = popupviewkontenloeschen.findViewById(R.id.btn_haushaltloeschen);

                popupbuilder.setView(popupviewkontenloeschen);
                final AlertDialog dialogloeschen = popupbuilder.create();

                btn_benutzerloeschen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialogloeschen.hide();
                    }
                });

                btn_haushaltloeschen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder popupbuilder2 = new AlertDialog.Builder(EinstellungenActivity.this);
                        View popupviewhaushaltloeschen = getLayoutInflater().inflate(R.layout.popup_haushaltloeschen, null);

                        btn_ja = popupviewhaushaltloeschen.findViewById(R.id.btn_bestaetigen);
                        btn_nein = popupviewhaushaltloeschen.findViewById(R.id.btn_abbruch);
                        tv_haushaltsname = popupviewhaushaltloeschen.findViewById(R.id.tv_haushaltsname);

                        tv_haushaltsname.setText(haushaltsname);

                        popupbuilder2.setView(popupviewhaushaltloeschen);
                        final AlertDialog dialogloeschenhaushalt = popupbuilder2.create();

                        btn_ja.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                requestQueue = Volley.newRequestQueue(getApplicationContext());

                                StringRequest loeschenrequest = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
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
                                        parameters.put("haushaltsid", ""+haushaltsid);


                                        return parameters;
                                    }
                                };

                                //fuegt die Werte der RequestQueue zu, sodass diese in die php Datei uebergeben werden koennen
                                requestQueue.add(loeschenrequest);

                                dialogloeschenhaushalt.hide();
                                dialogloeschen.hide();

                                Intent intent = new Intent(EinstellungenActivity.this, NutzerhaushalteActivity.class);
                                startActivity(intent);

                            }
                        });

                        btn_nein.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                //Wenn der Vorgang des Loeschens abgebrochen wird, kehrt der Nutzer in die Einstellungen zurück
                                dialogloeschenhaushalt.hide();
                            }
                        });

                        dialogloeschenhaushalt.show();

                    }

                });

                dialogloeschen.show();
            }
        });
    }
}
