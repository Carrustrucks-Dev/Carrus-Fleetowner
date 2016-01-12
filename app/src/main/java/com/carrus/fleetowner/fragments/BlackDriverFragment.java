package com.carrus.fleetowner.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carrus.fleetowner.BuildConfig;
import com.carrus.fleetowner.R;
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
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.carrus.fleetowner.utils.Constants.LIMIT;
import static com.carrus.fleetowner.utils.Constants.SORT;

/**
 * Created by Sunny on 12/9/15 for Fleet Owner for Fleet Owner for Fleet Owner.
 */
public class BlackDriverFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private RecyclerView mRecyclerView;
    private DriverListAdapter mAdapter;
    private SessionManager mSessionManager;
    private int skip = 0;
    private SwipeRefreshLayout swipeRefreshLayout;
    public boolean isRefreshView = false;
    private ConnectionDetector mConnectionDetector;
    private TextView mErrorTxtView;
    private List<Datum> bookingList;
    private DriverModel mDriverModel;
    private LinearLayout mErrorLayout;

    /**
     * Static factory method that takes an int parameter,
     * initializes the fragment's arguments, and returns the
     * new fragment to the client.
     */
    public static BlackDriverFragment newInstance() {
        BlackDriverFragment f = new BlackDriverFragment();
        Bundle args = new Bundle();
        args.putInt("index", 1);
        f.setArguments(args);
        return f;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View convertView = inflater.inflate(R.layout.fragment_bookinglist, container, false);
        init(convertView);
        intializeListners();
        return convertView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSessionManager = new SessionManager(getActivity());
        mConnectionDetector = new ConnectionDetector(getActivity());
        if (mConnectionDetector.isConnectingToInternet())
            getMyBooking(((DriverFragment) getParentFragment()).mSearchEdtTxt.getText().toString().trim());
        else {
            noInternetDialog();
        }
    }

    private void intializeListners() {
        mErrorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mErrorLayout.setVisibility(View.GONE);
                if (mConnectionDetector.isConnectingToInternet())
                    getMyBooking(((DriverFragment) getParentFragment()).mSearchEdtTxt.getText().toString().trim());
                else {
                    noInternetDialog();
                }
            }
        });
    }

    private void init(View view) {
        mErrorLayout =(LinearLayout) view.findViewById(R.id.errorLayout);
        mErrorTxtView = (TextView) view.findViewById(R.id.errorTxtView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
//        swipeRefreshLayout.setColorSchemeColors(
//                Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity()));
        mAdapter = new DriverListAdapter(getActivity(), bookingList, mRecyclerView, false);
        mRecyclerView.setAdapter(mAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshView = true;
                getMyBooking(((DriverFragment) getParentFragment()).mSearchEdtTxt.getText().toString().trim());
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (Constants.isUpComingUpdate) {
            Constants.isUpComingUpdate = false;
            isRefreshView = true;
            getMyBooking(((DriverFragment) getParentFragment()).mSearchEdtTxt.getText().toString().trim());
        }
    }

    public void getMyBooking(final String val) {
        mErrorLayout.setVisibility(View.GONE);
        if (isRefreshView) {
            swipeRefreshLayout.setRefreshing(true);
            skip = 0;
            bookingList = null;
        } else {
            if (bookingList == null || bookingList.size() == 0)
                Utils.loading_box(getActivity());
        }

        RestClient.getApiService().getallTrucker(mSessionManager.getAccessToken(), val, LIMIT + "", skip + "", SORT, Constants.DRIVERBLACK, new Callback<String>() {

            @Override
            public void success(String s, Response response) {
//                if(BuildConfig.DEBUG)
                Log.e("" + getClass().getSimpleName(), "Response> " + s);

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
                            mAdapter = new DriverListAdapter(getActivity(), bookingList, mRecyclerView, false);
                            mRecyclerView.setAdapter(mAdapter);
                            if (mDriverModel.getData().size() == LIMIT)
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
                        skip = skip + mDriverModel.getData().size();
                    } else {
                        if (ApiResponseFlags.Not_Found.getOrdinal() == status) {
                            bookingList.remove(bookingList.size() - 1);
                            mAdapter.notifyItemRemoved(bookingList.size());
                        } else {
                            mErrorTxtView.setText(mObject.getString("message"));
                            mErrorLayout.setVisibility(View.VISIBLE);
                        }

                        Toast.makeText(getActivity(), mObject.getString("message"), Toast.LENGTH_SHORT).show();

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
//                        Utils.shopAlterDialog(getActivity(), getResources().getString(R.string.nointernetconnection), false);
                        noInternetDialog();

                        if (bookingList == null || bookingList.size() == 0) {
                            mAdapter = new DriverListAdapter(getActivity(), bookingList, mRecyclerView, false);
                            mRecyclerView.setAdapter(mAdapter);
//                            mErrorTxtView.setText(getResources().getString(R.string.nointernetconnection));
//                            mErrorTxtView.setVisibility(View.VISIBLE);
                        }
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Unauthorized.getOrdinal()) {
                        Utils.shopAlterDialog(getActivity(), Utils.getErrorMsg(error), true);
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_Found.getOrdinal()) {
                        if(!val.isEmpty())
                        Toast.makeText(getActivity(), Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
                        else if (bookingList == null || bookingList.size() == 0) {
                            mErrorTxtView.setText(getResources().getString(R.string.nodriverfound));
                            mErrorLayout.setVisibility(View.VISIBLE);
                        }
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_MORE_RESULT.getOrdinal()) {
                        Toast.makeText(getActivity(), Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
                        try {
                            bookingList.remove(bookingList.size() - 1);
                            mAdapter.notifyItemRemoved(bookingList.size());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } catch (Exception ex) {
                    noInternetDialog();
                    if (bookingList == null || bookingList.size() == 0) {
                        mAdapter = new DriverListAdapter(getActivity(), bookingList, mRecyclerView, false);
                        mRecyclerView.setAdapter(mAdapter);
//                        mErrorTxtView.setText(getResources().getString(R.string.nointernetconnection));
//                        mErrorTxtView.setVisibility(View.VISIBLE);
                    }
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
                    getMyBooking(((DriverFragment) getParentFragment()).mSearchEdtTxt.getText().toString().trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }

    private void noInternetDialog() {
        CommonNoInternetDialog.WithActivity(getActivity()).Show(getResources().getString(R.string.nointernetconnection), getResources().getString(R.string.tryagain), getResources().getString(R.string.exit), new CommonNoInternetDialog.ConfirmationDialogEventsListener() {
            @Override
            public void OnOkButtonPressed() {
                isRefreshView = true;
                getMyBooking(((DriverFragment) getParentFragment()).mSearchEdtTxt.getText().toString().trim());
            }

            @Override
            public void OnCancelButtonPressed() {
                getActivity().finish();
            }
        });
    }
}
