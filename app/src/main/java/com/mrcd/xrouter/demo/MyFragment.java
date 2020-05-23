package com.mrcd.xrouter.demo;

import android.os.Bundle;

import com.mrcd.xrouter.XRouter;

public class MyFragment extends BaseFragment {
    @Override
    protected void initWidgets(Bundle savedInstanceState) {
        XRouter.getInstance().mainActivity().launch(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_my;
    }
}
