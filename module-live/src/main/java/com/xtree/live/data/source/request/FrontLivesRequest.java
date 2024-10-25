package com.xtree.live.data.source.request;

import com.xtree.base.utils.UuidUtil;

/**
 * Created by KAKA on 2024/10/22.
 * Describe:
 */
public class FrontLivesRequest {
    private String type;
    private int page;
    private int limit;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
