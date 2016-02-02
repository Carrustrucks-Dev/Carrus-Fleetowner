package com.carrus.fleetowner.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.carrus.fleetowner.R;
import com.carrus.fleetowner.interfaces.OnLoadMoreListener;
import com.carrus.fleetowner.models.Datum;
import com.carrus.fleetowner.utils.CircleTransform;
import com.carrus.fleetowner.utils.Utils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Sunny on 12/9/15 for Fleet Owner for Fleet Owner for Fleet Owner.
 */
public class DriverListAdapter extends RecyclerView.Adapter {
    //    private String[] mDataset;
    private final Activity mActivity;
    private List<Datum> myList;

    private final int VIEW_ITEM = 1;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private final int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isTouchable = false;
    private int selectedPos = -1;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public final TextView mNameTxtView;
        public final TextView vehiclenoTxtView;
        public final TextView licenseexpTxtView;
        public final ImageView mProfileIV;
        public final ImageView mCallBtnIV;
        public final RatingBar mRatingBar;
        public final LinearLayout mMainLayout;

        public ViewHolder(View v) {
            super(v);
            mNameTxtView = (TextView) v.findViewById(R.id.nameTxtView);
            vehiclenoTxtView = (TextView) v.findViewById(R.id.vehiclenoTxtView);
            licenseexpTxtView = (TextView) v.findViewById(R.id.licenseexpTxtView);
            mProfileIV = (ImageView) v.findViewById(R.id.profileIV);
            mCallBtnIV = (ImageView) v.findViewById(R.id.callBtnIV);
            mRatingBar = (RatingBar) v.findViewById(R.id.driverRating);
            mMainLayout = (LinearLayout) v.findViewById(R.id.mainLayout);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DriverListAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DriverListAdapter(Activity mActivity, List<Datum> myList, RecyclerView recyclerView, boolean isTouchable) {
        this.mActivity = mActivity;
        this.myList = myList;
        this.isTouchable = isTouchable;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager
                            .findLastVisibleItemPosition();
                    if (!loading
                            && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
//        // create a new view
//        View v = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.itemview_booking, parent, false);
//        // set the view's size, margins, paddings and layout parameters
//        ViewHolder vh = new ViewHolder(v);

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.itemview_driver, parent, false);

            vh = new ViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progress_item, parent, false);

            vh = new ProgressViewHolder(v);
        }

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.mTextView.setText(mDataset[position
        if (holder instanceof ViewHolder) {

            if (selectedPos != -1 && selectedPos == position) {
                ((ViewHolder) holder).mMainLayout.setBackgroundColor(Utils.getColor(mActivity, R.color.list_selected));
            } else {
                ((ViewHolder) holder).mMainLayout.setBackgroundColor(Utils.getColor(mActivity, R.color.windowBackground));
            }

            ((ViewHolder) holder).mNameTxtView.setText(Character.toUpperCase(myList.get(position).getDriverName().charAt(0)) + myList.get(position).getDriverName().substring(1));
//            ((ViewHolder) holder).vehiclenoTxtView.setVisibility(View.GONE);
            ((ViewHolder) holder).vehiclenoTxtView.setText(myList.get(position).getTrucks().toString().replace("[", "")
                    .replace("]", ""));
            try {
                ((ViewHolder) holder).licenseexpTxtView.setText(Utils.getMonthYear(myList.get(position).getDrivingLicense().getValidity()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ((ViewHolder) holder).mRatingBar.setRating(Float.valueOf(myList.get(position).getRating()));
            ((ViewHolder) holder).mRatingBar.setFocusable(false);

            ((ViewHolder) holder).mCallBtnIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + myList.get(position).getPhoneNumber()));
                        mActivity.startActivity(callIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            if (isTouchable)
                ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedPos = position;
                        notifyDataSetChanged();
                    }
                });
            if (myList.get(position).getProfilePicture() != null) {
                Picasso.with(mActivity).load(myList.get(position).getProfilePicture().getThumb()).transform(new CircleTransform()).into(((ViewHolder) holder).mProfileIV);
            }

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myList != null ? myList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        int VIEW_PROG = 0;
        return myList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public final ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded() {
        loading = false;
    }

    public String getSelectedDriver() {
        if (selectedPos != -1)
            return myList.get(selectedPos).getId();

        return null;
    }

}