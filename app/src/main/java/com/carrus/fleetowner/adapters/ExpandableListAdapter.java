package com.carrus.fleetowner.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carrus.fleetowner.R;
import com.carrus.fleetowner.ShowPODActivity;
import com.carrus.fleetowner.models.DocModel;
import com.carrus.fleetowner.models.ExpandableChildItem;
import com.carrus.fleetowner.models.Header;

import java.util.HashMap;
import java.util.List;


/**
 * Created by Saurbhv on 10/30/15 for Fleet Owner for Fleet Owner for Fleet Owner.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private final Context _context;
    private final List<Header> _listDataHeader; // header titles
    // child data in format of header title, child title
    private final HashMap<Header, List<ExpandableChildItem>> _listDataChild;
    private DocModel doc;

    public ExpandableListAdapter(Context context, List<Header> listDataHeader,
                                 HashMap<Header, List<ExpandableChildItem>> listChildData, DocModel doc) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.doc = doc;
    }


    public ExpandableListAdapter(Context context, List<Header> listDataHeader,
                                 HashMap<Header, List<ExpandableChildItem>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.doc = doc;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final ExpandableChildItem expandableChildItem = (ExpandableChildItem) getChild(groupPosition, childPosition);
        LayoutInflater infalInflater = (LayoutInflater) this._context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView typeCargoTxtView, weightTxtView;
        switch (expandableChildItem.getType()) {
            case 0:
                convertView = infalInflater.inflate(R.layout.itemview_cargodetails, null);
                typeCargoTxtView = (TextView) convertView.findViewById(R.id.typeCargoTxtView);
                weightTxtView = (TextView) convertView.findViewById(R.id.weightTxtView);
                typeCargoTxtView.setText(expandableChildItem.getName());
                weightTxtView.setText(expandableChildItem.getDetail() + _context.getResources().getString(R.string.ton));
                break;

            case 1:
                convertView = infalInflater.inflate(R.layout.itemview_desc, null);
                TextView mDescTxtView = (TextView) convertView.findViewById(R.id.descTxtView);
                if (expandableChildItem.getDetail() != null)
                    mDescTxtView.setText(expandableChildItem.getDetail());
                break;

            case 2:
                convertView = infalInflater.inflate(R.layout.itemview_documents, null);
                final LinearLayout podBtnLayout = (LinearLayout) convertView.findViewById(R.id.podBtnLayout);
                final LinearLayout invoiceBtnLayout = (LinearLayout) convertView.findViewById(R.id.invoiceBtnLayout);
                final LinearLayout consigmntBtnLayout = (LinearLayout) convertView.findViewById(R.id.consigmntBtnLayout);

                podBtnLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (doc.pod != null) {
                            Intent mIntent = new Intent(_context, ShowPODActivity.class);
                            mIntent.putExtra("url", doc.pod);
                            _context.startActivity(mIntent);
                        } else {
                            Toast.makeText(_context, _context.getResources().getString(R.string.podnotfound), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                invoiceBtnLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (doc.invoice != null) {
                            Intent mIntent = new Intent(_context, ShowPODActivity.class);
                            mIntent.putExtra("url", doc.invoice);
                            _context.startActivity(mIntent);
                        } else {
                            Toast.makeText(_context, _context.getResources().getString(R.string.invoicenotfound), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                consigmntBtnLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (doc.consigmentNote != null) {
                            Intent mIntent = new Intent(_context, ShowPODActivity.class);
                            mIntent.putExtra("url", doc.consigmentNote);
                            _context.startActivity(mIntent);
                        } else {
                            Toast.makeText(_context, _context.getResources().getString(R.string.consignmntnotfound), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;

        }
//        if (expandableChildItem.getType() == 1) {
////            TextView name = (TextView) convertView.findViewById(R.id.name);
////            TextView details = (TextView) convertView.findViewById(R.id.details);
////            name.setText(expandableChildItem.getName());
////            details.setText(expandableChildItem.getDetail());
//        } else if (expandableChildItem.getType() == 2) {
//
//        } else if (expandableChildItem.getType() == 3) {
//
//        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final Header mHeader = (Header) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_header, parent, false);
        }

        final TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.header_title);
        lblListHeader.setText(mHeader.getName());

        final ImageView btn_expand_toggle = (ImageView) convertView.findViewById(R.id.btn_expand_toggle);

//        if (mHeader.isVisible()) {
//            btn_expand_toggle.setImageResource(R.mipmap.circle_minus);
//        } else {
//            btn_expand_toggle.setImageResource(R.mipmap.circle_plus);
//        }

        int imageResourceId = isExpanded ? R.mipmap.btn_minus
                : R.mipmap.btn_plus;
        btn_expand_toggle.setImageResource(imageResourceId);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
