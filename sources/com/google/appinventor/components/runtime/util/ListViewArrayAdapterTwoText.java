package com.google.appinventor.components.runtime.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import java.util.List;

public class ListViewArrayAdapterTwoText {
    private ComponentContainer container;
    /* access modifiers changed from: private */
    public List<YailDictionary> currentItems;
    /* access modifiers changed from: private */
    public int detailTextColor;
    /* access modifiers changed from: private */
    public int detailTextSize;
    /* access modifiers changed from: private */
    public final Filter filter = new Filter() {
        /* access modifiers changed from: protected */
        public Filter.FilterResults performFiltering(CharSequence charSequence) {
            String filterQuery = charSequence.toString().toLowerCase();
            Filter.FilterResults results = new Filter.FilterResults();
            if (filterQuery == null || filterQuery.length() == 0) {
                results.count = ListViewArrayAdapterTwoText.this.currentItems.size();
                results.values = ListViewArrayAdapterTwoText.this.currentItems;
            } else {
                ListViewArrayAdapterTwoText.this.filterCurrentItems.clear();
                for (YailDictionary item : ListViewArrayAdapterTwoText.this.currentItems) {
                    if (item.get(Component.LISTVIEW_KEY_MAIN_TEXT).toString().concat(ElementsUtil.toStringEmptyIfNull(item.get(Component.LISTVIEW_KEY_DESCRIPTION))).contains(charSequence)) {
                        ListViewArrayAdapterTwoText.this.filterCurrentItems.add(item);
                    }
                }
                results.count = ListViewArrayAdapterTwoText.this.filterCurrentItems.size();
                results.values = ListViewArrayAdapterTwoText.this.filterCurrentItems;
            }
            return results;
        }

        /* access modifiers changed from: protected */
        public void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
            ListViewArrayAdapterTwoText.this.filterCurrentItems.clear();
            ListViewArrayAdapterTwoText.this.itemAdapter.clear();
            for (YailDictionary item : ListViewArrayAdapterTwoText.this.filterCurrentItems) {
                ListViewArrayAdapterTwoText.this.itemAdapter.add(item);
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

    public ListViewArrayAdapterTwoText(int textSize2, int detailTextSize2, int textColor2, int detailTextColor2, ComponentContainer container2, List<YailDictionary> items) {
        this.textSize = textSize2;
        this.detailTextSize = detailTextSize2;
        this.textColor = textColor2;
        this.detailTextColor = detailTextColor2;
        this.container = container2;
        this.currentItems = items;
        this.filterCurrentItems = items;
    }

    public ArrayAdapter<YailDictionary> createAdapter() {
        this.itemAdapter = new ArrayAdapter<YailDictionary>(this.container.$context(), 17367044, 16908308, this.currentItems) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(16908308);
                TextView text2 = (TextView) view.findViewById(16908309);
                YailDictionary row = (YailDictionary) ListViewArrayAdapterTwoText.this.filterCurrentItems.get(position);
                String val1 = ElementsUtil.toStringEmptyIfNull(row.get(Component.LISTVIEW_KEY_MAIN_TEXT));
                String val2 = ElementsUtil.toStringEmptyIfNull(row.get(Component.LISTVIEW_KEY_DESCRIPTION));
                text1.setText(val1);
                text2.setText(val2);
                text1.setTextColor(ListViewArrayAdapterTwoText.this.textColor);
                text2.setTextColor(ListViewArrayAdapterTwoText.this.detailTextColor);
                text1.setTextSize((float) ListViewArrayAdapterTwoText.this.textSize);
                text2.setTextSize((float) ListViewArrayAdapterTwoText.this.detailTextSize);
                return view;
            }

            public Filter getFilter() {
                return ListViewArrayAdapterTwoText.this.filter;
            }
        };
        return this.itemAdapter;
    }
}
