package com.carrus.fleetowner.utils;

import android.app.Activity;
import android.app.Dialog;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.carrus.fleetowner.R;


public class ImageChooserDialog {


    private final Activity activity;
    private OnButtonClicked onButtonClicked;

    /**
     * @param act The activity reference
     */
    private ImageChooserDialog(Activity act) {
        activity = act;
    }

    /**
     * @param activity The Activity reference
     * @return The value to be return
     */
    public static ImageChooserDialog With(Activity activity) {
        return new ImageChooserDialog(activity);
    }

    /**
     * @param message The message to be shown
     * @param onOkButtonClicked1 The button click reference
     */
    public void Show(String message, OnButtonClicked onOkButtonClicked1) {
        try {

            onButtonClicked = onOkButtonClicked1;
            final Dialog dialog = new Dialog(activity,
                    R.style.Theme_AppCompat_Translucent);
            dialog.setContentView(R.layout.dialog_image_chooser);
            dialog.setCancelable(true);
            WindowManager.LayoutParams layoutParams = dialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            TextView textMessage = (TextView) dialog
                    .findViewById(R.id.textMessage);
            textMessage.setMovementMethod(new ScrollingMovementMethod());
            textMessage.setText(message);
            Button btnGalery = (Button) dialog.findViewById(R.id.btnGalery);

            btnGalery.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {
                    onButtonClicked.onGaleryClicked();
                    dialog.dismiss();
                }

            });

            Button btnCamera = (Button) dialog.findViewById(R.id.btnCamera);

            btnCamera.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {
                    onButtonClicked.onCameraClicked();
                    dialog.dismiss();
                }

            });

            ImageView crossBtn=(ImageView) dialog.findViewById(R.id.crossBtn);
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

    public interface OnButtonClicked {
        void onGaleryClicked();
        void onCameraClicked();
    }
}
