package it.nobilia.diariodiabete.adapter;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import it.nobilia.diariodiabete.R;


public class AdapterLegend extends ArrayAdapter<LegendItem> {


    public AdapterLegend(Activity activity) {
        super(activity.getApplicationContext(), 0, LegendItem.getList(activity));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.legend_item, parent, false);
        }

        LegendItem sample = getItem(position);

        convertView.findViewById(R.id.color).setBackground(new ColorDrawable(sample.getColor()));
        ((TextView) convertView.findViewById(R.id.period)).setText(sample.getLable());

        return convertView;
    }
}
