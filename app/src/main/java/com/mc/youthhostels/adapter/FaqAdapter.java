package com.mc.youthhostels.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mc.youthhostels.R;
import com.mc.youthhostels.customviews.ExpandableTextView;
import com.mc.youthhostels.model.FaqArticle;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> {
    private Context context;
    private List<FaqArticle> articles = new ArrayList<>();

    public FaqAdapter(Context context, List<FaqArticle> articles) {
        this.context = context;
        this.articles = articles;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_faq_articles, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FaqArticle faqArticle = articles.get(position);
        holder.faqTitle.setText(faqArticle.getTitle());
        holder.faqContent.setText(Html.fromHtml(faqArticle.getBody()));
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void setFilter(List<FaqArticle> article) {
        articles = new ArrayList<>();
        articles.addAll(article);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txt_title)
        TextView faqTitle;
        @Bind(R.id.txt_content)
        ExpandableTextView faqContent;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
