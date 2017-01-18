package com.example.asmarasusanto.myapplication;

import java.util.ArrayList;

/**
 * Created by asmarasusanto on 1/16/17.
 */

public class Teman {
    private String urlFoto;
    private String namateman;
    private String statusteman;
    private String index;

    public Teman(){

    }

    public Teman(String name, String statusteman, String index, String thumbnailUrl ) {
        this.namateman = name;
        this.statusteman = statusteman;
        this.urlFoto = thumbnailUrl;
        this.index = index;
    }


    public void setNamateman(String namateman) {
        this.namateman = namateman;
    }

    public void setIndex(String index) { this.index = index; }

    public void setStatusteman(String statusteman) {
        this.statusteman = statusteman;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getNamateman() {
        return namateman;
    }

    public String getIndex() { return index; }

    public String getStatusteman() {
        return statusteman;
    }

    public String getUrlFoto() {
        return urlFoto;
    }
}
