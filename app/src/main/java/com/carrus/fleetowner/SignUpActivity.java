package com.carrus.fleetowner;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.carrus.fleetowner.gcm.DeviceTokenFetcher;
import com.carrus.fleetowner.models.CargoType;
import com.carrus.fleetowner.models.StateCityInfo;
import com.carrus.fleetowner.models.StateCityModel;
import com.carrus.fleetowner.multivaluesspinner.MultiSpinner;
import com.carrus.fleetowner.retrofit.RestClient;
import com.carrus.fleetowner.utils.ApiResponseFlags;
import com.carrus.fleetowner.utils.CommonNoInternetDialog;
import com.carrus.fleetowner.utils.Constants;
import com.carrus.fleetowner.utils.SessionManager;
import com.carrus.fleetowner.utils.Utils;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedString;

import static com.carrus.fleetowner.utils.Constants.COUNTRYNAME;
import static com.carrus.fleetowner.utils.Constants.DEVICE_TYPE;
import static com.carrus.fleetowner.utils.Constants.MY_FLURRY_APIKEY;
import static com.carrus.fleetowner.utils.Constants.SENDER_ID;

/**
 * Created by Sunny on 1/15/16 for Fleet Owner.
 */
public class SignUpActivity extends BaseActivity {

    private ImageView mBackButton;
    private MultiSpinner mAreaOprerationSpinner, mTypeOfCargo;
    private Spinner stateSpinner;
    private TextView mStateTxtView;
    private List<String> states;
    private List<String> areaOprationList;
    private RadioButton mShipperRadioBtn, mBrokerRadioBtn;
    private String USERTYPE = "FLEET_OWNER";
    private EditText mFullNameET, mPasswordET, mCnfrmPasswordET, mPhoneNumberET, mCompanyNameET, mAddressET, mPinCodeET, mEmailET, mCityET, mNumberTruckET;
    private SessionManager sessionManager;
    private CargoType mCargoType;
    private JSONArray mOperationArray, mCargoTypeArray;
    private boolean isOperationSelected = false, isCargoTypeSelected = false;
    private RadioButton mByCarrusRadioBtn, mByMeRadioBtn;
    private String assignmentBy = "1";
    private boolean isCargoCalled=false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        sessionManager = new SessionManager(this);
        getDeviceToken();
        initView();
        initializeListener();
        parseStates();
        getTypeCargo();
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
        mNumberTruckET = (EditText) findViewById(R.id.numberOfTruckET);
        mByCarrusRadioBtn = (RadioButton) findViewById(R.id.byCarrusRadioBtn);
        mByMeRadioBtn = (RadioButton) findViewById(R.id.byMeRadioBtn);
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
                    mStateTxtView.setTextColor(Utils.getColor(SignUpActivity.this, android.R.color.black));
                    mStateTxtView.setError(null);

                } else {
                    mStateTxtView.setText(getResources().getString(R.string.state));
                    mStateTxtView.setTextColor(Utils.getColor(SignUpActivity.this, R.color.gray_text));

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
                    genrateOTP();
                }
            }
        });

        mByCarrusRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    assignmentBy = "0";
                }
            }
        });

        mByMeRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    assignmentBy = "1";
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
        StateCityModel mStateCityModel = gson.fromJson(loadJSONFromAsset(), StateCityModel.class);
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

        areaOprationList = new ArrayList<>(states);
        areaOprationList.remove(0);
        final TreeMap<String, Boolean> items = new TreeMap<>();
        for (String item : areaOprationList) {
            items.put(item, Boolean.FALSE);
        }

        mAreaOprerationSpinner.setItems(items, getResources().getString(R.string.areaoprations), new MultiSpinner.MultiSpinnerListener() {

            @Override
            public void onItemsSelected(boolean[] selected) {

                // your operation with code...
                isOperationSelected = false;
                mOperationArray = new JSONArray();

                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {
                        isOperationSelected = true;
                        mOperationArray.put(areaOprationList.get(i));
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
        CommonNoInternetDialog.WithActivity(SignUpActivity.this).Show(getResources().getString(R.string.nointernetconnection), getResources().getString(R.string.tryagain), getResources().getString(R.string.exit), getResources().getString(R.string.callcarrus), new CommonNoInternetDialog.ConfirmationDialogEventsListener() {
            @Override
            public void OnOkButtonPressed() {
                if (isCargoCalled)
                    getTypeCargo();
                else
                    genrateOTP();

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


    private boolean isFieldFilled() {
        if (sessionManager.getDeviceToken().isEmpty()) {
            getDeviceToken();
        }

        if (checkETEmpty(mFullNameET))
            return false;

        else if (checkETEmpty(mEmailET))
            return false;
        else if (checkETEmpty(mPasswordET))
            return false;
        else if (checkETEmpty(mCnfrmPasswordET))
            return false;
        else if (checkETEmpty(mPhoneNumberET))
            return false;
//        else if (checkETEmpty(mCompanyNameET))
//            return false;
        else if (!isOperationSelected) {
            Toast.makeText(SignUpActivity.this, "Select Area of Operation", Toast.LENGTH_SHORT).show();
            return false;
        }
//          else if (checkETEmpty(mNumberTruckET))
//            return false;
//        else if (checkETEmpty(mAddressET))
//            return false;
//        else if (mStateTxtView.getText().toString().equalsIgnoreCase(getResources().getString(R.string.state))) {
//            mStateTxtView.setError(getResources().getString(R.string.select_value));
//            mStateTxtView.requestFocus();
//            return false;
//        } else if (checkETEmpty(mCityET))
//            return false;
//
//        else if (checkETEmpty(mPinCodeET))
//            return false;
//        else if (!isCargoTypeSelected) {
//            Toast.makeText(SignUpActivity.this, "Select Type of Cargo Handled", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        else if (!mPasswordET.getText().toString().trim().equalsIgnoreCase(mCnfrmPasswordET.getText().toString().trim())) {
            mCnfrmPasswordET.setError(getResources().getString(R.string.passwdnotmacth));
            mCnfrmPasswordET.requestFocus();
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmailET.getText().toString().trim()).matches()) {
            mEmailET.setError(getResources().getString(R.string.validemail_required));
            mEmailET.requestFocus();
            return false;
        } else if (mPhoneNumberET.getText().toString().trim().length() < 10) {
            mPhoneNumberET.setError(getResources().getString(R.string.phonelimit));
            mPhoneNumberET.requestFocus();
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

    private void register() {

        Utils.loading_box(SignUpActivity.this);
        RestClient.getApiService().register(new TypedString(USERTYPE), new TypedString(mEmailET.getText().toString().trim()), new TypedString(mFullNameET.getText().toString().trim()), new TypedString(mPasswordET.getText().toString().trim()), new TypedString(mPhoneNumberET.getText().toString().trim()), new TypedString(mCompanyNameET.getText().toString().trim()), new TypedString(mOperationArray.toString()), new TypedString((mNumberTruckET.getText().toString().trim().isEmpty()) ? "0" : mNumberTruckET.getText().toString().trim()), new TypedString(mAddressET.getText().toString().trim()), new TypedString(mCityET.getText().toString().trim()), new TypedString(((mStateTxtView.getText().toString().equalsIgnoreCase(getResources().getString(R.string.state))) ? "" : mStateTxtView.getText().toString().trim())), new TypedString(mPinCodeET.getText().toString().trim()), new TypedString(COUNTRYNAME), new TypedString(mCargoTypeArray.toString()), new TypedString(DEVICE_TYPE), new TypedString(Utils.getDeviceName()), new TypedString(sessionManager.getDeviceToken()), new TypedString(assignmentBy), new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                if (BuildConfig.DEBUG)
                    Log.v("" + getClass().getSimpleName(), "Response> " + s);
                try {
                    JSONObject mObject = new JSONObject(s);

                    int status = mObject.getInt("statusCode");

                    if (ApiResponseFlags.Created.getOrdinal() == status) {

                        FlurryAgent.onEvent("Signup Mode");
                        JSONObject mDataobject = mObject.getJSONObject("data");
//                        sessionManager.saveUserInfo(mDataobject.getString("accessToken"), mDataobject.getJSONObject("dataToSet").getString("userType"), mDataobject.getJSONObject("dataToSet").getString("email"), mDataobject.getJSONObject("dataToSet").getString("firstName"), mDataobject.getJSONObject("dataToSet").getString("companyName"), mDataobject.getJSONObject("dataToSet").getJSONObject("addressDe÷tails").getString("address"), "", mDataobject.getJSONObject("dataToSet").getString("phoneNumber"), "0", null);
                        sessionManager.saveUserInfo(mDataobject.getString("accessToken"), mDataobject.getJSONObject("dataToSet").getString("userType"), mDataobject.getJSONObject("dataToSet").getString("email"), mDataobject.getJSONObject("dataToSet").getString("fullName"), (mDataobject.getJSONObject("dataToSet").has("companyName") ? mDataobject.getJSONObject("dataToSet").getString("companyName") : ""), (mDataobject.getJSONObject("dataToSet").getJSONObject("addressDetails").has("address") ? mDataobject.getJSONObject("dataToSet").getJSONObject("addressDetails").getString("address") : ""), mDataobject.getJSONObject("dataToSet").getString("phoneNumber"), "");
                        Toast.makeText(SignUpActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                        // Closing all the Activities
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        // Add new Flag to start new Activity
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();

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
                        Log.v("error.getKind() >> " + error.getKind(), " MSg >> " + error.getResponse().getReason());

                    if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                        noInternetDialog();
                    } else {
                        Toast.makeText(SignUpActivity.this, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    noInternetDialog();
                }
            }
        });
    }

    private void getDeviceToken() {
        new DeviceTokenFetcher(this, new DeviceTokenFetcher.Listener() {
            @Override
            public void onDeviceTokenReceived(String deviceToken) {
                Log.e("Device Token", deviceToken + "");
                sessionManager.saveDeviceToken(deviceToken);
            }
        }).execute(SENDER_ID);
    }

    private void getTypeCargo() {
        isCargoCalled=true;
        Utils.loading_box(SignUpActivity.this);
        RestClient.getApiService().getTypeCargo(new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                isCargoCalled=false;
                if (BuildConfig.DEBUG)
                    Log.v("" + getClass().getSimpleName(), "Response> " + s);
                try {
                    JSONObject mObject = new JSONObject(s);

                    int status = mObject.getInt("statusCode");

                    if (ApiResponseFlags.OK.getOrdinal() == status) {
                        // mCargoType
                        Gson gson = new Gson();
                        mCargoType = gson.fromJson(s, CargoType.class);
                        setTypeCargoSpinner();

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

    private void setTypeCargoSpinner() {
        if (mCargoType.getData() != null && mCargoType.getData().size() != 0)
            mTypeOfCargo.setItems(mCargoType.getData(), getResources().getString(R.string.typecargohandle), new MultiSpinner.MultiSpinnerListener() {

                @Override
                public void onItemsSelected(boolean[] selected) {

                    // your operation with code...
                    isCargoTypeSelected = false;
                    mCargoTypeArray = new JSONArray();
                    for (int i = 0; i < selected.length; i++) {
                        if (selected[i]) {
                            isCargoTypeSelected = true;
                            mCargoTypeArray.put(mCargoType.getData().get(i).id);
                            Log.i("TAG", i + " : " + mCargoType.getData().get(i).typeCargoName);
                        }
                    }
                }
            });
    }

    private void openOTPDialog() {
        try {

            final Dialog dialog = new Dialog(SignUpActivity.this,
                    R.style.Theme_AppCompat_Translucent);
            dialog.setContentView(R.layout.dialog_otp_verifier);
            WindowManager.LayoutParams layoutParams = dialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            final EditText mEditText = (EditText) dialog.findViewById(R.id.otpET);
            Button submitBtn = (Button) dialog.findViewById(R.id.submitBtn);

            submitBtn.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {
                    if (mEditText.getText().toString().trim().isEmpty()) {
                        mEditText.setError(getResources().getString(R.string.fieldnotempty));
                        mEditText.requestFocus();
                    } else {
                        dialog.dismiss();
                        verifyOTP(mEditText.getText().toString().trim());
                    }
                }

            });
            ImageView crossBtn = (ImageView) dialog.findViewById(R.id.crossBtn);
            crossBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void genrateOTP() {
        Utils.loading_box(SignUpActivity.this);
        RestClient.getApiService().phoneVerificationGenerate(mPhoneNumberET.getText().toString().trim(), mEmailET.getText().toString().trim(), USERTYPE, "true", "false", new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                if (BuildConfig.DEBUG)
                    Log.v("" + getClass().getSimpleName(), "Response> " + s);
                try {
                    JSONObject mObject = new JSONObject(s);

                    int status = mObject.getInt("statusCode");

                    if (ApiResponseFlags.OK.getOrdinal() == status) {
                        FlurryAgent.onEvent("OTP Genrate Mode");
                        openOTPDialog();
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
                        Log.v("error.getKind() >> " + error.getKind(), " MSg >> " + error.getResponse().getReason());

                    if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                        noInternetDialog();
                    } else {
                        Toast.makeText(SignUpActivity.this, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    noInternetDialog();
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

    private void verifyOTP(String otp) {
        Utils.loading_box(SignUpActivity.this);
        RestClient.getApiService().phoneVerificationVerify(mPhoneNumberET.getText().toString().trim(), otp, USERTYPE, "true", new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                if (BuildConfig.DEBUG)
                    Log.v("" + getClass().getSimpleName(), "Response> " + s);
                try {
                    JSONObject mObject = new JSONObject(s);

                    int status = mObject.getInt("statusCode");

                    if (ApiResponseFlags.Created.getOrdinal() == status) {
                        FlurryAgent.onEvent("Verify OTP Mode");
                        Toast.makeText(SignUpActivity.this, mObject.getString("message"), Toast.LENGTH_SHORT).show();
                        register();
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
                        Log.v("error.getKind() >> " + error.getKind(), " MSg >> " + error.getResponse().getReason());

                    if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                        noInternetDialog();
                    } else {
                        Toast.makeText(SignUpActivity.this, Utils.getErrorMsg(error), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    noInternetDialog();
                }
            }
        });
    }
}
