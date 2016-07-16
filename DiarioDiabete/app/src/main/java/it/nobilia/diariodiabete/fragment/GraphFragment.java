package it.nobilia.diariodiabete.fragment;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

import it.nobilia.diariodiabete.dao.DataBaseHelper;
import it.nobilia.diariodiabete.dao.Sample;
import it.nobilia.diariodiabete.dao.SamplesTable;
import it.nobilia.diariodiabete.R;
import it.nobilia.diariodiabete.adapter.LegendItem;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraphFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";


    public static GraphFragment newInstance(int sectionNumber) {
        GraphFragment fragment = new GraphFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public GraphFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_graph, container, false);

        setChartDay(rootView);

        setChartMonth(rootView);

        return rootView;
    }

    private void setChartDay(View rootView){
        DataBaseHelper databaseHelper = new DataBaseHelper(getActivity());
        final SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ArrayList<Sample> day = SamplesTable.getSampleDay(db);
        db.close();

        if(!day.isEmpty()) {

            String[] array = getResources().getStringArray(R.array.periods_short);

            BarChart chart_day = (BarChart) rootView.findViewById(R.id.chart_day);
            chart_day.animateY(1000);
            chart_day.setDescription(getString(R.string.chart_day));
            chart_day.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                    (Toast.makeText(getActivity(), LegendItem.getLable(e.getXIndex(),getActivity()), Toast.LENGTH_SHORT)).show();
                }

                @Override
                public void onNothingSelected() {

                }
            });

            ArrayList<BarEntry> entries = new ArrayList<>();
            for (int i = 0; i < array.length; i++)
                entries.add(new BarEntry(0f, i));

            for (Sample sample : day) {
                //entries.remove(sample.getPeriodNum() - 1);
                //entries.add(sample.getPeriodNum() - 1 ,new BarEntry((float)sample.getGlycaemia(), sample.getPeriodNum() - 1));
                entries.remove(sample.getPeriodNum());
                entries.add(sample.getPeriodNum(), new BarEntry((float)sample.getGlycaemia(), sample.getPeriodNum()));
            }

            BarDataSet dataset = new BarDataSet(entries, getString(R.string.glycaemia));


            dataset.setColors(LegendItem.getColors(getActivity()));

            ArrayList<String> labels = new ArrayList<String>();
            for (int i = 0; i < array.length; i++)
                labels.add(array[i]);

            BarData data = new BarData(labels, dataset);
            chart_day.setData(data);
        }
    }

    private void setChartMonth(View rootView){
        DataBaseHelper databaseHelper = new DataBaseHelper(getActivity());
        final SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ArrayList<Sample> month = SamplesTable.getSampleMonth(db);
        db.close();

        if(!month.isEmpty()) {
            LineChart chart_month = (LineChart) rootView.findViewById(R.id.chart_month);
            chart_month.setDescription(getString(R.string.chart_month));

            ArrayList<Entry> entries = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<String>();

            int day = month.get(0).getDay();
            int count_entry = 0;
            float count_sample = 0f;
            float glycaemia = 0f;

            for (Sample sample : month) {
                if (sample.getDay() == day) {
                    count_sample++;
                    glycaemia = glycaemia + sample.getGlycaemia();
                }
                else {
                    entries.add(new Entry(glycaemia / count_sample, count_entry));
                    labels.add(Integer.toString(day));
                    count_entry++;

                    day = sample.getDay();
                    count_sample = 1f;
                    glycaemia = sample.getGlycaemia();
                }
            }

            //Add last value avoided by loop
            entries.add(new Entry(glycaemia / count_sample, count_entry));
            labels.add(Integer.toString(day));

            LineData data = new LineData(labels, new LineDataSet(entries, getString(R.string.glycaemia)));
            chart_month.setData(data);
        }
    }

}
