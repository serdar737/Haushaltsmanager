package com.example.ks.haushaltsmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class RezeptAnsichtActivity extends AppCompatActivity {

    Animation fab_klein_oeffnen, fab_klein_schliessen;
    boolean menueoffen = false;
    TextView tv_rezeptname, tv_zutat, tv_beschreibung;
    LinearLayout ll_zutaten, ll_beschreibung;
    RequestQueue requestQueue, requestQueue2;
    String url = "http://10.0.2.2:3306/rezeptansicht.php";
    String loeschenurl = "http://10.0.2.2:3306/loescherezept.php";
    String rezeptname;
    int haushaltsid, zahl, rezeptid;
    FloatingActionButton fab_loeschen, fab_bearbeiten, fab_menue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_rezept_ansicht);

        SharedPreferences prefs = getSharedPreferences("sharedprefs", MODE_PRIVATE);
        haushaltsid = prefs.getInt("HaushaltsID", -1);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            rezeptname = extras.getString("REZEPTNAME");
            rezeptid = extras.getInt("REZEPTID");
        }

        System.out.println(rezeptid);

        tv_rezeptname = findViewById(R.id.tv_rezeptname_ru);
        ll_beschreibung = findViewById(R.id.ll_rezeptzutaten);
        ll_zutaten = findViewById(R.id.ll_rezeptbeschreibung);
        fab_bearbeiten = findViewById(R.id.fab_rezeptbearbeiten);
        fab_loeschen = findViewById(R.id.fab_rezeptloeschen);
        fab_menue = findViewById(R.id.fab_rezeptmenue);

        //Die Animation Resource Files der Animationen fuer die FABs den Animationsvariabeln zuweisen
        fab_klein_oeffnen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_klein_oeffnen);
        fab_klein_schliessen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_klein_schliessen);

        tv_rezeptname.setText(rezeptname);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject obj = new JSONObject(response);
                    JSONArray j_zutat = obj.getJSONArray("zutat");
                    JSONArray j_menge = obj.getJSONArray("menge");
                    JSONArray j_masseinheit = obj.getJSONArray("masseinheit");
                    JSONArray j_beschreibung = obj.getJSONArray("beschreibung");

                    for (int z = 0; z < j_zutat.length(); z++) {
                        final JSONObject zutatobj = j_zutat.getJSONObject(z);
                        final JSONObject mengeobj = j_menge.getJSONObject(z);
                        final JSONObject masseinheitobj = j_masseinheit.getJSONObject(z);

                        tv_zutat = new TextView(getApplicationContext());
                        tv_zutat.setText(zutatobj.getString("Zutat")+" - "+mengeobj.getInt("Menge")+" "+masseinheitobj.getString("Masseinheit"));
                        ll_zutaten.addView(tv_zutat);
                    }

                    final JSONObject beschreibungobj = j_beschreibung.getJSONObject(0);

                    tv_beschreibung = new TextView(getApplicationContext());
                    tv_beschreibung.setText(beschreibungobj.getString("Beschreibung"));
                    ll_beschreibung.addView(tv_beschreibung);

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
                parameters.put("rezeptid", ""+rezeptid);

                return parameters;
            }
        };

        requestQueue.add(request);


        //OnClickListener fuer den Button welcher ein kleines Menue oeffnet, welches aus zwei weiteren FABs besteht
        fab_menue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (menueoffen == false) {
                    //Mit KLick auf den Menue Button oeffnet sich das kleine Menue daneben, bestehend aus dem Loschen und dem Bearbeiten Button
                    animationstarten();
                }
                else {
                    //Wenn die Buttons des Menues bereits geoeffnet sind und man klickt ein zweites Mal auf den Button, schließt das Menue sich
                    animationbeenden();
                }
            }
        });

        //OnClickListener fuer einen Button mit welchem man das gewaehlte Rezept loeschen kann
        fab_loeschen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rezeptloeschen();

            }
        });

        //OnCLickListener fuer einen Button, welcher einem in die Bearbeitungsansicht des Rezepts weiterleitet
        fab_bearbeiten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RezeptAnsichtActivity.this, RezeptBearbeitenActivity.class);
                intent.putExtra("REZEPTNAME", rezeptname);
                intent.putExtra("REZEPTID", rezeptid);
                startActivity(intent);
            }
        });

    }

    public void animationstarten() {
        fab_loeschen.startAnimation(fab_klein_oeffnen);
        fab_bearbeiten.startAnimation(fab_klein_oeffnen);
        fab_loeschen.setClickable(true);
        fab_bearbeiten.setClickable(true);
        menueoffen = true;
    }

    public void animationbeenden () {
        fab_loeschen.startAnimation(fab_klein_schliessen);
        fab_bearbeiten.startAnimation(fab_klein_schliessen);
        fab_loeschen.setClickable(false);
        fab_bearbeiten.setClickable(false);
        menueoffen = false;
    }

    public void rezeptloeschen() {
        requestQueue2 = Volley.newRequestQueue(getApplicationContext());

        final int temp_rezeptid = rezeptid;

        StringRequest loeschenrequest = new StringRequest(Request.Method.POST, loeschenurl, new Response.Listener<String>() {
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
                parameters.put("rezeptid", ""+temp_rezeptid);

                return parameters;
            }
        };

        //fuegt die Werte der RequestQueue zu, sodass diese in die php Datei uebergeben werden koennen
        requestQueue.add(loeschenrequest);

        Intent intent = new Intent(RezeptAnsichtActivity.this, RezeptUebersichtActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RezeptAnsichtActivity.this, RezeptUebersichtActivity.class);
        startActivity(intent);
    }
}
