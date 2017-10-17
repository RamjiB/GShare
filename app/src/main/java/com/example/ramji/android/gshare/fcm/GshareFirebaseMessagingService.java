package com.example.ramji.android.gshare.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.ramji.android.gshare.MainActivity;
import com.example.ramji.android.gshare.R;
import com.example.ramji.android.gshare.provider.GshareContract;
import com.example.ramji.android.gshare.provider.GshareProvider;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class GshareFirebaseMessagingService  extends FirebaseMessagingService{

    private static final String TAG = "GshareFirebaseMessaging";

    private static final String JSON_KEY_AUTHOR = GshareContract.COLUMN_AUTHOR;
    private static final String JSON_KEY_AUTHOR_KEY = GshareContract.COLUMN_AUTHOR_KEY;
    private static final String JSON_KEY_MESSAGE = GshareContract.COLUMN_MESSAGE;
    private static final String JSON_KEY_DATE = GshareContract.COLUMN_DATE;

    private static final int NOTIFICATION_MAX_CHARACTERS = 30;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG,"From: "+ remoteMessage.getFrom());

        //Check if message contains a data payload

        Map<String,String> data = remoteMessage.getData();

        if (data.size() > 0){
            Log.d(TAG,"Message data payload: " + data);

            insertGshare(data);

            //Send a notification that you got a new message
            sendNotification(data);

        }

    }

    /**
     * Create and show a simple notification containing the received FCM message
     *
     * @param data Map which has the message data in it
     */

    private void sendNotification(Map<String, String> data) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Create the pending intent to launch the activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String author = data.get(JSON_KEY_AUTHOR);
        String message = data.get(JSON_KEY_MESSAGE);

        // If the message is longer than the max number of characters we want in our
        // notification, truncate it and add the unicode character for ellipsis
        if (message.length() > NOTIFICATION_MAX_CHARACTERS) {
            message = message.substring(0, NOTIFICATION_MAX_CHARACTERS) + "\u2026";
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_action_name)
                .setContentTitle(String.format(getString(R.string.notification_message), author))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

    /**
     * Inserts a single share into the database;
     * @param data
     */

    private void insertGshare(final Map<String, String> data) {

        // Database operations should not be done on the main thread
        AsyncTask<Void, Void, Void> insertSquawkTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                ContentValues newMessage = new ContentValues();
                newMessage.put(GshareContract.COLUMN_AUTHOR, data.get(JSON_KEY_AUTHOR));
                newMessage.put(GshareContract.COLUMN_MESSAGE, data.get(JSON_KEY_MESSAGE).trim());
                newMessage.put(GshareContract.COLUMN_DATE, data.get(JSON_KEY_DATE));
                newMessage.put(GshareContract.COLUMN_AUTHOR_KEY, data.get(JSON_KEY_AUTHOR_KEY));
                getContentResolver().insert(GshareProvider.GshareMessages.CONTENT_URI, newMessage);
                return null;
            }
        };

        insertSquawkTask.execute();
    }
}
