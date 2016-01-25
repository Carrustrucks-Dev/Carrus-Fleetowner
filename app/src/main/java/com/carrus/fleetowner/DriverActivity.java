package com.carrus.fleetowner;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carrus.fleetowner.adapters.DividerItemDecoration;
import com.carrus.fleetowner.adapters.DriverListAdapter;
import com.carrus.fleetowner.interfaces.OnLoadMoreListener;
import com.carrus.fleetowner.models.Datum;
import com.carrus.fleetowner.models.DriverModel;
import com.carrus.fleetowner.retrofit.RestClient;
import com.carrus.fleetowner.utils.ApiResponseFlags;
import com.carrus.fleetowner.utils.CommonNoInternetDialog;
import com.carrus.fleetowner.utils.ConnectionDetector;
import com.carrus.fleetowner.utils.Constants;
import com.carrus.fleetowner.utils.SessionManager;
import com.carrus.fleetowner.utils.Utils;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.carrus.fleetowner.utils.Constants.ID;
import static com.carrus.fleetowner.utils.Constants.LIMIT;
import static com.carrus.fleetowner.utils.Constants.MY_FLURRY_APIKEY;
import static com.carrus.fleetowner.utils.Constants.SORT;

/**
 * Created by Sunny on 12/15/15 for Fleet Owner for Fleet Owner for Fleet Owner.
 */
public class DriverActivity extends BaseActivity {

