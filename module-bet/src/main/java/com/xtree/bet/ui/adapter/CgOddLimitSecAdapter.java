package com.xtree.bet.ui.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xtree.base.utils.NumberUtils;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.CgOddLimit;
import com.xtree.bet.ui.fragment.BtCarDialogFragment;
import com.xtree.bet.weight.CgOddLimitView;
import com.xtree.bet.weight.KeyboardView;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.utils.KLog;

public class CgOddLimitSecAdapter extends CgOddLimitView.Adapter<CgOddLimit> {
    private boolean flag;

    private KeyboardView keyboardView;

    private BtCarDialogFragment.KeyBoardListener mKeyBoardListener;
    private TextChangedListener mTextChangedListener;
    ICallBack mCallBack;

    public void setKeyboardView(KeyboardView keyboardView) {
        this.keyboardView = keyboardView;
    }

    public void setTextChangedListener(TextChangedListener textChangedListener) {
        this.mTextChangedListener = textChangedListener;
    }

    public void setKeyBoardListener(BtCarDialogFragment.KeyBoardListener keyBoardListener) {
        this.mKeyBoardListener = keyBoardListener;
    }

    public CgOddLimitSecAdapter(Context context, List<CgOddLimit> datas, ICallBack mCallBack) {
        super(context, datas);
        this.mCallBack = mCallBack;
    }

    @Override
    public int layoutId() {
        return R.layout.bt_layout_car_cg_item;
    }

    @Override
    protected void convert(View itemView, CgOddLimit cgOddLimit, int position) {
        KLog.i("cgOddLimit", (cgOddLimit == null) + "   " + position);
        if (cgOddLimit == null) {
            return;
        }
        if (getItemCount() > 1 || !TextUtils.equals("单关", cgOddLimit.getCgName())) { // 串关

            itemView.findViewById(R.id.csl_cg_dan).setVisibility(View.GONE);
            itemView.findViewById(R.id.csl_cg_cc).setVisibility(View.VISIBLE);
            EditText etAmount = itemView.findViewById(R.id.et_bt_amount_cc);
            etAmount.setHint("限制" + cgOddLimit.getCMin() + "-" + cgOddLimit.getCMax());
            etAmount.setEnabled(cgOddLimit.getCMin() > 0 && cgOddLimit.getCMax() > 0);
            if (sizeChange) {
                itemView.findViewById(R.id.csl_win_cc).setVisibility(View.GONE);
                etAmount.setText("");
            }
            ((TextView) itemView.findViewById(R.id.iv_name)).setText(cgOddLimit.getCgName());
            ((TextView) itemView.findViewById(R.id.iv_name)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallBack.onClick(cgOddLimit);
                }
            });
            ((TextView) itemView.findViewById(R.id.iv_zs_amount)).setText("x" + cgOddLimit.getBtCount());

            etAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    textChanged(etAmount, etAmount.getText(), cgOddLimit, cgOddLimit.getCMin(), cgOddLimit.getCMax(), cgOddLimit.getCOdd(),
                            R.string.bt_bt_win, R.string.bt_bt_pay, itemView.findViewById(R.id.tv_win_cc), itemView.findViewById(R.id.tv_pay_cc), itemView.findViewById(R.id.csl_win_cc));
                }
            });
            disableShowInput(etAmount);

            etAmount.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    keyboardView.setEditText(etAmount, itemView.findViewById(R.id.csl_cg_cc), cgOddLimit.getCMax());
                    itemView.findViewById(R.id.csl_win_cc).setVisibility(View.VISIBLE);
                } else {
                    itemView.findViewById(R.id.csl_win_cc).setVisibility(View.GONE);
                }
                if (hasFocus && !keyboardView.isShowing()) {
                    mKeyBoardListener.showKeyBoard(true);
                }

            });

            etAmount.setOnClickListener(view -> {
                if (!etAmount.hasFocus()) {
                    keyboardView.setEditText(etAmount, itemView.findViewById(R.id.csl_cg_cc), cgOddLimit.getCMax());
                }
                itemView.findViewById(R.id.csl_win_cc).setVisibility(View.VISIBLE);
                if (!keyboardView.isShowing()) {
                    mKeyBoardListener.showKeyBoard(true);
                }
            });
        } else {
            itemView.findViewById(R.id.csl_cg_dan).setVisibility(View.VISIBLE);
            itemView.findViewById(R.id.csl_cg_cc).setVisibility(View.GONE);
            EditText etAmount = itemView.findViewById(R.id.et_bt_amount_dan);
            etAmount.setHint("限制" + cgOddLimit.getDMin() + "-" + cgOddLimit.getDMax());
            etAmount.setEnabled(cgOddLimit.getDMin() > 0 || cgOddLimit.getDMax() > 0);

            etAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    textChanged(etAmount, etAmount.getText(), cgOddLimit, cgOddLimit.getDMin(), cgOddLimit.getDMax(), cgOddLimit.getDOdd(),
                            R.string.bt_bt_win, R.string.bt_bt_pay_1, itemView.findViewById(R.id.tv_win_dan), itemView.findViewById(R.id.tv_pay_dan), itemView.findViewById(R.id.csl_win_dan));
                }
            });
            disableShowInput(etAmount);

            etAmount.setOnFocusChangeListener((view, b) -> {
                mKeyBoardListener.showKeyBoard(keyboardView.isShowing());
                keyboardView.setEditText(etAmount, itemView.findViewById(R.id.csl_cg_dan), cgOddLimit.getDMax());
            });
            etAmount.setFocusable(true);
            etAmount.setFocusableInTouchMode(true);
            etAmount.requestFocus();
            etAmount.findFocus();
            keyboardView.setEditText(etAmount, itemView.findViewById(R.id.csl_cg_dan), cgOddLimit.getDMax());

            etAmount.setOnClickListener(view -> {
                mKeyBoardListener.showKeyBoard(true);
                keyboardView.setEditText(etAmount, itemView.findViewById(R.id.csl_cg_dan), cgOddLimit.getDMax());
            });
        }
    }

    Disposable disposable;

    /**
     * @param etAmount
     * @param charSequence
     * @param cgOddLimit
     * @param minValue       最小限额
     * @param maxValue       最大限额
     * @param odd            赔率
     * @param winResStringId 可赢金额 string id
     * @param payResStringId 投注金额 string id
     * @param tvWin          可赢金额 textview
     * @param tvPay          投注金额 textview
     * @param cslWin         单关或串关groupview
     */
    private void textChanged(EditText etAmount, CharSequence charSequence,
                             CgOddLimit cgOddLimit, double minValue, double maxValue, double odd,
                             int winResStringId, int payResStringId, TextView tvWin, TextView tvPay, View cslWin) {
        if (!etAmount.isEnabled()) {
            return;
        }

        if (charSequence != null && !TextUtils.isEmpty(charSequence.toString())) {
            double amount;
            if (charSequence.toString().startsWith(".")) {
                etAmount.setText("0");
                amount = 0;
            } else {
                amount = Double.valueOf(charSequence.toString());
            }

            if (amount < minValue) {
                tvWin.setText(mContext.getResources().getString(winResStringId, String.valueOf(odd * amount - amount)));
                tvPay.setText(mContext.getResources().getString(payResStringId, String.valueOf(amount * cgOddLimit.getBtCount())));
                if (!flag) {
                    flag = true;
                    disposable = Observable.timer(2, TimeUnit.SECONDS).subscribe(aLong -> {
                        if (TextUtils.isEmpty(etAmount.getText()) || Double.valueOf(etAmount.getText().toString()) >= minValue) {
                            flag = false;
                            return;
                        }
                        ((BaseActivity) mContext).runOnUiThread(() -> {
                            etAmount.setText(NumberUtils.format(minValue, 0));

                            tvWin.setText(mContext.getResources().getString(winResStringId, NumberUtils.format(odd * minValue - amount, 2)));
                            tvPay.setText(mContext.getResources().getString(payResStringId, NumberUtils.format(minValue * cgOddLimit.getBtCount(), 2)));
                            flag = false;
                        });
                        if (disposable.isDisposed())
                            disposable.dispose();
                    });
                }

            } else if (amount > maxValue) {
                etAmount.setText(NumberUtils.format(maxValue, 0));
                tvWin.setText(mContext.getResources().getString(winResStringId, NumberUtils.format(odd * maxValue - maxValue, 2)));
                tvPay.setText(mContext.getResources().getString(payResStringId, NumberUtils.format(maxValue * cgOddLimit.getBtCount(), 2)));
            } else {
                KLog.d("odd:" + odd + ", amount:" + amount + ", odd * amount - amount :" + (odd * amount - amount));
                tvWin.setText(mContext.getResources().getString(winResStringId, NumberUtils.format(odd * amount - amount, 2)));
                tvPay.setText(mContext.getResources().getString(payResStringId, NumberUtils.format(amount * cgOddLimit.getBtCount(), 2)));
            }
            cslWin.setVisibility(View.VISIBLE);
            cgOddLimit.setBtAmount(TextUtils.isEmpty(etAmount.getText()) ? 0 : Double.valueOf(etAmount.getText().toString()));
        } else {
            if (!sizeChange) {
                cslWin.setVisibility(View.VISIBLE);
            }
            cgOddLimit.setBtAmount(0);
            tvWin.setText(mContext.getResources().getString(winResStringId, "0"));
            tvPay.setText(mContext.getResources().getString(payResStringId, "0"));
        }

        if (mTextChangedListener != null) {
            mTextChangedListener.onTextChanged();
        }
    }

    public void disableShowInput(EditText editText) {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            editText.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {
            }
            try {
                method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {
            }
        }
    }

    public interface TextChangedListener {
        void onTextChanged();
    }

    public interface ICallBack {
        void onClick(CgOddLimit vo);
    }
}
