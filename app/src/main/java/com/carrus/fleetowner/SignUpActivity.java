package com.carrus.fleetowner;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.carrus.fleetowner.gcm.DeviceTokenFetcher;
import com.carrus.fleetowner.models.StateCityInfo;
import com.carrus.fleetowner.models.StateCityModel;
import com.carrus.fleetowner.multivaluesspinner.MultiSpinner;
import com.carrus.fleetowner.retrofit.RestClient;
import com.carrus.fleetowner.utils.ApiResponseFlags;
import com.carrus.fleetowner.utils.CommonNoInternetDialog;
import com.carrus.fleetowner.utils.SessionManager;
import com.carrus.fleetowner.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.carrus.fleetowner.utils.Constants.COUNTRYNAME;
import static com.carrus.fleetowner.utils.Constants.SENDER_ID;

/**
 * Created by Sunny on 1/15/16 for Fleet Owner.
 */
public class SignUpActivity extends BaseActivity {

    private ImageView mBackButton;
    private MultiSpinner mAreaOprerationSpinner, mTypeOfCargo;
    private Spinner stateSpinner;
    private TextView mStateTxtView;
    private StateCityModel mStateCityModel;
    private List<String> states;
    private List<String> areaOprationList;
    private RadioButton mShipperRadioBtn, mBrokerRadioBtn;
    private String USERTYPE = "FLEET_OWNER";
    private EditText mFullNameET, mPasswordET, mCnfrmPasswordET, mPhoneNumberET, mCompanyNameET, mAddressET, mPinCodeET, mEmailET, mCityET;
    private SessionManager sessionManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        sessionManager = new SessionManager(this);
        getDeviceToken();
        initView();
        initializeListener();
        parseStates();

    }

    private void initView() {
        mBackButton = (ImageView) findViewById(R.id.menu_back_btn);
        mBackButton.setVisibility(View.VISIBLE);
        TextView headerTxtView = (TextView) findViewById(R.id.headerTxtView);
        headerTxtView.setText(getResources().getString(R.string.signup_header));
        mAreaOprerationSpinner = (MultiSpinner) findViewById(R.id.areaOfOperationSpinner);
        mTypeOfCargo = (MultiSpinner) findViewById(R.id.typeOfCargoSpinner);
        stateSpinner = (Spinner) findViewById(R.id.stateSpinner);
        mStateTxtView = (TextView) findViewById(R.id.stateTV);

        mShipperRadioBtn = (RadioButton) findViewById(R.id.shipperRadioBtn);
        mBrokerRadioBtn = (RadioButton) findViewById(R.id.brokerRadioBtn);
        mFullNameET = (EditText) findViewById(R.id.fullnameET);
        mPasswordET = (EditText) findViewById(R.id.passwordET);
        mPhoneNumberET = (EditText) findViewById(R.id.phoneNumberET);
        mCompanyNameET = (EditText) findViewById(R.id.companyET);
        mAddressET = (EditText) findViewById(R.id.addressET);
        mPinCodeET = (EditText) findViewById(R.id.zipCodeET);
        mCnfrmPasswordET = (EditText) findViewById(R.id.cnfrmPasswordET);
        mEmailET = (EditText) findViewById(R.id.emailET);
        mCityET = (EditText) findViewById(R.id.cityET);
    }

    private void initializeListener() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    mStateTxtView.setText(states.get(position));
                    mStateTxtView.setTextColor(getResources().getColor(android.R.color.black));
                    mStateTxtView.setError(null);

                } else {
                    mStateTxtView.setText(getResources().getString(R.string.state));
                    mStateTxtView.setTextColor(getResources().getColor(R.color.gray_text));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mShipperRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mBrokerRadioBtn.setChecked(false);
                    USERTYPE = "FLEET_OWNER";
                }

            }
        });

        mBrokerRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mShipperRadioBtn.setChecked(false);
                    USERTYPE = "BROKER";
                }

            }
        });

        findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFieldFilled()) {
//                    Toast.makeText(SignUpActivity.this, "Field Filled", Toast.LENGTH_SHORT).show();
//                    register();
                }
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


    private void parseStates() {
        states = new ArrayList<>();
        Gson gson = new Gson();
        //states.add("");
        mStateCityModel = gson.fromJson(loadJSONFromAsset(), StateCityModel.class);
        for (StateCityInfo mStateCityInfo : mStateCityModel.data) {
            states.add(mStateCityInfo.state);
        }
        Set<String> uniqueList = new HashSet<String>(states);
        states = new ArrayList<String>(uniqueList); //let GC will doing free memory

        //Sorting
        Collections.sort(states, new Comparator<String>() {
            @Override
            public int compare(String obj1, String obj2) {

                return obj1.compareTo(obj2);
            }
        });

        areaOprationList=new ArrayList<>(states);
        areaOprationList.remove(0);
       final TreeMap<String, Boolean> items = new TreeMap<>();
        for(String item : areaOprationList) {
            items.put(item, Boolean.FALSE);
        }

        mAreaOprerationSpinner.setItems(items, getResources().getString(R.string.areaoprations),new MultiSpinner.MultiSpinnerListener() {

            @Override
            public void onItemsSelected(boolean[] selected) {

                // your operation with code...
                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {
                        Log.i("TAG", i + " : " + areaOprationList.get(i));
                    }
                }
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(SignUpActivity.this,
                android.R.layout.simple_spinner_item, states);
        stateSpinner.setAdapter(adapter);
    }


    private void noInternetDialog() {
        CommonNoInternetDialog.WithActivity(SignUpActivity.this).Show(getResources().getString(R.string.nointernetconnection), getResources().getString(R.string.tryagain), getResources().getString(R.string.exit), new CommonNoInternetDialog.ConfirmationDialogEventsListener() {
            @Override
            public void OnOkButtonPressed() {
                getTypeCargo();
            }

            @Override
            public void OnCancelButtonPressed() {
                finish();
            }
        });
    }


    private boolean isFieldFilled() {
        if (sessionManager.getDeviceToken().isEmpty()) {
            getDeviceToken();
        }

        if (checkETEmpty(mFullNameET))
            return false;

        else if (checkETEmpty(mEmailET))
            return false;
        else if (checkETEmpty(mCompanyNameET))
            return false;
         else if (checkETEmpty(mPasswordET))
            return false;
        else if (checkETEmpty(mCnfrmPasswordET))
            return false;
        else if (checkETEmpty(mPhoneNumberET))
            return false;
        else if (checkETEmpty(mAddressET))
            return false;
        else if (mStateTxtView.getText().toString().equalsIgnoreCase(getResources().getString(R.string.state))) {
            mStateTxtView.setError(getResources().getString(R.string.select_value));
            mStateTxtView.requestFocus();
            return false;
        }else if (checkETEmpty(mCityET))
            return false;

        else if (checkETEmpty(mPinCodeET))
            return false;
        else if (!mPasswordET.getText().toString().trim().equalsIgnoreCase(mCnfrmPasswordET.getText().toString().trim())) {
            mCnfrmPasswordET.setError(getResources().getString(R.string.passwdnotmacth));
            mCnfrmPasswordET.requestFocus();
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmailET.getText().toString().trim()).matches()) {
            mEmailET.setError(getResources().getString(R.string.validemail_required));
            mEmailET.requestFocus();
            return false;
        }


        return true;
    }

    private boolean checkETEmpty(EditText mEditText) {
        if (mEditText.getText().toString().trim().isEmpty()) {
            mEditText.setError(getResources().getString(R.string.fieldnotempty));
            mEditText.requestFocus();
            return true;
        }
        return false;
    }

//    private void register() {
//
//        JSONArray mJsonArray=new JSONArray();
//        mJsonArray.put(PARTNERSHIP_ID);
//        Log.v("VALUE", mJsonArray.toString());
//        Utils.loading_box(SignUpActivity.this);
//        RestClient.getApiService().register(new TypedString(USERTYPE), new TypedString(mEmailET.getText().toString().trim()), new TypedString(mFirstNameET.getText().toString().trim()), new TypedString(mLastNameET.getText().toString().trim()), new TypedString(mPasswordET.getText().toString().trim()), new TypedString(mPhoneNumberET.getText().toString().trim()), new TypedString(mCompanyNameET.getText().toString().trim()), new TypedString(mJsonArray.toString()), new TypedString(mAddressET.getText().toString().trim()), new TypedString(mCityTxtView.getText().toString().trim()), new TypedString(mStateTxtView.getText().toString().trim()), new TypedString(mPinCodeET.getText().toString().trim()), new TypedString(mCountryTxtView.getText().toString().trim()), new TypedString(DEVICE_TYPE), new TypedString(Utils.getDeviceName()), new TypedString(sessionManager.getDeviceToken()), new Callback<String>() {
//            @Override
//            public void success(String s, Response response) {
//                Log.v("" + getClass().getSimpleName(), "Response> " + s);
//                try {
//                    JSONObject mObject = new JSONObject(s);
//
//                    int status = mObject.getInt("statusCode");
//
//                    if (ApiResponseFlags.Created.getOrdinal() == status) {
//
//
//                        JSONObject mDataobject = mObject.getJSONObject("data");
//                        sessionManager.saveUserInfo(mDataobject.getString("accessToken"), mDataobject.getJSONObject("dataToSet").getString("userType"), mDataobject.getJSONObject("dataToSet").getString("email"), mDataobject.getJSONObject("dataToSet").getString("firstName") + " " + mDataobject.getJSONObject("dataToSet").getString("lastName"), mDataobject.getJSONObject("dataToSet").getString("companyName"), mDataobject.getJSONObject("dataToSet").getJSONObject("addressDetails").getString("address"), "", mDataobject.getJSONObject("dataToSet").getString("phoneNumber"), "0", null);
//                        Toast.makeText(SignUpActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();
//                        startActivityForResult(new Intent(SignUpActivity.this, MainActivity.class), 500);
//                        finish();
//
//                    } else {
//                        Toast.makeText(SignUpActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                Utils.loading_box_stop();
//
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Utils.loading_box_stop();
//                try {
//                    Log.v("error.getKind() >> " + error.getKind(), " MSg >> " + error.getResponse().getReason());
//
//                    if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
//                        Toast.makeText(SignUpActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
//                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Unauthorized.getOrdinal()) {
//                        Utils.shopAlterDialog(SignUpActivity.this, Utils.getErrorMsg(error), false);
//                    } else if (error.getResponse().getStatus() == ApiResponseFlags.Not_Found.getOrdinal()) {
//                        Toast.makeText(SignUpActivity.this, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception ex) {
//                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

    private void getDeviceToken() {
        new DeviceTokenFetcher(this, new DeviceTokenFetcher.Listener() {
            @Override
            public void onDeviceTokenReceived(String deviceToken) {
                Log.e("Device Token", deviceToken + "");
                sessionManager.saveDeviceToken(deviceToken);
            }
        }).execute(SENDER_ID);
    }

    private void getTypeCargo(){
        Utils.loading_box(SignUpActivity.this);
        RestClient.getApiService().getTypeCargo(new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.v("" + getClass().getSimpleName(), "Response> " + s);
                try {
                    JSONObject mObject = new JSONObject(s);

                    int status = mObject.getInt("statusCode");

                    if (ApiResponseFlags.OK.getOrdinal() == status) {




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
                    Log.v("error.getKind() >> " + error.getKind(), " MSg >> " + error.getResponse().getReason());

                    if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                        noInternetDialog();
                    }
                } catch (Exception ex) {
                    noInternetDialog();
                }
            }
        });
    }
}
