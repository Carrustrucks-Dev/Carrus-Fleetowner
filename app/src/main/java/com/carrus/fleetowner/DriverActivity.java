package com.carrus.fleetowner;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.carrus.fleetowner.adapters.DividerItemDecoration;
import com.carrus.fleetowner.adapters.DriverListAdapter;
import com.carrus.fleetowner.interfaces.OnLoadMoreListener;
import com.carrus.fleetowner.models.Datum;
import com.carrus.fleetowner.models.DriverModel;
import com.carrus.fleetowner.retrofit.RestClient;
import com.carrus.fleetowner.utils.ApiResponseFlags;
import com.carrus.fleetowner.utils.ConnectionDetector;
import com.carrus.fleetowner.utils.Constants;
import com.carrus.fleetowner.utils.SessionManager;
import com.carrus.fleetowner.utils.Utils;
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
import static com.carrus.fleetowner.utils.Constants.SORT;

/**
 * Created by Sunny on 12/15/15.
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
                new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshView = true;
                getDrivers();
            }
        });
        mSessionManager = new SessionManager(mContext);
        mConnectionDetector = new ConnectionDetector(mContext);
        if (mConnectionDetector.isConnectingToInternet())
            getDrivers();
        else {
            Utils.shopAlterDialog(mContext, getResources().getString(R.string.nointernetconnection), false);
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
                    Utils.shopAlterDialog(mContext, getResources().getString(R.string.nointernetconnection), false);
                }
            }
        });

    }

    private void getDrivers() {
        if (isRefreshView) {
            swipeRefreshLayout.setRefreshing(true);
            skip = 0;
            bookingList = null;
        } else {
            if (bookingList == null || bookingList.size() == 0)
                Utils.loading_box(mContext);
        }

        RestClient.getApiService().getallTrucker(mSessionManager.getAccessToken(), "", LIMIT + "", skip + "", SORT, Constants.DRIVERWHITE, new Callback<String>() {

            @Override
            public void success(String s, Response response) {
                Log.v("" + getClass().getSimpleName(), "Response> " + s);

                try {
                    JSONObject mObject = new JSONObject(s);
                    int status = mObject.getInt("statusCode");
                    if (ApiResponseFlags.OK.getOrdinal() == status) {
                        Gson gson = new Gson();
                        mDriverModel = gson.fromJson(s, DriverModel.class);
                        // specify an adapter (see also next example)
                        if (bookingList == null) {
                            bookingList = new ArrayList<Datum>();
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
                        } else {
//                            mErrorTxtView.setText(mObject.getString("message"));
//                            mErrorTxtView.setVisibility(View.VISIBLE);
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
                    Log.v("error.getKind() >> " + error.getKind(), " MSg >> " + error.getResponse().getStatus());

                    if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                        Toast.makeText(mContext, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
//                        mErrorTxtView.setText(getResources().getString(R.string.nointernetconnection));
//                        mErrorTxtView.setVisibility(View.VISIBLE);
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Unauthorized.getOrdinal()) {
                        Utils.shopAlterDialog(mContext, Utils.getErrorMsg(error), true);
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_Found.getOrdinal()) {
                        Toast.makeText(mContext, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(mContext, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
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
                    getDrivers();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }

    private void assignDriver(String id) {
        Utils.loading_box(DriverActivity.this);
        RestClient.getApiService().assignTrucker(mSessionManager.getAccessToken(), id, getIntent().getStringExtra(ID), new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.v("" + getClass().getSimpleName(), "Response> " + s);
                try {
                    JSONObject mObject = new JSONObject(s);

                    int status = mObject.getInt("statusCode");

                    if (ApiResponseFlags.OK.getOrdinal() == status) {
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
                    Log.v("error.getKind() >> " + error.getKind(), " MSg >> " + error.getResponse().getStatus());

                    if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                        Toast.makeText(mContext, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
//                        mErrorTxtView.setText(getResources().getString(R.string.nointernetconnection));
//                        mErrorTxtView.setVisibility(View.VISIBLE);
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Unauthorized.getOrdinal()) {
                        Utils.shopAlterDialog(mContext, Utils.getErrorMsg(error), true);
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
}
