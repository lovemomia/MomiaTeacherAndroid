package com.youxing.common.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

/**
 * 类似iOS group样式ListView的Adapter
 *
 * Created by Jun Deng on 15/6/4.
 */
public abstract class GroupStyleAdapter extends BasicAdapter {

    private final Context context;

    public GroupStyleAdapter(Context context) {
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    final public int getCount() {
        int sectionCount = getSectionCount();
        int count = sectionCount;
        for (int i = 0; i < sectionCount; i++) {
            count += getCountInSection(i);
        }
        return count;
    }

    abstract public int getSectionCount();

    abstract public int getCountInSection(int section);

    public boolean isSectionAtPosition(int position) {
        return getIndexForPosition(position).row == -1;
    }

    public IndexPath getIndexForPosition(int position) {
        IndexPath index = new IndexPath();
        int sumCount = 0;
        int section;
        for (section = 0; section < getSectionCount(); section++) {
            int rowCount = getCountInSection(section);
            sumCount += (rowCount + 1);
            if (position < sumCount) {
                index.section = section;
                index.row = rowCount - (sumCount - position);
                break;
            }
        }
        return index;
    }

    @Override
    public boolean isEnabled(int position) {
        if (isSectionAtPosition(position)) {
            return false;
        }
        return super.isEnabled(position);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    final public View getView(int position, View convertView, ViewGroup parent) {
        IndexPath indexPath = getIndexForPosition(position);
        if (indexPath.row == -1) {
            View view = getViewForSection(convertView, parent, indexPath.section);
            if (!isEnabled(position)) {
                view.setClickable(true);
                view.setFocusable(true);
            }
            return view;
        } else {
            View view = getViewForRow(convertView, parent, indexPath.section, indexPath.row);
            view.setBackgroundColor(getBackgroundColorForRow(indexPath));
            return view;
        }
    }

    abstract public View getViewForRow(View convertView, ViewGroup parent, int section, int row);

    public int getBackgroundColorForRow(IndexPath indexPath) {
        return Color.WHITE;
    }

    public View getViewForSection(View convertView, ViewGroup parent, int section) {
        View view = new View(context);
        view.setBackgroundColor(Color.parseColor("#F4F4F4"));
        view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                getHeightForSectionView(section)));
        return view;
    }

    /**
     * 设置section高度
     *
     * @param section
     * @return
     */
    public int getHeightForSectionView(int section) {
        return 20;
    }

    /**
     * 维护section与row关系的索引
     * <p>
     *     注：如果row为-1，表示当前item为section
     */
    public static class IndexPath {
        public int section;
        public int row;
    }

}
