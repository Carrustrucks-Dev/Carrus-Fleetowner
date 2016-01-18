package com.carrus.fleetowner;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.carrus.fleetowner.models.PartnerModel;
import com.carrus.fleetowner.models.PartnerShip;
import com.carrus.fleetowner.models.StateCityInfo;
import com.carrus.fleetowner.models.StateCityModel;
import com.carrus.fleetowner.retrofit.RestClient;
import com.carrus.fleetowner.utils.ApiResponseFlags;
import com.carrus.fleetowner.utils.CommonNoInternetDialog;
import com.carrus.fleetowner.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Sunny on 1/15/16 for Fleet Owner.
 */
public class SignUpActivity extends BaseActivity {

    private ImageView mBackButton;
    private PartnerModel partnerModel;
    private Spinner mParnterSpinner, stateSpinner;
    private TextView mTypeCompanyTxtView, mStateTxtView;
    private List<PartnerShip> mPartnerList;
    private boolean isParntrClick = false;
    private StateCityModel mStateCityModel;
    private List<String> states=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();
        initializeListener();
        parseStates();
        getPartnerShip();
    }

    private void initView() {
        mBackButton = (ImageView) findViewById(R.id.menu_back_btn);
        mBackButton.setVisibility(View.VISIBLE);
        TextView headerTxtView = (TextView) findViewById(R.id.headerTxtView);
        headerTxtView.setText(getResources().getString(R.string.signup_header));
        mParnterSpinner = (Spinner) findViewById(R.id.partnerSpinner);
        stateSpinner = (Spinner) findViewById(R.id.stateSpinner);
        mTypeCompanyTxtView = (TextView) findViewById(R.id.typeOfCompanyTV);
        mStateTxtView = (TextView) findViewById(R.id.stateTV);
    }

    private void initializeListener() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mParnterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    mTypeCompanyTxtView.setText(mPartnerList.get(position).getPartnershipName());
                    mTypeCompanyTxtView.setTextColor(getResources().getColor(android.R.color.black));
                } else {
                    mTypeCompanyTxtView.setText(getResources().getString(R.string.typecompany));
                    mTypeCompanyTxtView.setTextColor(getResources().getColor(R.color.gray_text));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    mStateTxtView.setText(states.get(position));
                    mStateTxtView.setTextColor(getResources().getColor(android.R.color.black));
                }else{
                    mStateTxtView.setText(getResources().getString(R.string.typecompany));
                    mStateTxtView.setTextColor(getResources().getColor(R.color.gray_text));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public String loadJSONFromAsset() {
        AssetManager assetManager = getResources().getAssets();
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(assetManager.open(
                    "StateCity.json")));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close(); // stop reading
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    private void parseStates(){
        Gson gson = new Gson();
        states.add("");
        mStateCityModel = gson.fromJson(loadJSONFromAsset(), StateCityModel.class);
        for (StateCityInfo mStateCityInfo: mStateCityModel.data){
            states.add(mStateCityInfo.state);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(SignUpActivity.this,
                android.R.layout.simple_spinner_item, states);
        stateSpinner.setAdapter(adapter);
    }

    private void getPartnerShip() {
        Utils.loading_box(SignUpActivity.this);
        RestClient.getApiService().getPartnerShip(new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                if (BuildConfig.DEBUG)
                    Log.v("" + getClass().getSimpleName(), "Response> " + s);
                try {
                    JSONObject mObject = new JSONObject(s);

                    int status = mObject.getInt("statusCode");

                    if (ApiResponseFlags.OK.getOrdinal() == status) {

                        Gson gson = new Gson();
                        partnerModel = gson.fromJson(s, PartnerModel.class);
                        mPartnerList = new ArrayList<>();
                        mPartnerList.add(new PartnerShip());
                        for (PartnerShip mPartnerShip : partnerModel.getData()) {
                            if (mPartnerShip.getStatus().equalsIgnoreCase("ACTIVATE")) {
                                mPartnerList.add(mPartnerShip);
                            }
                        }

                        ArrayAdapter<PartnerShip> adapter = new ArrayAdapter<PartnerShip>(SignUpActivity.this,
                                android.R.layout.simple_spinner_item, mPartnerList);
                        mParnterSpinner.setAdapter(adapter);

                    } else {
                        Toast.makeText(SignUpActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();
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

                    noInternetDialog();

                } catch (Exception ex) {
                    noInternetDialog();
                }
            }
        });
    }

    private void noInternetDialog() {
        CommonNoInternetDialog.WithActivity(SignUpActivity.this).Show(getResources().getString(R.string.nointernetconnection), getResources().getString(R.string.tryagain), getResources().getString(R.string.exit), new CommonNoInternetDialog.ConfirmationDialogEventsListener() {
            @Override
            public void OnOkButtonPressed() {
                getPartnerShip();
            }

            @Override
            public void OnCancelButtonPressed() {
                finish();
            }
        });
    }
}
