package com.carrus.fleetowner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.carrus.fleetowner.retrofit.RestClient;
import com.carrus.fleetowner.utils.ApiResponseFlags;
import com.carrus.fleetowner.utils.CommonNoInternetDialog;
import com.carrus.fleetowner.utils.Constants;
import com.carrus.fleetowner.utils.SessionManager;
import com.carrus.fleetowner.utils.Utils;
import com.flurry.android.FlurryAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.carrus.fleetowner.utils.Constants.BIDVALUE;
import static com.carrus.fleetowner.utils.Constants.ID;
import static com.carrus.fleetowner.utils.Constants.MY_FLURRY_APIKEY;
import static com.carrus.fleetowner.utils.Constants.NOTES;
import static com.carrus.fleetowner.utils.Constants.QUOTEID;
import static com.carrus.fleetowner.utils.Constants.TYPE;

/**
 * Created by Sunny on 11/19/15 for Fleet Owner for Fleet Owner for Fleet Owner.
 */
public class QuoteDialogActivity extends BaseActivity {

    private EditText offrbidEdtxt, notesEdtxt;
    private SessionManager sessionManager;
    private Button mSubmitBtn;
    private RadioGroup radioTrackGroup;
    private RadioButton radioButton;
    private String id, quoteId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_quote);
        sessionManager = new SessionManager(this);
        init();
        initializeClickListners();
    }

    private void init() {
        offrbidEdtxt = (EditText) findViewById(R.id.offrbidEdtxt);
        notesEdtxt = (EditText) findViewById(R.id.notesEdtxt);
        mSubmitBtn = (Button) findViewById(R.id.submitBtn);
        radioTrackGroup = (RadioGroup) findViewById(R.id.trackingRadio);
        final Intent mIntent = getIntent();
        id = mIntent.getStringExtra(ID);
        if (mIntent.getBooleanExtra(TYPE, false)) {
            offrbidEdtxt.setText("" + mIntent.getLongExtra(BIDVALUE, 0));
            offrbidEdtxt.setEnabled(false);
            notesEdtxt.setText(mIntent.getStringExtra(NOTES));
            quoteId = mIntent.getStringExtra(QUOTEID);
            mSubmitBtn.setText(getResources().getString(R.string.modify));
        }

    }

    private void initializeClickListners() {


        findViewById(R.id.crossIV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                finish();
            }
        });


        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get selected radio button from radioGroup
                int selectedId = radioTrackGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioButton = (RadioButton) findViewById(selectedId);

