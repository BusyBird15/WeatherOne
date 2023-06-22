package com.google.appinventor.components.runtime;

public interface ObservableDataSource<K, V> extends DataSource<K, V> {
    void addDataObserver(DataSourceChangeListener dataSourceChangeListener);

    void notifyDataObservers(K k, Object obj);

    void removeDataObserver(DataSourceChangeListener dataSourceChangeListener);
}
