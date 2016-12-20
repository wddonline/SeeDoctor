package org.wdd.app.android.seedoctor.ui.encyclopedia.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiDiseaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WikiFragment extends Fragment {

    private enum WikiType {
        Disease,
        Drugs,
        EmergencyTreatment,
        Department,
        Doctor,
        Hospital
    }

    private View rootView;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_wiki, container, false);
            initTitle();
            initView();
        }
        if (rootView.getParent() != null) {
            ((ViewGroup)rootView.getParent()).removeView(rootView);
        }
        return rootView;
    }

    private void initTitle() {


    }

    private void initView() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_wiki_recyclerview);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        List<WikiItem> data = new ArrayList<>();
        data.add(new WikiItem(WikiType.Disease, R.mipmap.ic_launcher, R.string.wiki_disease));
        data.add(new WikiItem(WikiType.Drugs, R.mipmap.ic_launcher, R.string.wiki_drugs));
        data.add(new WikiItem(WikiType.EmergencyTreatment, R.mipmap.ic_launcher, R.string.wiki_emergency_treatment));
        data.add(new WikiItem(WikiType.Department, R.mipmap.ic_launcher, R.string.wiki_department));
        data.add(new WikiItem(WikiType.Doctor, R.mipmap.ic_launcher, R.string.wiki_doctor));
        data.add(new WikiItem(WikiType.Hospital, R.mipmap.ic_launcher, R.string.hospital));
        WikiAdapter adapter = new WikiAdapter(getContext(), data);
        adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
        recyclerView.setAdapter(adapter);
    }

    private class WikiAdapter extends AbstractCommonAdapter<WikiItem> {

        public WikiAdapter(Context context, List<WikiItem> data) {
            super(context, data);
        }

        @Override
        protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(getContext(), R.layout.item_wiki_grid, null);
            WikiViewHolder viewHolder = new WikiViewHolder(view);
            return viewHolder;
        }

        @Override
        protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, final WikiItem item, int position) {
            WikiViewHolder viewHolder = (WikiViewHolder) holder;
            viewHolder.iconView.setImageResource(item.iconRes);
            viewHolder.labelView.setText(item.labelRes);
            viewHolder.clickView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (item.type) {
                        case Disease:
                            WikiDiseaseActivity.show(getContext());
                            break;
                        case Drugs:
                            break;
                        case EmergencyTreatment:
                            break;
                        case Department:
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    private class WikiViewHolder extends RecyclerView.ViewHolder {
        View clickView;
        ImageView iconView;
        TextView labelView;

        public WikiViewHolder(View itemView) {
            super(itemView);
            clickView = itemView.findViewById(R.id.item_wiki_grid_clickable);
            iconView = (ImageView) itemView.findViewById(R.id.item_wiki_grid_icon);
            labelView = (TextView) itemView.findViewById(R.id.item_wiki_grid_label);
        }
    }

    private class WikiItem {

        WikiType type;
        int iconRes;
        int labelRes;

        public WikiItem(WikiType type, int iconRes, int labelRes) {
            this.type = type;
            this.iconRes = iconRes;
            this.labelRes = labelRes;
        }
    }
}
