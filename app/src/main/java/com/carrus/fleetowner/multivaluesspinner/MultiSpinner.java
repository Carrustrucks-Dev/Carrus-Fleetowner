package com.carrus.fleetowner.multivaluesspinner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


import com.carrus.fleetowner.R;
import com.carrus.fleetowner.models.CargoDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class MultiSpinner extends Spinner implements OnMultiChoiceClickListener, OnCancelListener {

    private List<String> items;
    private boolean[] selected;
    private String defaultText = "Select Items";
    private MultiSpinnerListener listener;

    public MultiSpinner(Context context) {
        super(context);
    }

    public MultiSpinner(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public MultiSpinner(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        selected[which] = isChecked;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // refresh text on spinner
        StringBuilder spinnerBuffer = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            if (selected[i]) {
                spinnerBuffer.append(items.get(i));
                spinnerBuffer.append(", ");
            }
        }

        String spinnerText = "";
        spinnerText = spinnerBuffer.toString();
        if (spinnerText.length() > 2) {
            spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
        } else {
            spinnerText = defaultText;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                R.layout.textview_for_spinner,
                new String[]{spinnerText});
        setAdapter(adapter);
        if (selected.length > 0) {
            listener.onItemsSelected(selected);
        }

    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(defaultText);
        builder.setMultiChoiceItems(
                items.toArray(new CharSequence[items.size()]), selected, this);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.setOnCancelListener(this);
        builder.show();
        return true;
    }

    /**
     * Sets items to this spinner.
     *
     * @param items    A TreeMap where the keys are the values to display in the spinner
     *                 and the value the initial selected state of the key.
     * @param listener A MultiSpinnerListener.
     */
    public void setItems(TreeMap<String, Boolean> items,String text,
                         MultiSpinnerListener listener) {
        this.items = new ArrayList<>(items.keySet());
        this.listener = listener;
        this.defaultText=text;
        List<Boolean> values = new ArrayList<>(items.values());
        selected = new boolean[values.size()];
        for (int i = 0; i < items.size(); i++) {
            selected[i] = values.get(i);
        }

        // all text on the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                R.layout.textview_for_spinner, new String[]{defaultText});
        setAdapter(adapter);

        // Set Spinner Text
        onCancel(null);
    }

    public void setItems(List<CargoDetails> items,String text,
                         MultiSpinnerListener listener) {
        this.listener = listener;
        this.defaultText=text;
        this.items = new ArrayList<>();
        List<Boolean> values = new ArrayList<>();
        selected = new boolean[items.size()];
        for (CargoDetails mCargoDetails: items){
            this.items.add(mCargoDetails.typeCargoName);
            values.add(false);
        }

        for (int i = 0; i < this.items.size(); i++) {
            selected[i] = values.get(i);
        }

        // all text on the spinner
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
//                R.layout.textview_for_spinner, new String[]{defaultText});
        CustomStringArrayAdapter adapter=new CustomStringArrayAdapter(getContext(), R.layout.spinner_layout, R.id.spinner_textview, this.items);
        setAdapter(adapter);

        // Set Spinner Text
        onCancel(null);
    }

    public interface MultiSpinnerListener {
        void onItemsSelected(boolean[] selected);
    }

    public class CustomStringArrayAdapter extends ArrayAdapter<String>
    {

        public CustomStringArrayAdapter(Context context, int layoutResourceId, int textViewResourceId, List<String> objects)
        {
            super(context, layoutResourceId, textViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent)
        {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
            View row = inflater.inflate(R.layout.spinner_layout, parent, false);
            TextView label = (TextView) row.findViewById(R.id.spinner_textview);
            label.setText(getItem(position));
            LinearLayout layout = (LinearLayout) row.findViewById(R.id.spinner_linear_layout);

            return row;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
            View row = inflater.inflate(R.layout.spinner_layout, parent, false);
            TextView label = (TextView) row.findViewById(R.id.spinner_textview);
            label.setText(getItem(position));

            return row;
        }

    }
}