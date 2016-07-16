package it.nobilia.diariodiabete.fragment;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.DateFormatSymbols;
import java.util.Locale;

import it.nobilia.diariodiabete.dialog.DateDialog;
import it.nobilia.diariodiabete.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment{
    private static final String ARG_SECTION_NUMBER = "section_number";

    private EditText name_et;
    private EditText surname_et;

    private NumberPicker mNumberPickerHeight;
    private NumberPicker mNumberPickerWeight;

    private TextView birthday_tv;

    private RadioButton male_rb;
    private RadioButton female_rb;

    private EditText email_et;
    private EditText sos_et;

    private Button saveButton;

    private SharedPreferences sharedPref;

    public static final String TAG = "profileFragment";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Profile.
     */
    public static ProfileFragment newInstance(int sectionNumber) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_profile, container, false);

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        setEditText(rootView);

        setNumberPicker(rootView);

        setRadioButton(rootView);

        setDatePicker(rootView);

        setButtonSave(rootView);

        return rootView;
    }

    private void setEditText(View rootView){
        String name = sharedPref.getString(getString(R.string.name), "");
        name_et = (EditText) rootView.findViewById(R.id.name_et);
        if(!name.isEmpty())
            name_et.setText(name);

        String surname = sharedPref.getString(getString(R.string.surname),"");
        surname_et = (EditText) rootView.findViewById(R.id.surname_et);
        if(!surname.isEmpty())
            surname_et.setText(surname);

        String email = sharedPref.getString(getString(R.string.email),"");
        email_et = (EditText) rootView.findViewById(R.id.email_et);
        if(!email.isEmpty())
            email_et.setText(email);

        String sos = sharedPref.getString(getString(R.string.sos),"");
        sos_et = (EditText) rootView.findViewById(R.id.sos_et);
        if(!sos.isEmpty())
            sos_et.setText(sos);
    }

    private void setDatePicker(View rootView){
        birthday_tv = (TextView) rootView.findViewById(R.id.birth_tv);

        String birth = sharedPref.getString(getString(R.string.birth), "");
        if(!birth.isEmpty()) {
            birthday_tv.setText(birth);
            birthday_tv.setTextColor(getResources().getColor(R.color.black));
        }
        else
            birthday_tv.setText(getResources().getText(R.string.hint_birth));

        birthday_tv.setClickable(true);
        birthday_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DateDialog();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
    }

    private void setNumberPicker(View rootView){
        mNumberPickerHeight = (NumberPicker) rootView.findViewById(R.id.height_np);
        setUpPickerValue(mNumberPickerHeight);
        setUpPickerColor(mNumberPickerHeight);

        mNumberPickerWeight = (NumberPicker) rootView.findViewById(R.id.weight_np);
        setUpPickerValue(mNumberPickerWeight);
        setUpPickerColor(mNumberPickerWeight);

        int height = sharedPref.getInt(getString(R.string.height),-1);
        if(height > 0)
            mNumberPickerHeight.setValue(height);

        int weight = sharedPref.getInt(getString(R.string.weight), -1);
        if(weight > 0)
            mNumberPickerWeight.setValue(weight);
    }

    private void setRadioButton(View rootView){
        male_rb = (RadioButton) rootView.findViewById(R.id.male_rb);
        female_rb = (RadioButton) rootView.findViewById(R.id.female_rb);

        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switch (compoundButton.getId()){
                    case R.id.male_rb:
                        female_rb.setChecked(!b);
                        break;

                    case R.id.female_rb:
                        male_rb.setChecked(!b);
                        break;
                }
            }
        };

        male_rb.setOnCheckedChangeListener(listener);
        female_rb.setOnCheckedChangeListener(listener);

        String gender = sharedPref.getString(getString(R.string.gender),"");
        if(!gender.isEmpty()){
            if(gender.equals("F"))
                female_rb.setChecked(true);
            else
                male_rb.setChecked(true);
        }

    }

    private void setButtonSave(View rootView){
        saveButton = (Button) rootView.findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = name_et.getText().toString();
                String surname = surname_et.getText().toString();

                int height = mNumberPickerHeight.getValue();
                int weight = mNumberPickerWeight.getValue();

                String birthday = birthday_tv.getText().toString();

                String gender = "";
                if(male_rb.isChecked())
                    gender = "M";
                else if(female_rb.isChecked())
                    gender = "F";

                String email = email_et.getText().toString();
                String sos = sos_et.getText().toString();

                if(name.isEmpty()){
                    (Toast.makeText(getActivity(), getResources().getText(R.string.hint_name), Toast.LENGTH_SHORT)).show();
                }
                else if(surname.isEmpty()){
                    (Toast.makeText(getActivity(), getResources().getText(R.string.hint_surname), Toast.LENGTH_SHORT)).show();
                }
                else if(height == 0){
                    (Toast.makeText(getActivity(), getResources().getText(R.string.hint_height), Toast.LENGTH_SHORT)).show();
                }
                else if(weight == 0){
                    (Toast.makeText(getActivity(), getResources().getText(R.string.hint_weight), Toast.LENGTH_SHORT)).show();
                }
                else if(birthday.equals(getResources().getString(R.string.hint_birth))){
                    (Toast.makeText(getActivity(), getResources().getText(R.string.hint_birth), Toast.LENGTH_SHORT)).show();
                }
                else if(gender.equals("")){
                    (Toast.makeText(getActivity(), getResources().getText(R.string.hint_gender), Toast.LENGTH_SHORT)).show();
                }
                else if(email.isEmpty()){
                    (Toast.makeText(getActivity(), getResources().getText(R.string.hint_email), Toast.LENGTH_SHORT)).show();
                }
                else if(sos.isEmpty()){
                    (Toast.makeText(getActivity(), getResources().getText(R.string.hint_sos), Toast.LENGTH_SHORT)).show();
                }
                else{
                    name = name.trim();
                    char first = Character.toUpperCase(name.charAt(0));
                    name = first + name.substring(1);

                    surname = surname.trim();
                    first = Character.toUpperCase(surname.charAt(0));
                    surname = first + surname.substring(1);

                    email = email.trim();

                    sos = sos.trim();

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.name), name);
                    editor.putString(getString(R.string.surname), surname);
                    editor.putInt(getString(R.string.height), height);
                    editor.putInt(getString(R.string.weight), weight);
                    editor.putString(getString(R.string.birth), birthday);
                    editor.putString(getString(R.string.gender), gender);
                    editor.putString(getString(R.string.email), email);
                    editor.putString(getString(R.string.sos), sos);
                    editor.commit();

                    (Toast.makeText(getActivity(), getResources().getText(R.string.contact_saved), Toast.LENGTH_SHORT)).show();
                }
            }
        });
    }

    private void setUpPickerColor(NumberPicker np){
        int count = np.getChildCount();
        int color = getActivity().getResources().getColor(R.color.black);

        for(int i = 0; i < count; i++){
            View child = np.getChildAt(i);
            if(child instanceof EditText){
                try{
                    Field selectorWheelPaintField = np.getClass().getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(np)).setColor(color);
                    ((EditText)child).setTextColor(color);
                    np.invalidate();
                }
                catch(NoSuchFieldException e){
                    Log.e("NumberPicker", e.toString());
                }
                catch(IllegalAccessException e){
                    Log.e("NumberPicker", e.toString());
                }
                catch(IllegalArgumentException e){
                    Log.e("NumberPicker", e.toString());
                }
            }
        }
    }

    private void setUpPickerValue(NumberPicker np){
        int min = 0,max = 0;
        String[] values = null;

        switch (np.getId()){
            case R.id.height_np:
                min = 0;
                max = 300;
                break;

            case R.id.weight_np:
                min = 0;
                max = 500;
                break;
        }

        np.setDisplayedValues(null);
        np.setMinValue(min);
        np.setMaxValue(max);
        np.setWrapSelectorWheel(false);
        np.setDisplayedValues(values);
    }

    public void updateBirth(int year, int month, int day){
        birthday_tv.setText(day + " " + (new DateFormatSymbols(Locale.ITALIAN)).getMonths()[month] + " " + year);
        birthday_tv.setTextColor(getResources().getColor(R.color.black));
    }
}
