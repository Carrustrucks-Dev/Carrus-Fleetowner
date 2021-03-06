package com.carrus.fleetowner;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.carrus.fleetowner.fragments.DriverFragment;
import com.carrus.fleetowner.fragments.MyBookingFragment;
import com.carrus.fleetowner.fragments.ProfileFragment;
import com.carrus.fleetowner.fragments.TruckReguestsFragment;
import com.carrus.fleetowner.fragments.TrucksFragment;
import com.carrus.fleetowner.fragments.WebViewFragment;
import com.carrus.fleetowner.gcm.GcmMessageHandler;
import com.carrus.fleetowner.retrofit.RestClient;
import com.carrus.fleetowner.utils.ApiResponseFlags;
import com.carrus.fleetowner.utils.CommonNoInternetDialog;
import com.carrus.fleetowner.utils.Constants;
import com.carrus.fleetowner.utils.SessionManager;
import com.carrus.fleetowner.utils.Utils;
import com.flurry.android.FlurryAgent;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends BaseActivity implements FragmentDrawer.FragmentDrawerListener, TrucksFragment.onSwiperListenerChange {

    private static String TAG = MainActivity.class.getSimpleName();

    private FragmentDrawer drawerFragment;
    private DrawerLayout mDrawerLayout;
    private TextView mHeaderTextView;
    private ImageView mMenuButton, mBackButton;
    private int selectedPos = -1;
    private SessionManager mSessionManager;
    private Bundle bundle = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSessionManager = new SessionManager(this);
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerFragment.setUp(mDrawerLayout);
        drawerFragment.setDrawerListener(this);

        initializeView();
        initializeClickListners();
        // display the first navigation drawer view on app launch
        if (getIntent().getBooleanExtra("fromNotification", false)) {
            bundle = new Bundle();
            bundle.putString("id", getIntent().getStringExtra("id"));
            displayView(1);
        } else
            displayView(0);
    }

    private void initializeView() {
        mHeaderTextView = (TextView) findViewById(R.id.headerTxtView);
        mMenuButton = (ImageView) findViewById(R.id.menu_drawer_btn);
        mMenuButton.setVisibility(View.VISIBLE);
        mBackButton = (ImageView) findViewById(R.id.menu_back_btn);
    }

    private void initializeClickListners() {
        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RtlHardcoded")
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public void onDrawerItemSelected(int position) {
        displayView(position);
    }

    @Override
    public void onHeaderSelected() {
        selectedPos = -1;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, new ProfileFragment());
        fragmentTransaction.commit();

        // set the toolbar title
//            getSupportActionBar().setTitle(title);
        mHeaderTextView.setText(getString(R.string.myprofile));
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                if (selectedPos != 0) {
                    selectedPos = 0;
                    fragment = new MyBookingFragment();
                    if (bundle != null)
                        fragment.setArguments(bundle);
                    title = getString(R.string.mybooking);
                }
                break;
            case 1:
                if (selectedPos != 1) {
                    selectedPos = 1;
                    fragment = new TruckReguestsFragment();
                    title = getString(R.string.trucksrequest);
                }
                break;
            case 2:
                if (selectedPos != 2) {
                    selectedPos = 2;
                    fragment = new TrucksFragment();
                    title = getString(R.string.trucks);
                }
                break;

            case 3:
                if (selectedPos != 3) {
                    selectedPos = 3;
                    fragment = new DriverFragment();
                    title = getString(R.string.drivers);
                }
                break;

            case 4:
                if (selectedPos != 4) {
                    selectedPos = 4;
                    fragment = new WebViewFragment();
                    title = getString(R.string.aboutus);
                }
                break;


            case 5:
                if (selectedPos != 5) {
//                    selectedPos = 2;
                    try {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + Constants.CONTACT_CARRUS));
                        startActivity(callIntent);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;

//            case 3:
//                if (selectedPos != 3) {
//                    selectedPos = 3;
//
//                }
//                break;
            case 6:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.quit)
                        .setMessage(R.string.really_quit)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Stop the activity
                                // MainActivity.this.finish();
                                logout();
                            }

                        })
                        .setNegativeButton(R.string.no, null)
                        .show();

                break;

            case 55:
                if (selectedPos != 55) {
                    selectedPos = 55;
                    fragment = new WebViewFragment();
                    title = getString(R.string.term_privacy);
                }
                break;

            default:
                selectedPos = 0;
                fragment = new MyBookingFragment();
                title = getString(R.string.mybooking);
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment, title);
            fragmentTransaction.commit();

            // set the toolbar title
//            getSupportActionBar().setTitle(title);
            mHeaderTextView.setText(title);
        }
    }

    public void onRefreshImageView() {
        drawerFragment.loadImage();
    }

    @Override
    public void onStopDrawerSwip() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mMenuButton.setVisibility(View.GONE);
        mBackButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStartDrawerSwipe() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mMenuButton.setVisibility(View.VISIBLE);
        mBackButton.setVisibility(View.GONE);
    }

    @Override
    public void stopService() {

    }

    private void logout() {
        Utils.loading_box(MainActivity.this);
        RestClient.getApiService().logout(mSessionManager.getAccessToken(), new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                if (BuildConfig.DEBUG)
                    Log.v("" + getClass().getSimpleName(), "Response> " + s);

                try {
                    JSONObject mObject = new JSONObject(s);

                    int status = mObject.getInt("statusCode");

                    if (ApiResponseFlags.OK.getOrdinal() == status) {
                        FlurryAgent.onEvent("SignOut Mode");
                        new SessionManager(MainActivity.this).logoutUser();
                        GcmMessageHandler.ClearNotification(MainActivity.this);
                    } else {
                        Toast.makeText(MainActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Utils.loading_box_stop();
            }

            @Override
            public void failure(RetrofitError error) {
                try {
                    Utils.loading_box_stop();
                    if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
//                        Toast.makeText(MainActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
                        noInternetDialog();
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Unauthorized.getOrdinal()) {
                        Utils.shopAlterDialog(MainActivity.this, Utils.getErrorMsg(error), true);
                    }
                } catch (Exception ex) {
                    noInternetDialog();
//                    Toast.makeText(MainActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void noInternetDialog() {
        CommonNoInternetDialog.WithActivity(MainActivity.this).Show(getResources().getString(R.string.nointernetconnection), getResources().getString(R.string.tryagain), getResources().getString(R.string.exit),getResources().getString(R.string.callcarrus), new CommonNoInternetDialog.ConfirmationDialogEventsListener() {
            @Override
            public void OnOkButtonPressed() {
                logout();
            }

            @Override
            public void OnCancelButtonPressed() {
                finish();
            }

            @Override
            public void OnNutralButtonPressed() {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + Constants.CONTACT_CARRUS));
                    startActivity(callIntent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
