package com.carrus.fleetowner.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carrus.fleetowner.BuildConfig;
import com.carrus.fleetowner.MainActivity;
import com.carrus.fleetowner.R;
import com.carrus.fleetowner.models.MyBookingDataModel;
import com.carrus.fleetowner.models.Trucks;
import com.carrus.fleetowner.models.TrucksType;
import com.carrus.fleetowner.retrofit.RestClient;
import com.carrus.fleetowner.utils.ApiResponseFlags;
import com.carrus.fleetowner.utils.CommonNoInternetDialog;
import com.carrus.fleetowner.utils.ConnectionDetector;
import com.carrus.fleetowner.utils.Constants;
import com.carrus.fleetowner.utils.SessionManager;
import com.carrus.fleetowner.utils.Utils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Sunny on 10/29/15 for Fleet Owner for Fleet Owner for Fleet Owner.
 */
public class TrucksFragment extends Fragment implements GoogleMap.OnMarkerClickListener {

    private static final String mBroadcastUiAction = "com.carrus.carrusshipper.broadcast.UI";
    private static final String mBroadcastAction = "com.carrus.carrusshipper.broadcast.AccessDenied";

    // Google Map
    private GoogleMap googleMap;
    //Markers List
    private final ArrayList<Marker> mMarkerArray = new ArrayList<>();
    //    private ArrayList<TrackingModel> mTrackermodel = new ArrayList<>();
    private final ArrayList<TrucksType> mTrackermodel = new ArrayList<>();
    private MainActivity mainActivity;
    private ConnectionDetector mConnectionDetector;
    private SessionManager mSessionManager;
    private LinearLayout mBottomView;
    private Trucks mTrucks;
    private boolean isMarkerMatch = false;
    private TextView nameTxtView, typeTxtView, locationTxtView, statusTxtView, truckNumberTxtView, crnTxtView, nicknameTxtView, errorTxtView;
    private String selectedNumber = null;
    private IntentFilter mIntentFilter;
    private Marker now;
    private EditText mSearchEdtTxt;
    private int selectedPos = 0;
    private LinearLayout mErrorLayout;


    public TrucksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        GMapV2GetRouteDirection v2GetRouteDirection = new GMapV2GetRouteDirection();

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mBroadcastUiAction);
        mIntentFilter.addAction(mBroadcastAction);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mBottomView = (LinearLayout) rootView.findViewById(R.id.bottomview);
        mConnectionDetector = new ConnectionDetector(getActivity());
        hideProfile();
        try {
            // Loading map
            mSessionManager = new SessionManager(getActivity());
            init(rootView);
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    private void init(View view) {
//        ImageView mProfileIV = (ImageView) view.findViewById(R.id.profileIV);
        nameTxtView = (TextView) view.findViewById(R.id.nameTxtView);
        typeTxtView = (TextView) view.findViewById(R.id.typeTxtView);
        locationTxtView = (TextView) view.findViewById(R.id.locationTxtView);
        statusTxtView = (TextView) view.findViewById(R.id.statusTxtView);
        mSearchEdtTxt = (EditText) view.findViewById(R.id.searchEdtTxt);
        truckNumberTxtView = (TextView) view.findViewById(R.id.crntopTxtView);
        crnTxtView = (TextView) view.findViewById(R.id.crnTxtView);
        nicknameTxtView = (TextView) view.findViewById(R.id.nicknameTxtView);
        mErrorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
        errorTxtView = (TextView) view.findViewById(R.id.errorTxtView);

        mSearchEdtTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Your piece of code on keyboard search click
                    if (mSearchEdtTxt.getText().toString().trim().isEmpty()) {
                        mSearchEdtTxt.setError(getResources().getString(R.string.entertrackid));
                        mSearchEdtTxt.requestFocus();
                    } else {
                        Utils.hideSoftKeyboard(getActivity());
                        if (searchTrackingId()) {
                            Toast.makeText(getActivity(), "No booking found", Toast.LENGTH_SHORT).show();
                        }

                    }

                    return true;
                }
                return false;
            }
        });

        (view.findViewById(R.id.callBtnIV)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedNumber != null) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + selectedNumber));
                    startActivity(callIntent);
                }
            }
        });


        mBottomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("value", mTrackermodel.get(selectedPos));
