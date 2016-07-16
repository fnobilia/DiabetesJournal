package it.nobilia.diariodiabete.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ListFragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import it.nobilia.diariodiabete.adapter.AdapterSamples;
import it.nobilia.diariodiabete.dao.SamplesTable;
import it.nobilia.diariodiabete.dialog.SampleDialog;
import it.nobilia.diariodiabete.R;
import it.nobilia.diariodiabete.dao.DataBaseHelper;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class DiaryFragment extends ListFragment implements View.OnClickListener{

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final String TAG = "diaryFragment";

    private Button newSample;
    private AdapterSamples adapterSamples;
    private TextView empty;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types of parameters
    public static DiaryFragment newInstance(int sectionNumber) {
        DiaryFragment fragment = new DiaryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DiaryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataBaseHelper databaseHelper = new DataBaseHelper(getActivity());
        final SQLiteDatabase db = databaseHelper.getReadableDatabase();
        adapterSamples = new AdapterSamples(getActivity(), SamplesTable.getSample(db));
        db.close();

        setListAdapter(adapterSamples);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  rootView = inflater.inflate(R.layout.fragment_diary, container, false);

        newSample = (Button) rootView.findViewById(R.id.add_button);
        newSample.setOnClickListener(this);

        empty = (TextView) rootView.findViewById(R.id.empty_diary);

        if(adapterSamples.isEmpty())
            empty.setVisibility(View.VISIBLE);
        else
            empty.setVisibility(View.INVISIBLE);

        return rootView;
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


    @Override
    public void onClick(View view) {
        DialogFragment newFragment = new SampleDialog();
        newFragment.show(getFragmentManager(), "sampleDialog");
    }

    public void updateData(){
        adapterSamples.clear();

        DataBaseHelper databaseHelper = new DataBaseHelper(getActivity());
        final SQLiteDatabase db = databaseHelper.getReadableDatabase();
        adapterSamples.addAll(SamplesTable.getSample(db));
        db.close();

        adapterSamples.notifyDataSetChanged();

        if(adapterSamples.isEmpty())
            empty.setVisibility(View.VISIBLE);
        else
            empty.setVisibility(View.INVISIBLE);
    }

}
