package com.mc.youthhostels.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mc.youthhostels.R;
import com.mc.youthhostels.dialog.Checkable;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChooserAdapter extends RecyclerView.Adapter<ChooserAdapter.ViewHolder> {
    private Context context;
    private List<Checkable> elements = new ArrayList<>();

    public ChooserAdapter(Context context, List<Checkable> elements) {
        this.context = context;
        this.elements = elements;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_checkable, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Checkable checkable = elements.get(position);
        init(holder, checkable);

        if (checkable.isChecked()) {
            makeActive(holder);
        }

        holder.board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkable.isChecked()) {
                    init(holder, checkable);
                } else {
                    makeActive(holder);
                }
                checkable.actionOnSetChecked();
            }
        });
    }

    private void init(ViewHolder holder, Checkable checkable) {
        holder.name.setText(checkable.getName());
        holder.name.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        holder.board.setBackground(ContextCompat.getDrawable(context, R.drawable.back));

        holder.name.setGravity(Gravity.NO_GRAVITY);
        holder.description.setVisibility(View.VISIBLE);
        holder.description.setTextColor(ContextCompat.getColor(context, R.color.grey_checkable));

        String description = checkable.getDescription();
        if (description != null && description.length() > 0) {
            holder.description.setText(description);
        } else {
            holder.name.setGravity(Gravity.CENTER_VERTICAL);
            holder.description.setVisibility(View.GONE);
        }
    }

    private void makeActive(ViewHolder holder) {
        holder.board.setBackgroundColor(ContextCompat.getColor(context, R.color.checkable_active));
        int whiteColor = ContextCompat.getColor(context, R.color.white);
        holder.name.setTextColor(whiteColor);
        holder.description.setTextColor(whiteColor);
    }

    public void setElements(List<Checkable> elements) {
        this.elements = elements;
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txt_name)
        TextView name;
        @Bind(R.id.txt_description)
        TextView description;
        @Bind(R.id.lyt_checkable_element)
        LinearLayout board;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
