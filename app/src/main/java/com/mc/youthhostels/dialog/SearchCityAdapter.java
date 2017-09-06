package com.mc.youthhostels.dialog;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mc.youthhostels.R;
import com.mc.youthhostels.events.SuggestionSelectedEvent;

import de.greenrobot.event.EventBus;
import entity.Property.Search.Suggestion;
import entity.Property.Search.Suggestions;

public class SearchCityAdapter extends BaseAdapter {

    private Context mContext;
    private ListView listView;
    private SearchPlaceDialogN dialog;
    private Suggestions data;
    private static LayoutInflater inflater = null;

    public SearchCityAdapter(Context context, Suggestions list, SearchPlaceDialogN dialog, ListView listView) {
        this.mContext = context;
        this.data = list;
        this.listView = listView;
        this.dialog = dialog;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.getSuggestions().size();
    }

    @Override
    public Object getItem(int position) {
        return data.getSuggestions().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        /*if (convertView == null) {
            vi = inflater.inflate(R.layout.row_item_search, null);
        } else {
            vi = convertView;
        }*/
        //row_item_search_separator
        if (data != null) {
            Suggestion item = data.getSuggestions().get(position);
            if (item.getDecorator().isSeparator()) {
                vi = inflater.inflate(R.layout.row_item_search_separator, null);
                if(item.getDecorator().isCity()){
                    ((TextView) vi.findViewById(R.id.txtView_separator_name)).setText( R.string.city);
                }else if(item.getDecorator().isDistrict()){
                    ((TextView) vi.findViewById(R.id.txtView_separator_name)).setText( R.string.district);
                }else if(item.getDecorator().isLandmark()){
                    ((TextView) vi.findViewById(R.id.txtView_separator_name)).setText( R.string.landmark);
                }else{
                    ((TextView) vi.findViewById(R.id.txtView_separator_name)).setText( R.string.property);
                }

                return vi;
            }
            vi = inflater.inflate(R.layout.row_item_search, null);
            if(item.getDecorator().isCity()){
                ((TextView) vi.findViewById(R.id.txtView_city)).setText(item.getDecorator().getCityName());
                ((TextView) vi.findViewById(R.id.textView_country)).setText(item.getDecorator().getCountryName());
            }else if(item.getDecorator().isDistrict()){
                ((TextView) vi.findViewById(R.id.txtView_city)).setText(item.getDecorator().getDistrictName());
                ((TextView) vi.findViewById(R.id.textView_country)).setText(item.getDecorator().getCountryName());
            }else if(item.getDecorator().isLandmark()){
                ((TextView) vi.findViewById(R.id.txtView_city)).setText(item.getDecorator().getLandmarkName());
                ((TextView) vi.findViewById(R.id.textView_country)).setText(item.getDecorator().getCountryName());
            }else{
                ((TextView) vi.findViewById(R.id.txtView_city)).setText(item.getDecorator().getPropertyCityName());
                ((TextView) vi.findViewById(R.id.textView_country)).setText(item.getHb_country());
            }
        }

        vi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                listView.setItemChecked(position, true);
//                listView.setVisibility(View.GONE);
                Suggestion suggestion = data.getSuggestions().get(position);
//                mCBForString.onSelectItem((Suggestion) suggestion);
//                dialog.dismiss();
//                InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputManager.hideSoftInputFromWindow(((Activity) mContext).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                EventBus.getDefault().post(new SuggestionSelectedEvent(suggestion));
            }
        });
        return vi;
    }

    public void updateList(Suggestions pSuggestions) {
        data = pSuggestions;
    }
}
