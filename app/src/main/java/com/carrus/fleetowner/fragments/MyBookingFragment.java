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

/**
 * Created by Sunny on 10/30/15 for Fleet Owner for Fleet Owner.
 */
public class MyBookingFragment extends Fragment {

    private TextView mUpComingTextView, mPastTextView;
    private int selectedFlag = 0;
    private ViewPager vpPager;
    private MyPagerAdapter adapterViewPager;

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
        setSelectionUpcoming();
    }

    private void init(View view) {
        mUpComingTextView = (TextView) view.findViewById(R.id.upcomingTextView);
        mPastTextView = (TextView) view.findViewById(R.id.pastTextView);
        vpPager = (ViewPager) view.findViewById(R.id.vpPager);
//         adapterViewPager = new MyPagerAdapter(getChildFragmentManager());
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
                        setSelectionUpcoming();
                        break;

                    case 1:
                        setSeclectionPast();
                        break;
                    default:
                        setSelectionUpcoming();

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mUpComingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedFlag != 0) {
                    setSelectionUpcoming();
                }

            }
        });
        mPastTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedFlag != 1) {
                    setSeclectionPast();
                }

            }
        });
    }

    private void setSelectionUpcoming() {
        selectedFlag = 0;
        mUpComingTextView.setBackgroundResource(R.drawable.tab_background);
        mPastTextView.setBackgroundResource(R.drawable.tab_past_background_white);
        mUpComingTextView.setTextColor(getResources().getColor(R.color.windowBackground));
        mPastTextView.setTextColor(getResources().getColor(R.color.tabcolor_dark));
        vpPager.setCurrentItem(0);
    }

    private void setSeclectionPast() {

        selectedFlag = 1;
        mUpComingTextView.setBackgroundResource(R.drawable.tab_upcming_background_white);
        mPastTextView.setBackgroundResource(R.drawable.tab_background);
        mUpComingTextView.setTextColor(getResources().getColor(R.color.tabcolor_dark));
        mPastTextView.setTextColor(getResources().getColor(R.color.windowBackground));
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
                    return UpComingFragment.newInstance();
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return PastFragment.newInstance();
                default:
                    return UpComingFragment.newInstance();
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }

//    private void setFragment(Fragment fragment, int button_id) {
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        // If fragment doesn't exist yet, create one
//        if (fragment.isAdded()) {
//            if (fragment instanceof UpComingFragment) {
//                fragmentTransaction.hide(myFragmentList.get(1));
//            } else if (fragment instanceof PastFragment) {
//                fragmentTransaction.hide(myFragmentList.get(0));
//            }
//            fragmentTransaction.show(fragment);
//        } else { // re-use the old fragment
//            if (fragment instanceof PastFragment) {
//                fragmentTransaction.hide(myFragmentList.get(0));
//            }
//            fragmentTransaction.add(R.id.bookingcontainer_body, fragment, button_id + "stack_item");
//        }
//
//        fragmentTransaction.commit();
//
//    }

}
