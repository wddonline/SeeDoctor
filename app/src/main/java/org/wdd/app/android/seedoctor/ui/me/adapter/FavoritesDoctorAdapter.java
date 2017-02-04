package org.wdd.app.android.seedoctor.ui.me.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.database.model.DbDoctor;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.views.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 11/28/16.
 */

public class FavoritesDoctorAdapter extends AbstractCommonAdapter<FavoritesDoctorAdapter.DoctorFavorites> {

    public enum Mode {
        Normal,
        Select
    }

    private SwipeLayout openSwipeLayout;
    private Mode mode = Mode.Normal;
    private FavoritesDoctorCallback callback;

    private int selectedCount = 0;

    public FavoritesDoctorAdapter(Context context, List<DoctorFavorites> data) {
        super(context, data);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorites_hospital, parent, false);
        RecyclerView.ViewHolder viewHolder = new HospitalVH(view);
        return viewHolder;
    }

    @Override
    protected void onBindDataViewHolder(final RecyclerView.ViewHolder holder, final DoctorFavorites favorites, final int position) {
        final HospitalVH hospitalVH = (HospitalVH) holder;
        hospitalVH.imageView.setImageUrl(favorites.doctor.photourl);
        hospitalVH.nameView.setText(favorites.doctor.doctorname);
        hospitalVH.rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (openSwipeLayout != null) {
                    openSwipeLayout.close(true);
                    return true;
                }
                return false;
            }
        });
        hospitalVH.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mode) {
                    case Normal:
                        if (callback != null) callback.jumpToDetailActivity(position, favorites.doctor.doctorid, favorites.doctor.doctorname);
                        break;
                    case Select:
                        favorites.isSelected = !favorites.isSelected;
                        if (favorites.isSelected) {
                            selectedCount++;
                        } else {
                            selectedCount--;
                        }
                        int size = getLoadStatus() == LoadStatus.NoMore ? data.size() : data.size() - 1;
                        if (selectedCount == size) {
                            if (callback != null) callback.onAllSelected();
                        } else {
                            if (callback != null) callback.onPartSelected();
                        }
                        notifyItemChanged(position);
                        break;
                }
            }
        });
        hospitalVH.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mode = Mode.Select;
                notifyDataSetChanged();
                callback.switchSelectMode();
                return true;
            }
        });
        hospitalVH.checkBox.setVisibility(mode == Mode.Select ? View.VISIBLE : View.GONE);
        hospitalVH.checkBox.setChecked(favorites.isSelected);
        hospitalVH.swipeLayout.setRightSwipeEnabled(mode == Mode.Normal);
        hospitalVH.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                openSwipeLayout = layout;
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {
                openSwipeLayout = null;
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });
        hospitalVH.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (openSwipeLayout != null) openSwipeLayout.close();
                if (callback == null) return;
                callback.onDoctorDeleted(position, favorites);
            }
        });
    }

    public void unselectAll() {
        selectedCount = 0;
        for (DoctorFavorites favorites : data) {
            favorites.isSelected = false;
        }
        notifyDataSetChanged();
    }

    public void selectAll() {
        for (DoctorFavorites favorites : data) {
            favorites.isSelected = true;
        }
        notifyDataSetChanged();
    }

    public void setCallback(FavoritesDoctorCallback callback) {
        this.callback = callback;
    }

    public List<DbDoctor> getSelectedItem() {
        List<DbDoctor> items = new ArrayList<>();
        for (DoctorFavorites f : data) {
            if (f.isSelected) {
                items.add(f.doctor);
            }
        }
        return items;
    }

    public List<DoctorFavorites> getSelectedOriginItem() {
        List<DoctorFavorites> items = new ArrayList<>();
        for (DoctorFavorites f : data) {
            if (f.isSelected) {
                items.add(f);
            }
        }
        return items;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        if (mode == Mode.Normal) {
            for (DoctorFavorites favorites : data) {
                favorites.isSelected = false;
            }
        }
        notifyDataSetChanged();
    }

    public Mode getMode() {
        return mode;
    }

    private class HospitalVH extends RecyclerView.ViewHolder {

        SwipeLayout swipeLayout;
        View rootView;
        CheckBox checkBox;
        NetworkImageView imageView;
        TextView nameView;
        View deleteView;

        public HospitalVH(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.item_favorites_hospital_swipe);
            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            swipeLayout.addDrag(SwipeLayout.DragEdge.Right, itemView.findViewById(R.id.item_favorites_hospital_drawer));

            rootView = itemView.findViewById(R.id.item_favorites_hospital_root);
            checkBox = (CheckBox) itemView.findViewById(R.id.item_favorites_hospital_check);
            imageView = (NetworkImageView) itemView.findViewById(R.id.item_favorites_hospital_img);
            nameView = (TextView) itemView.findViewById(R.id.item_favorites_hospital_name);
            deleteView = itemView.findViewById(R.id.item_favorites_hospital_delete);
        }
    }

    public static class DoctorFavorites {

        boolean isSelected;
        public DbDoctor doctor;

        public DoctorFavorites() {
        }

        public DoctorFavorites(boolean isSelected, DbDoctor hospital) {
            this.isSelected = isSelected;
            this.doctor = hospital;
        }
    }

    public interface FavoritesDoctorCallback {

        void jumpToDetailActivity(int position, String doctorid, String doctorname);
        void onDoctorDeleted(int position, DoctorFavorites favorites);
        void switchSelectMode();
        void onAllSelected();
        void onPartSelected();

    }
}
