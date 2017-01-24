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
import org.wdd.app.android.seedoctor.database.model.DbDisease;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 11/28/16.
 */

public class FavoritesDiseaseAdapter extends AbstractCommonAdapter<FavoritesDiseaseAdapter.DiseaseFavorites> {

    public enum Mode {
        Normal,
        Select
    }

    private SwipeLayout openSwipeLayout;
    private Mode mode = Mode.Normal;
    private FavoritesDiseaseCallback callback;

    private int selectedCount = 0;

    public FavoritesDiseaseAdapter(Context context, List<DiseaseFavorites> data) {
        super(context, data);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorites_department, parent, false);
        RecyclerView.ViewHolder viewHolder = new HospitalVH(view);
        return viewHolder;
    }

    @Override
    protected void onBindDataViewHolder(final RecyclerView.ViewHolder holder, final DiseaseFavorites favorites, final int position) {
        final HospitalVH diseaseVH = (HospitalVH) holder;
        diseaseVH.nameView.setText(favorites.disease.diseasename);
        diseaseVH.rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (openSwipeLayout != null) {
                    openSwipeLayout.close(true);
                    return true;
                }
                return false;
            }
        });
        diseaseVH.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mode) {
                    case Normal:
                        if (callback != null) callback.jumpToDetailActivity(position, favorites.disease.diseaseid, favorites.disease.diseasename);
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
        diseaseVH.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mode = Mode.Select;
                notifyDataSetChanged();
                callback.switchSelectMode();
                return true;
            }
        });
        diseaseVH.checkBox.setVisibility(mode == Mode.Select ? View.VISIBLE : View.GONE);
        diseaseVH.checkBox.setChecked(favorites.isSelected);
//        diseaseVH.swipeLayout.setRightSwipeEnabled(mode == Mode.Normal);
        diseaseVH.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
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
        diseaseVH.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (openSwipeLayout != null) openSwipeLayout.close();
                if (callback == null) return;
                callback.onDiseaseDeleted(position, favorites);
            }
        });
    }

    public void unselectAll() {
        selectedCount = 0;
        for (DiseaseFavorites favorites : data) {
            favorites.isSelected = false;
        }
        notifyDataSetChanged();
    }

    public void selectAll() {
        for (DiseaseFavorites favorites : data) {
            favorites.isSelected = true;
        }
        notifyDataSetChanged();
    }

    public void setCallback(FavoritesDiseaseCallback callback) {
        this.callback = callback;
    }

    public List<DbDisease> getSelectedItem() {
        List<DbDisease> items = new ArrayList<>();
        for (DiseaseFavorites f : data) {
            if (f.isSelected) {
                items.add(f.disease);
            }
        }
        return items;
    }

    public List<DiseaseFavorites> getSelectedOriginItem() {
        List<DiseaseFavorites> items = new ArrayList<>();
        for (DiseaseFavorites f : data) {
            if (f.isSelected) {
                items.add(f);
            }
        }
        return items;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        if (mode == Mode.Normal) {
            for (DiseaseFavorites favorites : data) {
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

    public static class DiseaseFavorites {

        boolean isSelected;
        public DbDisease disease;

        public DiseaseFavorites() {
        }

        public DiseaseFavorites(boolean isSelected, DbDisease disease) {
            this.isSelected = isSelected;
            this.disease = disease;
        }
    }

    public interface FavoritesDiseaseCallback {

        void jumpToDetailActivity(int position, String diseaseid, String diseasename);
        void onDiseaseDeleted(int position, DiseaseFavorites favorites);
        void switchSelectMode();
        void onAllSelected();
        void onPartSelected();

    }
}
