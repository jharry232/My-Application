package com.example.mylocation;

public class MyLocation {

    public int id;
    public String n;
    public double la, lo;

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public double getLa() {
        return la;
    }

    public void setLa(double la) {
        this.la = la;
    }

    public double getLo() {
        return lo;
    }

    public void setLo(double lo) {
        this.lo = lo;
    }

    public MyLocation(int id, String n, Double la, Double lo) {
        this.id = id;
        this.n = n;
        this.la = la;
        this.lo = lo;
    }

    public MyLocation(String n, Double la, Double lo) {
        this.n = n;
        this.la = la;
        this.lo = lo;
    }
}