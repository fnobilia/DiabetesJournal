package it.nobilia.diariodiabete.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import it.nobilia.diariodiabete.dao.Sample;
import it.nobilia.diariodiabete.R;
import it.nobilia.diariodiabete.dao.DataBaseHelper;
import it.nobilia.diariodiabete.dao.SamplesTable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PdfFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PdfFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private String filePath;
    private Button open;
    private Button create;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static PdfFragment newInstance(int sectionNumber) {
        PdfFragment fragment = new PdfFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PdfFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        filePath = "/sdcard/" + getString(R.string.pdf_file) + ".pdf";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pdf, container, false);

        create = ((Button) rootView.findViewById(R.id.pdf_create));
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageReport();
            }
        });

        open = ((Button) rootView.findViewById(R.id.pdf_open));
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageOpen();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        manageButton();
    }

    private void manageButton(){
        if(fileExist()){
            open.setVisibility(View.VISIBLE);
            create.setText(getText(R.string.pdf_recreate));
        }
        else{
            open.setVisibility(View.INVISIBLE);
            create.setText(getText(R.string.pdf_create));
        }
    }

    private void manageReport(){
        DataBaseHelper databaseHelper = new DataBaseHelper(getActivity());
        final SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ArrayList<Sample> samples = SamplesTable.getSample(db);
        db.close();

        if (samples.isEmpty()) {
            (Toast.makeText(getActivity(), getResources().getText(R.string.pdf_empty), Toast.LENGTH_SHORT)).show();
        } else {
            final ProgressDialog progress = new ProgressDialog(getActivity());
            progress.setMessage(getString(R.string.pdf_generating));
            progress.setIndeterminate(true);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();

            final Activity activity = getActivity();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String message;

                    DataBaseHelper databaseHelper = new DataBaseHelper(getActivity());
                    final SQLiteDatabase db = databaseHelper.getReadableDatabase();

                    if (generatePDF(SamplesTable.getSample(db))) {

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.setMessage(getString(R.string.pdf_sending));
                            }
                        });

                        if(openPDF())
                            message = getString(R.string.pdf_file_ok_open_ok);
                        else
                            message = getString(R.string.pdf_file_ok_open_ko) + filePath;
                    } else
                        message = getString(R.string.pdf_file_ko);

                    final String final_message = message;
                    db.close();

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress.dismiss();
                            (Toast.makeText(getActivity(), final_message, Toast.LENGTH_SHORT)).show();
                        }
                    });

                }
            }).start();
        }
    }

    private boolean generatePDF(ArrayList<Sample> samples){
        try {
            File file = new File(filePath);

            if (file.exists()) {
                file.delete();
            }

            file.createNewFile();

            Document document = new Document();

            PdfWriter.getInstance(document, new FileOutputStream(file.getAbsoluteFile()));

            document.open();

            int month = samples.get(0).getMonth();

            for (Sample sample: samples) {
                if(sample.getMonth() != month) {
                    month = sample.getMonth();

                    /*String divisor = "";
                    for(int i = 0; i<106; i++)     divisor = divisor +"°";
                    document.add(new Paragraph(divisor));*/
                    document.add(new Paragraph(" "));
                }

                document.add(new Paragraph(sample.getPDFLine(getActivity())));
            }

            document.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (DocumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean openPDF(){
        File file = new File(filePath);
        Uri path = Uri.fromFile(file);

        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
        pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfOpenintent.setDataAndType(path, "application/pdf");

        try {
            startActivity(pdfOpenintent);
        }
        catch (ActivityNotFoundException e) {
            return false;
        }

        return true;
    }

    private boolean fileExist(){
        return new File(filePath).exists();
    }

    private void manageOpen(){
        String message;
        if(openPDF())
            message = getString(R.string.open_ok);
        else
            message = getString(R.string.open_ko) + filePath;

        (Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT)).show();
    }


}
