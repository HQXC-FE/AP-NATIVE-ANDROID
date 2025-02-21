package me.xtree.mvvmhabit.utils;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class FragmentUtils {

    public static void addFragment(FragmentManager fm, Fragment currentFragment, Fragment targeFragment, @IdRes int resId, String tag) {
        if (!targeFragment.isAdded()) {
            fm.beginTransaction().hide(currentFragment)
                    .add(resId, targeFragment, tag)
                    .addToBackStack(tag)
                    .commitAllowingStateLoss();
        } else {
            fm.beginTransaction()
                    .hide(currentFragment)
                    .show(targeFragment)
                    .addToBackStack(tag)
                    .commitAllowingStateLoss();
        }
    }
}
