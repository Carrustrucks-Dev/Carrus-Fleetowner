package com.carrus.fleetowner;

import android.content.Intent;
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
import com.carrus.fleetowner.utils.ConnectionDetector;
import com.carrus.fleetowner.utils.SessionManager;
import com.carrus.fleetowner.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.carrus.fleetowner.utils.Constants.ID;
import static com.carrus.fleetowner.utils.Constants.BIDVALUE;
import static com.carrus.fleetowner.utils.Constants.NOTES;
import static com.carrus.fleetowner.utils.Constants.TYPE;
import static com.carrus.fleetowner.utils.Constants.QUOTEID;

/**
 * Created by Sunny on 11/19/15.
 */
public class QuoteDialogActivity extends BaseActivity {

    private EditText offrbidEdtxt, notesEdtxt;
    private SessionManager sessionManager;
    private Button mSubmitBtn;
    private float userRating = 0;
    private ConnectionDetector mConnectionDetector;
    private RadioGroup radioTrackGroup;
    private RadioButton radioButton;
    private String id, quoteId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_quote);
        sessionManager = new SessionManager(this);
        mConnectionDetector = new ConnectionDetector(this);
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
            notesEdtxt.setText(mIntent.getStringExtra(NOTES));
            quoteId=mIntent.getStringExtra(QUOTEID);
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

                Toast.makeText(QuoteDialogActivity.this,
                        radioButton.getText(), Toast.LENGTH_SHORT).show();
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
                Log.v("" + getClass().getSimpleName(), "Response> " + s);

                try {
                    JSONObject mObject = new JSONObject(s);
                    int status = mObject.getInt("statusCode");
                    if (ApiResponseFlags.OK.getOrdinal() == status) {
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
                    Log.v("error.getKind() >> " + error.getKind(), " MSg >> " + error.getResponse().getStatus());

                    if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                        Toast.makeText(QuoteDialogActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Unauthorized.getOrdinal()) {
                        Utils.shopAlterDialog(QuoteDialogActivity.this, Utils.getErrorMsg(error), true);
                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_Found.getOrdinal()) {
                        Toast.makeText(QuoteDialogActivity.this, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();

                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_MORE_RESULT.getOrdinal()) {
                        Toast.makeText(QuoteDialogActivity.this, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ex) {
                    Toast.makeText(QuoteDialogActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void modifyQuotes() {
        Utils.loading_box(QuoteDialogActivity.this);
        JSONArray mBidArray = new JSONArray();
        mBidArray.put(id);

        RestClient.getApiService().modifyQuotes(sessionManager.getAccessToken(), mBidArray.toString(), radioButton.getText().toString().toUpperCase(), offrbidEdtxt.getText().toString().trim(), notesEdtxt.getText().toString().trim(), quoteId,
                new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        Log.v("" + getClass().getSimpleName(), "Response> " + s);

                        try {
                            JSONObject mObject = new JSONObject(s);
                            int status = mObject.getInt("statusCode");
                            if (ApiResponseFlags.OK.getOrdinal() == status) {
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
                            Log.v("error.getKind() >> " + error.getKind(), " MSg >> " + error.getResponse().getStatus());

                            if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                                Toast.makeText(QuoteDialogActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
                            } else if (error.getResponse().getStatus() == ApiResponseFlags.Unauthorized.getOrdinal()) {
                                Utils.shopAlterDialog(QuoteDialogActivity.this, Utils.getErrorMsg(error), true);
                            } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_Found.getOrdinal()) {
                                Toast.makeText(QuoteDialogActivity.this, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();

                            } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_MORE_RESULT.getOrdinal()) {
                                Toast.makeText(QuoteDialogActivity.this, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception ex) {
                            Toast.makeText(QuoteDialogActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
