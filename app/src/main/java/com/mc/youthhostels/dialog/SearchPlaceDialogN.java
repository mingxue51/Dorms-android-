package com.mc.youthhostels.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mc.youthhostels.events.SuggestionCurrentLocationClickedEvent;

import java.util.List;

import api.API;
import de.greenrobot.event.EventBus;
import entity.Property.Search.SearchCity;
import entity.Property.Search.Suggestion;
import entity.Property.Search.Suggestions;

public class SearchPlaceDialogN extends DialogFragment implements OnClickListener {
    public final static String TAG = "SEARCH_PLACE_DIALOG";
    public static final int STRING_LENGTH = 2;
    private SearchCityAdapter mAdapter = null;
    private List<Suggestion> mSuggestions;
    private SearchPlaceDialogListener listener;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ListView listView;
        final EditText editCity;
        Dialog dialog = null;
        View dialogView = null;
        LinearLayout currentLocation = null;

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        dialogView = (View) layoutInflater.inflate(com.mc.youthhostels.R.layout.custom_dialog_search, null);
        listView = ((ListView) dialogView.findViewById(com.mc.youthhostels.R.id.listView_city));

        View header = (View) layoutInflater.inflate(com.mc.youthhostels.R.layout.partial_current_location, null);
        listView.addHeaderView(header);

        currentLocation = (LinearLayout) header.findViewById(com.mc.youthhostels.R.id.current_location_layout);
        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCurrentLocationClicked();
                EventBus.getDefault().post(new SuggestionCurrentLocationClickedEvent());
                dismiss();
            }
        });

        mAdapter = new SearchCityAdapter(getActivity(), new Suggestions(), this, listView);
        listView.setAdapter(mAdapter);

        editCity = ((EditText) dialogView.findViewById(com.mc.youthhostels.R.id.editTxt_city));


        editCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editCity.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(editCity, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });
        editCity.requestFocus();

        editCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txtCity = ((EditText) editCity.findViewById(com.mc.youthhostels.R.id.editTxt_city)).getText().toString();
                if (txtCity.length() > STRING_LENGTH) {
                    SearchCity srch = new SearchCity();
                    srch.setTerm(txtCity);
                    srch.setFilter("");
                    API api = API.getInstance(getActivity());
                    api.GetSuggestions(srch, new API.IGetRealTime() {
                        @Override
                        public void getData(List<?> list) {
                            //mSuggestions.clear();
                            mSuggestions = (List<Suggestion>) list;
                            final Suggestions suggestions = new Suggestions(mSuggestions);
                            suggestions.getSectionedList().getSectionedList();
                            if(getActivity() == null) return;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    mAdapter.updateList(suggestions);
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                        }

                        @Override
                        public void onError(String message) {
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(dialogView);
        return dialog;
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    private boolean checkInterfaceImplemented() {
        if (listener != null && listener instanceof SearchPlaceDialogListener) {
            return true;
        }
        return false;
    }

    private void onCurrentLocationClicked() {
        if (checkInterfaceImplemented()) {
            listener.onCurrentLocationClicked();
        }
    }

    public interface SearchPlaceDialogListener {
        void onCurrentLocationClicked();
    }
}
