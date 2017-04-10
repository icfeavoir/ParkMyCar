package com.pmc.pierre.pmc;

/**
 * Created by Pierre on 06/04/2017.
 */
public class Capteur {

    private int numero;
    private int state;
    private double lon, lat, distance;

    public Capteur(int numero, int state, double lat, double lon, double distance){
        this.numero = numero;
        this.state = state;
        this.lat = lat;
        this.lon = lon;
        this.distance = distance;
    }

    public double[] getCoords(){
        return new double[]{this.getLon(), this.getLat()};
    }

    public double getLon(){
        return this.lon;
    }

    public double getLat(){
        return this.lat;
    }

    public int getNumero(){
        return this.numero;
    }

    public int getStateInt(){
        return this.state;
    }

    public double getDistance(){
        return this.distance;
    }
}
