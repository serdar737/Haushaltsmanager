package com.example.ks.haushaltsmanager;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ActivityDataSource extends AsyncTask<String, Void, String> {

    private TextView textView;

    private  URLConnection conn;

    public ActivityDataSource(TextView textView) {
        this.textView = textView;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            openConnection();
            return readResult();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Öffnet eine Verbindung {@link URLConnection}.
     * @throws IOException
     */
    private void openConnection() throws IOException{
        //Adresse der PHP Schnittstelle für die Verbindung zur MySQL Datenbank
        URL url = new URL("http://10.0.2.2:8080/quercus-4.0.39/reader.php");
        conn = url.openConnection();
        conn.setDoOutput(true);
    }

    /**
     * Ließt das Ergebnis aus der geöffneten Verbindung.
     * @return liefert ein String mit dem gelesenen Werten.
     * @throws IOException
     */
    private String readResult()throws IOException{
        String result = null;
        //Lesen der Rückgabewerte vom Server
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = null;
        //Solange Daten bereitstehen werden diese gelesen.
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        if(!isBlank(result)) {
            this.textView.setText(result);
        }
    }

    private boolean isBlank(String value){
        return value == null || value.trim().isEmpty();
    }


}
