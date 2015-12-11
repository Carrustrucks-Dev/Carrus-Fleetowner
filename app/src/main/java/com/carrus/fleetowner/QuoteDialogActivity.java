package com.carrus.fleetowner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;


import com.carrus.fleetowner.retrofit.RestClient;
import com.carrus.fleetowner.utils.ApiResponseFlags;
import com.carrus.fleetowner.utils.ConnectionDetector;
import com.carrus.fleetowner.utils.SessionManager;
import com.carrus.fleetowner.utils.Utils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import static com.carrus.fleetowner.utils.Constants.TYPE;
import static com.carrus.fleetowner.utils.Constants.BIDVALUE;
import static com.carrus.fleetowner.utils.Constants.TRUCKTYPE;
import static com.carrus.fleetowner.utils.Constants.NOTES;
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
        mSubmitBtn=(Button) findViewById(R.id.submitBtn);
        radioTrackGroup=(RadioGroup) findViewById(R.id.trackingRadio);
        final Intent mIntent=getIntent();
        if(mIntent.getBooleanExtra(TYPE, false)){
            offrbidEdtxt.setText(""+mIntent.getLongExtra(BIDVALUE, 0));
            notesEdtxt.setText(mIntent.getStringExtra(NOTES));
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
                if(mSubmitBtn.getText().toString().equalsIgnoreCase(getResources().getString(R.string.quote))){

                }else if(mSubmitBtn.getText().toString().equalsIgnoreCase(getResources().getString(R.string.modify))){

                }

            }
        });
    }
}