//                Toast.makeText(QuoteDialogActivity.this,
//                        radioButton.getText(), Toast.LENGTH_SHORT).show();
                // Close dialog
                if (mSubmitBtn.getText().toString().equalsIgnoreCase(getResources().getString(R.string.quote))) {
                    if (isCostEmpty())
                        addQuotes();
                } else if (mSubmitBtn.getText().toString().equalsIgnoreCase(getResources().getString(R.string.modify))) {
                    if (isCostEmpty())
                        modifyQuotes();
                }

            }
        });
    }

    private boolean isCostEmpty() {
        if (offrbidEdtxt.getText().toString().trim().isEmpty()) {
            offrbidEdtxt.setError(getResources().getString(R.string.quotesamountrequired));
            offrbidEdtxt.requestFocus();
            return false;
        }

        return true;
    }

    private void addQuotes() {
        Utils.loading_box(QuoteDialogActivity.this);
        JSONArray mBidArray = new JSONArray();
        mBidArray.put(id);

        RestClient.getApiService().addQuotes(sessionManager.getAccessToken(), mBidArray.toString(), radioButton.getText().toString().toUpperCase(), offrbidEdtxt.getText().toString().trim(), notesEdtxt.getText().toString().trim(), new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                if (BuildConfig.DEBUG)
                    Log.v("" + getClass().getSimpleName(), "Response> " + s);

                try {
                    JSONObject mObject = new JSONObject(s);
                    int status = mObject.getInt("statusCode");
                    if (ApiResponseFlags.OK.getOrdinal() == status) {
                        FlurryAgent.onEvent("Quotes added Mode");
                        Constants.isTruckQuotesUpdated = true;
                        Constants.isTruckPendingUpdate = true;

                        Toast.makeText(QuoteDialogActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent output = new Intent();
                        setResult(RESULT_OK, output);
                        finish();
                    } else {
                        Toast.makeText(QuoteDialogActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Utils.loading_box_stop();
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.loading_box_stop();
                try {
                    if (BuildConfig.DEBUG)
                        Log.v("error.getKind() >> " + error.getKind(), " MSg >> " + error.getResponse().getStatus());

                    if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                        noInternetDialog();
//                        Utils.shopAlterDialog(QuoteDialogActivity.this, getResources().getString(R.string.nointernetconnection), false);
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Unauthorized.getOrdinal()) {
                        Utils.shopAlterDialog(QuoteDialogActivity.this, Utils.getErrorMsg(error), true);
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Bad_Request.getOrdinal()) {
                        Toast.makeText(QuoteDialogActivity.this, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_Found.getOrdinal()) {
                        Toast.makeText(QuoteDialogActivity.this, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();

                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_MORE_RESULT.getOrdinal()) {
                        Toast.makeText(QuoteDialogActivity.this, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ex) {
                    noInternetDialog();
//                    Utils.shopAlterDialog(QuoteDialogActivity.this, getResources().getString(R.string.nointernetconnection), false);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, MY_FLURRY_APIKEY);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

    private void modifyQuotes() {
        Utils.loading_box(QuoteDialogActivity.this);
        JSONArray mBidArray = new JSONArray();
        mBidArray.put(id);

        RestClient.getApiService().modifyQuotes(sessionManager.getAccessToken(), mBidArray.toString(), radioButton.getText().toString().toUpperCase(), offrbidEdtxt.getText().toString().trim(), notesEdtxt.getText().toString().trim(), quoteId,
                new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        if (BuildConfig.DEBUG)
                            Log.v("" + getClass().getSimpleName(), "Response> " + s);

                        try {
                            JSONObject mObject = new JSONObject(s);
                            int status = mObject.getInt("statusCode");
                            if (ApiResponseFlags.OK.getOrdinal() == status) {
                                FlurryAgent.onEvent("Quotes modify Mode");
                                Constants.isTruckQuotesUpdated = true;
                                Toast.makeText(QuoteDialogActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();
                                Intent output = new Intent();
                                setResult(RESULT_OK, output);
                                finish();
                            } else {
                                Toast.makeText(QuoteDialogActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Utils.loading_box_stop();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Utils.loading_box_stop();
                        try {
                            if (BuildConfig.DEBUG)
                                Log.v("error.getKind() >> " + error.getKind(), " MSg >> " + error.getResponse().getStatus());

                            if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                                noInternetDialog();
//                                Utils.shopAlterDialog(QuoteDialogActivity.this, getResources().getString(R.string.nointernetconnection), false);
                            } else if (error.getResponse().getStatus() == ApiResponseFlags.Unauthorized.getOrdinal()) {
                                Utils.shopAlterDialog(QuoteDialogActivity.this, Utils.getErrorMsg(error), true);
                            } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_Found.getOrdinal()) {
                                Toast.makeText(QuoteDialogActivity.this, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();

                            } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_MORE_RESULT.getOrdinal()) {
                                Toast.makeText(QuoteDialogActivity.this, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception ex) {
                            noInternetDialog();
//                            Utils.shopAlterDialog(QuoteDialogActivity.this, getResources().getString(R.string.nointernetconnection), false);
                        }
                    }
                });
    }

    private void noInternetDialog() {
        CommonNoInternetDialog.WithActivity(QuoteDialogActivity.this).Show(getResources().getString(R.string.nointernetconnection), getResources().getString(R.string.tryagain), getResources().getString(R.string.exit), getResources().getString(R.string.callcarrus), new CommonNoInternetDialog.ConfirmationDialogEventsListener() {
            @Override
            public void OnOkButtonPressed() {
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
