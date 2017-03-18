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
import org.wdd.app.android.seedoctor.database.model.DbDrug;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by richard on 11/28/16.
 */

public class FavoritesDrugAdapter extends AbstractCommonAdapter<FavoritesDrugAdapter.DrugFavorites> {

    public enum Mode {
        Normal,
        Select
    }

    private SwipeLayout openSwipeLayout;
    private Mode mode = Mode.Normal;
    private FavoritesDrugCallback callback;

    private int selectedCount = 0;

    public FavoritesDrugAdapter(Context context, List<DrugFavorites> data) {
        super(context, data);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorites_department, parent, false);
        RecyclerView.ViewHolder viewHolder = new HospitalVH(view);
        return viewHolder;
    }

    @Override
    protected void onBindDataViewHolder(final RecyclerView.ViewHolder holder, final DrugFavorites favorites, final int position) {
        final HospitalVH drugVH = (HospitalVH) holder;
        drugVH.nameView.setText(favorites.drug.drugname);
        drugVH.rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (openSwipeLayout != null) {
                    openSwipeLayout.close(true);
                    return true;
                }
                return false;
            }
        });
        drugVH.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mode) {
                    case Normal:
                        if (callback != null) callback.jumpToDetailActivity(favorites.drug.drugid, favorites.drug.drugname);
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
        drugVH.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mode = Mode.Select;
                notifyDataSetChanged();
                callback.switchSelectMode();
                return true;
            }
        });
        drugVH.checkBox.setVisibility(mode == Mode.Select ? View.VISIBLE : View.GONE);
        drugVH.checkBox.setChecked(favorites.isSelected);
        drugVH.swipeLayout.setRightSwipeEnabled(mode == Mode.Normal);
        drugVH.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
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
        drugVH.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (openSwipeLayout != null) openSwipeLayout.close();
                if (callback == null) return;
                callback.onDrugDeleted(favorites);
            }
        });
    }

    public void unselectAll() {
        selectedCount = 0;
        for (DrugFavorites favorites : data) {
            favorites.isSelected = false;
        }
        notifyDataSetChanged();
    }

    public void selectAll() {
        for (DrugFavorites favorites : data) {
            favorites.isSelected = true;
        }
        selectedCount = getLoadStatus() == LoadStatus.NoMore ? data.size() : data.size() - 1;
        notifyDataSetChanged();
    }

    public void setCallback(FavoritesDrugCallback callback) {
        this.callback = callback;
    }

    public void removeDataByDrugId(String drugid) {
        Iterator<DrugFavorites> iterator = data.iterator();
        DrugFavorites item;
        int position = 0;
        while (iterator.hasNext()) {
            item = iterator.next();
            if (item.drug.drugid.equals(drugid)) {
                iterator.remove();
                notifyItemRemoved(position);
                break;
            }
            position++;
        }
    }

    public void removeDataById(int id) {
        Iterator<DrugFavorites> iterator = data.iterator();
        DrugFavorites item;
        int position = 0;
        while (iterator.hasNext()) {
            item = iterator.next();
            if (item.drug.id == id) {
                iterator.remove();
                notifyItemRemoved(position);
                break;
            }
            position++;
        }
    }

    public List<DbDrug> getSelectedItem() {
        List<DbDrug> items = new ArrayList<>();
        for (DrugFavorites f : data) {
            if (f.isSelected) {
                items.add(f.drug);
            }
        }
        return items;
    }

    public List<DrugFavorites> getSelectedOriginItem() {
        List<DrugFavorites> items = new ArrayList<>();
        for (DrugFavorites f : data) {
            if (f.isSelected) {
                items.add(f);
            }
        }
        return items;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        if (mode == Mode.Normal) {
            for (DrugFavorites favorites : data) {
                favorites.isSelected = false;
            }
            selectedCount = 0;
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

    public static class DrugFavorites {

        boolean isSelected;
        public DbDrug drug;

        public DrugFavorites() {
        }

        public DrugFavorites(boolean isSelected, DbDrug drug) {
            this.isSelected = isSelected;
            this.drug = drug;
        }
    }

    public interface FavoritesDrugCallback {

        void jumpToDetailActivity(String drugid, String drugname);
        void onDrugDeleted(DrugFavorites favorites);
        void switchSelectMode();
        void onAllSelected();
        void onPartSelected();

    }
}
