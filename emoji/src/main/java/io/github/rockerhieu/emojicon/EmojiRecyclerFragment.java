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
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemChildLongClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;

import java.util.ArrayList;

import io.github.rockerhieu.emojicon.emoji.Emojicon;
import io.github.rockerhieu.emojicon.emoji.Nature;
import io.github.rockerhieu.emojicon.emoji.Objects;
import io.github.rockerhieu.emojicon.emoji.People;
import io.github.rockerhieu.emojicon.emoji.Places;
import io.github.rockerhieu.emojicon.emoji.Symbols;
import io.github.rockerhieu.emojicon.entity.EmojiEntity;
import io.github.rockerhieu.emojicon.entity.LabelEntity;

/**
 * @author Andrew
 */
public class EmojiRecyclerFragment extends Fragment {

    public static EmojiRecyclerFragment newInstance(boolean useSystemDefault) {
        EmojiRecyclerFragment fragment = new EmojiRecyclerFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(USE_SYSTEM_DEFAULT_KEY, useSystemDefault);
        fragment.setArguments(bundle);
        return fragment;
    }

    private OnEmojiconClickedListener mOnEmojiconClickedListener;
    private boolean mUseSystemDefault = false;

    private RecyclerView recyclerView;
    private static final String USE_SYSTEM_DEFAULT_KEY = "useSystemDefaults";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUseSystemDefault = getArguments().getBoolean(USE_SYSTEM_DEFAULT_KEY);
        } else {
            mUseSystemDefault = false;
        }
        mClickEmojiHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(mOnEmojiconClickedListener != null && msg.what == -1){
                    mOnEmojiconClickedListener.onEmojiconClicked(((EmojiEntity) msg.obj).getEmojicon());
                    sendMessageDelayed(obtainMessage(-1, msg.obj), 200);
                }
            }
        };
    }

    private Handler mClickEmojiHandler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = new RecyclerView(requireContext());
        return recyclerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<MultiItemEntity> list = new ArrayList<>();
        fillList(list, getString(R.string.people), People.DATA);
        fillList(list, getString(R.string.nature), Nature.DATA);
        fillList(list, getString(R.string.objects), Objects.DATA);
        fillList(list, getString(R.string.places), Places.DATA);
        fillList(list, getString(R.string.symbols), Symbols.DATA);
        int edge = ConvertUtils.dp2px(12) * 2;
        int spanCount = (ScreenUtils.getAppScreenWidth() - edge) / ConvertUtils.dp2px(42f);
        EmojiAdapter emojiAdapter = new EmojiAdapter(list, mUseSystemDefault, (adapter, position, v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
                mClickEmojiHandler.removeMessages(-1);
            }
        });
        emojiAdapter.setOnItemChildLongClickListener((adapter, view1, position) -> {
            if(view1.getId() == R.id.emojicon_icon && mOnEmojiconClickedListener != null){
                MultiItemEntity itemEntity = emojiAdapter.getItem(position);
                Message message =  mClickEmojiHandler.obtainMessage();
                if (itemEntity instanceof EmojiEntity) {
                    message.what = -1;
                    message.obj = itemEntity;
                    mClickEmojiHandler.sendMessage(message);
                }
            }
            return true;
        }
        );
        emojiAdapter.setOnItemChildClickListener((adapter, view1, position) -> {
                    if(view1.getId() == R.id.emojicon_icon && mOnEmojiconClickedListener != null){
                        MultiItemEntity itemEntity = emojiAdapter.getItem(position);
                        if (itemEntity instanceof EmojiEntity) {
                            mOnEmojiconClickedListener.onEmojiconClicked(((EmojiEntity) itemEntity).getEmojicon());
                        }
                    }
                }
        );

        emojiAdapter.setGridSpanSizeLookup((gridLayoutManager, viewType, position) -> {
            if (viewType == EmojiEntity.ITEM_TYPE)
                return 1;
            if (viewType == LabelEntity.ITEM_TYPE)
                return spanCount;
            return 1;
        });

        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view);
                MultiItemEntity itemEntity = emojiAdapter.getItem(position);
                if(itemEntity.getItemType() == LabelEntity.ITEM_TYPE){
                    outRect.top = ConvertUtils.dp2px(8);
                    outRect.bottom = ConvertUtils.dp2px(8);
                }
                super.getItemOffsets(outRect, view, parent, state);
            }
        });
        recyclerView.setAdapter(emojiAdapter);
    }

    private void fillList(ArrayList<MultiItemEntity> list, String label, Emojicon[] DATA) {
        list.add(new LabelEntity(label));
        for (Emojicon datum : DATA) {
            list.add(new EmojiEntity(datum));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnEmojiconClickedListener = getEmojiconClickedListener(this);
    }

    public @Nullable OnEmojiconClickedListener getEmojiconClickedListener(Fragment fragment) {
        Fragment parentFragment = fragment.getParentFragment();
        if (parentFragment != null) {
            if (parentFragment instanceof OnEmojiconClickedListener) {
                return (OnEmojiconClickedListener) parentFragment;
            } else {
                return getEmojiconClickedListener(parentFragment);
            }
        } else {
            if (fragment.requireActivity() instanceof OnEmojiconClickedListener) {
                return (OnEmojiconClickedListener) fragment.requireActivity();
            } else {
                return null;
            }
        }
    }

    @Override
    public void onDestroyView() {
        mOnEmojiconClickedListener = null;
        mClickEmojiHandler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }

    public static void input(EditText editText, Emojicon emojicon) {
        if (editText == null || emojicon == null) {
            return;
        }

        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        if (start < 0) {
            editText.append(emojicon.getEmoji());
        } else {
            editText.getText().replace(Math.min(start, end), Math.max(start, end), emojicon.getEmoji(), 0, emojicon.getEmoji().length());
        }
    }
}
