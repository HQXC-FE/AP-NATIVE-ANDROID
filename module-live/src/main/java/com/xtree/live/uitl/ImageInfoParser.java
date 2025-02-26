package com.xtree.live.uitl;

import android.graphics.Point;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.xtree.live.exception.BitmapDecodingException;
import com.xtree.live.socket.BNC;

import java.io.IOException;
import java.io.InputStream;

import me.xtree.mvvmhabit.utils.Utils;

public class ImageInfoParser {


    public static  @NonNull Point calculateImageViewDimensions(float imageRealWidth, float imageRealHeight, int availableImageWidth, int availableImageHeight, int miniWidth, int miniHeight) {
        float ratio = imageRealWidth / imageRealHeight;
        float maxHeight = Math.min(availableImageHeight, Math.max(imageRealHeight, miniHeight));
        float maxWidth = Math.min(availableImageWidth, Math.max(imageRealWidth, miniWidth));

        float ratioContainer = maxWidth / maxHeight;

        int width;
        int height;
        if(ratio >= ratioContainer){
            width = (int) maxWidth;
            height = (int) (maxWidth / ratio);
        }else {
            height = (int) maxHeight;
            width = (int) (maxHeight * ratio);
        }

        if(height < miniHeight){
            height = miniHeight;
            width = (int) (height * ratio);
            if(width > availableImageWidth)width = availableImageWidth;
        }else if(width < miniWidth){
            width = miniWidth;
            height = (int) (width / ratio);
            if(height > availableImageHeight)height = availableImageHeight;
        }
        return new Point(width, height);
    }

    public @NonNull static Point resolveImageSizeFromUri(String imageUri, int defaultWidth, int defaultHeight) {
        if(TextUtils.isEmpty(imageUri)) return defaultPoint(defaultWidth, defaultHeight);
        if(imageUri.startsWith("content://")){
            try {
                InputStream input = PartAuthority.getAttachmentStream(Utils.getContext(), Uri.parse(imageUri));
                Pair<Integer, Integer> dimensions = BitmapUtil.getDimensions(input);
                return new Point(dimensions.first, dimensions.second);
            } catch (IOException | IllegalArgumentException | BitmapDecodingException e) {
                return defaultPoint(defaultWidth, defaultHeight);
            }
        }
        imageUri = imageUri.toLowerCase();
        String[] segments = imageUri.split("\\.");
        int atLeastLength = imageUri.endsWith(BNC.SUFFIX) ? 4 : 3;
        if(segments.length < atLeastLength) return defaultPoint(defaultWidth, defaultHeight);
        String sizeStr = segments[segments.length - atLeastLength + 1];
        if(TextUtils.isEmpty(sizeStr) || !sizeStr.contains("x")) return defaultPoint(defaultWidth, defaultHeight);
        String[] sizeArray = sizeStr.split("x");
        if(sizeArray.length != 2) return defaultPoint(defaultWidth, defaultHeight);
        try {
            int width = Integer.parseInt(sizeArray[0]);
            int height = Integer.parseInt(sizeArray[1]);
            if(width == 0 || height == 0) return defaultPoint(defaultWidth, defaultHeight);
            return new Point(Integer.parseInt(sizeArray[0]), Integer.parseInt(sizeArray[1]));
        }catch (NumberFormatException t){
            return defaultPoint(defaultWidth, defaultHeight);
        }
    }
    private static Point defaultPoint(int defaultWidth, int defaultHeight){
        return new Point(defaultWidth, defaultHeight);
    }
}

