package com.xtree.live.uitl;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

public class HtmlHelper {
    public static Spanned applyHtmlStr(String s, TextView text){
        String formattedHtml = s.replace(" ", "&nbsp;")
                .replace("\r\n", "<br />")
                .replace("\r", "<br />")
                .replace("\n", "<br />")
                .replace("<img&nbsp;src=", "<img src=");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // Android N (API 24)及以上版本
            return Html.fromHtml(formattedHtml, Html.FROM_HTML_MODE_LEGACY,
                    new HtmlImageGetter(text), null);
        } else {
            // Android N之前的版本
            return Html.fromHtml(formattedHtml, new HtmlImageGetter(text), null);
        }
    }
}
