package com.xtree.lottery.ui.view.viewmodel;

import android.text.TextUtils;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;

/**
 * Created by KAKA on 2024/5/6.
 * Describe:
 */
public class BetInputViewModel {

    public final MutableLiveData<String> lotteryNumbs = new MutableLiveData<>("");
    public ObservableField<Boolean> showSeatView = new ObservableField<>(false);

    private LotteryBetsModel betModel;

    public void initData(LotteryBetsModel model) {
        this.betModel = model;
    }

    /**
     * 清空彩票输入框
     */
    public void clear() {
        lotteryNumbs.setValue("");
    }

//    /**
//     * 彩票输入框删除重复号码
//     */
//    public void deleteDuplicates(View view) {
//
//        if (betModel == null) {
//            return;
//        }
//
//        //是否存在此玩法规则
//        String rules = INPUT_PLAYS_MAP.get(betModel.getTitle());
//        if (rules == null) {
//            return;
//        }
//
//        String[] split = rules.split(",");
//
//        if (split.length < 3) {
//            return;
//        }
//
//        String s = lotteryNumbs.get();
//        String cleanValidNumbers = LotteryAnalyzer.getCleanValidNumbers(s,Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
//        //格式化号码集
//        lotteryNumbs.set(cleanValidNumbers);
//
//        Set<String> duplicateNumbers = LotteryAnalyzer.getDuplicateNumbers(s,Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
//        String[] invalidCharacters = LotteryAnalyzer.getInvalidCharacters(s,Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
//
//        if (duplicateNumbers.size() == 0) {
//            ToastUtils.show("当前没有重复号码", ToastUtils.ShowType.Default);
//            return;
//        }
//
//        String msg = "以下号码重复，已进行自动去重\n" +
//                TextUtils.join(",", duplicateNumbers) +
//                "\n以下号码错误，已进行自动过滤\n" +
//                TextUtils.join(",", invalidCharacters);
//        Context realContext = view.getContext();
//        while (realContext instanceof ContextWrapper && !(realContext instanceof Activity)) {
//            realContext = ((ContextWrapper) realContext).getBaseContext();
//        }
//
//        if (realContext instanceof Activity) {
//            Activity activity = (Activity) realContext;
//            // 继续你的逻辑
//            MsgDialog dialog = new MsgDialog(activity,
//                    view.getContext().getString(R.string.txt_kind_tips),
//                    msg,
//                    true,
//                    new TipDialog.ICallBack() {
//                        @Override
//                        public void onClickLeft() {
//
//                        }
//
//                        @Override
//                        public void onClickRight() {
//                            if (pop != null) {
//                                pop.dismiss();
//                            }
//                        }
//                    });
//
//            pop = new XPopup.Builder(activity)
//                    .dismissOnTouchOutside(true)
//                    .dismissOnBackPressed(true)
//                    .asCustom(dialog).show();
//        }else {
//            // 处理错误情况
//            ToastUtils.showError(msg);
//        }
//
//    }


    /**
     * 格式化输入字符串匹配规则
     */
    public String reFormatNums(String input) {
        if (TextUtils.isEmpty(input)) {
            return input;
        }

        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            // 如果是数字，且前一个字符是数字，插入空格
            if (i > 0 && Character.isDigit(currentChar)) {
                char previousChar = input.charAt(i - 1);
                if (Character.isDigit(previousChar)) {
                    formatted.append(' ');
                }
            }
            formatted.append(currentChar);
        }

        return formatted.toString();
    }
}
