package com.xtree.live.uitl;

import android.content.res.Configuration;

public class WindowModeUtils {
    public static boolean isFreeFormMode(Configuration configuration) {
        String configStr = configuration.toString();
        return configStr.contains("mWindowingMode=freeform") ||
                configStr.contains("mWindowingMode=100") ||
                configStr.contains("mWindowingMode=hwMultiwindow-freeform");
    }
}
