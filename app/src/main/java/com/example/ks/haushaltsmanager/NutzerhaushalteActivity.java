package com.example.ks.haushaltsmanager;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
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

public class NutzerhaushalteActivity extends AppCompatActivity {

    LinearLayout llnutzerhaushalte;
    Button btn_haushalt;
    TextView tv_benutzername;
    String name;

    int benutzerid;

    RequestQueue requestQueue;
    String insertUrl = "http://10.0.2.2:3306/zeigehaushalte.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_nutzerhaushalte);

        SharedPreferences prefs = getSharedPreferences("sharedprefs", MODE_PRIVATE);
        benutzerid = prefs.getInt("ID", -1);
        name = prefs.getString("Name", "Unbekannter Nutzer");

        llnutzerhaushalte = findViewById(R.id.ll_nutzerhaushalte);
        tv_benutzername = findViewById(R.id.tv_benutzernameuebersicht);
        tv_benutzername.setText(name);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response.toString());
                    JSONArray haushalte = obj.getJSONArray("haushalte");

                    for (int z = 0; z < haushalte.length(); z++) {
                        JSONObject haushalt = haushalte.getJSONObject(z);

                        btn_haushalt = new Button(getApplicationContext());
                        btn_haushalt.setText(haushalt.getInt("HaushaltsID"));
                        llnutzerhaushalte.addView(btn_haushalt);
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
                parameters.put("benutzerid", ""+benutzerid);

                return parameters;
            }
        };

        requestQueue.add(request);
    }
}
