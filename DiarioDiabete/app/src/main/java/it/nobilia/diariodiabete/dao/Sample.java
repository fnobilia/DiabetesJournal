package it.nobilia.diariodiabete.dao;


import android.app.Activity;

import java.util.Locale;

public class Sample {

    private int id;
    private int glycaemia;
    private int insulin;
    private String period_txt;
    private int period_num;
    private String time;
    private int day;
    private int month;
    private int year;

    public Sample(int id, int glycaemia, int insulin, String period_txt, int period_num, String time, int day, int month, int year) {
        this.id = id;
        this.glycaemia = glycaemia;
        this.insulin = insulin;
        this.period_txt = period_txt;
        this.period_num = period_num;
        this.time = time;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public Sample(int glycaemia, int insulin, String period_txt, int period_num, String time, int day, int month, int year) {
        this.id = -1;
        this.glycaemia = glycaemia;
        this.insulin = insulin;
        this.period_txt = period_txt;
        this.period_num = period_num;
        this.time = time;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public int getGlycaemia() {
        return glycaemia;
    }

    public void setGlycaemia(int glycaemia) {
        this.glycaemia = glycaemia;
    }

    public int getInsulin() {
        return insulin;
    }

    public void setInsulin(int insulin) {
        this.insulin = insulin;
    }

    public String getPeriodTxt() {
        return period_txt;
    }

    public void setPeriodTxt(String period) {
        this.period_txt = period;
    }

    public int getPeriodNum() {
        return period_num;
    }

    public void setPeriodNum(int period) {
        this.period_num = period;
    }

    public String getDate() {
        return day+"-"+month+"-"+year;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String[] getArray(){
        return new String[]{Integer.toString(this.id),Integer.toString(this.glycaemia),Integer.toString(this.insulin),this.period_txt,Integer.toString(this.period_num),this.time,Integer.toString(this.day),Integer.toString(this.month),Integer.toString(this.year)};
    }

    @Override
    public String toString() {
        return "Sample{" +
                "id=" + id +
                ", glycaemia=" + glycaemia +
                ", insulin=" + insulin +
                ", period='" + period_txt + '\'' +
                ", time='" + time + '\'' +
                ", date='" + day+"-"+month+"-"+year + '\'' +
                '}';
    }

    public String getPDFLine(Activity activity){
        if(activity.getResources().getConfiguration().locale.toLanguageTag().contains(Locale.ENGLISH.toLanguageTag()))
            return getPDFLineEN();
        else
            return getPDFLineIT();
    }

    public String getPDFLineEN() {
        String init = "Value of " + day+"-"+(month+1)+"-"+year + " at " + time + " ("+ period_txt + ")";

        for(int i = init.length(); i<71; i++)
            init = init +" ";

        init = init + "Glycaemia: " + glycaemia + "     " + "Insulin: " + insulin;

        return init;
    }

    public String getPDFLineIT() {
        String init = "Misurazione del " + day+"-"+(month+1)+"-"+year + " alle " + time + " ("+ period_txt + ")";

        for(int i = init.length(); i<71; i++)
            init = init +" ";

        init = init + "Glicemia: " + glycaemia + "     " + "Insulina: " + insulin;

        return init;
    }
}
