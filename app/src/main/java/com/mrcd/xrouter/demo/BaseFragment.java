package com.mrcd.xrouter.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getContentLayout(), container, false);
        initWidgets(savedInstanceState);
        return rootView;
    }

    protected abstract void initWidgets(Bundle savedInstanceState);

    protected abstract int getContentLayout();


}
