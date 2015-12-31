package com.carrus.fleetowner.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;


/**
 * Common Confirmation Dialog with callbacks for activity/Fragment
 */

public class CommonNoInternetDialog {

    private static AlertDialog mAlertDialog = null;

    /**
     * @param act
     * @return
     */
    public static ConDialogActivity WithActivity(Activity act) {
        return new ConDialogActivity(act);
    }


    public interface ConfirmationDialogEventsListener {


        void OnOkButtonPressed();

        void OnCancelButtonPressed();

    }

    /**
     * Confirmation dialog class for activity
     */
    public static class ConDialogActivity {
        private final Activity activity;
        private ConfirmationDialogEventsListener confirmationDialogEvents;


        /**
         * @param activity
         */
        public ConDialogActivity(Activity activity) {
            this.activity = activity;

        }


        /**
         * @param message
         * @param okButtonText
         * @param cancelButtonText
         * @param confirmationDialogEvents1
         */
        public void Show(String message, String okButtonText, String cancelButtonText, ConfirmationDialogEventsListener confirmationDialogEvents1) {
            try {
                this.confirmationDialogEvents = confirmationDialogEvents1;
                if (mAlertDialog == null)
                    mAlertDialog = new AlertDialog.Builder(activity)
                            // Set Dialog Icon
//                .setIcon(R.drawable.androidhappy)
                            // Set Dialog Title
                            .setTitle("")
                                    // Set Dialog Message
                            .setMessage(message)
                            .setCancelable(false)

                                    // Positive button
                            .setPositiveButton(okButtonText, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do something else
                                    dialog.dismiss();
                                    mAlertDialog=null;
                                    confirmationDialogEvents.OnOkButtonPressed();
                                }
                            })
                            .setNegativeButton(cancelButtonText, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    mAlertDialog=null;
                                    confirmationDialogEvents.OnCancelButtonPressed();
                                }
                            })
                            .create();
                mAlertDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