//                Intent intent = new Intent(getActivity(), BookingDetailsActivity.class);
//                intent.putExtras(bundle);
//                startActivityForResult(intent, 600);
            }
        });
    }


    private boolean searchTrackingId() {
        if (mTrucks != null)
            for (int i = 0; i < mTrucks.getData().size(); i++) {

                if (mTrucks.getData().get(i).getBooking().size() != 0 && mSearchEdtTxt.getText().toString().trim().equals(mTrucks.getData().get(i).getBooking().get(0).getCrn())) {
                    googleMap.clear();
                    LatLng location = new LatLng(Double.valueOf(mTrucks.getData().get(i).getCurrentCoordinates().getLat()), Double.valueOf(mTrucks.getData().get(i).getCurrentCoordinates().getLong()));

                    Marker marker = null;
                    if (mTrucks.getData().get(i).getTruckerColor().equalsIgnoreCase("WHITE")) {

                        marker = googleMap.addMarker(new MarkerOptions().position(location)
                                        .title(mTrucks.getData().get(i).getTrucker().get(0).getDriverName())
                                        .snippet("")
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_truck_white))
                        );

                    } else if (mTrucks.getData().get(i).getTruckerColor().equalsIgnoreCase("RED")) {

                        marker = googleMap.addMarker(new MarkerOptions().position(location)
                                        .title(mTrucks.getData().get(i).getTrucker().get(0).getDriverName())
                                        .snippet("")
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_truck_red))
                        );

                    } else if (mTrucks.getData().get(i).getTruckerColor().equalsIgnoreCase("BLACK")) {

                        marker = googleMap.addMarker(new MarkerOptions().position(location)
                                        .title(mTrucks.getData().get(i).getTrucker().get(0).getDriverName())
                                        .snippet("")
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_truck_black))
                        );

                    } else if (mTrucks.getData().get(i).getTruckerColor().equalsIgnoreCase("BLUE")) {

                        marker = googleMap.addMarker(new MarkerOptions().position(location)
                                        .title(mTrucks.getData().get(i).getTrucker().get(0).getDriverName())
                                        .snippet("")
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_truck_blue))
                        );

                    }

                    CameraUpdate center =
                            CameraUpdateFactory.newLatLng(location);
                    mMarkerArray.add(marker);
                    mTrackermodel.add(mTrucks.getData().get(i));
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(7);

                    googleMap.moveCamera(center);
                    googleMap.animateCamera(zoom);
                    return false;
                }
            }

        return true;
    }

    /**
     * function to load map. If map is not created it will create it for you
     */
    private void initilizeMap() {
        mainActivity = (MainActivity) getActivity();

        if (!mConnectionDetector.isConnectingToInternet()) {
            Utils.shopAlterDialog(getActivity(), getResources().getString(R.string.nointernetconnection), false);
            return;
        }

        if (googleMap == null) {

            SupportMapFragment fragmentManager = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

            googleMap = fragmentManager.getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getActivity(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            if (googleMap != null) {
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setCompassEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
//                googleMap.getUiSettings().setAllGesturesEnabled(true);
                googleMap.setTrafficEnabled(true);
//                googleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
                googleMap.setOnMarkerClickListener(this);

                getAllTrucks();
            }


        }
    }

    //Add marker function on google map
    private void addmarkers() {
        selectedNumber = null;
        hideProfile();
        googleMap.clear();
        mMarkerArray.clear();
        mTrackermodel.clear();
        isMarkerMatch = false;
        for (int i = 0; i < mTrucks.getData().size(); i++) {

            LatLng location = new LatLng(Double.valueOf(mTrucks.getData().get(i).getCurrentCoordinates().getLat()), Double.valueOf(mTrucks.getData().get(i).getCurrentCoordinates().getLong()));
            Marker marker = null;
            if (mTrucks.getData().get(i).getTruckerColor().equalsIgnoreCase("WHITE")) {
                marker = googleMap.addMarker(new MarkerOptions().position(location)
//                                    .title(mOnGoingShipper.mData.get(i).shipper.firstName)
//                                    .snippet(mOnGoingShipper.mData.get(i).shipper.firstName)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_truck_white))
                );

            } else if (mTrucks.getData().get(i).getTruckerColor().equalsIgnoreCase("RED")) {
                marker = googleMap.addMarker(new MarkerOptions().position(location)
//                                    .title(mOnGoingShipper.mData.get(i).shipper.firstName)
//                                    .snippet(mOnGoingShipper.mData.get(i).shipper.firstName)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_truck_red))
                );

            } else if (mTrucks.getData().get(i).getTruckerColor().equalsIgnoreCase("BLACK")) {
                marker = googleMap.addMarker(new MarkerOptions().position(location)
//                                    .title(mOnGoingShipper.mData.get(i).shipper.firstName)
//                                    .snippet(mOnGoingShipper.mData.get(i).shipper.firstName)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_truck_black))
                );

            } else if (mTrucks.getData().get(i).getTruckerColor().equalsIgnoreCase("BLUE")) {
                marker = googleMap.addMarker(new MarkerOptions().position(location)
//                                    .title(mOnGoingShipper.mData.get(i).shipper.firstName)
//                                    .snippet(mOnGoingShipper.mData.get(i).shipper.firstName)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_truck_blue))
                );

            }

            mMarkerArray.add(marker);
            mTrackermodel.add(mTrucks.getData().get(i));

        }


        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : mMarkerArray) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 200; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

//        googleMap.moveCamera(cu);

        googleMap.animateCamera(cu);

    }

    @Override
    public void onResume() {
        super.onResume();
        initilizeMap();
        getActivity().registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(mReceiver);
        super.onPause();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        for (int i = 0; i < mMarkerArray.size(); i++) {
            if (marker.equals(mMarkerArray.get(i))) {
                if (mTrackermodel.get(i).getBooking().size() != 0) {
                    isMarkerMatch = true;
                    selectedPos = i;
                    selectedNumber = mTrackermodel.get(i).getTrucker().get(0).getPhoneNumber();
                    nameTxtView.setText(mTrackermodel.get(i).getTrucker().get(0).getDriverName());
                    typeTxtView.setText(mTrackermodel.get(i).getTypeTruck().get(0).typeTruckName);
                    truckNumberTxtView.setText(mTrackermodel.get(i).getTruckNumber());
                    crnTxtView.setText(getResources().getString(R.string.crn) + mTrackermodel.get(i).getBooking().get(0).getCrn());
                    locationTxtView.setText(mTrackermodel.get(i).getBooking().get(0).getPickUp().getCity() + getResources().getString(R.string.towithspaces) + mTrackermodel.get(i).getBooking().get(0).getDropOff().getCity());
                    nicknameTxtView.setText(getResources().getString(R.string.nickname) + mTrackermodel.get(i).getTruckName());
//                    Picasso.with(getActivity()).load(R.mipmap.icon_placeholder).resize(100, 100).transform(new CircleTransform()).into(mProfileIV);
                    if (mTrackermodel.get(i).getStatus().equalsIgnoreCase(getResources().getString(R.string.activate)))
                        statusTxtView.setText(getResources().getString(R.string.ongoing));
                    else
                        statusTxtView.setText(mTrackermodel.get(i).getStatus());

                    showProfile();
                } else
                    Toast.makeText(getActivity(), getResources().getString(R.string.nodetailsfound), Toast.LENGTH_SHORT).show();
                break;
            }
        }

        return false;
    }

    //Change Drawer mode
    public interface onSwiperListenerChange {
        void onStopDrawerSwip();

        void onStartDrawerSwipe();

        void stopService();

    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(mBroadcastUiAction)) {
                Bundle bundle = intent.getExtras();

                MyBookingDataModel mTrackingModel = (MyBookingDataModel) bundle.getSerializable("data");
                if (now != null) {
                    now.remove();
                }

                MarkerOptions markerOptions = new MarkerOptions();
                assert mTrackingModel != null;
                markerOptions.position(new LatLng(mTrackingModel.crruentTracking.get(0).lat, mTrackingModel.crruentTracking.get(0).longg));
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_truck_white));
                now = googleMap.addMarker(markerOptions);

            } else if (intent.getAction().equals(mBroadcastAction)) {
                Utils.shopAlterDialog(getActivity(), intent.getStringExtra("data"), true);
                mainActivity.stopService();
            }
        }
    };

    //Path Direction Call
