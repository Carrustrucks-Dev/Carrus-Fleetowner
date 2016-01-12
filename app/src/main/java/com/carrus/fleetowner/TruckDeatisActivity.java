package com.carrus.fleetowner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.carrus.fleetowner.adapters.ExpandableListAdapter;
import com.carrus.fleetowner.models.ExpandableChildItem;
import com.carrus.fleetowner.models.Header;
import com.carrus.fleetowner.models.TruckAssignDetails;
import com.carrus.fleetowner.models.TruckQuotesDetails;
import com.carrus.fleetowner.models.TrucksDetailsModel;
import com.carrus.fleetowner.retrofit.RestClient;
import com.carrus.fleetowner.utils.ApiResponseFlags;
import com.carrus.fleetowner.utils.Constants;
import com.carrus.fleetowner.utils.SessionManager;
import com.carrus.fleetowner.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.carrus.fleetowner.utils.Constants.BIDVALUE;
import static com.carrus.fleetowner.utils.Constants.CHILDACTIVITY;
import static com.carrus.fleetowner.utils.Constants.ID;
import static com.carrus.fleetowner.utils.Constants.NOTES;
import static com.carrus.fleetowner.utils.Constants.QUOTEID;
import static com.carrus.fleetowner.utils.Constants.TRUCKTYPE;
import static com.carrus.fleetowner.utils.Constants.TYPE;
import static com.carrus.fleetowner.utils.Constants.VALUE;


/**
 * Created by Sunny on 12/10/15 for Fleet Owner for Fleet Owner for Fleet Owner.
 */
public class TruckDeatisActivity extends BaseActivity {

