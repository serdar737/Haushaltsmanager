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

public class EinkaufslisteActivity extends AppCompatActivity {

    TextView tv_haushaltsname;
    FloatingActionButton fab_artikelhinzufuegen;
    LinearLayout linearlayoutartikel;
    Button btn_weiter;
    EditText et_artikelname, et_menge, et_kaufhaeufigkeit;
    String artikelname;
    CheckBox checkbox;
    int haushaltsid, menge;
    RequestQueue requestQueue;
    String insertUrl = "http://10.0.2.2:3306/artikelzueinkaufsliste.php";

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

                        artikelname = et_artikelname.getText().toString();
                        menge = Integer.parseInt(et_menge.getText().toString());
                        artikelCheckBoxNeu();
                        dialog.hide();
                    }
                });


                dialog.show();
            }
        });
    }

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
                //parameters.put("haushaltsid", haushaltsid);
                parameters.put("artikelname", artikelname);
                //parameters.put("menge", menge);
                return parameters;
            }
        };

        //fuegt die Werte der RequestQueue zu, sodass diese in die php Datei uebergeben werden koennen
        requestQueue.add(request);
    }

}
