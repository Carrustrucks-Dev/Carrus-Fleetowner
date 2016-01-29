package com.carrus.fleetowner;

/**
 * Created by Ravi on 29/07/15 for Fleet Owner for Fleet Owner for Fleet Owner.
 */

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carrus.fleetowner.adapters.NavigationDrawerAdapter;
import com.carrus.fleetowner.interfaces.ClickListener;
import com.carrus.fleetowner.models.NavDrawerItem;
import com.carrus.fleetowner.utils.CircleTransform;
import com.carrus.fleetowner.utils.SessionManager;
import com.carrus.fleetowner.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FragmentDrawer extends Fragment {

    private static String TAG = FragmentDrawer.class.getSimpleName();

    //    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View containerView;
    private static String[] titles = null;
    private TypedArray icons = null;
    private FragmentDrawerListener drawerListener;
    private SessionManager mSessionManager;
    private ImageView mProfileIV;

    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    private List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();

        // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(titles[i]);
            navItem.setIcon(icons.getResourceId(i, 0));
            data.add(navItem);
        }

        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // drawer labels
        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);

        // drawer icons
        Resources res = getResources();
        icons = res.obtainTypedArray(R.array.nav_drawer_icons);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        mProfileIV = (ImageView) layout.findViewById(R.id.profileIV);
        mSessionManager = new SessionManager(getActivity());
        loadImage();
        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        final TextView mCompanyTxtView = (TextView) layout.findViewById(R.id.companyTxtView);
        mCompanyTxtView.setText(mSessionManager.getCompanyName());
        final TextView mUserNameTxtView = (TextView) layout.findViewById(R.id.usernameTxtView);
        mUserNameTxtView.setText(mSessionManager.getName());
        NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(position);
                mDrawerLayout.closeDrawer(containerView);
            }

        }));

        LinearLayout mHeaderLayout = (LinearLayout) layout.findViewById(R.id.nav_header_container);

        mHeaderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(containerView);
                drawerListener.onHeaderSelected();
            }
        });

        layout.findViewById(R.id.termsTxtView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(containerView);
                drawerListener.onDrawerItemSelected(55);
            }
        });

        return layout;
    }

    public void loadImage() {
        if (mSessionManager.getProfilePic() != null && !mSessionManager.getProfilePic().isEmpty())
            Picasso.with(getActivity()).load(mSessionManager.getProfilePic()).placeholder(R.mipmap.icon_placeholder).error(R.mipmap.icon_placeholder).resize((int) Utils.convertDpToPixel(70, getActivity()), (int) Utils.convertDpToPixel(70, getActivity())).transform(new CircleTransform()).into(mProfileIV);
    }

    public void setUp(DrawerLayout drawerLayout) {

        containerView = getActivity().findViewById(R.id.fragment_navigation_drawer);
        mDrawerLayout = drawerLayout;

    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private final GestureDetector gestureDetector;
        private final ClickListener clickListener;

        public RecyclerTouchListener(Context context, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
//                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                }

            });
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept){

        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildLayoutPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

    }

    public interface FragmentDrawerListener {
        void onDrawerItemSelected(int position);

        void onHeaderSelected();
    }
}
