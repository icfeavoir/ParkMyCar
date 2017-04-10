package com.pmc.pierre.pmc;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
            URL url;
            String res;
            try {
                url = new URL("http://ajc-courcite.fr/pmc/getData.php");

                BufferedInputStream bis = new BufferedInputStream(url.openStream());
                byte[] buffer = new byte[1024];
                StringBuilder sb = new StringBuilder();
                int bytesRead = 0;
                //append each line of result in the StringBuilder (better than concat)
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
