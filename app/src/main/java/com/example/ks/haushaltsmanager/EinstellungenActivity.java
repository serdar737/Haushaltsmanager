package com.example.ks.haushaltsmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Die Activity Einstellungen beinhaltet die Benutzer und Haushaltseinstellungen.
 * Sie wird vom Hauptmenue aus aufgerufen.
 */
public class EinstellungenActivity extends AppCompatActivity {

    Button btn_haushaltwechseln, btn_hilfe, btn_ausloggen, btn_loeschen, btn_benutzerloeschen, btn_haushaltloeschen, btn_ja, btn_nein, btn_ja2, btn_nein2, btn_info;

    int haushaltsid, benutzerid;

    RequestQueue requestQueue, requestQueue2;

    String insertUrl = "http://10.0.2.2:3306/loeschehaushalt.php";
    String insertUrl2 = "http://10.0.2.2:3306/loeschenutzer.php";
    String haushaltsname;

    TextView tv_haushaltsname;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_einstellungen);

        SharedPreferences prefs = getSharedPreferences("sharedprefs", MODE_PRIVATE);
        haushaltsid = prefs.getInt("HaushaltsID", -1);
        benutzerid = prefs.getInt("ID", -1);
        haushaltsname = prefs.getString("Haushaltsname", "Unbekannter Haushalt");

        btn_haushaltwechseln = findViewById(R.id.btn_haushaltwechseln);
        btn_hilfe = findViewById(R.id.btn_hilfeaufrufen);
        btn_ausloggen = findViewById(R.id.btn_ausloggen);
        btn_loeschen = findViewById(R.id.btn_loeschen);
        btn_info = findViewById(R.id.btn_haushaltsinfo);

        //Dieser Button leitet den Benutzer in die Uebersicht seiner Haushalte weiter
        btn_haushaltwechseln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EinstellungenActivity.this, NutzerhaushalteActivity.class);
                startActivity(intent);
            }
        });

        //Dieser Button leitet den Benutzer in die Uebersicht des Haushalts weiter, in welchem er angemeldet ist
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EinstellungenActivity.this, HaushaltsInformationenActivity.class);
                startActivity(intent);
            }
        });

        //Dieser Button leitet den Benutzer zur HilfeActivity weiter, wo er schnell die wichtigsten Sachen nachlesen kann
        btn_hilfe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EinstellungenActivity.this, HilfeActivity.class);
                startActivity(intent);
            }
        });

        //Mit KLick auf diesen Button, loggt der Benutzer sich aus seinem Account aus und alle SharedPreferences werden auf Standard zurueckgesetzt
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

        /** Durch Nutzung des Loeschen Buttons, oeffnet sich ein popup Fenster in welchem der Benutzer aussuchen kann
         *  ob er nur seinen haushalt oder sein ganzes Benutzerkonto loeschen moechte.
         *  Bei der jeweiligen Auswahl, oeffnet sich ein neues Popup Fenster in welchem der Benutzer die
         *  unwiderrufliche Entscheidung bestaetigen oder den Vorgang abbrechen kann
         */
        btn_loeschen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder popupbuilder = new AlertDialog.Builder(EinstellungenActivity.this);
                final View popupviewkontenloeschen = getLayoutInflater().inflate(R.layout.popup_kontenloeschen, null);

                btn_benutzerloeschen = popupviewkontenloeschen.findViewById(R.id.btn_benutzerloeschen);
                btn_haushaltloeschen = popupviewkontenloeschen.findViewById(R.id.btn_haushaltloeschen);

                popupbuilder.setView(popupviewkontenloeschen);
                final AlertDialog dialogloeschen = popupbuilder.create();

                btn_benutzerloeschen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder popupbuilder3 = new AlertDialog.Builder(EinstellungenActivity.this);
                        View popupviewbenutzerloeschen = getLayoutInflater().inflate(R.layout.popup_benutzerloeschen, null);

                        btn_ja2 = popupviewbenutzerloeschen.findViewById(R.id.btn_bestaetigen2);
                        btn_nein2 = popupviewbenutzerloeschen.findViewById(R.id.btn_abbruch2);

                        popupbuilder3.setView(popupviewbenutzerloeschen);
                        final AlertDialog dialogloeschenbenutzer = popupbuilder3.create();

                        btn_ja2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                benutzerkontoLoeschen();
                                dialogloeschenbenutzer.hide();
                                dialogloeschen.hide();
                            }
                        });

                        btn_nein2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Wenn der Vorgang der Kontoloeschung abgebrochen wird, kerht der Benutzer zum vorherigen Fenster zurueck
                                dialogloeschenbenutzer.hide();
                            }
                        });

                        dialogloeschenbenutzer.show();
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

                                haushaltLoeschen();

                                dialogloeschenhaushalt.hide();
                                dialogloeschen.hide();

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

    /**
     * Wenn der Benutzer wuenscht sein Konto nicht laenger nutzen zu wollen, kann er es loeschen.
     * Dies geht nur, wenn er keinem Haushalt mehr zugeordnet ist.
     * Um sein Konto loeschen zu koennen, muss er sich also entweder erst aus allen Haushalten abmelden oder diese loeschen.
     * Nach dem das Konto geloescht wurde, wird er auf den Loginbildschirm gebracht
     */
    public void benutzerkontoLoeschen() {
        requestQueue2 = Volley.newRequestQueue(getApplicationContext());

        StringRequest loeschenrequest2 = new StringRequest(Request.Method.POST, insertUrl2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);

                    String geloescht = obj.getString("geloescht");

                    if (geloescht.equals("true")) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Benutzerkonto gelöscht!", Toast.LENGTH_SHORT);
                        toast.show();

                        SharedPreferences prefs = getSharedPreferences("sharedprefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor spe = prefs.edit();
                        spe.putInt("HaushaltsID", -1);
                        spe.putString("Haushaltsname", "Unbekannter Haushalt");
                        spe.putInt("ID", -1);
                        spe.apply();

                        Intent intent = new Intent(EinstellungenActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Konto kann nicht gelöscht werden, da du noch in Haushalten angemeldet bist.", Toast.LENGTH_LONG);
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
                parameters.put("haushaltsid", ""+haushaltsid);
                parameters.put("benutzerid", ""+benutzerid);


                return parameters;
            }
        };

        //fuegt die Werte der RequestQueue zu, sodass diese in die php Datei uebergeben werden koennen
        requestQueue2.add(loeschenrequest2);
    }

    /**
     * Mit dieser Methode wird der Haushalt geloescht, in welchem der Benutzer gerade angemeldet ist.
     */
    public void haushaltLoeschen() {
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest loeschenrequest = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);

                    String loeschbar = obj.getString("loeschbar");

                    if (loeschbar.equals("true")) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Haushalt gelöscht!", Toast.LENGTH_SHORT);
                        toast.show();

                        SharedPreferences prefs = getSharedPreferences("sharedprefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor spe = prefs.edit();
                        spe.putInt("HaushaltsID", -1);
                        spe.putString("Haushaltsname", "Unbekannter Haushalt");
                        spe.apply();

                        Intent intent = new Intent(EinstellungenActivity.this, EinstellungenActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Haushalt kann nicht  gelöscht werden! Es sind noch Nutzer angemeldet.", Toast.LENGTH_LONG);
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
                parameters.put("haushaltsid", ""+haushaltsid);


                return parameters;
            }
        };

        //fuegt die Werte der RequestQueue zu, sodass diese in die php Datei uebergeben werden koennen
        requestQueue.add(loeschenrequest);
    }
}
