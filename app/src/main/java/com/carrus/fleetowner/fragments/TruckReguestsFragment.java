package com.carrus.fleetowner.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carrus.fleetowner.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 12/9/15 for Fleet Owner for Fleet Owner for Fleet Owner.
 */
public class TruckReguestsFragment extends Fragment {

    private TextView mNewRequestTV, mPendingQuotesTV, mPendingAssignTV;
    private int selectedFlag = 0;
    private List<Fragment> myFragmentList = new ArrayList<>();
    private ViewPager vpPager;
    private MyPagerAdapter adapterViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View convertView = inflater.inflate(R.layout.fragment_truck, container, false);
        init(convertView);
        initializeClickListners();

        return convertView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setSelectionNewReuest();
    }

    private void init(View view) {
        mNewRequestTV = (TextView) view.findViewById(R.id.newrequestTV);
        mPendingQuotesTV = (TextView) view.findViewById(R.id.pendingquotesTV);
        mPendingAssignTV = (TextView) view.findViewById(R.id.pendingassignTV);
        vpPager = (ViewPager) view.findViewById(R.id.vpPager);
        vpPager.setOffscreenPageLimit(3);
        vpPager.setAdapter(adapterViewPager);
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
                        setSelectionNewReuest();
                        break;

                    case 1:
                        setSeclectionQuotes();
                        break;

                    case 2:
                        setSeclectionAssign();
                        break;

                    default:
                        setSelectionNewReuest();

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mNewRequestTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedFlag != 0) {
                    setSelectionNewReuest();
                }

            }
        });
        mPendingQuotesTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedFlag != 1) {
                    setSeclectionQuotes();
                }

            }
        });

        mPendingAssignTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedFlag != 2) {
                    setSeclectionAssign();
                }

            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        adapterViewPager = new MyPagerAdapter(getChildFragmentManager()); //here used child fragment manager
    }

    private void setSelectionNewReuest() {

        selectedFlag = 0;
        mNewRequestTV.setBackgroundResource(R.drawable.tab_background);
        mPendingQuotesTV.setBackgroundResource(R.drawable.tab_square_background_white);
        mPendingAssignTV.setBackgroundResource(R.drawable.tab_past_background_white);
        mNewRequestTV.setTextColor(getResources().getColor(R.color.windowBackground));
        mPendingQuotesTV.setTextColor(getResources().getColor(R.color.tabcolor_dark));
        mPendingAssignTV.setTextColor(getResources().getColor(R.color.tabcolor_dark));
        vpPager.setCurrentItem(0);

    }

    private void setSeclectionQuotes() {

        selectedFlag = 1;
        mNewRequestTV.setBackgroundResource(R.drawable.tab_upcming_background_white);
        mPendingAssignTV.setBackgroundResource(R.drawable.tab_past_background_white);
        mPendingQuotesTV.setBackgroundResource(R.drawable.tab_background);
        mNewRequestTV.setTextColor(getResources().getColor(R.color.tabcolor_dark));
        mPendingAssignTV.setTextColor(getResources().getColor(R.color.tabcolor_dark));
        mPendingQuotesTV.setTextColor(getResources().getColor(R.color.windowBackground));
        vpPager.setCurrentItem(1);

    }

    private void setSeclectionAssign() {

        selectedFlag = 2;
        mNewRequestTV.setBackgroundResource(R.drawable.tab_upcming_background_white);
        mPendingQuotesTV.setBackgroundResource(R.drawable.tab_square_background_white);
        mPendingAssignTV.setBackgroundResource(R.drawable.tab_background);
        mNewRequestTV.setTextColor(getResources().getColor(R.color.tabcolor_dark));
        mPendingQuotesTV.setTextColor(getResources().getColor(R.color.tabcolor_dark));
        mPendingAssignTV.setTextColor(getResources().getColor(R.color.windowBackground));
        vpPager.setCurrentItem(2);

    }

//    private void setFragment(Fragment fragment, int button_id) {
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        // If fragment doesn't exist yet, create one
//        if (fragment.isAdded()) {
//            if (fragment instanceof TruckPendingReqFragment) {
//                fragmentTransaction.hide(myFragmentList.get(1));
//                fragmentTransaction.hide(myFragmentList.get(2));
//            } else if (fragment instanceof TruckQuotesFragment) {
//                fragmentTransaction.hide(myFragmentList.get(0));
//                fragmentTransaction.hide(myFragmentList.get(2));
//            } else if (fragment instanceof TruckAssignFragment) {
//                fragmentTransaction.hide(myFragmentList.get(0));
//                fragmentTransaction.hide(myFragmentList.get(1));
//            }
//            fragmentTransaction.show(fragment);
//        } else { // re-use the old fragment
//            if (fragment instanceof TruckQuotesFragment) {
//                fragmentTransaction.hide(myFragmentList.get(0));
//                if (myFragmentList.get(2).isAdded())
//                    fragmentTransaction.hide(myFragmentList.get(2));
//            } else if (fragment instanceof TruckAssignFragment) {
//                fragmentTransaction.hide(myFragmentList.get(0));
//                if (myFragmentList.get(1).isAdded())
//                    fragmentTransaction.hide(myFragmentList.get(1));
//            }
//            fragmentTransaction.add(R.id.bookingcontainer_body, fragment, button_id + "stack_item");
//        }
//
//        fragmentTransaction.commit();
//
//    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private final int NUM_ITEMS = 3;

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
                    return TruckPendingReqFragment.newInstance();
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return TruckQuotesFragment.newInstance();
                case 2: // Fragment # 0 - This will show FirstFragment different title
                    return TruckAssignFragment.newInstance();
                default:
                    return TruckPendingReqFragment.newInstance();
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }

}