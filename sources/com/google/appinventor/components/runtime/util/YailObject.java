package com.google.appinventor.components.runtime.util;

import androidx.annotation.NonNull;
import java.util.Iterator;

public interface YailObject<T> extends Iterable<T> {
    Object getObject(int i);

    boolean isEmpty();

    @NonNull
    Iterator<T> iterator();

    int size();
}