//    public void getDriectionToDestination(final LatLng currentposition, final String start, final String end, String mode, final int pos) {
//        Utils.loading_box(getActivity());
//        RestClient.getGoogleApiService().getDriections(start, end, "false", "metric", mode, new Callback<String>() {
//            @Override
//            public void success(String s, Response response) {
//                googleMap.clear();
//                // convert String into InputStream
//                InputStream in = new ByteArrayInputStream(s.getBytes());
//                DocumentBuilder builder = null;
//
//                try {
//                    builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//                    Document doc = builder.parse(in);
//                    ArrayList<LatLng> directionPoint = v2GetRouteDirection.getDirection(doc);
//                    Log.v("SIZE > ", "SIZE OF LIST > " + directionPoint.size());
//                    PolylineOptions rectLine = new PolylineOptions().width(6).color(Color.parseColor("#1F58B9"));
//
//                    for (int i = 0; i < directionPoint.size(); i++) {
//                        rectLine.add(directionPoint.get(i));
//                    }
//
//                    // Adding route on the map
//                    googleMap.addPolyline(rectLine);
//                    MarkerOptions markerOptions = new MarkerOptions();
//                    markerOptions.position(currentposition);
//                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_van));
//                    now = googleMap.addMarker(markerOptions);
//
//                    String[] ar = start.split("[,]");
//                    String[] ad = end.split("[,]");
//                    googleMap.addMarker(new MarkerOptions().title(mTrackermodel.get(pos).pickUp.name).snippet(mTrackermodel.get(pos).pickUp.companyName + ", " + mTrackermodel.get(pos).pickUp.address + ", " + mTrackermodel.get(pos).pickUp.city + "," + mTrackermodel.get(pos).pickUp.state + "\n" + mTrackermodel.get(pos).pickUp.contactNumber).position(new LatLng(Double.valueOf(ar[0]), Double.valueOf(ar[1]))).icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_location_blue)));
//                    googleMap.addMarker(new MarkerOptions().title(mTrackermodel.get(pos).dropOff.name).snippet(mTrackermodel.get(pos).dropOff.companyName + ", " + mTrackermodel.get(pos).dropOff.address + ", " + mTrackermodel.get(pos).dropOff.city + "," + mTrackermodel.get(pos).dropOff.state + "\n" + mTrackermodel.get(pos).dropOff.contactNumber).position(new LatLng(Double.valueOf(ad[0]), Double.valueOf(ad[1]))).icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_location_blue)));
//
//
//                    LatLngBounds.Builder mbuilder = new LatLngBounds.Builder();
//                    mbuilder.include(new LatLng(Double.valueOf(ar[0]), Double.valueOf(ar[1])));
//                    mbuilder.include(new LatLng(Double.valueOf(ad[0]), Double.valueOf(ad[1])));
//
//                    Display display = getActivity().getWindowManager().getDefaultDisplay();
//                    Point size = new Point();
//                    display.getSize(size);
//                    int width = size.x;
////                    int height = size.y;
//
//
//
//                    LatLngBounds bounds = mbuilder.build();
////                    int padding = 250; // offset from edges of the map in pixels
//                    int padding = ((width * 10) / 40); // offset from edges of the map
//                    // in pixels
//                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
//
//                    try {
//                        googleMap.animateCamera(cu);
//                    } catch (Exception e) {
//                        padding=0;
//                        cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
//                        googleMap.animateCamera(cu);
//                        e.printStackTrace();
//                    }
//
//                    selectedNumber = mTrackermodel.get(pos).shipper.phoneNumber;
//                    nameTxtView.setText(mTrackermodel.get(pos).shipper.firstName + " " + mTrackermodel.get(pos).shipper.lastName);
//                    typeTxtView.setText(mTrackermodel.get(pos).truck.truckType.typeTruckName + ", " + mTrackermodel.get(pos).truck.truckNumber);
//                    locationTxtView.setText(mTrackermodel.get(pos).pickUp.city + " to " + mTrackermodel.get(pos).dropOff.city);
////                    Picasso.with(getActivity()).load(R.mipmap.icon_placeholder).resize(100, 100).transform(new CircleTransform()).into(mProfileIV);
//                    statusTxtView.setText(mTrackermodel.get(pos).bookingStatus.replace("_", " "));
//
//                    switch (mTrackermodel.get(pos).bookingStatus.toUpperCase()) {
//                        case "REACHED_DESTINATION":
//                        case "REACHED_PICKUP_LOCATION":
//                            statusTxtView.setTextColor(getResources().getColor(R.color.tabcolor_dark));
//                            break;
//
//                        case "ON_GOING":
//                        case "UP_GOING":
//                            statusTxtView.setTextColor(getResources().getColor(R.color.colorPrimary));
//                            break;
//
//                        case "CONFIRMED":
//                            statusTxtView.setTextColor(getResources().getColor(R.color.green));
//                            break;
//
//                        case "HALT":
//                        case "COMPLETED":
//                            statusTxtView.setTextColor(getResources().getColor(R.color.gray_text));
//                            break;
//
//                        case "CANCELED":
//                            statusTxtView.setTextColor(getResources().getColor(R.color.red));
//                            break;
//
//                    }
//                    showProfile();
//
////                    Intent serviceIntent = new Intent(getActivity(), MyService.class);
////                    serviceIntent.putExtra("bookingId", mTrackermodel.get(pos).crruentTracking.get(0).bookingId);
////                    getActivity().startService(serviceIntent);
//
//                } catch (ParserConfigurationException e) {
//                    e.printStackTrace();
//                } catch (SAXException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
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
//                        Toast.makeText(getActivity(), getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
//                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Unauthorized.getOrdinal()) {
//                        Utils.shopAlterDialog(getActivity(), Utils.getErrorMsg(error), true);
//                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_Found.getOrdinal()) {
//                        Toast.makeText(getActivity(), Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception ex) {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

    private void getAllTrucks() {
        mErrorLayout.setVisibility(View.GONE);
        Utils.loading_box(getActivity());

        RestClient.getApiService().getallTruck(mSessionManager.getAccessToken(), "0", "0", Constants.SORT, "ALL", new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                if (BuildConfig.DEBUG)
                    Log.v("" + getClass().getSimpleName(), "Response> " + s);
                try {
                    JSONObject mObject = new JSONObject(s);

                    int status = mObject.getInt("statusCode");

                    if (ApiResponseFlags.OK.getOrdinal() == status) {
                        Gson gson = new Gson();
                        mTrucks = gson.fromJson(s, Trucks.class);
                        addmarkers();
                    } else {
                        Toast.makeText(getActivity(), mObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                    if (BuildConfig.DEBUG)
                        Log.v("error.getKind() >> " + error.getKind(), " MSg >> " + error.getResponse().getStatus());

                    if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
//                        Utils.shopAlterDialog(getActivity(), getResources().getString(R.string.nointernetconnection), false);
                        CommonNoInternetDialog.WithActivity(getActivity()).Show(getResources().getString(R.string.nointernetconnection), getResources().getString(R.string.tryagain), getResources().getString(R.string.exit), new CommonNoInternetDialog.ConfirmationDialogEventsListener() {
                            @Override
                            public void OnOkButtonPressed() {
                                getAllTrucks();
                            }

                            @Override
                            public void OnCancelButtonPressed() {
                                getActivity().finish();
                            }
                        });
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Unauthorized.getOrdinal()) {
                        Utils.shopAlterDialog(getActivity(), Utils.getErrorMsg(error), true);
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_Found.getOrdinal()) {
//                        Toast.makeText(getActivity(), Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
                        mErrorLayout.setVisibility(View.VISIBLE);
                        errorTxtView.setText("" + Utils.getErrorMsg(error));
                    }
                } catch (Exception ex) {
                    Utils.shopAlterDialog(getActivity(), getResources().getString(R.string.nointernetconnection), false);
                }
            }
        });
    }

    //    /***
//     * shows login layout
//     */
    private void showProfile() {
        final Animation animationFadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);
        mBottomView.setAnimation(animationFadeIn);
        mBottomView.setVisibility(View.VISIBLE);
    }

    //    /***
//     * hides login layout
//     */
    private void hideProfile() {
        final Animation animationFadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fadeout);
        mBottomView.setAnimation(animationFadeOut);
        mBottomView.setVisibility(View.GONE);
    }

}
