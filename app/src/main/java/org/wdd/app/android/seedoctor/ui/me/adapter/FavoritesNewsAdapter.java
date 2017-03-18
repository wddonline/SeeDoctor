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
import org.wdd.app.android.seedoctor.database.model.DBNews;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.views.NetworkImageView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by richard on 11/28/16.
 */

public class FavoritesNewsAdapter extends AbstractCommonAdapter<FavoritesNewsAdapter.NewsFavorites> {

    public enum Mode {
        Normal,
        Select
    }

    private SwipeLayout openSwipeLayout;
    private Mode mode = Mode.Normal;
    private FavoritesNewsCallback callback;

    private int selectedCount = 0;

    public FavoritesNewsAdapter(Context context, List<NewsFavorites> data) {
        super(context, data);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorites_hospital, parent, false);
        RecyclerView.ViewHolder viewHolder = new NewsVH(view);
        return viewHolder;
    }

    @Override
    protected void onBindDataViewHolder(final RecyclerView.ViewHolder holder, final NewsFavorites favorites, final int position) {
        final NewsVH newsVH = (NewsVH) holder;
        newsVH.imageView.setImageUrl(favorites.news.image);
        newsVH.nameView.setText(favorites.news.title);
        newsVH.rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (openSwipeLayout != null) {
                    openSwipeLayout.close(true);
                    return true;
                }
                return false;
            }
        });
        newsVH.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mode) {
                    case Normal:
                        if (callback != null) callback.jumpToDetailActivity(favorites.news.news_id, favorites.news.image, favorites.news.title);
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
        newsVH.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mode = Mode.Select;
                notifyDataSetChanged();
                callback.switchSelectMode();
                return true;
            }
        });
        newsVH.checkBox.setVisibility(mode == Mode.Select ? View.VISIBLE : View.GONE);
        newsVH.checkBox.setChecked(favorites.isSelected);
        newsVH.swipeLayout.setRightSwipeEnabled(mode == Mode.Normal);
        newsVH.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
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
        newsVH.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (openSwipeLayout != null) openSwipeLayout.close();
                if (callback == null) return;
                callback.onNewsDeleted(favorites);
            }
        });
    }

    public void unselectAll() {
        selectedCount = 0;
        for (NewsFavorites favorites : data) {
            favorites.isSelected = false;
        }
        notifyDataSetChanged();
    }

    public void selectAll() {
        for (NewsFavorites favorites : data) {
            favorites.isSelected = true;
        }
        selectedCount = getLoadStatus() == LoadStatus.NoMore ? data.size() : data.size() - 1;
        notifyDataSetChanged();
    }

    public void setCallback(FavoritesNewsCallback callback) {
        this.callback = callback;
    }

    public void removeDataByNewsId(String newsid) {
        Iterator<NewsFavorites> iterator = data.iterator();
        NewsFavorites item;
        int position = 0;
        while (iterator.hasNext()) {
            item = iterator.next();
            if (item.news.news_id.equals(newsid)) {
                iterator.remove();
                notifyItemRemoved(position);
                break;
            }
            position++;
        }
    }

    public void removeDataById(int id) {
        Iterator<NewsFavorites> iterator = data.iterator();
        NewsFavorites item;
        int position = 0;
        while (iterator.hasNext()) {
            item = iterator.next();
            if (item.news.id == id) {
                iterator.remove();
                notifyItemRemoved(position);
                break;
            }
            position++;
        }
    }

    public List<DBNews> getSelectedItem() {
        List<DBNews> items = new ArrayList<>();
        for (NewsFavorites f : data) {
            if (f.isSelected) {
                items.add(f.news);
            }
        }
        return items;
    }

    public List<NewsFavorites> getSelectedOriginItem() {
        List<NewsFavorites> items = new ArrayList<>();
        for (NewsFavorites f : data) {
            if (f.isSelected) {
                items.add(f);
            }
        }
        return items;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        if (mode == Mode.Normal) {
            for (NewsFavorites favorites : data) {
                favorites.isSelected = false;
            }
            selectedCount = 0;
        }
        notifyDataSetChanged();
    }

    public Mode getMode() {
        return mode;
    }

    private class NewsVH extends RecyclerView.ViewHolder {

        SwipeLayout swipeLayout;
        View rootView;
        CheckBox checkBox;
        NetworkImageView imageView;
        TextView nameView;
        View deleteView;

        public NewsVH(View itemView) {
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

    public static class NewsFavorites {

        boolean isSelected;
        public DBNews news;

        public NewsFavorites() {
        }

        public NewsFavorites(boolean isSelected, DBNews news) {
            this.isSelected = isSelected;
            this.news = news;
        }
    }

    public interface FavoritesNewsCallback {

        void jumpToDetailActivity(String newsId, String image, String title);
        void onNewsDeleted(NewsFavorites favorites);
        void switchSelectMode();
        void onAllSelected();
        void onPartSelected();

    }
}
