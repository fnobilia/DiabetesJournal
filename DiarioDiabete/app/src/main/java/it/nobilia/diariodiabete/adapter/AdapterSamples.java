package it.nobilia.diariodiabete.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import it.nobilia.diariodiabete.dao.Sample;
import it.nobilia.diariodiabete.R;
import it.nobilia.diariodiabete.dao.DataBaseHelper;
import it.nobilia.diariodiabete.dao.SamplesTable;
import it.nobilia.diariodiabete.fragment.OnFragmentInteractionListener;


public class AdapterSamples extends ArrayAdapter<Sample> {

    private OnFragmentInteractionListener mListener;
    private Activity activity;

    public AdapterSamples(Activity activity, ArrayList<Sample> list) {
        super(activity.getApplicationContext(), 0, list);

        this.activity = activity;

        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sample_item, parent, false);
        }

        final Sample sample = getItem(position);

        View.OnLongClickListener listener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                alertDialog.setCancelable(true);

                alertDialog.setTitle(Html.fromHtml("<font color='#80CBC4'>" + activity.getResources().getString(R.string.sample_title) + "</font>"));

                String[] dateArray = sample.getDate().split("-");
                if (activity.getResources().getConfiguration().locale.toLanguageTag().contains(Locale.ENGLISH.toLanguageTag())) {
                    alertDialog.setMessage("Would you like to delete the sample of " + dateArray[0] + " " + (new DateFormatSymbols(Locale.ENGLISH)).getMonths()[Integer.valueOf(dateArray[1])] + " " + dateArray[2] + " at " + sample.getTime() + "?");
                } else {
                    alertDialog.setMessage("Vuoi eliminare la misurazione del " + dateArray[0] + " " + (new DateFormatSymbols(Locale.ITALIAN)).getMonths()[Integer.valueOf(dateArray[1])] + " " + dateArray[2] + " delle ore " + sample.getTime() + "?");
                }

                alertDialog
                        .setNegativeButton(R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                        .setPositiveButton(R.string.delete,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        DataBaseHelper databaseHelper = new DataBaseHelper(activity);
                                        final SQLiteDatabase db = databaseHelper.getWritableDatabase();
                                        SamplesTable.deleteSample(db, sample.getId());
                                        db.close();

                                        mListener.updateDiary();

                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialogClass = alertDialog.create();

                alertDialogClass.show();

                return true;
            }
        };

        convertView.findViewById(R.id.sample_item_container).setOnLongClickListener(listener);

        View line = convertView.findViewById(R.id.line);
        line.setOnLongClickListener(listener);
        if (activity.getResources().getConfiguration().locale.toLanguageTag().contains(Locale.ENGLISH.toLanguageTag())) {
            switch (sample.getPeriodTxt()) {
                case "Nighttime":
                    line.setBackground(new ColorDrawable(getContext().getResources().getColor(R.color.android_celestial)));
                    break;
                case "Awakening":
                    line.setBackground(new ColorDrawable(getContext().getResources().getColor(R.color.black)));
                    break;
                case "2h after breakfast":
                    line.setBackground(new ColorDrawable(getContext().getResources().getColor(R.color.purple)));
                    break;
                case "Before lunch":
                    line.setBackground(new ColorDrawable(getContext().getResources().getColor(R.color.green)));
                    break;
                case "2h after lunch":
                    line.setBackground(new ColorDrawable(getContext().getResources().getColor(R.color.orange)));
                    break;
                case "Before dinner":
                    line.setBackground(new ColorDrawable(getContext().getResources().getColor(R.color.red)));
                    break;
                case "2h after dinner":
                    line.setBackground(new ColorDrawable(getContext().getResources().getColor(R.color.blue)));
                    break;
                case "Before sleeping":
                    line.setBackground(new ColorDrawable(getContext().getResources().getColor(R.color.yellow)));
                    break;
            }
        }
        else{
            switch (sample.getPeriodTxt()) {
                case "Di notte":
                    line.setBackground(new ColorDrawable(getContext().getResources().getColor(R.color.android_celestial)));
                    break;
                case "Al risveglio":
                    line.setBackground(new ColorDrawable(getContext().getResources().getColor(R.color.black)));
                    break;
                case "2h dopo colazione":
                    line.setBackground(new ColorDrawable(getContext().getResources().getColor(R.color.purple)));
                    break;
                case "Prima di pranzo":
                    line.setBackground(new ColorDrawable(getContext().getResources().getColor(R.color.green)));
                    break;
                case "2h dopo pranzo":
                    line.setBackground(new ColorDrawable(getContext().getResources().getColor(R.color.orange)));
                    break;
                case "Prima di cena":
                    line.setBackground(new ColorDrawable(getContext().getResources().getColor(R.color.red)));
                    break;
                case "2h dopo cena":
                    line.setBackground(new ColorDrawable(getContext().getResources().getColor(R.color.blue)));
                    break;
                case "Prima di coricarsi":
                    line.setBackground(new ColorDrawable(getContext().getResources().getColor(R.color.yellow)));
                    break;
            }
        }

        TextView glycaemia = (TextView) convertView.findViewById(R.id.glycaemia_value);
        glycaemia.setOnLongClickListener(listener);

        TextView insulin = (TextView) convertView.findViewById(R.id.insulin_value);
        insulin.setOnLongClickListener(listener);

        TextView date = (TextView) convertView.findViewById(R.id.date_value);
        date.setOnLongClickListener(listener);

        TextView time = (TextView) convertView.findViewById(R.id.time_value);
        time.setOnLongClickListener(listener);

        TextView period = (TextView) convertView.findViewById(R.id.period_label);
        period.setText(sample.getPeriodTxt());

        glycaemia.setText(Integer.toString(sample.getGlycaemia()));
        insulin.setText(Integer.toString(sample.getInsulin()));

        String[] dateArray = sample.getDate().split("-");
        if (activity.getResources().getConfiguration().locale.toLanguageTag().contains(Locale.ENGLISH.toLanguageTag())) {
            date.setText(dateArray[0] + " " + (new DateFormatSymbols(Locale.ENGLISH)).getMonths()[Integer.valueOf(dateArray[1])] + " " + dateArray[2]);
        }else{
            date.setText(dateArray[0] + " " + (new DateFormatSymbols(Locale.ITALIAN)).getMonths()[Integer.valueOf(dateArray[1])] + " " + dateArray[2]);
        }

        time.setText(sample.getTime());

        return convertView;
    }
}
