package com.mc.youthhostels.fragments;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mc.youthhostels.managers.WrappableGridLayoutManager;
import com.mc.youthhostels.R;
import com.mc.youthhostels.adapter.ChooserAdapter;
import com.mc.youthhostels.dialog.Checkable;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public abstract class ChooserFragment extends Fragment {

    @Bind(R.id.list)
    RecyclerView list;

    private ChooserAdapter chooserAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chooserAdapter = new ChooserAdapter(getContext(), getData());
    }

    abstract List<Checkable> getData();
    abstract boolean isScrollable();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResource(), container, false);
        ButterKnife.bind(this, view);

        WrappableGridLayoutManager layoutManager = new WrappableGridLayoutManager(getContext(),
                                                                                  2,
                                                                                  isScrollable());
        list.setLayoutManager(layoutManager);
        list.setAdapter(chooserAdapter);
        chooserAdapter.notifyDataSetChanged();

        return view;
    }

    abstract @LayoutRes int getLayoutResource();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
