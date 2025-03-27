package com.xtree.lottery.utils;

import android.graphics.Bitmap;

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

}
