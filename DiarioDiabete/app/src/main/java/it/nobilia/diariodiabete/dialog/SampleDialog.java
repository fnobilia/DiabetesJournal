package it.nobilia.diariodiabete.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import it.nobilia.diariodiabete.dao.Sample;
import it.nobilia.diariodiabete.R;
import it.nobilia.diariodiabete.adapter.LegendItem;
import it.nobilia.diariodiabete.dao.DataBaseHelper;
import it.nobilia.diariodiabete.dao.SamplesTable;
import it.nobilia.diariodiabete.fragment.OnFragmentInteractionListener;

public class SampleDialog extends DialogFragment{

    public static final String TAG = "sampleDialog";

    private OnFragmentInteractionListener mListener;

    private EditText glycaemia_et;
    private EditText insulin_et;
    private Spinner spinner;
    private TextView showTime;
    private TextView showDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.fragment_dialog, null);

        setEditText(rootView);
        setSpinner(rootView);
        setDatePicker(rootView);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(rootView)
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SampleDialog.this.getDialog().cancel();
                    }
                });

        Dialog dialog = builder.create();

        final Activity activity_temp = getActivity();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button b = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (checkData(activity_temp)) {
                            mListener.updateDiary();
                            SampleDialog.this.getDialog().dismiss();
                        }
                    }
                });
            }
        });

        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setEditText(View rootView){
        glycaemia_et = (EditText) rootView.findViewById(R.id.glycaemia_et);
        insulin_et = (EditText) rootView.findViewById(R.id.insulin_et);
    }

    private void setSpinner(View rootView){
        spinner = (Spinner) rootView.findViewById(R.id.periods_s);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.periods, R.layout.item_spinner);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    private void setDatePicker(View rootView){
        final Calendar c = Calendar.getInstance();

        showTime = (TextView) rootView.findViewById(R.id.show_time);

        int minute = c.get(Calendar.MINUTE);
        if(minute < 10)
            showTime.setText(c.get(Calendar.HOUR_OF_DAY) + ":0" + minute);
        else
            showTime.setText(c.get(Calendar.HOUR_OF_DAY) + ":" + minute);

        showTime.setClickable(true);
        showTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimerDialog();
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });

        showDate = (TextView) rootView.findViewById(R.id.show_date);
        showDate.setText(c.get(Calendar.DAY_OF_MONTH)+"-"+(Integer.valueOf(c.get(Calendar.MONTH)) + 1)+"-"+c.get(Calendar.YEAR));
        showDate.setClickable(true);
        showDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DateDialog();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
    }

    public void updateDate(int year, int month, int day){
        showDate.setText(day + "-" + (month+1) + "-" + year);
        showDate.setTextColor(getResources().getColor(R.color.white));
    }

    public void updateTime(int hourOfDay, int minute){
        if(minute < 10)
            showTime.setText(hourOfDay + ":0" + minute);
        else
            showTime.setText(hourOfDay + ":" + minute);

        showTime.setTextColor(getResources().getColor(R.color.white));
    }

    private boolean checkData(Activity activity){
        String glycaemia = glycaemia_et.getText().toString();
        String insulin = insulin_et.getText().toString();

        if(insulin == null || insulin.equals(""))   insulin = "0";

        String period = null;
        String checkSelected = LegendItem.getLable(-1,activity);
        if(!spinner.getSelectedItem().toString().equals(checkSelected))
            period = spinner.getSelectedItem().toString();

        String time = showTime.getText().toString();
        String date = showDate.getText().toString();
        String[] date_array = date.split("-");

        if(glycaemia.isEmpty()) {
            (Toast.makeText(getActivity(), getResources().getText(R.string.glycaemia_empty), Toast.LENGTH_SHORT)).show();
            return false;
        }
        else if(period == null){
            (Toast.makeText(getActivity(), getResources().getText(R.string.period_empty), Toast.LENGTH_SHORT)).show();
            return false;
        }
        else{
            DataBaseHelper databaseHelper = new DataBaseHelper(getActivity());
            final SQLiteDatabase db = databaseHelper.getWritableDatabase();
            SamplesTable.insertSample(db,
                    new Sample(
                            Integer.valueOf(glycaemia),
                            Integer.valueOf(insulin),
                            period,
                            LegendItem.getPeriodNum(period,activity),
                            time,
                            Integer.valueOf(date_array[0]),
                            (Integer.valueOf(date_array[1]) - 1),
                            Integer.valueOf(date_array[2])));
            db.close();

            return true;
        }
    }

}