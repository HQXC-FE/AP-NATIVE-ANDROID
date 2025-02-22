package com.xtree.live.inter;

import androidx.annotation.NonNull;

public interface SearchContent {
    @NonNull
    String content();

    void search(String content);
}
