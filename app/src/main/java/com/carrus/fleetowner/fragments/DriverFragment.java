package com.carrus.fleetowner.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carrus.fleetowner.R;
import com.carrus.fleetowner.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 12/9/15.
 */
public class DriverFragment extends Fragment {

    private TextView mWhiteTextView, mBlackTextView;
    private int selectedFlag = 0;
    private List<Fragment> myFragmentList = new ArrayList<>();
    private ViewPager vpPager;
    private MyPagerAdapter adapterViewPager;
    private EditText mSearchEdtTxt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View convertView = inflater.inflate(R.layout.fragment_mybooking, container, false);
        init(convertView);
        initializeClickListners();

        return convertView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        adapterViewPager = new MyPagerAdapter(getChildFragmentManager()); //here used child fragment manager
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setSelectionWhite(0);
    }

    private void init(View view) {
        mWhiteTextView = (TextView) view.findViewById(R.id.upcomingTextView);
        mWhiteTextView.setText(getResources().getString(R.string.white));
        mBlackTextView = (TextView) view.findViewById(R.id.pastTextView);
        mBlackTextView.setText(getResources().getString(R.string.black));
        final LinearLayout mSearchLayout = (LinearLayout) view.findViewById(R.id.searchLayout);
        mSearchLayout.setVisibility(View.VISIBLE);
        mSearchEdtTxt=(EditText) view.findViewById(R.id.searchEdtTxt);
        vpPager = (ViewPager) view.findViewById(R.id.vpPager);
        vpPager.setAdapter(adapterViewPager);
        mSearchEdtTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Your piece of code on keyboard search click
                    if (mSearchEdtTxt.getText().toString().trim().isEmpty()) {
                        mSearchEdtTxt.setError(getResources().getString(R.string.enterdrivername));
                        mSearchEdtTxt.requestFocus();
                    } else {
                        Utils.hideSoftKeyboard(getActivity());
//                        if (searchTrackingId()) {
//                            Toast.makeText(getActivity(), "No booking found", Toast.LENGTH_SHORT).show();
//                        }
                        if(selectedFlag==0){

                            ((WhiteDriverFragment)adapterViewPager.getItem(0)).isRefreshView=true;
                            ((WhiteDriverFragment)adapterViewPager.getItem(0)).getMyBooking(mSearchEdtTxt.getText().toString().trim());

                        }else{
                            ((BlackDriverFragment)adapterViewPager.getItem(1)).isRefreshView=true;
                            ((BlackDriverFragment)adapterViewPager.getItem(1)).getMyBooking(mSearchEdtTxt.getText().toString().trim());
                        }

                    }

                    return true;
                }
                return false;
            }
        });
    }

    private void initializeClickListners() {

        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        setSelectionWhite(0);
                        break;

                    case 1:
                        setSeclectionBlack(1);
                        break;
                    default:
                        setSelectionWhite(0);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mWhiteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedFlag != 0) {
                    setSelectionWhite(0);
                }

            }
        });
        mBlackTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedFlag != 1) {
                    setSeclectionBlack(1);
                }

            }
        });
    }

    private void setSelectionWhite(int button_id) {
        selectedFlag = 0;
        mWhiteTextView.setBackgroundResource(R.drawable.tab_background);
        mBlackTextView.setBackgroundResource(R.drawable.tab_past_background_white);
        mWhiteTextView.setTextColor(getResources().getColor(R.color.windowBackground));
        mBlackTextView.setTextColor(getResources().getColor(R.color.tabcolor_dark));
        vpPager.setCurrentItem(button_id);

    }

    private void setSeclectionBlack(int button_id) {

        selectedFlag = 1;
        mWhiteTextView.setBackgroundResource(R.drawable.tab_upcming_background_white);
        mBlackTextView.setBackgroundResource(R.drawable.tab_background);
        mWhiteTextView.setTextColor(getResources().getColor(R.color.tabcolor_dark));
        mBlackTextView.setTextColor(getResources().getColor(R.color.windowBackground));
        vpPager.setCurrentItem(button_id);

    }


    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return WhiteDriverFragment.newInstance(0);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return BlackDriverFragment.newInstance(1);
                default:
                    return WhiteDriverFragment.newInstance(0);
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }
}
