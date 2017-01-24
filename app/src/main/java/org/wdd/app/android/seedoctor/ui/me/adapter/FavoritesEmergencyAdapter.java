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
import org.wdd.app.android.seedoctor.database.model.DbEmergency;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 11/28/16.
 */

public class FavoritesEmergencyAdapter extends AbstractCommonAdapter<FavoritesEmergencyAdapter.EmergencyFavorites> {

    public enum Mode {
        Normal,
        Select
    }

    private SwipeLayout openSwipeLayout;
    private Mode mode = Mode.Normal;
    private FavoritesEmergencyCallback callback;

    private int selectedCount = 0;

    public FavoritesEmergencyAdapter(Context context, List<EmergencyFavorites> data) {
        super(context, data);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorites_department, parent, false);
        RecyclerView.ViewHolder viewHolder = new HospitalVH(view);
        return viewHolder;
    }

    @Override
    protected void onBindDataViewHolder(final RecyclerView.ViewHolder holder, final EmergencyFavorites favorites, final int position) {
        final HospitalVH emergencyVH = (HospitalVH) holder;
        emergencyVH.nameView.setText(favorites.emergency.eme);
        emergencyVH.rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (openSwipeLayout != null) {
                    openSwipeLayout.close(true);
                    return true;
                }
                return false;
            }
        });
        emergencyVH.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mode) {
                    case Normal:
                        if (callback != null) callback.jumpToDetailActivity(position, favorites.emergency.emeid, favorites.emergency.eme);
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
        emergencyVH.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mode = Mode.Select;
                notifyDataSetChanged();
                callback.switchSelectMode();
                return true;
            }
        });
        emergencyVH.checkBox.setVisibility(mode == Mode.Select ? View.VISIBLE : View.GONE);
        emergencyVH.checkBox.setChecked(favorites.isSelected);
//        emergencyVH.swipeLayout.setRightSwipeEnabled(mode == Mode.Normal);
        emergencyVH.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
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
        emergencyVH.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (openSwipeLayout != null) openSwipeLayout.close();
                if (callback == null) return;
                callback.onEmergencyDeleted(position, favorites);
            }
        });
    }

    public void unselectAll() {
        selectedCount = 0;
        for (EmergencyFavorites favorites : data) {
            favorites.isSelected = false;
        }
        notifyDataSetChanged();
    }

    public void selectAll() {
        for (EmergencyFavorites favorites : data) {
            favorites.isSelected = true;
        }
        notifyDataSetChanged();
    }

    public void setCallback(FavoritesEmergencyCallback callback) {
        this.callback = callback;
    }

    public List<DbEmergency> getSelectedItem() {
        List<DbEmergency> items = new ArrayList<>();
        for (EmergencyFavorites f : data) {
            if (f.isSelected) {
                items.add(f.emergency);
            }
        }
        return items;
    }

    public List<EmergencyFavorites> getSelectedOriginItem() {
        List<EmergencyFavorites> items = new ArrayList<>();
        for (EmergencyFavorites f : data) {
            if (f.isSelected) {
                items.add(f);
            }
        }
        return items;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        if (mode == Mode.Normal) {
            for (EmergencyFavorites favorites : data) {
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
        TextView nameView;
        View deleteView;

        public HospitalVH(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.item_favorites_department_swipe);
            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            swipeLayout.addDrag(SwipeLayout.DragEdge.Right, itemView.findViewById(R.id.item_favorites_department_drawer));

            rootView = itemView.findViewById(R.id.item_favorites_department_root);
            checkBox = (CheckBox) itemView.findViewById(R.id.item_favorites_department_check);
            nameView = (TextView) itemView.findViewById(R.id.item_favorites_department_name);
            deleteView = itemView.findViewById(R.id.item_favorites_department_delete);
        }
    }

    public static class EmergencyFavorites {

        boolean isSelected;
        public DbEmergency emergency;

        public EmergencyFavorites() {
        }

        public EmergencyFavorites(boolean isSelected, DbEmergency emergency) {
            this.isSelected = isSelected;
            this.emergency = emergency;
        }
    }

    public interface FavoritesEmergencyCallback {

        void jumpToDetailActivity(int position, String emergencyid, String emergencyname);
        void onEmergencyDeleted(int position, EmergencyFavorites favorites);
        void switchSelectMode();
        void onAllSelected();
        void onPartSelected();

    }
}
