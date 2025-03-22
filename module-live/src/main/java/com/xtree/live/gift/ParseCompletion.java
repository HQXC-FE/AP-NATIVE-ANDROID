package com.xtree.live.gift;

import androidx.annotation.NonNull;

import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

import java.io.File;
import java.util.List;

public class ParseCompletion implements SVGAParser.ParseCompletion, SVGAParser.PlayCallback {

    private SVGAParser.ParseCompletion callback1;
    private SVGAParser.PlayCallback callback2;

    public ParseCompletion(SVGAParser.ParseCompletion callback1, SVGAParser.PlayCallback callback2) {
        this.callback1 = callback1;
        this.callback2 = callback2;
    }

    public void release(){
        callback1 = null;
        callback2 = null;
    }

    @Override
    public void onComplete(@NonNull SVGAVideoEntity svgaVideoEntity) {
        if(callback1 != null)callback1.onComplete(svgaVideoEntity);
    }

    @Override
    public void onError() {
        if(callback1 != null)callback1.onError();
    }

    @Override
    public void onPlay(@NonNull List<? extends File> list) {
        if(callback2 != null)callback2.onPlay(list);
    }
}

