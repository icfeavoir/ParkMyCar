package com.pmc.pierre.pmc;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pierre on 06/04/2017.
 */
public class PMCSearch {

    static final int PERIMETRE_PAR_DEFAUT = 10; //en km;
    private int perimetre;
    private double coordX, coordY;
    private List<Capteur> listeCapteur = new ArrayList<Capteur>();

    public PMCSearch(){
        this(PERIMETRE_PAR_DEFAUT, 0, 0);
    }
    public PMCSearch(double x, double y) {
        this(PERIMETRE_PAR_DEFAUT, x, y);
    }
    public PMCSearch(int perimetre, double x, double y){
        this.perimetre = perimetre;
        this.coordX = x;
        this.coordY = y;
    }
    private void addCapteur(Capteur c){
        listeCapteur.add(c);
    }
    public List<Capteur> getListeCapteur(){
        return this.listeCapteur;
    }

    public void getDataFromDB(){
        //get data
        String aff = "";
        try {
            String result = new AsyncGetData().execute().get(); //start the search asynchonous mode
            JSONObject jsonObj = new JSONObject(result);
            JSONArray listeCapteurs = jsonObj.getJSONArray("result");

            for(int i=0; i<listeCapteurs.length(); i++){
                JSONObject JSONcapteur = listeCapteurs.getJSONObject(i);
                int numCapteur = JSONcapteur.getInt("capteur");
                int stateCapteur = JSONcapteur.getInt("state");
                double lat = JSONcapteur.getDouble("lat");
                double lon = JSONcapteur.getDouble("lon");
                double distance = JSONcapteur.getDouble("distance");

                this.addCapteur(new Capteur(numCapteur, stateCapteur, lat, lon, distance));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Capteur c : this.getListeCapteur()){
            sb.append("Capteur "+c.getNumero()+" --> "+c.getStateInt()+" | "+c.getCoords()[0]+", "+c.getCoords()[1]+" Distance : "+c.getDistance()+"\n");
        }
        return sb.toString();
    }

    private class AsyncGetData extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... args){
            BufferedReader reader = null;
            StringBuilder sb = null;
            try {
                String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode("pmc", "UTF-8");
                data += "&" + URLEncoder.encode("passwd", "UTF-8") + "=" + URLEncoder.encode("pmc_user", "UTF-8");

                URL url = new URL("http://ajc-courcite.fr/pmc/getData.php");
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();

                //response
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                sb = new StringBuilder();
                String line = null;

                while((line = reader.readLine()) != null){
                    sb.append(line);
                }

            }catch(Exception e){
                e.printStackTrace();
            }

            return sb.toString();
        }
    }
}
