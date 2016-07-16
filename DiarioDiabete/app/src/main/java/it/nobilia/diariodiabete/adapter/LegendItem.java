package it.nobilia.diariodiabete.adapter;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

import it.nobilia.diariodiabete.R;

public class LegendItem {

    private String lable;
    private int color;

    public LegendItem(String lable, int color) {
        this.lable = lable;
        this.color = color;
    }

    public String getLable() {
        return lable;
    }

    public int getColor() {
        return color;
    }

    public static ArrayList<LegendItem> getList(Activity activity){
        ArrayList<LegendItem> list = new ArrayList<>();

        if(activity.getResources().getConfiguration().locale.toLanguageTag().contains(Locale.ENGLISH.toLanguageTag())) {
            list.add(new LegendItem("Nighttime", activity.getResources().getColor(R.color.android_celestial)));
            list.add(new LegendItem("Awakening",activity.getResources().getColor(R.color.black)));
            list.add(new LegendItem("2h after breakfast", activity.getResources().getColor(R.color.purple)));
            list.add(new LegendItem("Before lunch", activity.getResources().getColor(R.color.green)));
            list.add(new LegendItem("2h after lunch", activity.getResources().getColor(R.color.orange)));
            list.add(new LegendItem("Before dinner", activity.getResources().getColor(R.color.red)));
            list.add(new LegendItem("2h after dinner", activity.getResources().getColor(R.color.blue)));
            list.add(new LegendItem("Before sleeping", activity.getResources().getColor(R.color.yellow)));
        }else{
            list.add(new LegendItem("Di notte", activity.getResources().getColor(R.color.android_celestial)));
            list.add(new LegendItem("Al risveglio",activity.getResources().getColor(R.color.black)));
            list.add(new LegendItem("2h dopo colazione", activity.getResources().getColor(R.color.purple)));
            list.add(new LegendItem("Prima di pranzo", activity.getResources().getColor(R.color.green)));
            list.add(new LegendItem("2h dopo pranzo", activity.getResources().getColor(R.color.orange)));
            list.add(new LegendItem("Prima di cena", activity.getResources().getColor(R.color.red)));
            list.add(new LegendItem("2h dopo cena", activity.getResources().getColor(R.color.blue)));
            list.add(new LegendItem("Prima di coricarsi", activity.getResources().getColor(R.color.yellow)));
        }
        
        return list;
    }

    public static String[] getLables(Activity activity){
        String[] labels = new String[8];

        for(int i=0; i<labels.length; i++)
            labels[i] = getLable(i,activity);

        return labels;
    }

    public static int[] getColors(Activity activity){
        int[] colors = new int[8];

        for(int i=0; i<colors.length; i++)
            colors[i] = getColor(i, activity);

        return colors;
    }

    public static String getLable(int index, Activity activity){
        if(activity.getResources().getConfiguration().locale.toLanguageTag().contains(Locale.ENGLISH.toLanguageTag())) {
            switch (index) {
                case 0:     return "Nighttime";
                case 1:     return "Awakening";
                case 2:     return "2h after breakfast";
                case 3:     return "Before lunch";
                case 4:     return "2h after lunch";
                case 5:     return "Before dinner";
                case 6:     return "2h after dinner";
                case 7:     return "Before sleeping";


                default: return "Select period";
            }
        }
        else{
            switch (index) {
                case 0:     return "Di notte";
                case 1:     return "Al risveglio";
                case 2:     return "2h dopo colazione";
                case 3:     return "Prima di pranzo";
                case 4:     return "2h dopo pranzo";
                case 5:     return "Prima di cena";
                case 6:     return "2h dopo cena";
                case 7:     return "Prima di coricarsi";


                default: return "Seleziona periodo";
            }
        }
    }

    public static int getColor(int index, Activity activity){
        switch (index) {
            case 0:     return activity.getResources().getColor(R.color.android_celestial);
            case 1:     return activity.getResources().getColor(R.color.black);
	        case 2:     return activity.getResources().getColor(R.color.purple);
            case 3:     return activity.getResources().getColor(R.color.green);
            case 4:     return activity.getResources().getColor(R.color.orange);
            case 5:     return activity.getResources().getColor(R.color.red);
            case 6:     return activity.getResources().getColor(R.color.blue);
            case 7:     return activity.getResources().getColor(R.color.yellow);
            
            default: return -1;
        }
    }

    public static int getPeriodNum(String period, Activity activity){
        if(activity.getResources().getConfiguration().locale.toLanguageTag().contains(Locale.ENGLISH.toLanguageTag())) {
            switch (period) {
                case "Nighttime": return 0;
                case "Awakening": return 1;
                case "2h after breakfast": return 2;
                case "Before lunch": return 3;
                case "2h after lunch": return 4;
                case "Before dinner": return 5;
                case "2h after dinner": return 6;
                case "Before sleeping": return 7;

                default: return -1;
            }
        }
        else{
            switch (period) {
                case "Di notte": return 0;
                case "Al risveglio": return 1;
                case "2h dopo colazione": return 2;
                case "Prima di pranzo": return 3;
                case "2h dopo pranzo": return 4;
                case "Prima di cena": return 5;
                case "2h dopo cena": return 6;
                case "Prima di coricarsi": return 7;

                default: return -1;
            }
        }
    }

}
