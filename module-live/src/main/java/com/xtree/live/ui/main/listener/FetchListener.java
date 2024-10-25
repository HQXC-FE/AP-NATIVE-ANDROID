package com.xtree.live.ui.main.listener;

import androidx.lifecycle.Observer;

import com.xtree.live.data.source.response.FrontLivesResponse;

public interface FetchListener<T> {
    void fetch(int page, int limit, Observer<T> success,Observer<Object> error);
}