    private TextView mTruckNameTV;
    private TextView timePickupTxtView;
    private TextView addresPickupTxtView;
    private TextView datePickupTxtView;
    private TextView timeDropTxtView;
    private TextView addressDropTxtView;
    private TextView dateDropTxtview;
    private ImageView mBackBtn;
    private TrucksDetailsModel mTrucksDetailsModel;
    private TruckQuotesDetails mTruckQuotesDetails;
    private TruckAssignDetails mTruckAssignDetails;
    private ExpandableListView mExpandableListView;
    private List<Header> listDataHeader;
    private HashMap<Header, List<ExpandableChildItem>> listDataChild;
    private Button mQuoteBtn, mIgnoreBtn;
    private SessionManager mSessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truckdetails);
        init();
        initializeClickListner();
    }

    private void init() {
        mSessionManager = new SessionManager(this);
        TextView headerTxtView = (TextView) findViewById(R.id.headerTxtView);
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
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        listDataHeader.add(new Header(getResources().getString(R.string.cargodetails)));
        listDataHeader.add(new Header(getResources().getString(R.string.notes_cap)));
        listDataHeader.add(new Header(getResources().getString(R.string.shippernotes)));

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
        } else if (intent.getStringExtra(TYPE).equalsIgnoreCase("assigment")) {
            headerTxtView.setText(getResources().getString(R.string.pendingassign_head));
            mQuoteBtn.setText(getResources().getString(R.string.assign_driver));
            mIgnoreBtn.setVisibility(View.GONE);
            mTruckAssignDetails =
                    (TruckAssignDetails) bundle.getSerializable(VALUE);
            setAssignValuesonViews();
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
                Intent intent = new Intent(TruckDeatisActivity.this, QuoteDialogActivity.class);
                if (mQuoteBtn.getText().toString().equalsIgnoreCase(getResources().getString(R.string.quote))) {
                    intent.putExtra(TYPE, false);
                    intent.putExtra(ID, mTrucksDetailsModel.getId());
                    startActivityForResult(intent, CHILDACTIVITY);
                } else if (mQuoteBtn.getText().toString().equalsIgnoreCase(getResources().getString(R.string.modify))) {
                    intent.putExtra(TYPE, true);
                    intent.putExtra(BIDVALUE, mTruckQuotesDetails.getOfferCost());
                    intent.putExtra(TRUCKTYPE, mTruckQuotesDetails.getTruck().truckType.typeTruckName);
                    intent.putExtra(NOTES, mTruckQuotesDetails.getQuoteNote());
                    intent.putExtra(ID, mTruckQuotesDetails.getId());
                    intent.putExtra(QUOTEID, mTruckQuotesDetails.getQuoteId());
                    startActivityForResult(intent, CHILDACTIVITY);
                } else if (mQuoteBtn.getText().toString().equalsIgnoreCase(getResources().getString(R.string.assign_driver))) {
                    Intent mIntent = new Intent(TruckDeatisActivity.this, DriverActivity.class);
                    mIntent.putExtra(ID, mTruckAssignDetails.getId());
                    startActivityForResult(mIntent, CHILDACTIVITY);
                }

            }
        });

        mIgnoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ignoreRequest();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHILDACTIVITY && resultCode == RESULT_OK && data != null) {
//            Toast.makeText(TruckDeatisActivity.this, "Successfully bid", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setQuoteValuesonViews() {

        mTruckNameTV.setText(mTruckQuotesDetails.getTruck().truckType.typeTruckName);
        addresPickupTxtView.setText(mTruckQuotesDetails.getPickUp().getLocation() + getResources().getString(R.string.comma_spraction) + mTruckQuotesDetails.getPickUp().getState());

        try {
            datePickupTxtView.setText(Utils.getFullDateTime(mTruckQuotesDetails.getPickUp().getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        timePickupTxtView.setText(mTruckQuotesDetails.getPickUp().getTime());
        addressDropTxtView.setText(mTruckQuotesDetails.getDropOff().getLocation() + getResources().getString(R.string.comma_spraction) + mTruckQuotesDetails.getDropOff().getState());

        try {
            dateDropTxtview.setText(Utils.getFullDateTime(mTruckQuotesDetails.getDropOff().getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        timeDropTxtView.setText(mTruckQuotesDetails.getDropOff().getTime());

        // Adding child data
        ArrayList<ExpandableChildItem> cargoDetails = new ArrayList<>();
        cargoDetails.add(new ExpandableChildItem(mTruckQuotesDetails.getCargo().cargoType.typeCargoName, mTruckQuotesDetails.getCargo().weight + "", 0));

        // Adding child data
        ArrayList<ExpandableChildItem> notes = new ArrayList<>();
        notes.add(new ExpandableChildItem("", mTruckQuotesDetails.getNote(), 1));

        // Adding child data
        ArrayList<ExpandableChildItem> fleetowner = new ArrayList<>();
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
        addresPickupTxtView.setText(mTrucksDetailsModel.pickUp.getLocation() + getResources().getString(R.string.comma_spraction) + mTrucksDetailsModel.pickUp.getState());

        try {
            datePickupTxtView.setText(Utils.getFullDateTime(mTrucksDetailsModel.pickUp.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        timePickupTxtView.setText(mTrucksDetailsModel.pickUp.getTime());
        addressDropTxtView.setText(mTrucksDetailsModel.dropOff.getLocation() + getResources().getString(R.string.comma_spraction) + mTrucksDetailsModel.dropOff.getState());

        try {
            dateDropTxtview.setText(Utils.getFullDateTime(mTrucksDetailsModel.dropOff.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        timeDropTxtView.setText(mTrucksDetailsModel.dropOff.getTime());

        // Adding child data
        ArrayList<ExpandableChildItem> cargoDetails = new ArrayList<>();
        cargoDetails.add(new ExpandableChildItem(mTrucksDetailsModel.getCargo().cargoType.typeCargoName, mTrucksDetailsModel.getCargo().weight + "", 0));

        // Adding child data
        ArrayList<ExpandableChildItem> notes = new ArrayList<>();
        notes.add(new ExpandableChildItem("", mTrucksDetailsModel.getNote(), 1));

        // Adding child data
        ArrayList<ExpandableChildItem> fleetowner = new ArrayList<>();
//        fleetowner.add(new ExpandableChildItem("", mTrucksDetailsModel.getShipper().firstName, 1));

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

    private void setAssignValuesonViews() {

        mTruckNameTV.setText(mTruckAssignDetails.getTruck().truckType.typeTruckName);
        addresPickupTxtView.setText(mTruckAssignDetails.getPickUp().city + getResources().getString(R.string.comma_spraction) + mTruckAssignDetails.getPickUp().state);

        try {
            datePickupTxtView.setText(Utils.getFullDateTime(mTruckAssignDetails.getPickUp().date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        timePickupTxtView.setText(mTruckAssignDetails.getPickUp().time);
        addressDropTxtView.setText(mTruckAssignDetails.getDropOff().city + getResources().getString(R.string.comma_spraction) + mTruckAssignDetails.getDropOff().state);

        try {
            dateDropTxtview.setText(Utils.getFullDateTime(mTruckAssignDetails.getDropOff().date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        timeDropTxtView.setText(mTruckAssignDetails.getDropOff().time);

        // Adding child data
        ArrayList<ExpandableChildItem> cargoDetails = new ArrayList<>();
        cargoDetails.add(new ExpandableChildItem(mTruckAssignDetails.getCargo().cargoType.typeCargoName, mTruckAssignDetails.getCargo().weight + "", 0));

        // Adding child data
        ArrayList<ExpandableChildItem> notes = new ArrayList<>();
        if (mTruckAssignDetails.getTruckerNote() != null)
            notes.add(new ExpandableChildItem("", mTruckAssignDetails.getTruckerNote().toString(), 1));

        // Adding child data
        ArrayList<ExpandableChildItem> fleetowner = new ArrayList<>();
        fleetowner.add(new ExpandableChildItem("", mTruckAssignDetails.getShipper().firstName, 1));

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

    private void ignoreRequest() {
        Utils.loading_box(TruckDeatisActivity.this);
        RestClient.getApiService().ignoreBid(mSessionManager.getAccessToken(), mTrucksDetailsModel.getId(), new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                if(BuildConfig.DEBUG)
                Log.v("" + getClass().getSimpleName(), "Response> " + s);
                try {
                    JSONObject mObject = new JSONObject(s);

                    int status = mObject.getInt("statusCode");

                    if (ApiResponseFlags.OK.getOrdinal() == status) {

                        Constants.isTruckPendingUpdate = true;
                        Toast.makeText(TruckDeatisActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        Toast.makeText(TruckDeatisActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Utils.loading_box_stop();
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.loading_box_stop();

                try {
                    if(BuildConfig.DEBUG)
                    Log.v("error.getKind() >> " + error.getKind(), " MSg >> " + error.getResponse().getStatus());
                    if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                        Utils.shopAlterDialog(TruckDeatisActivity.this, getResources().getString(R.string.nointernetconnection), false);
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Unauthorized.getOrdinal()) {
                        Utils.shopAlterDialog(TruckDeatisActivity.this, Utils.getErrorMsg(error), true);
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_Found.getOrdinal()) {
                        Toast.makeText(TruckDeatisActivity.this, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Utils.shopAlterDialog(TruckDeatisActivity.this, getResources().getString(R.string.nointernetconnection), false);
                }

            }
        });
    }
}
