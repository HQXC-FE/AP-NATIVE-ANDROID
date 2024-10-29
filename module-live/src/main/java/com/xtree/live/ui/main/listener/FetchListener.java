package com.xtree.live.ui.main.listener;

import androidx.lifecycle.Observer;

import java.util.Map;

public interface FetchListener<T> {
    void fetch(int page, int limit, Map<String, Object> params, Observer<T> success, Observer<Object> error);
}
