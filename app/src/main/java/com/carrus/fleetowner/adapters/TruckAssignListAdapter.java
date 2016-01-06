package com.carrus.fleetowner.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carrus.fleetowner.R;
import com.carrus.fleetowner.TruckDeatisActivity;
import com.carrus.fleetowner.interfaces.OnLoadMoreListener;
import com.carrus.fleetowner.models.TruckAssignDetails;
import com.carrus.fleetowner.utils.Utils;

import java.text.ParseException;
import java.util.List;

import static com.carrus.fleetowner.utils.Constants.TYPE;
import static com.carrus.fleetowner.utils.Constants.VALUE;

/**
 * Created by Sunny on 12/9/15 for Fleet Owner.
 */
public class TruckAssignListAdapter extends RecyclerView.Adapter {
    //    private String[] mDataset;
    private final Activity mActivity;
    private List<TruckAssignDetails> myList;

    private final int VIEW_ITEM = 1;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private final int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public final TextView mAddressTxtView;
        public final TextView mTypeCargoTxtView;
        public final TextView mWeightTxtView;
        public final TextView mBudgetTxtView;
        public final TextView mDatetxtView;
        public final TextView mMonthTxtView;
        public final TextView mLabelTxtView;

        public ViewHolder(View v) {
            super(v);
            mDatetxtView = (TextView) v.findViewById(R.id.dateTxtView);
            mMonthTxtView = (TextView) v.findViewById(R.id.monthTxtView);
            mAddressTxtView = (TextView) v.findViewById(R.id.addressTxtView);
            mTypeCargoTxtView = (TextView) v.findViewById(R.id.typeCargoTxtView);
            mWeightTxtView = (TextView) v.findViewById(R.id.weightTxtView);
            mBudgetTxtView = (TextView) v.findViewById(R.id.budgetTxtView);
            mLabelTxtView = (TextView) v.findViewById(R.id.budgetTVlabel);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TruckAssignListAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TruckAssignListAdapter(Activity mActivity, List<TruckAssignDetails> myList, RecyclerView recyclerView) {
        this.mActivity = mActivity;
        this.myList = myList;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    R.layout.itemview_truck, parent, false);

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
            try {
                ((ViewHolder) holder).mDatetxtView.setText(Utils.getDate(myList.get(position).getBookingCreatedAt()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                ((ViewHolder) holder).mMonthTxtView.setText(Utils.getMonth(myList.get(position).getBookingCreatedAt()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

//            ((ViewHolder) holder).mNameTxtView.setText(myList.get(position).getDriverName());
            ((ViewHolder) holder).mAddressTxtView.setText(myList.get(position).getPickUp().city + mActivity.getResources().getString(R.string.towithspaces) + myList.get(position).getDropOff().city);
            ((ViewHolder) holder).mTypeCargoTxtView.setText(myList.get(position).getCargo().cargoType.typeCargoName);
            ((ViewHolder) holder).mWeightTxtView.setText(myList.get(position).getCargo().weight + mActivity.getResources().getString(R.string.ton));
            ((ViewHolder) holder).mBudgetTxtView.setText(mActivity.getResources().getString(R.string.rs) + myList.get(position).getAcceptPrice());
            ((ViewHolder) holder).mLabelTxtView.setText(mActivity.getResources().getString(R.string.finalbid));

            ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(VALUE, myList.get(position));
                    Intent intent = new Intent(mActivity, TruckDeatisActivity.class);
                    intent.putExtras(bundle);
                    intent.putExtra(TYPE, "assigment");
                    mActivity.startActivityForResult(intent, 500);

                }
            });

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

}