package com.carrus.fleetowner.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carrus.fleetowner.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 12/9/15.
 */
public class DriverFragment extends Fragment {

    private TextView mWhiteTextView, mBlackTextView;
    private int selectedFlag = 0;
    private List<Fragment> myFragmentList = new ArrayList<>();
    private Bundle bundle = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View convertView = inflater.inflate(R.layout.fragment_mybooking, container, false);
        bundle = this.getArguments();
        init(convertView);
        initializeClickListners();

        return convertView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myFragmentList.add(WhiteDriverFragment.newInstance(0));
        myFragmentList.add(BlackDriverFragment.newInstance(1));

        setSelectionUpcoming(0);
    }

    private void init(View view) {
        mWhiteTextView = (TextView) view.findViewById(R.id.upcomingTextView);
        mWhiteTextView.setText(getResources().getString(R.string.white));
        mBlackTextView = (TextView) view.findViewById(R.id.pastTextView);
        mBlackTextView.setText(getResources().getString(R.string.black));
        final LinearLayout mSearchLayout = (LinearLayout) view.findViewById(R.id.searchLayout);
        mSearchLayout.setVisibility(View.VISIBLE);
        if (bundle != null) {
//            Intent i = new Intent(getActivity(), RatingDialogActivity.class);
//            i.putExtra("bookingid", bundle.getString("id"));
//            startActivity(i);
        }
    }

    private void initializeClickListners() {


        mWhiteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedFlag != 0) {
                    setSelectionUpcoming(0);
                }

            }
        });
        mBlackTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedFlag != 1) {
                    setSeclectionPast(1);
                }

            }
        });
    }

    private void setSelectionUpcoming(int button_id) {
        selectedFlag = 0;
        mWhiteTextView.setBackgroundResource(R.drawable.tab_background);
        mBlackTextView.setBackgroundResource(R.drawable.tab_past_background_white);
        mWhiteTextView.setTextColor(getResources().getColor(R.color.windowBackground));
        mBlackTextView.setTextColor(getResources().getColor(R.color.tabcolor_dark));
        setFragment(myFragmentList.get(0), button_id);

    }

    private void setSeclectionPast(int button_id) {

        selectedFlag = 1;
        mWhiteTextView.setBackgroundResource(R.drawable.tab_upcming_background_white);
        mBlackTextView.setBackgroundResource(R.drawable.tab_background);
        mWhiteTextView.setTextColor(getResources().getColor(R.color.tabcolor_dark));
        mBlackTextView.setTextColor(getResources().getColor(R.color.windowBackground));
        setFragment(myFragmentList.get(1), button_id);

    }


    private void setFragment(Fragment fragment, int button_id) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // If fragment doesn't exist yet, create one
        if (fragment.isAdded()) {
            if (fragment instanceof WhiteDriverFragment) {
                fragmentTransaction.hide(myFragmentList.get(1));
            } else if (fragment instanceof BlackDriverFragment) {
                fragmentTransaction.hide(myFragmentList.get(0));
            }
            fragmentTransaction.show(fragment);
        } else { // re-use the old fragment
            if (fragment instanceof BlackDriverFragment) {
                fragmentTransaction.hide(myFragmentList.get(0));
            }
            fragmentTransaction.add(R.id.bookingcontainer_body, fragment, button_id + "stack_item");
        }

        fragmentTransaction.commit();

    }

}
