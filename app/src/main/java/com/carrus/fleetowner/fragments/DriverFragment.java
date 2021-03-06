package com.carrus.fleetowner.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
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

/**
 * Created by Sunny on 12/9/15 for Fleet Owner for Fleet Owner for Fleet Owner.
 */
public class DriverFragment extends Fragment {

    private TextView mWhiteTextView, mBlackTextView;
    private int selectedFlag = 0;
    private ViewPager vpPager;
    private MyPagerAdapter adapterViewPager;
    public EditText mSearchEdtTxt;
    private boolean isSearchWhite = false;
    private boolean isSearchBlack = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View convertView = inflater.inflate(R.layout.fragment_mybooking, container, false);
        init(convertView);
        initializeClickListners();

        return convertView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        adapterViewPager = new MyPagerAdapter(getChildFragmentManager()); //here used child fragment manager
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setSelectionWhite();
    }

    private void init(View view) {
        mWhiteTextView = (TextView) view.findViewById(R.id.upcomingTextView);
        mWhiteTextView.setText(getResources().getString(R.string.white));
        mBlackTextView = (TextView) view.findViewById(R.id.pastTextView);
        mBlackTextView.setText(getResources().getString(R.string.black));
        final LinearLayout mSearchLayout = (LinearLayout) view.findViewById(R.id.searchLayout);
        mSearchLayout.setVisibility(View.VISIBLE);
        mSearchEdtTxt = (EditText) view.findViewById(R.id.searchEdtTxt);
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
                        performSearch();
                    }

                    return true;
                }
                return false;
            }
        });


        mSearchEdtTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub

                if (s.length() == 0 && isSearchWhite) {
                    isSearchWhite = false;
                    performSearch();
                } else if (s.length() == 0 && isSearchBlack) {
                    isSearchBlack = false;
                    performSearch();
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


    private void performSearch() {
        Utils.hideSoftKeyboard(getActivity());
//                        if (searchTrackingId()) {
//                            Toast.makeText(getActivity(), "No booking found", Toast.LENGTH_SHORT).show();
//                        }
        if (selectedFlag == 0) {
            if (getActiveFragment(vpPager, 0) != null) {
                if(!mSearchEdtTxt.getText().toString().trim().isEmpty())
                isSearchWhite = true;
                ((WhiteDriverFragment) getActiveFragment(vpPager, 0)).isRefreshView = true;
                ((WhiteDriverFragment) getActiveFragment(vpPager, 0)).getMyBooking(mSearchEdtTxt.getText().toString().trim());
            }
        } else {
            if (getActiveFragment(vpPager, 1) != null) {
                if(!mSearchEdtTxt.getText().toString().trim().isEmpty())
                isSearchBlack = true;
                ((BlackDriverFragment) getActiveFragment(vpPager, 1)).isRefreshView = true;
                ((BlackDriverFragment) getActiveFragment(vpPager, 1)).getMyBooking(mSearchEdtTxt.getText().toString().trim());
            }
        }
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
                        setSelectionWhite();
                        break;

                    case 1:
                        setSeclectionBlack();
                        break;
                    default:
                        setSelectionWhite();

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
                    setSelectionWhite();
                }

            }
        });
        mBlackTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedFlag != 1) {
                    setSeclectionBlack();
                }

            }
        });
    }

    private void setSelectionWhite() {

        mSearchEdtTxt.setText("");
        Utils.hideSoftKeyboard(getActivity());
        mSearchEdtTxt.setError(null);
        selectedFlag = 0;
        mWhiteTextView.setBackgroundResource(R.drawable.tab_background);
        mBlackTextView.setBackgroundResource(R.drawable.tab_past_background_white);
        mWhiteTextView.setTextColor(Utils.getColor(getActivity(), R.color.windowBackground));
        mBlackTextView.setTextColor(Utils.getColor(getActivity(), R.color.tabcolor_dark));
        vpPager.setCurrentItem(0);

    }

    private void setSeclectionBlack() {
        mSearchEdtTxt.setText("");
        Utils.hideSoftKeyboard(getActivity());
        mSearchEdtTxt.setError(null);
        selectedFlag = 1;
        mWhiteTextView.setBackgroundResource(R.drawable.tab_upcming_background_white);
        mBlackTextView.setBackgroundResource(R.drawable.tab_background);
        mWhiteTextView.setTextColor(Utils.getColor(getActivity(), R.color.tabcolor_dark));
        mBlackTextView.setTextColor(Utils.getColor(getActivity(), R.color.windowBackground));
        vpPager.setCurrentItem(1);

    }


    public class MyPagerAdapter extends FragmentPagerAdapter {
        private final int NUM_ITEMS = 2;

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
                    return WhiteDriverFragment.newInstance();
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return BlackDriverFragment.newInstance();
                default:
                    return WhiteDriverFragment.newInstance();
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }

    private Fragment getActiveFragment(ViewPager container, int position) {
        String name = makeFragmentName(container.getId(), position);
        return getChildFragmentManager().findFragmentByTag(name);
    }

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }
}
