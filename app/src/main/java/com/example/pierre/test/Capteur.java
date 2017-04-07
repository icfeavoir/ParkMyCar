package com.example.pierre.test;

/**
 * Created by Pierre on 06/04/2017.
 */
public class Capteur {

    private int numero;
    private int state;
    private double coordX, coordY, distance;

    public Capteur(int numero, int state, double coordX, double coordY, double distance){
        this.numero = numero;
        this.state = state;
        this.coordX = coordX;
        this.coordY = coordY;
        this.distance = distance;
    }

    public double[] getCoords(){
        return new double[]{this.coordX, this.coordY};
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
