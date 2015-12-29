package com.carrus.fleetowner;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.carrus.fleetowner.adapters.ExpandableListAdapter;
import com.carrus.fleetowner.models.ExpandableChildItem;
import com.carrus.fleetowner.models.Header;
import com.carrus.fleetowner.models.MyBookingDataModel;
import com.carrus.fleetowner.utils.SessionManager;
import com.carrus.fleetowner.utils.Utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sunny on 11/6/15.
 */
public class BookingDetailsActivity extends BaseActivity {

    private TextView headerTxtView;
    private ImageView mBackBtn;
    //    private RecyclerView recyclerview;
    private MyBookingDataModel mMyBookingDataModel;
    private TextView nameDetailTxtView, typeDetailTxtView, locationDetailsTxtView, trackDetailsIdTxtView, statusTxtView, addresPickupTxtView, datePickupTxtView, timePickupTxtView, addressDropTxtView, dateDropTxtview, timeDropTxtView, paymentModeTxtView, totalCostTxtView, namePickUpTxtView, phonePickUpTxtView, codePickUpTxtView, nameDropofTxtView, phoneDropofTxtView, codeDropofTxtView;
    private ExpandableListView mExpandableListView;
    private List<Header> listDataHeader;
    private HashMap<Header, List<ExpandableChildItem>> listDataChild;
    private ExpandableListAdapter listAdapter;
    private ImageView mProfileIV, locationIV;
    private LinearLayout topView;
    private SessionManager mSessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingdetails);
        init();
        initializeClickListner();

    }

    private void init() {
        mSessionManager = new SessionManager(this);
        headerTxtView = (TextView) findViewById(R.id.headerTxtView);
        headerTxtView.setText(getResources().getString(R.string.bookingdetails));
        mBackBtn = (ImageView) findViewById(R.id.menu_back_btn);
        mBackBtn.setVisibility(View.VISIBLE);
//        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
//        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        topView = (LinearLayout) findViewById(R.id.topView);
        mExpandableListView = (ExpandableListView) findViewById(R.id.recyclerview);
        nameDetailTxtView = (TextView) findViewById(R.id.nameDetailTxtView);
        typeDetailTxtView = (TextView) findViewById(R.id.typeDetailTxtView);
        locationDetailsTxtView = (TextView) findViewById(R.id.locationDetailsTxtView);
        trackDetailsIdTxtView = (TextView) findViewById(R.id.trackDetailsIdTxtView);
        statusTxtView = (TextView) findViewById(R.id.statusTxtView);
        addresPickupTxtView = (TextView) findViewById(R.id.addresPickupTxtView);
        datePickupTxtView = (TextView) findViewById(R.id.datePickupTxtView);
        timePickupTxtView = (TextView) findViewById(R.id.timePickupTxtView);
        addressDropTxtView = (TextView) findViewById(R.id.addressDropTxtView);
        dateDropTxtview = (TextView) findViewById(R.id.dateDropTxtview);
        timeDropTxtView = (TextView) findViewById(R.id.timeDropTxtView);
        paymentModeTxtView = (TextView) findViewById(R.id.paymentModeTxtView);
        totalCostTxtView = (TextView) findViewById(R.id.totalCostTxtView);
        mProfileIV = (ImageView) findViewById(R.id.profileIV);
        locationIV = (ImageView) findViewById(R.id.locationBtnIV);
        namePickUpTxtView = (TextView) findViewById(R.id.namePickupTxtView);
        phonePickUpTxtView = (TextView) findViewById(R.id.phonePickupTxtView);
        codePickUpTxtView = (TextView) findViewById(R.id.codePickupTxtView);
        nameDropofTxtView = (TextView) findViewById(R.id.nameDropTxtView);
        phoneDropofTxtView = (TextView) findViewById(R.id.phoneDropTxtView);
        codeDropofTxtView = (TextView) findViewById(R.id.codeDropTxtView);

        // Listview Group click listener
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
        listDataHeader.add(new Header(getResources().getString(R.string.documents), false));
        listDataHeader.add(new Header(getResources().getString(R.string.notes_cap), false));
        listDataHeader.add(new Header(getResources().getString(R.string.shippernotes), false));

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        if (getIntent().getStringExtra("id") == null) {
            mMyBookingDataModel =
                    (MyBookingDataModel) bundle.getSerializable("value");
            setValuesonViews();
        } else {
            getBookingDetails(getIntent().getStringExtra("id"));
        }
    }

    private void initializeClickListner() {
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        shareBtnIV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onShareClick();
//            }
//        });

        findViewById(R.id.callBtnIV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + mMyBookingDataModel.shipper.phoneNumber));
                startActivity(callIntent);
            }
        });

        locationIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("value", mMyBookingDataModel);
                intent.putExtras(bundle);
                intent.putExtra("edittextvalue", mMyBookingDataModel);
                setResult(RESULT_OK, intent);
                finish();

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

    private void setValuesonViews() {

        nameDetailTxtView.setText(mMyBookingDataModel.shipper.firstName + getResources().getString(R.string.space) + mMyBookingDataModel.shipper.lastName);
        typeDetailTxtView.setText(mMyBookingDataModel.truck.truckType.typeTruckName + getResources().getString(R.string.comma_spraction) + mMyBookingDataModel.truck.truckNumber);
        locationDetailsTxtView.setText(mMyBookingDataModel.pickUp.city + getResources().getString(R.string.towithspaces) + mMyBookingDataModel.dropOff.city);
        if (mMyBookingDataModel.crn != null && !mMyBookingDataModel.crn.equalsIgnoreCase(""))
            trackDetailsIdTxtView.setText(getResources().getString(R.string.crn) + mMyBookingDataModel.crn);

        statusTxtView.setText(mMyBookingDataModel.bookingStatus.replace("_", " "));

        addresPickupTxtView.setText(mMyBookingDataModel.pickUp.address + getResources().getString(R.string.comma_spraction) + mMyBookingDataModel.pickUp.city + getResources().getString(R.string.comma_spraction) + mMyBookingDataModel.pickUp.state + getResources().getString(R.string.comma_spraction) + mMyBookingDataModel.pickUp.zipCode);

        try {
            datePickupTxtView.setText(Utils.getFullDateTime(mMyBookingDataModel.pickUp.date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        timePickupTxtView.setText(mMyBookingDataModel.pickUp.time);
        addressDropTxtView.setText(mMyBookingDataModel.dropOff.address + getResources().getString(R.string.comma_spraction) + mMyBookingDataModel.dropOff.city + getResources().getString(R.string.comma_spraction) + mMyBookingDataModel.dropOff.state + getResources().getString(R.string.comma_spraction) + mMyBookingDataModel.dropOff.zipCode);

        try {
            dateDropTxtview.setText(Utils.getFullDateTime(mMyBookingDataModel.dropOff.date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        timeDropTxtView.setText(mMyBookingDataModel.dropOff.time);
        paymentModeTxtView.setText(mMyBookingDataModel.paymentMode);
        totalCostTxtView.setText(getResources().getString(R.string.rupee) + mMyBookingDataModel.acceptPrice);
        namePickUpTxtView.setText(mMyBookingDataModel.pickUp.companyName);
        phonePickUpTxtView.setText(mMyBookingDataModel.pickUp.contactNumber);
        codePickUpTxtView.setText(mMyBookingDataModel.pickUp.tin);
        nameDropofTxtView.setText(mMyBookingDataModel.dropOff.companyName);
        phoneDropofTxtView.setText(mMyBookingDataModel.dropOff.contactNumber);
        codeDropofTxtView.setText(mMyBookingDataModel.dropOff.tin);

        // Adding child data
        ArrayList<ExpandableChildItem> cargoDetails = new ArrayList<ExpandableChildItem>();
        cargoDetails.add(new ExpandableChildItem(mMyBookingDataModel.cargo.cargoType.typeCargoName, mMyBookingDataModel.cargo.weight + "", 0));

        // Adding child data
        ArrayList<ExpandableChildItem> docDetails = new ArrayList<ExpandableChildItem>();
        docDetails.add(new ExpandableChildItem(mMyBookingDataModel.cargo.cargoType.typeCargoName, mMyBookingDataModel.cargo.weight + "", 2));

        // Adding child data
        ArrayList<ExpandableChildItem> notes = new ArrayList<ExpandableChildItem>();
        notes.add(new ExpandableChildItem("", mMyBookingDataModel.jobNote, 1));

        // Adding child data
        ArrayList<ExpandableChildItem> fleetowner = new ArrayList<ExpandableChildItem>();
        fleetowner.add(new ExpandableChildItem("", mMyBookingDataModel.truckerNote, 1));

        listDataChild.put(listDataHeader.get(0), cargoDetails); // Header, Child data
        listDataChild.put(listDataHeader.get(1), docDetails);
        listDataChild.put(listDataHeader.get(2), notes);
        listDataChild.put(listDataHeader.get(3), fleetowner);

        listAdapter = new ExpandableListAdapter(BookingDetailsActivity.this, listDataHeader, listDataChild, mMyBookingDataModel.doc);
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

    private void getBookingDetails(String id) {
//        Utils.loading_box(BookingDetailsActivity.this);
//
//        RestClient.getApiService().getSingleOnGoingBookingTrack(mSessionManager.getAccessToken(), id, 100, 0, Constants.SORT, new Callback<String>() {
//            @Override
//            public void success(String s, Response response) {
//                Log.v("" + getClass().getSimpleName(), "Response> " + s);
//                try {
//                    JSONObject mObject = new JSONObject(s);
//
//                    int status = mObject.getInt("statusCode");
//
//                    if (ApiResponseFlags.OK.getOrdinal() == status) {
//                        Gson gson = new Gson();
//                        MyBookingModel mOnGoingShipper = gson.fromJson(s, MyBookingModel.class);
//
//                        if (mOnGoingShipper.mData.size() != 0) {
//                            mMyBookingDataModel = mOnGoingShipper.mData.get(0);
//                            setValuesonViews();
//                        } else {
//                            finish();
//                        }
//
//                    } else {
//                        Toast.makeText(BookingDetailsActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                Utils.loading_box_stop();
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Utils.loading_box_stop();
//                try {
//                    Log.v("error.getKind() >> " + error.getKind(), " MSg >> " + error.getResponse().getStatus());
//
//                    if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
//                        Toast.makeText(BookingDetailsActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
//                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Unauthorized.getOrdinal()) {
//                        Utils.shopAlterDialog(BookingDetailsActivity.this, Utils.getErrorMsg(error), true);
//                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_Found.getOrdinal()) {
//                        Toast.makeText(BookingDetailsActivity.this, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception ex) {
//                    Toast.makeText(BookingDetailsActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
//                }
//                finish();
//            }
//        });
    }

    private void performCancelAction() {
//        Utils.loading_box(BookingDetailsActivity.this);
//        RestClient.getApiService().cancelBooking(mSessionManager.getAccessToken(), "CANCELED", mMyBookingDataModel.id, new Callback<String>() {
//            @Override
//            public void success(String s, Response response) {
//                Log.v("" + getClass().getSimpleName(), "Response> " + s);
//                try {
//                    JSONObject mObject = new JSONObject(s);
//
//                    int status = mObject.getInt("statusCode");
//
//                    if (ApiResponseFlags.OK.getOrdinal() == status) {
//                        Constants.isPastUpdate = true;
//                        Constants.isUpComingUpdate = true;
//
//                        Toast.makeText(BookingDetailsActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();
//                        finish();
//
//                    } else {
//                        Toast.makeText(BookingDetailsActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                Utils.loading_box_stop();
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Utils.loading_box_stop();
//                try {
//                    Log.v("error.getKind() >> " + error.getKind(), " MSg >> " + error.getResponse().getStatus());
//
//                    if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
//                        Toast.makeText(BookingDetailsActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
//                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Unauthorized.getOrdinal()) {
//                        Utils.shopAlterDialog(BookingDetailsActivity.this, Utils.getErrorMsg(error), true);
//                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_Found.getOrdinal()) {
//                        Toast.makeText(BookingDetailsActivity.this, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception ex) {
//                    Toast.makeText(BookingDetailsActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    private void onShareClick() {
        Resources resources = getResources();

        Intent emailIntent = new Intent();
        emailIntent.setAction(Intent.ACTION_SEND);
        // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
        emailIntent.putExtra(Intent.EXTRA_TEXT, mMyBookingDataModel.crn);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.crnsubject));
        emailIntent.setType("message/rfc822");

        PackageManager pm = getPackageManager();
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");

        Intent openInChooser = Intent.createChooser(emailIntent, resources.getString(R.string.share_chooser_text));

        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
        List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
        for (int i = 0; i < resInfo.size(); i++) {
            // Extract the label, append it, and repackage it in a LabeledIntent
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;
//            if(packageName.contains("android.email")) {
//                emailIntent.setPackage(packageName);
//            }
//            else
            if (packageName.contains("mms") || packageName.contains("android.email")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                intent.setAction(Intent.ACTION_SEND);
//                intent.setType("text/plain");
                intent.setType("message/rfc822");


                if (packageName.contains("android.email")) {
                    intent.putExtra(Intent.EXTRA_TEXT, mMyBookingDataModel.crn);
                    intent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.crnsubject));
                }

                if (packageName.contains("mms")) {
                    intent.putExtra(Intent.EXTRA_TEXT, mMyBookingDataModel.crn);
                }
                intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
            }
        }

        // convert intentList to array
        LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);

        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
        startActivity(openInChooser);
    }
}
