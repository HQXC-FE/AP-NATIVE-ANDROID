package io.github.rockerhieu.emojicon.emoji;

import androidx.annotation.NonNull;

import java.util.List;

public class EmojiCategory {
    private String category;
    private List<Emojicon> emojicons;

    public EmojiCategory(String category, List<Emojicon> emojicons) {
        this.category = category;
        this.emojicons = emojicons;
    }

    @NonNull
    public String getCategory() {
        return category;
    }

    public void setCategory(@NonNull String category) {
        this.category = category;
    }

    public @NonNull List<Emojicon> getEmojicons() {
        return emojicons;
    }

    public void setEmojicons(@NonNull List<Emojicon> emojicons) {
        this.emojicons = emojicons;
    }
}
