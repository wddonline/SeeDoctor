package org.wdd.app.android.seedoctor.ui.encyclopedia.view;

import android.content.Context;
import android.view.View;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.views.BottomPopupMenu;
import org.wdd.app.android.seedoctor.views.wheel_picker.WheelPicker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 1/12/17.
 */

public class WheelPickerMenu extends BottomPopupMenu {

    private WheelPicker pickerView;
    private View cancelBtn;
    private View okBtn;

    private ProvinceSelectedListener listener;
    private List<String> data;

    public WheelPickerMenu(Context context) {
        super(context);
    }

    @Override
    public View createView(Context context) {
        View view = View.inflate(context, R.layout.dialog_hospital_province, null);
        return view;
    }

    @Override
    protected void initViews(View rootView) {
        pickerView = (WheelPicker) rootView.findViewById(R.id.dialog_hospital_province_picker);
        cancelBtn = rootView.findViewById(R.id.dialog_hospital_province_cancel);
        okBtn = rootView.findViewById(R.id.dialog_hospital_province_ok);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener == null) return;
                listener.onProvinceSelected(pickerView.getCurrentItemPosition());
            }
        });
    }

    public void setData(List<String> data) {
        this.data = new ArrayList<>();
        this.data.addAll(data);
        pickerView.setData(this.data);
    }

    public void setInitPosition(int position) {
        pickerView.setSelectedItemPosition(position);
    }

    public void setProvinceSelectedListener(ProvinceSelectedListener listener) {
        this.listener = listener;
    }

    public interface ProvinceSelectedListener {

        void onProvinceSelected(int position);

    }
}
