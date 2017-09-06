package com.mc.youthhostels.fragments.booking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mc.youthhostels.R;
import com.mc.youthhostels.activities.AppActivity;

import api.API;
import butterknife.Bind;
import butterknife.ButterKnife;
import entity.Generic.Content;
import helper.App;
import helper.H;

public class ContentFragment extends Fragment {

    private static final String QUERY_TYPE = "queryParameter";
    private String titleText;

    @Bind(R.id.txt_legacy_terms)
    TextView textContent;

    public static ContentFragment newInstance(String contentType) {
        ContentFragment fragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(QUERY_TYPE, contentType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String queryType = arguments.getString(QUERY_TYPE);
            getContent(queryType);
            if (queryType.equalsIgnoreCase(Content.PRIVACY)) {
                titleText = H.getString(R.string.drawer_privacy);
            } else {
                titleText = H.getString(R.string.terms_and_conditions);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppActivity activity = (AppActivity) getActivity();
        activity.setDefaultActionBar();
        activity.setActionBarTitle(titleText);
    }

    public void getContent(String queryType) {
        showLoading();
        API api = API.getInstance(App.getInstance());
        api.GetContent(queryType, new API.IGetRealTimeObject() {
            @Override
            public void getData(final Object object) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Content content = (Content) object;
                        textContent.setText(content.getContentHTML());
                        hideLoading();
                    }
                });
            }
            @Override
            public void onError(String message) {
                hideLoading();
            }
        });
    }

    public void hideLoading() {
        App.getInstance().hideLoading();
    }

    public void showLoading() {
        App.getInstance().showLoading();
    }
}
