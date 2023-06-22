package com.google.appinventor.components.runtime.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import java.util.List;

public class ListViewArrayAdapterSingleText {
    private ComponentContainer container;
    /* access modifiers changed from: private */
    public List<YailDictionary> currentItems;
    /* access modifiers changed from: private */
    public final Filter filter = new Filter() {
        /* access modifiers changed from: protected */
        public Filter.FilterResults performFiltering(CharSequence charSequence) {
            String filterQuery = charSequence.toString().toLowerCase();
            Filter.FilterResults results = new Filter.FilterResults();
            if (filterQuery == null || filterQuery.length() == 0) {
                results.count = ListViewArrayAdapterSingleText.this.currentItems.size();
                results.values = ListViewArrayAdapterSingleText.this.currentItems;
            } else {
                for (YailDictionary item : ListViewArrayAdapterSingleText.this.currentItems) {
                    ListViewArrayAdapterSingleText.this.filterCurrentItems.clear();
                    if (item.get(Component.LISTVIEW_KEY_MAIN_TEXT).toString().contains(filterQuery)) {
                        ListViewArrayAdapterSingleText.this.filterCurrentItems.add(item);
                    }
                }
                results.count = ListViewArrayAdapterSingleText.this.filterCurrentItems.size();
                results.values = ListViewArrayAdapterSingleText.this.filterCurrentItems;
            }
            return results;
        }

        /* access modifiers changed from: protected */
        public void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
            List unused = ListViewArrayAdapterSingleText.this.filterCurrentItems = (List) filterResults.values;
            ListViewArrayAdapterSingleText.this.itemAdapter.clear();
            if (ListViewArrayAdapterSingleText.this.filterCurrentItems != null) {
                for (int i = 0; i < ListViewArrayAdapterSingleText.this.filterCurrentItems.size(); i++) {
                    ListViewArrayAdapterSingleText.this.itemAdapter.add(ListViewArrayAdapterSingleText.this.filterCurrentItems.get(i));
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public List<YailDictionary> filterCurrentItems;
    /* access modifiers changed from: private */
    public ArrayAdapter<YailDictionary> itemAdapter;
    /* access modifiers changed from: private */
    public int textColor;
    /* access modifiers changed from: private */
    public int textSize;

    public ListViewArrayAdapterSingleText(int textSize2, int textColor2, ComponentContainer container2, List<YailDictionary> items) {
        this.textSize = textSize2;
        this.textColor = textColor2;
        this.container = container2;
        this.currentItems = items;
        this.filterCurrentItems = items;
    }

    public ArrayAdapter<YailDictionary> createAdapter() {
        this.itemAdapter = new ArrayAdapter<YailDictionary>(this.container.$context(), 17367043, this.currentItems) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(16908308);
                text1.setText(ElementsUtil.toStringEmptyIfNull(((YailDictionary) ListViewArrayAdapterSingleText.this.filterCurrentItems.get(position)).get(Component.LISTVIEW_KEY_MAIN_TEXT)));
                text1.setTextColor(ListViewArrayAdapterSingleText.this.textColor);
                text1.setTextSize((float) ListViewArrayAdapterSingleText.this.textSize);
                return view;
            }

            public Filter getFilter() {
                return ListViewArrayAdapterSingleText.this.filter;
            }
        };
        return this.itemAdapter;
    }
}
