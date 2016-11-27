package org.wdd.app.android.seedoctor.ui.drug.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wdd.app.android.seedoctor.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DrugMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrugMainFragment extends Fragment {

    public static DrugMainFragment newInstance(String param1, String param2) {
        DrugMainFragment fragment = new DrugMainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_drug_main, container, false);
    }

}
