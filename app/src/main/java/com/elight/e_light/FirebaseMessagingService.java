//package com.elight.e_light;
//
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Intent;
//import android.support.v4.app.NotificationCompat;
//
//import com.google.firebase.messaging.RemoteMessage;
//
///**
// * Created by Olije favour on 4/25/2018.
// */
//
//public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
//
//    @Override
//
//    public void onMessageReceived(RemoteMessage remoteMessage){
//
//        super.onMessageReceived(remoteMessage);
//
//
//        String messageTitle=remoteMessage.getNotification().getTitle();
//        String messagebody=remoteMessage.getNotification().getBody();
//        String click_action=remoteMessage.getNotification().getClickAction();
//        String dataMessage=remoteMessage.getData().get("message");
//        String dataFrom=remoteMessage.getData().get("from_user_id");
//
//
//        Intent intent = new Intent (click_action);
//        intent.putExtra("message", dataMessage);
//        intent.putExtra("from_user_id", dataFrom);
//
//
//
//        PendingIntent  pendingIntent= PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationCompat.Builder noti = new  NotificationCompat.Builder(this,getString(R.string.default_notification_channel_id))
//                .setContentTitle(messageTitle)
//                .setContentText(messagebody)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentIntent(pendingIntent);
////                .setLargeIcon(R.mipmap.ic_launcher);
//
//        int notificationId=007;
//
//        NotificationManager notificationManager= (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//
//        notificationManager.notify(notificationId,noti.build());
//
//    }
//}
