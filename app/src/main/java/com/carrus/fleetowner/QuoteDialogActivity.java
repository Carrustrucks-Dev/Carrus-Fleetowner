package com.carrus.fleetowner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private EditText offrbidEdtxt, trucktypeEdtxt, notesEdtxt;
    private SessionManager sessionManager;
    private Button mSubmitBtn;
    private float userRating = 0;
    private ConnectionDetector mConnectionDetector;

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
        trucktypeEdtxt = (EditText) findViewById(R.id.trucktypeEdtxt);
        notesEdtxt = (EditText) findViewById(R.id.notesEdtxt);
        mSubmitBtn=(Button) findViewById(R.id.submitBtn);
        final Intent mIntent=getIntent();
        if(mIntent.getBooleanExtra(TYPE, false)){
            offrbidEdtxt.setText("Rs"+mIntent.getStringExtra(BIDVALUE));
            trucktypeEdtxt.setText(mIntent.getStringExtra(TRUCKTYPE));
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
                // Close dialog
                if(mSubmitBtn.getText().toString().equalsIgnoreCase(getResources().getString(R.string.quote))){

                }else if(mSubmitBtn.getText().toString().equalsIgnoreCase(getResources().getString(R.string.modify))){

                }

            }
        });
    }
}