    private ImageView mBackBtn;
    private final String TAG = getClass().getSimpleName();
    private RecyclerView mRecyclerView;
    private DriverListAdapter mAdapter;
    private SessionManager mSessionManager;
    private int skip = 0;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isRefreshView = false;
    private ConnectionDetector mConnectionDetector;
    private List<Datum> bookingList;
    private DriverModel mDriverModel;
    private Context mContext;
    private EditText mSearchEdtTxt;
    private TextView mErrorTxtView;
    private LinearLayout mErrorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        mContext = this;
        init();
        initializeClickListner();
    }

    private void init() {
        TextView headerTxtView = (TextView) findViewById(R.id.headerTxtView);
        headerTxtView.setText(getResources().getString(R.string.drivers));
        mBackBtn = (ImageView) findViewById(R.id.menu_back_btn);
        mBackBtn.setVisibility(View.VISIBLE);
        mSearchEdtTxt = (EditText) findViewById(R.id.searchEdtTxt);
        mErrorLayout =(LinearLayout) findViewById(R.id.errorLayout);
        mErrorTxtView = (TextView) findViewById(R.id.errorTxtView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
//        swipeRefreshLayout.setColorSchemeColors(
//                Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(mContext));
        mAdapter = new DriverListAdapter((Activity) mContext, bookingList, mRecyclerView, true);
        mRecyclerView.setAdapter(mAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshView = true;
                getDrivers(mSearchEdtTxt.getText().toString().trim());
            }
        });
        mSessionManager = new SessionManager(mContext);
        mConnectionDetector = new ConnectionDetector(mContext);
        if (mConnectionDetector.isConnectingToInternet())
            getDrivers(mSearchEdtTxt.getText().toString().trim());
        else {
           noInternetDialog();
        }
    }

    private void initializeClickListner() {
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.assigndriverBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mConnectionDetector.isConnectingToInternet())
                    if (mAdapter.getSelectedDriver() == null) {
                        Utils.shopAlterDialog(mContext, getResources().getString(R.string.selecetDriver), false);
                    } else {
                        assignDriver(mAdapter.getSelectedDriver());
                    }
                else {
                    noInternetDialog();
                }
            }
        });

        mSearchEdtTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Your piece of code on keyboard search click
                    if (mSearchEdtTxt.getText().toString().trim().isEmpty()) {
                        mSearchEdtTxt.setError(getResources().getString(R.string.enterdrivername));
                        mSearchEdtTxt.requestFocus();
                    } else {
                        Utils.hideSoftKeyboard(DriverActivity.this);
                        isRefreshView = true;
                        getDrivers(mSearchEdtTxt.getText().toString().trim());
                    }

                    return true;
                }
                return false;
            }
        });

        mErrorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mErrorLayout.setVisibility(View.GONE);
                if (mConnectionDetector.isConnectingToInternet())
                    getDrivers(mSearchEdtTxt.getText().toString().trim());
                else {
                    noInternetDialog();
                }
            }
        });

                mSearchEdtTxt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        // TODO Auto-generated method stub

                        if (s.length() == 0) {
                            getDrivers(mSearchEdtTxt.getText().toString().trim());
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        // TODO Auto-generated method stub
                    }
                });

    }

    private void getDrivers(String val) {
        mErrorLayout.setVisibility(View.GONE);
        if (isRefreshView) {
            swipeRefreshLayout.setRefreshing(true);
            skip = 0;
            bookingList = null;
        } else {
            if (bookingList == null || bookingList.size() == 0)
                Utils.loading_box(mContext);
        }

        RestClient.getApiService().getallTrucker(mSessionManager.getAccessToken(), val, LIMIT + "", skip + "", SORT, Constants.DRIVERWHITE, new Callback<String>() {

            @Override
            public void success(String s, Response response) {
                if(BuildConfig.DEBUG)
                Log.v("" + getClass().getSimpleName(), "Response> " + s);

                try {
                    JSONObject mObject = new JSONObject(s);
                    int status = mObject.getInt("statusCode");
                    if (ApiResponseFlags.OK.getOrdinal() == status) {
                        Gson gson = new Gson();
                        mDriverModel = gson.fromJson(s, DriverModel.class);
                        // specify an adapter (see also next example)
                        if (bookingList == null) {
                            bookingList = new ArrayList<>();
                            bookingList.addAll(mDriverModel.getData());
                            mAdapter = new DriverListAdapter((Activity) mContext, bookingList, mRecyclerView, true);
                            mRecyclerView.setAdapter(mAdapter);
                            setonScrollListener();
                        } else {
                            bookingList.remove(bookingList.size() - 1);
                            mAdapter.notifyItemRemoved(bookingList.size());
                            //add items one by one
                            int start = bookingList.size();
                            int end = start + mDriverModel.getData().size();
                            int j = 0;
                            for (int i = start + 1; i <= end; i++) {
                                bookingList.add(mDriverModel.getData().get(j));
                                mAdapter.notifyItemInserted(bookingList.size());
                                j++;
                            }
                            mAdapter.setLoaded();
                        }
                        skip = skip + LIMIT;
                    } else {
                        if (ApiResponseFlags.Not_Found.getOrdinal() == status) {
                            bookingList.remove(bookingList.size() - 1);
                            mAdapter.notifyItemRemoved(bookingList.size());
                        }

                        Toast.makeText(mContext, mObject.getString("message"), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Utils.loading_box_stop();
                isRefreshView = false;
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                swipeRefreshLayout.setRefreshing(false);
                Utils.loading_box_stop();
                try {
                    if(BuildConfig.DEBUG)
                    Log.v("error.getKind() >> " + error.getKind(), " MSg >> " + error.getResponse().getStatus());

                    if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                        noInternetDialog();
                        mAdapter = new DriverListAdapter((Activity) mContext, bookingList, mRecyclerView, true);
                        mRecyclerView.setAdapter(mAdapter);
//                        mErrorTxtView.setText(getResources().getString(R.string.nointernetconnection));
//                        mErrorTxtView.setVisibility(View.VISIBLE);
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Unauthorized.getOrdinal()) {
                        Utils.shopAlterDialog(mContext, Utils.getErrorMsg(error), true);
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_Found.getOrdinal()) {
//                        Toast.makeText(mContext, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
                        mAdapter = new DriverListAdapter((Activity) mContext, bookingList, mRecyclerView, true);
                        mRecyclerView.setAdapter(mAdapter);
                            mErrorTxtView.setText(getResources().getString(R.string.nodriverfound));
                            mErrorLayout.setVisibility(View.VISIBLE);
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_MORE_RESULT.getOrdinal()) {
                        Toast.makeText(mContext, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
                        try {
                            bookingList.remove(bookingList.size() - 1);
                            mAdapter.notifyItemRemoved(bookingList.size());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } catch (Exception ex) {
                    noInternetDialog();
                    mAdapter = new DriverListAdapter((Activity) mContext, bookingList, mRecyclerView, true);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        });
    }

    private void setonScrollListener() {

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                try {
                    bookingList.add(null);
                    mAdapter.notifyItemInserted(bookingList.size() - 1);
                    getDrivers(mSearchEdtTxt.getText().toString().trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, MY_FLURRY_APIKEY);
        FlurryAgent.onEvent("Driver Assign Mode");
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

    private void assignDriver(String id) {
        Utils.loading_box(DriverActivity.this);
        RestClient.getApiService().assignTrucker(mSessionManager.getAccessToken(), id, getIntent().getStringExtra(ID), new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                if(BuildConfig.DEBUG)
                Log.v("" + getClass().getSimpleName(), "Response> " + s);
                try {
                    JSONObject mObject = new JSONObject(s);

                    int status = mObject.getInt("statusCode");

                    if (ApiResponseFlags.Created.getOrdinal() == status) {
                        FlurryAgent.onEvent("Driver Assign Mode");
                        Constants.isTruckAssignUpdate = true;
                        Toast.makeText(DriverActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        Toast.makeText(DriverActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(mContext, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();

                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Unauthorized.getOrdinal()) {
                        Utils.shopAlterDialog(mContext, Utils.getErrorMsg(error), true);
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Bad_Request.getOrdinal()) {
                        Utils.shopAlterDialog(mContext, getResources().getString(R.string.trucknotfound), false);
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_Found.getOrdinal()) {
                        Toast.makeText(mContext, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_MORE_RESULT.getOrdinal()) {
                        Toast.makeText(mContext, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ex) {
                    Toast.makeText(mContext, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void noInternetDialog(){
        CommonNoInternetDialog.WithActivity((Activity)mContext).Show(getResources().getString(R.string.nointernetconnection), getResources().getString(R.string.tryagain), getResources().getString(R.string.exit), new CommonNoInternetDialog.ConfirmationDialogEventsListener() {
            @Override
            public void OnOkButtonPressed() {
                isRefreshView = true;
                getDrivers(mSearchEdtTxt.getText().toString().trim());
            }

            @Override
            public void OnCancelButtonPressed() {
                finish();
            }
        });
    }
}
