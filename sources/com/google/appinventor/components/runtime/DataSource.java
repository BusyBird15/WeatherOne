package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.SimpleObject;

@SimpleObject
public interface DataSource<K, V> {
    V getDataValue(K k);
}
