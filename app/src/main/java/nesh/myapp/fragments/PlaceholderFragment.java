package nesh.myapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import nesh.myapp.MainActivity;


/**
 * Created by nesh on 4/23/17.
 */

public class PlaceholderFragment extends Fragment {
    public static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment() {}

    public static PlaceholderFragment newInstance(MainActivity.SectionsPagerAdapter adapter, int sectionNumber) {
        PlaceholderFragment fragment = null;

        switch (sectionNumber) {
            case 1:
                fragment = new TranslateFragment(adapter);
                break;
            case 2:
                fragment = new HistoryFragment();
                break;
            case 3:
                fragment = new FavoriteFragment();
                break;
            default:
                fragment = new PlaceholderFragment();
        }

        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
}
