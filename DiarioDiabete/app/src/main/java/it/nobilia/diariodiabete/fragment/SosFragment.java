/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
package it.nobilia.diariodiabete.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import it.nobilia.diariodiabete.R;


public class SosFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private TextView sos_number;
    private Button sos_call;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static SosFragment newInstance(int sectionNumber) {
        SosFragment fragment = new SosFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public SosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  rootView = inflater.inflate(R.layout.fragment_sos, container, false);

        sos_number = (TextView) rootView.findViewById(R.id.sos_tv);
        sos_call = (Button) rootView.findViewById(R.id.sos_call);

        final String sos = (getActivity().getPreferences(Context.MODE_PRIVATE)).getString(getString(R.string.sos), "");

        if(sos.isEmpty()) {
            sos_number.setText(getResources().getText(R.string.hint_sos_fragment));
            sos_call.setVisibility(View.INVISIBLE);
        }
        else {
            sos_number.setText(sos);
            sos_call.setVisibility(View.VISIBLE);
        }

        rootView.findViewById(R.id.sos_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + sos));
                getActivity().startActivity(intent);
            }
        });

        return rootView;
    }



}
