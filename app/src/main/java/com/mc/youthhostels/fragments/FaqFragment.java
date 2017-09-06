package com.mc.youthhostels.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mc.youthhostels.R;
import com.mc.youthhostels.activities.AppActivity;
import com.mc.youthhostels.adapter.FaqAdapter;
import com.mc.youthhostels.model.FaqArticle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import helper.App;
import helper.H;

public class FaqFragment extends Fragment {

    @Bind(R.id.faq_recycle_view)
    RecyclerView listFaq;

    @Bind(R.id.search_help)
    EditText searchField;

    private FaqAdapter faqAdapter;
    List<FaqArticle> articles;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faq_articles, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        JSONObject json = (JSONObject) JSONValue.parse(readFaqArtiles());
        if (json != null) {
            if (json.containsKey("articles")) {
                articles = FaqArticle.faqList(json.get("articles"));
                faqAdapter = new FaqAdapter(getActivity(), articles);
            }
        }
        searchField.addTextChangedListener(filterTextWatcher);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listFaq.setLayoutManager(layoutManager);
        listFaq.setAdapter(faqAdapter);

        listFaq.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                App.getInstance().getCurrentActivity().hideKeyboard();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        AppActivity activity = (AppActivity) getActivity();
        activity.setDefaultActionBar();
        activity.setActionBarTitle(H.getString(R.string.faq_articles));
    }

    public String readFaqArtiles() {
        String json = null;
        try {
            InputStream inputStream = App.getInstance().getCurrentActivity()
                    .getAssets().open("faq_articles.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception e) {
            H.logE(e);
        }
        return json;
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,int after) {
        }

        @Override
        public void onTextChanged(CharSequence sequence, int start, int before,int count) {
                List<FaqArticle> searchResults = search(sequence.toString());
                faqAdapter.setFilter(searchResults);
                listFaq.scrollToPosition(0);
                App.getInstance().showToast(String.format(H.getString(R.string.search_found_message),
                                                                      searchResults.size(),
                                                                      sequence.toString()));
        }
    };

    private List<FaqArticle> search(String query) {
        List<FaqArticle> filteredModelList = new ArrayList<>();

        query = query.toLowerCase();
        for (FaqArticle article : articles) {
            String articleContent = article.getBody().toLowerCase();
            String articleName = article.getTitle().toLowerCase();
            if (articleContent.contains(query) || articleName.contains(query)) {
                filteredModelList.add(article);
            }
        }
        return filteredModelList;
    }
}
