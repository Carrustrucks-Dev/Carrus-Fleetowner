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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.carrus.fleetowner.adapters.ExpandableListAdapter;
import com.carrus.fleetowner.models.ExpandableChildItem;
import com.carrus.fleetowner.models.Header;
import com.carrus.fleetowner.models.MyBookingDataModel;
import com.carrus.fleetowner.utils.Utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sunny on 11/6/15 for Fleet Owner for Fleet Owner for Fleet Owner.
 */
public class BookingDetailsActivity extends BaseActivity {

    private ImageView mBackBtn;
    //    private RecyclerView recyclerview;
    private MyBookingDataModel mMyBookingDataModel;
    private TextView nameDetailTxtView, typeDetailTxtView, locationDetailsTxtView, trackDetailsIdTxtView, statusTxtView, addresPickupTxtView, datePickupTxtView, timePickupTxtView, addressDropTxtView, dateDropTxtview, timeDropTxtView, paymentModeTxtView, totalCostTxtView, namePickUpTxtView, phonePickUpTxtView, codePickUpTxtView, nameDropofTxtView, phoneDropofTxtView, codeDropofTxtView;
    private ExpandableListView mExpandableListView;
    private List<Header> listDataHeader;
    private HashMap<Header, List<ExpandableChildItem>> listDataChild;
    private ImageView locationIV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingdetails);
        init();
        initializeClickListner();

    }

    private void init() {
        TextView headerTxtView = (TextView) findViewById(R.id.headerTxtView);
        headerTxtView.setText(getResources().getString(R.string.bookingdetails));
        mBackBtn = (ImageView) findViewById(R.id.menu_back_btn);
        mBackBtn.setVisibility(View.VISIBLE);
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
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        listDataHeader.add(new Header(getResources().getString(R.string.cargodetails)));
        listDataHeader.add(new Header(getResources().getString(R.string.documents)));
        listDataHeader.add(new Header(getResources().getString(R.string.notes_cap)));
        listDataHeader.add(new Header(getResources().getString(R.string.shippernotes)));

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        if (getIntent().getStringExtra("id") == null) {
            mMyBookingDataModel =
                    (MyBookingDataModel) bundle.getSerializable("value");
            setValuesonViews();
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
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
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
        totalCostTxtView.setText(getResources().getString(R.string.rupee) + NumberFormat.getInstance().format(Long.valueOf(mMyBookingDataModel.acceptPrice)));
        namePickUpTxtView.setText(mMyBookingDataModel.pickUp.companyName);
        phonePickUpTxtView.setText(mMyBookingDataModel.pickUp.contactNumber);
        codePickUpTxtView.setText(mMyBookingDataModel.pickUp.tin);
        nameDropofTxtView.setText(mMyBookingDataModel.dropOff.companyName);
        phoneDropofTxtView.setText(mMyBookingDataModel.dropOff.contactNumber);
        codeDropofTxtView.setText(mMyBookingDataModel.dropOff.tin);

        // Adding child data
        ArrayList<ExpandableChildItem> cargoDetails = new ArrayList<>();
        cargoDetails.add(new ExpandableChildItem(mMyBookingDataModel.cargo.cargoType.typeCargoName, mMyBookingDataModel.cargo.weight + "", 0));

        // Adding child data
        ArrayList<ExpandableChildItem> docDetails = new ArrayList<>();
        docDetails.add(new ExpandableChildItem(mMyBookingDataModel.cargo.cargoType.typeCargoName, mMyBookingDataModel.cargo.weight + "", 2));

        // Adding child data
        ArrayList<ExpandableChildItem> notes = new ArrayList<>();
        notes.add(new ExpandableChildItem("", mMyBookingDataModel.jobNote, 1));

        // Adding child data
        ArrayList<ExpandableChildItem> fleetowner = new ArrayList<>();
        fleetowner.add(new ExpandableChildItem("", mMyBookingDataModel.truckerNote, 1));

        listDataChild.put(listDataHeader.get(0), cargoDetails); // Header, Child data
        listDataChild.put(listDataHeader.get(1), docDetails);
        listDataChild.put(listDataHeader.get(2), notes);
        listDataChild.put(listDataHeader.get(3), fleetowner);

        ExpandableListAdapter listAdapter = new ExpandableListAdapter(BookingDetailsActivity.this, listDataHeader, listDataChild, mMyBookingDataModel.doc);
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

// --Commented out by Inspection START (1/12/16, 5:30 PM):
//    private void onShareClick() {
//        Resources resources = getResources();
//
//        Intent emailIntent = new Intent();
//        emailIntent.setAction(Intent.ACTION_SEND);
//        // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
//        emailIntent.putExtra(Intent.EXTRA_TEXT, mMyBookingDataModel.crn);
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.crnsubject));
//        emailIntent.setType("message/rfc822");
//
//        PackageManager pm = getPackageManager();
//        Intent sendIntent = new Intent(Intent.ACTION_SEND);
//        sendIntent.setType("text/plain");
//
//        Intent openInChooser = Intent.createChooser(emailIntent, resources.getString(R.string.share_chooser_text));
//
//        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
//        List<LabeledIntent> intentList = new ArrayList<>();
//        for (int i = 0; i < resInfo.size(); i++) {
//            // Extract the label, append it, and repackage it in a LabeledIntent
//            ResolveInfo ri = resInfo.get(i);
//            String packageName = ri.activityInfo.packageName;
////            if(packageName.contains("android.email")) {
////                emailIntent.setPackage(packageName);
////            }
////            else
//            if (packageName.contains("mms") || packageName.contains("android.email")) {
//                Intent intent = new Intent();
//                intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
//                intent.setAction(Intent.ACTION_SEND);
////                intent.setType("text/plain");
//                intent.setType("message/rfc822");
//
//
//                if (packageName.contains("android.email")) {
//                    intent.putExtra(Intent.EXTRA_TEXT, mMyBookingDataModel.crn);
//                    intent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.crnsubject));
//                }
//
//                if (packageName.contains("mms")) {
//                    intent.putExtra(Intent.EXTRA_TEXT, mMyBookingDataModel.crn);
//                }
//                intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
//            }
//        }
//
//        // convert intentList to array
//        LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);
//
//        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
//        startActivity(openInChooser);
//    }
// --Commented out by Inspection STOP (1/12/16, 5:30 PM)
}
