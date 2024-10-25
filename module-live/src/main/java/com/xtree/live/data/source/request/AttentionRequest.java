package com.xtree.live.data.source.request;

public class AttentionRequest {
    public AttentionRequest(){
        super();
        this.type ="0";
    }
    private String type ;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
