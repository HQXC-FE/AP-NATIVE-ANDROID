/*
 * Copyright 2014 Hieu Rocker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.rockerhieu.emojicon;

import android.content.Context;
import android.text.Spannable;
import android.util.SparseIntArray;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hieu Rocker (rockerhieu@gmail.com)
 */
public final class EmojiconHandler {
    private EmojiconHandler() {
    }

    private static final SparseIntArray sEmojiModifiersMap = new SparseIntArray(5);
    private static Map<String, Integer> sEmojisModifiedMap = new HashMap<>();

    static {
        sEmojiModifiersMap.put(0x1f3fb, 1);
        sEmojiModifiersMap.put(0x1f3fc, 1);
        sEmojiModifiersMap.put(0x1f3fd, 1);
        sEmojiModifiersMap.put(0x1f3fe, 1);
        sEmojiModifiersMap.put(0x1f3ff, 1);
    }


    private static boolean isSoftBankEmoji(char c) {
        return ((c >> 12) == 0xe);
    }

    /**
     * Convert emoji characters of the given Spannable to the according emojicon.
     *
     * @param context
     * @param text
     * @param emojiSize
     * @param emojiAlignment
     * @param textSize
     */
    public static void addEmojis(Context context, Spannable text, int emojiSize, int emojiAlignment, int textSize) {
        addEmojis(context, text, emojiSize, emojiAlignment, textSize, 0, -1, false);
    }

    /**
     * Convert emoji characters of the given Spannable to the according emojicon.
     *
     * @param context
     * @param text
     * @param emojiSize
     * @param emojiAlignment
     * @param textSize
     * @param index
     * @param length
     */
    public static void addEmojis(Context context, Spannable text, int emojiSize, int emojiAlignment, int textSize, int index, int length) {
        addEmojis(context, text, emojiSize, emojiAlignment, textSize, index, length, false);
    }

    /**
     * Convert emoji characters of the given Spannable to the according emojicon.
     *
     * @param context
     * @param text
     * @param emojiSize
     * @param emojiAlignment
     * @param textSize
     * @param useSystemDefault
     */
    public static void addEmojis(Context context, Spannable text, int emojiSize, int emojiAlignment, int textSize, boolean useSystemDefault) {
        addEmojis(context, text, emojiSize, emojiAlignment, textSize, 0, -1, useSystemDefault);
    }

    /**
     * Convert emoji characters of the given Spannable to the according emojicon.
     *
     * @param context
     * @param text
     * @param emojiSize
     * @param emojiAlignment
     * @param textSize
     * @param index
     * @param length
     * @param useSystemDefault
     */
    public static void addEmojis(Context context, Spannable text, int emojiSize, int emojiAlignment, int textSize, int index, int length, boolean useSystemDefault) {
        if (useSystemDefault || text == null) {
            return;
        }

    }

}
