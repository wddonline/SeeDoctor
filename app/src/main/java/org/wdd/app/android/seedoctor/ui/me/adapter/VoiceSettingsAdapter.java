package org.wdd.app.android.seedoctor.ui.me.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.me.model.SettingModel;

import java.util.List;

/**
 * Created by richard on 1/22/17.
 */

public class VoiceSettingsAdapter extends AbstractCommonAdapter<SettingModel> {

    private SettingSelectedListener listener;

    public VoiceSettingsAdapter(Context context, List<SettingModel> data) {
        super(context, data);
    }

    public void setSettingSelectedListener(SettingSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_voice_settings, null);
        SettingViewHolder viewHolder = new SettingViewHolder(view);
        return viewHolder;
    }

    @Override
    protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, final SettingModel item, final int position) {
        SettingViewHolder viewHolder = (SettingViewHolder) holder;
        viewHolder.labelView.setText(item.name);
        viewHolder.checkBox.setChecked(item.checked);
        viewHolder.clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingModel setting;
                for (int i = 0; i < data.size(); i++) {
                    setting = data.get(i);
                    if (i == position) {
                        setting.checked = true;
                        if (listener != null) {
                            listener.onSettingSelected(setting);
                        }
                    } else {
                        setting.checked = false;
                    }
                }
                notifyDataSetChanged();
            }
        });
    }

    private class SettingViewHolder extends RecyclerView.ViewHolder {

        private View clickView;
        private TextView labelView;
        private CheckBox checkBox;

        public SettingViewHolder(View itemView) {
            super(itemView);
            clickView = itemView.findViewById(R.id.item_voice_settings_check);
            labelView = (TextView) itemView.findViewById(R.id.item_voice_settings_label);
            checkBox = (CheckBox) itemView.findViewById(R.id.item_voice_settings_check);
        }
    }

    public interface SettingSelectedListener {

        void onSettingSelected(SettingModel setting);

    }
}
