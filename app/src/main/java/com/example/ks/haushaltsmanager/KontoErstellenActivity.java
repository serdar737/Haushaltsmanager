package com.example.ks.haushaltsmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class KontoErstellenActivity extends AppCompatActivity {

    EditText et_benutzername, et_name, et_email, et_passwort;
    Button btn_kontoeroeffnen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konto_erstellen);

        et_benutzername = findViewById(R.id.et_benutzernameerstellen);
        et_name = findViewById(R.id.et_nameerstellen);
        et_email = findViewById(R.id.et_maileingeben);
        et_passwort = findViewById(R.id.et_passworterstellen);
        btn_kontoeroeffnen = findViewById(R.id.btn_kontoeroeffnen);

        btn_kontoeroeffnen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KontoErstellenActivity.this, HaushaltBeitretenActivity.class);
                //intent.putExtra("BENUTZERNAME_UEBERGABE", benutzername);
                startActivity(intent);

            }
        });
    }
}
