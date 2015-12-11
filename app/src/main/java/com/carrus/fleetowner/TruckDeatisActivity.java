package com.carrus.fleetowner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.carrus.fleetowner.adapters.ExpandableListAdapter;
import com.carrus.fleetowner.models.ExpandableChildItem;
import com.carrus.fleetowner.models.Header;
import com.carrus.fleetowner.models.TruckQuotesDetails;
import com.carrus.fleetowner.models.TrucksDetailsModel;
import com.carrus.fleetowner.utils.Utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.carrus.fleetowner.utils.Constants.BIDVALUE;
import static com.carrus.fleetowner.utils.Constants.NOTES;
import static com.carrus.fleetowner.utils.Constants.TRUCKTYPE;
import static com.carrus.fleetowner.utils.Constants.TYPE;
import static com.carrus.fleetowner.utils.Constants.VALUE;

/**
 * Created by Sunny on 12/10/15.
 */
public class TruckDeatisActivity extends BaseActivity {

    private TextView headerTxtView, mTruckNameTV, timePickupTxtView, addresPickupTxtView, datePickupTxtView, timeDropTxtView, addressDropTxtView, dateDropTxtview;
    private ImageView mBackBtn;
    private TrucksDetailsModel mTrucksDetailsModel;
    private TruckQuotesDetails mTruckQuotesDetails;
    private ExpandableListView mExpandableListView;
    private List<Header> listDataHeader;
    private HashMap<Header, List<ExpandableChildItem>> listDataChild;
    private Button mQuoteBtn, mIgnoreBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truckdetails);
        init();
        initializeClickListner();
    }

    private void init() {
        headerTxtView = (TextView) findViewById(R.id.headerTxtView);
        mBackBtn = (ImageView) findViewById(R.id.menu_back_btn);
        mBackBtn.setVisibility(View.VISIBLE);
        mExpandableListView = (ExpandableListView) findViewById(R.id.recyclerview);
        mTruckNameTV = (TextView) findViewById(R.id.trucknameTV);
        timePickupTxtView = (TextView) findViewById(R.id.timePickupTxtView);
        addresPickupTxtView = (TextView) findViewById(R.id.addresPickupTxtView);
        datePickupTxtView = (TextView) findViewById(R.id.datePickupTxtView);
        timeDropTxtView = (TextView) findViewById(R.id.timeDropTxtView);
        addressDropTxtView = (TextView) findViewById(R.id.addressDropTxtView);
        dateDropTxtview = (TextView) findViewById(R.id.dateDropTxtview);
        mQuoteBtn = (Button) findViewById(R.id.quoteBtn);
        mIgnoreBtn = (Button) findViewById(R.id.ignoreBtn);

        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);
                return false;
            }
        });

        // Listview Group expanded listener
        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {


            }
        });

        // Listview Group collasped listener
        mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {


            }
        });

        // Listview on child click listener
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                return false;
            }
        });

        //prepareListData();
        listDataHeader = new ArrayList<Header>();
        listDataChild = new HashMap<Header, List<ExpandableChildItem>>();

        // Adding child data
        listDataHeader.add(new Header(getResources().getString(R.string.cargodetails), false));
        listDataHeader.add(new Header(getResources().getString(R.string.shippernotes), false));
        listDataHeader.add(new Header(getResources().getString(R.string.fleetownernotes), false));

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (intent.getStringExtra(TYPE).equalsIgnoreCase("new request")) {
            headerTxtView.setText(getResources().getString(R.string.newRequest));
            mQuoteBtn.setText(getResources().getString(R.string.quote));
            mIgnoreBtn.setVisibility(View.VISIBLE);
            mTrucksDetailsModel =
                    (TrucksDetailsModel) bundle.getSerializable(VALUE);
            setValuesonViews();
        } else if (intent.getStringExtra(TYPE).equalsIgnoreCase("quote")) {
            headerTxtView.setText(getResources().getString(R.string.pendingquotes_head));
            mQuoteBtn.setText(getResources().getString(R.string.modify));
            mIgnoreBtn.setVisibility(View.GONE);
            mTruckQuotesDetails =
                    (TruckQuotesDetails) bundle.getSerializable(VALUE);
            setQuoteValuesonViews();
        }

    }

    private void initializeClickListner() {
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mQuoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQuoteBtn.getText().toString().equalsIgnoreCase(getResources().getString(R.string.quote)))
                    startActivity(new Intent(TruckDeatisActivity.this, QuoteDialogActivity.class));
                else if (mQuoteBtn.getText().toString().equalsIgnoreCase(getResources().getString(R.string.modify))) {
                    Intent intent = new Intent(TruckDeatisActivity.this, QuoteDialogActivity.class);
                    intent.putExtra(TYPE, true);
                    intent.putExtra(BIDVALUE, mTruckQuotesDetails.getOfferCost());
                    intent.putExtra(TRUCKTYPE, mTruckQuotesDetails.getTruck().truckType.typeTruckName);
                    intent.putExtra(NOTES, mTruckQuotesDetails.getQuoteNote());
                    startActivity(intent);
                }
            }
        });

        mIgnoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setQuoteValuesonViews() {

        mTruckNameTV.setText(mTruckQuotesDetails.getTruck().truckType.typeTruckName);
        addresPickupTxtView.setText(mTruckQuotesDetails.getPickUp().getLocation() + ", " + mTruckQuotesDetails.getPickUp().getState());

        try {
            datePickupTxtView.setText(Utils.getFullDateTime(mTruckQuotesDetails.getPickUp().getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        timePickupTxtView.setText(mTruckQuotesDetails.getPickUp().getTime());
        addressDropTxtView.setText(mTruckQuotesDetails.getDropOff().getLocation() + ", " + mTruckQuotesDetails.getDropOff().getState());

        try {
            dateDropTxtview.setText(Utils.getFullDateTime(mTruckQuotesDetails.getDropOff().getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        timeDropTxtView.setText(mTruckQuotesDetails.getDropOff().getTime());

        // Adding child data
        ArrayList<ExpandableChildItem> cargoDetails = new ArrayList<ExpandableChildItem>();
        cargoDetails.add(new ExpandableChildItem(mTruckQuotesDetails.getCargo().cargoType.typeCargoName, mTruckQuotesDetails.getCargo().weight + "", 0));

        // Adding child data
        ArrayList<ExpandableChildItem> notes = new ArrayList<ExpandableChildItem>();
        notes.add(new ExpandableChildItem("", mTruckQuotesDetails.getNote(), 1));

        // Adding child data
        ArrayList<ExpandableChildItem> fleetowner = new ArrayList<ExpandableChildItem>();
        fleetowner.add(new ExpandableChildItem("", mTruckQuotesDetails.getShipper().firstName, 1));

        listDataChild.put(listDataHeader.get(0), cargoDetails); // Header, Child data
        listDataChild.put(listDataHeader.get(1), notes);
        listDataChild.put(listDataHeader.get(2), fleetowner);

        ExpandableListAdapter listAdapter = new ExpandableListAdapter(TruckDeatisActivity.this, listDataHeader, listDataChild);
        mExpandableListView.setAdapter(listAdapter);
        setListViewHeight(mExpandableListView);
//        chnageHieghtListView();
        final ScrollView scrollview = (ScrollView) findViewById(R.id.mainscrollview);

        scrollview.post(new Runnable() {
            public void run() {
                scrollview.scrollTo(0, 0);
            }
        });

    }

    private void setValuesonViews() {

        mTruckNameTV.setText(mTrucksDetailsModel.getTruck().truckType.typeTruckName);
        addresPickupTxtView.setText(mTrucksDetailsModel.pickUp.getLocation() + ", " + mTrucksDetailsModel.pickUp.getState());

        try {
            datePickupTxtView.setText(Utils.getFullDateTime(mTrucksDetailsModel.pickUp.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        timePickupTxtView.setText(mTrucksDetailsModel.pickUp.getTime());
        addressDropTxtView.setText(mTrucksDetailsModel.dropOff.getLocation() + ", " + mTrucksDetailsModel.dropOff.getState());

        try {
            dateDropTxtview.setText(Utils.getFullDateTime(mTrucksDetailsModel.dropOff.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        timeDropTxtView.setText(mTrucksDetailsModel.dropOff.getTime());

        // Adding child data
        ArrayList<ExpandableChildItem> cargoDetails = new ArrayList<ExpandableChildItem>();
        cargoDetails.add(new ExpandableChildItem(mTrucksDetailsModel.getCargo().cargoType.typeCargoName, mTrucksDetailsModel.getCargo().weight + "", 0));

        // Adding child data
        ArrayList<ExpandableChildItem> notes = new ArrayList<ExpandableChildItem>();
        notes.add(new ExpandableChildItem("", mTrucksDetailsModel.getNote(), 1));

        // Adding child data
        ArrayList<ExpandableChildItem> fleetowner = new ArrayList<ExpandableChildItem>();
        fleetowner.add(new ExpandableChildItem("", mTrucksDetailsModel.getShipper().firstName, 1));

        listDataChild.put(listDataHeader.get(0), cargoDetails); // Header, Child data
        listDataChild.put(listDataHeader.get(1), notes);
        listDataChild.put(listDataHeader.get(2), fleetowner);

        ExpandableListAdapter listAdapter = new ExpandableListAdapter(TruckDeatisActivity.this, listDataHeader, listDataChild);
        mExpandableListView.setAdapter(listAdapter);
        setListViewHeight(mExpandableListView);
//        chnageHieghtListView();
        final ScrollView scrollview = (ScrollView) findViewById(R.id.mainscrollview);

        scrollview.post(new Runnable() {
            public void run() {
                scrollview.scrollTo(0, 0);
            }
        });

    }

    private void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += groupItem.getMeasuredHeight();
            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }
}
