package org.wdd.app.android.seedoctor.ui.encyclopedia.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wdd.app.android.seedoctor.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class DrugSecondCategaryFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_drug_second_categary_list, container, false);

        return view;
    }
}
