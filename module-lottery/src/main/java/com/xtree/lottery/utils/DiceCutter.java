package com.xtree.lottery.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import com.xtree.lottery.R;

/**
 * 骰子裁剪 1-7 最后一个是问号
 */
public class DiceCutter {
    public static Bitmap cutDiceImage(Bitmap sourceBitmap, int row, int colIndex) {
        // 计算每个骰子的宽度和高度
        int totalWidth = sourceBitmap.getWidth();  // 总宽度
        int totalHeight = sourceBitmap.getHeight(); // 总高度
        float horizontalSpacing = sourceBitmap.getWidth() * 1 / 363; // 左右间距
        float verticalSpacing = horizontalSpacing; // 上下间距

        int diceCountPerRow = 7; // 每行骰子数量
        int diceCountPerCol = 2; // 每列骰子数量

        // 计算单个骰子的宽度和高度，减去每个骰子之间的间距
        float width = (totalWidth - (diceCountPerRow - 1) * horizontalSpacing) / diceCountPerRow;
        float height = (totalHeight - (diceCountPerCol - 1) * verticalSpacing) / diceCountPerCol;

        int rowIndex = row - 1;
        // 计算裁剪的起点位置
        int x = (int) (rowIndex * (width + horizontalSpacing));
        int y = (int) (colIndex * (height + verticalSpacing));

        // 裁剪单个骰子
        return Bitmap.createBitmap(sourceBitmap, x, y, (int) width, (int) height);
    }

    /**
     * 骰子开奖结果图
     *
     * @return
     */
    public static @Nullable Bitmap diceResult(Resources resources, int num) {
        if (num < 1 || num > 6) return null;

        final int[] diceResIds = {
                R.mipmap.dice_1, R.mipmap.dice_2, R.mipmap.dice_3,
                R.mipmap.dice_4, R.mipmap.dice_5, R.mipmap.dice_6
        };

        return BitmapFactory.decodeResource(resources, diceResIds[num - 1]);
    }


    /**
     * 骰子选号图  1-7 最后一个是问号
     *
     * @return
     */
    public static @Nullable Bitmap diceChooseCode(Resources resources, int row, int colIndex) {
        if (row < 1 || row > 7) return null;

        final int[][] diceResIds = {
                {
                        R.mipmap.dice_001, R.mipmap.dice_002, R.mipmap.dice_003,
                        R.mipmap.dice_004, R.mipmap.dice_005, R.mipmap.dice_006,
                        R.mipmap.dice_007
                },
                {
                        R.mipmap.dice_01, R.mipmap.dice_02, R.mipmap.dice_03,
                        R.mipmap.dice_04, R.mipmap.dice_05, R.mipmap.dice_06,
                        R.mipmap.dice_07
                }
        };

        if (colIndex < 0 || colIndex >= diceResIds.length) return null;

        return BitmapFactory.decodeResource(resources, diceResIds[colIndex][row - 1]);
    }

}
