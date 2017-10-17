package com.example.ramji.android.gshare.fcm;


//import android.util.Log;
//
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.FirebaseInstanceIdService;
//
//public class GshareFirebaseInstanceIdService extends FirebaseInstanceIdService {
//
//    private static final String TAG = GshareFirebaseInstanceIdService.class.getSimpleName();
//
//    @Override
//    public void onTokenRefresh() {
//        //Get the updated instance id
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.d(TAG,"Refreshed token: "+refreshedToken);
//
//        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//        // Instance ID token to your app server.
//        sendRegistrationToServer(refreshedToken);
//    }
//
//    /**
//     * Persist token to third-party servers.
//     * <p>
//     * Modify this method to associate the user's FCM InstanceID token with any server-side account
//     * maintained by your application.
//     *
//     * @param refreshedToken The new token.
//     */
//
//    private void sendRegistrationToServer(String refreshedToken) {
//        // This method is blank, but if you were to build a server that stores users token
//        // information, this is where you'd send the token to the server.
//    }
//}
