package com.example.ks.haushaltsmanager;

import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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

/**
 * EinkaufslisteActivity
 * Diese Activity beinhaltet die Einkaufsliste des Haushaltes.
 * Sobald die Activity geoeffnet wird, wird eine DB Verbindung gestartet und ueber die String Request mithilfe der zugewiesenen
 * php-Datei, die Artikel ausgelesen und eine CheckBox mit dem Artikelnamen erstellt, die zu dem Haushalt gehoeren.
 */

public class EinkaufslisteActivity extends AppCompatActivity {

    Button btn_weiter;

    CheckBox checkbox;

    EditText et_artikelname, et_menge, et_kaufhaeufigkeit;

    FloatingActionButton fab_artikelhinzufuegen;

    int haushaltsid, menge;

    LinearLayout linearlayoutartikel;

    RequestQueue requestQueue, requestQueue2;

    String artikelname;
    String insertUrl = "http://10.0.2.2:3306/artikelzueinkaufsliste.php";
    String readUrl = "http://10.0.2.2:3306/zeigeeinkaufsliste.php";

    TextView tv_haushaltsname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_einkaufsliste);

        SharedPreferences prefs = getSharedPreferences("sharedprefs", MODE_PRIVATE);
        haushaltsid = prefs.getInt("HaushaltsID", -1);

        tv_haushaltsname = findViewById(R.id.tv_haushalteinkauf);
        fab_artikelhinzufuegen = findViewById(R.id.fab_artikelhinzufuegen);
        linearlayoutartikel = findViewById(R.id.linearlayoutartikel);

        leseEinkaufsliste();

        //FloatingActionButton oeffnet ein PopupFenster zum hinzufuegen eines neuen Artikels
        fab_artikelhinzufuegen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder popupbuilder = new AlertDialog.Builder(EinkaufslisteActivity.this);
                View popupview = getLayoutInflater().inflate(R.layout.popup_artikelhinzufuegen, null);
                btn_weiter = popupview.findViewById(R.id.btn_weiter_artikel);
                et_artikelname = popupview.findViewById(R.id.et_artikelname);
                et_menge = popupview.findViewById(R.id.et_menge);
                et_kaufhaeufigkeit = popupview.findViewById(R.id.et_kaufhaeufigkeit);

                popupbuilder.setView(popupview);
                final AlertDialog dialog = popupbuilder.create();

                btn_weiter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Eine Menge muss mit eingegeben werden, solange dies nicht der Fall ist, kann kein neuer Artikel hinzugefuegt werden
                        String tempmenge = et_menge.getText().toString();

                        if (tempmenge.equals("")) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Bitte Menge angeben", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else {
                            artikelname = et_artikelname.getText().toString();
                            menge = Integer.parseInt(tempmenge);
                            artikelCheckBoxNeu();
                            dialog.hide();
                        }
                    }
                });


                dialog.show();
            }
        });
    }

    /**
     * Die Methode artikelCheckBoxNeu erstellt mit der Eingabe des Artikelnamens eine neue Checkbox und fuegt diese
     * dem LinearLayout dynamisch hinzu
     */
    public void artikelCheckBoxNeu() {
        checkbox = new CheckBox(getApplicationContext());
        checkbox.setText(artikelname);
        linearlayoutartikel.addView(checkbox);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
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
                parameters.put("artikelname", artikelname);
                parameters.put("menge", ""+menge);
                return parameters;
            }
        };

        //fuegt die Werte der RequestQueue zu, sodass diese in die php Datei uebergeben werden koennen
        requestQueue.add(request);
    }

    /**
     * Die Methode leseEinkaufsliste wird im onCreate der Activity aufgerufen und beinhaltet die StringRequest um aus der DB die
     * Artikel die zu der Einkaufsliste des Haushaltes gehoeren zu lesen
     */
    public void leseEinkaufsliste() {

        requestQueue2 = Volley.newRequestQueue(getApplicationContext());

        StringRequest liste_request = new StringRequest(Request.Method.POST, readUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray rezepte = jobj.getJSONArray("liste");

                    for (int z = 0; z < rezepte.length(); z++) {
                        JSONObject rezept = rezepte.getJSONObject(z);

                        CheckBox checkbox_liste = new CheckBox(getApplicationContext());
                        checkbox_liste.setText(rezept.getString("Artikelname"));
                        linearlayoutartikel.addView(checkbox_liste);
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

        requestQueue2.add(liste_request);
    }

}
