package com.example.pierre.test;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    private EditText pseudo;
    private TextView text;
    private TextView affichePseudo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.text = (TextView) findViewById(R.id.data);

        this.affichePseudo = (TextView) findViewById(R.id.pseudoMem);
        this.affichePseudo.setText("Bonjour "+this.getPseudo());

        Button connection = (Button) findViewById(R.id.button_connection);
        connection.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                getData();
            }
        });

        Button notifView = (Button) findViewById(R.id.button_notifView);
        notifView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //change view
                Intent intentMain = new Intent(MainActivity.this, NotificationPMC.class);
                startActivity(intentMain);
            }
        });

        Button searchView = (Button) findViewById(R.id.button_searchView);
        searchView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //change view
                Intent intentMain = new Intent(MainActivity.this, SearchViewPMC.class);
                startActivity(intentMain);
            }
        });

        Button pseudo = (Button) findViewById(R.id.save);
        pseudo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                EditText pseudoText = (EditText) findViewById(R.id.pseudoField);
                savePseudo(pseudoText.getText()+"");
            }
        });

        //suppression de la notif (si besoin) par l'id
        final NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);

        if(!isOnline())
                openPopup();
    }

    public void openPopup(){
        new AlertDialog.Builder(this)
                .setTitle("Pas de connexion internet")
                .setMessage("Il semble que vous ne soyez pas connecter à internet. Voulez-vous vous connecter ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS), 0);
                    }
                })
                .setNegativeButton("FERMER", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public String getPseudo(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        String pseudo = preferences.getString("pseudo", "Inconnu");
        return pseudo;
    }

    public void savePseudo(String pseudo){
        affichePseudo.setText("Nouveau pseudo : "+pseudo);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("pseudo", pseudo);
        editor.commit();
    }

    public void getData(){
        //get data
        text.setText("Download...");
        String aff = "";
        try {
            String result = new AsyncData().execute().get();
            JSONObject jsonObj = new JSONObject(result);
            JSONArray listeCapteurs = jsonObj.getJSONArray("result");

            for(int i=0; i<listeCapteurs.length(); i++){
                JSONObject capteur = listeCapteurs.getJSONObject(i);
                String numCapteur = capteur.getString("capteur");
                String stateCapteur = capteur.getString("state");

                aff += "Capteur "+numCapteur+" --> "+stateCapteur+"\n";
            }
            text.setText(aff);
        }catch(Exception e){
            text.setText("Erreur :(");
        }
    }

    private class AsyncData extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... args){
            URL url;
            String res;
            try {
                url = new URL("http://ajc-courcite.fr/pmc/getData.php");

                BufferedInputStream bis = new BufferedInputStream(url.openStream());
                byte[] buffer = new byte[1024];
                StringBuilder sb = new StringBuilder();
                int bytesRead = 0;
                while((bytesRead = bis.read(buffer)) > 0) {
                    String text = new String(buffer, 0, bytesRead);
                    sb.append(text);
                }
                bis.close();
                res = sb.toString();
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                res = "Err1";
            } catch (IOException e) {
                // TODO Auto-generated catch block
                res = "Err2";
            } catch(Exception e){
                res = "Err3";
            }

            return res;
        }
    }
}
