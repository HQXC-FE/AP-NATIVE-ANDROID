package com.xtree.recharge.ui.widget;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A custom ItemDecoration that applies spacing between RecyclerView items.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    // Constants for default values
    private static final int DEFAULT_SPACES = 0;
    private static final int DEFAULT_ORIENTATION = RecyclerView.VERTICAL;
    private final int spaces;
    private final int orientation;

    /**
     * Constructor with default orientation.
     *
     * @param spaces Spacing size in pixels. Default is 0 if invalid value is provided.
     */
    public SpacesItemDecoration(int spaces) {
        this(DEFAULT_ORIENTATION, spaces);
    }

    /**
     * Constructor with specified orientation and spacing.
     *
     * @param orientation Layout orientation: {@link RecyclerView#VERTICAL} or {@link RecyclerView#HORIZONTAL}.
     * @param spaces      Spacing size in pixels. Default is 0 if invalid value is provided.
     */
    public SpacesItemDecoration(int spaces,int orientation) {
        this.orientation = (orientation == RecyclerView.VERTICAL || orientation == RecyclerView.HORIZONTAL)
                ? orientation
                : DEFAULT_ORIENTATION;
        this.spaces = Math.max(spaces, 0); // Ensure non-negative spacing
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getAdapter() == null) {
            return; // Skip decoration if adapter is not set
        }

        // Apply spacing based on the orientation
        if (orientation == RecyclerView.VERTICAL) {
            outRect.set(0, 0, 0, spaces);
        } else {
            outRect.set(0, 0, spaces, 0);
        }
    }
}