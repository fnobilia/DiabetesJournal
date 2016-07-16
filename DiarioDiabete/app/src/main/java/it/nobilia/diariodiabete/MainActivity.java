package it.nobilia.diariodiabete;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Locale;

import it.nobilia.diariodiabete.dialog.SampleDialog;
import it.nobilia.diariodiabete.fragment.OnFragmentInteractionListener;
import it.nobilia.diariodiabete.fragment.PdfFragment;
import it.nobilia.diariodiabete.fragment.SosFragment;
import it.nobilia.diariodiabete.R;
import it.nobilia.diariodiabete.fragment.DiaryFragment;
import it.nobilia.diariodiabete.fragment.GraphFragment;
import it.nobilia.diariodiabete.fragment.ProfileFragment;


public class MainActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks, OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        //Set diary fragment
        setDiaryFragment(getFragmentManager(),1,true);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();

        switch (position) {
            case 0:
                fragmentManager.beginTransaction().replace(R.id.container, ProfileFragment.newInstance(position + 1), ProfileFragment.TAG).commit();
                onSectionAttached(position);
                break;
            case 1:
                setDiaryFragment(fragmentManager, position, false);
                break;
            case 2:
                fragmentManager.beginTransaction().replace(R.id.container, GraphFragment.newInstance(position + 1)).commit();
                onSectionAttached(position);
                break;
            case 3:
                fragmentManager.beginTransaction().replace(R.id.container, PdfFragment.newInstance(position + 1)).commit();
                onSectionAttached(position);
                break;
            case 4:
                fragmentManager.beginTransaction().replace(R.id.container, SosFragment.newInstance(position + 1)).commit();
                onSectionAttached(position);
                break;
        }
    }

    private void onSectionAttached(int position) {
        String[] stringArray = getResources().getStringArray(R.array.sections);
        mTitle = stringArray[position];
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    private void setDiaryFragment(FragmentManager fragmentManager, int position, boolean select){
        fragmentManager.beginTransaction().replace(R.id.container, DiaryFragment.newInstance(position + 1), DiaryFragment.TAG).commit();
        onSectionAttached(position);

        if(select)
            mNavigationDrawerFragment.selectItem(position);
    }

    @Override
    public void onFragmentInteraction(String id){}

    @Override
    public void onFragmentInteraction(int year, int month, int day){
        SampleDialog sampleDialog = (SampleDialog) getFragmentManager().findFragmentByTag(SampleDialog.TAG);

        if (sampleDialog != null)
            sampleDialog.updateDate(year,month,day);

        ProfileFragment profileFragment = (ProfileFragment) getFragmentManager().findFragmentByTag(ProfileFragment.TAG);

        if (profileFragment != null)
            profileFragment.updateBirth(year, month, day);
    }

    @Override
    public void onFragmentInteraction(int hourOfDay, int minute){
        SampleDialog sampleDialog = (SampleDialog) getFragmentManager().findFragmentByTag(SampleDialog.TAG);

        if (sampleDialog != null)
            sampleDialog.updateTime(hourOfDay, minute);
    }

    @Override
    public void updateDiary() {
        DiaryFragment diaryFragment = (DiaryFragment) getFragmentManager().findFragmentByTag(DiaryFragment.TAG);

        if (diaryFragment != null)
            diaryFragment.updateData();
    }

}
