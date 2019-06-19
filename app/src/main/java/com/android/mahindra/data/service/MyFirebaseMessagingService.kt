package com.android.mahindra.data.service

/**
 * @author Kaiser Perwez
 */
 
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.android.mahindra.R

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "MyFirebaseMsgService"
    private var parsed_data: String? = null
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        /*Log.d(TAG, "From: " + remoteMessage!!.from!!)
        
        // Check if message contains an empty data payload.
        if (remoteMessage.data.isEmpty()) return


        Log.d(TAG, "Message data payload: " + remoteMessage.data)
        parseHeaderData(remoteMessage.data)
       var retMap = Gson().fromJson(parsed_data, object : TypeToken<HashMap<String, String>>() {}.type)


        try {
             if (retMap["key1"] == "value1") {}
                showTipStatusNotification(
                    retMap["id"] ?: "0",
                    retMap["title"] ?: getString(R.string.app_name),
                    retMap["body"] ?: "My BODY",
                    remoteMessage.notification?.channelId ?: getString(R.string.default_notification_channel_id)
                )
            

        } catch (e: Exception) {
            e.printStackTrace()
        }
*/
    }

    private fun parseHeaderData(headerData: Map<String, String>) {
        parsed_data = headerData["data"]
    }

    private fun showTipStatusNotification(notif_tip_id: String, title: String, body: String, channelId: String) {

       /* val intentNotif = Intent(this, LoginActivity::class.java)
        intentNotif.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        val intentPending = PendingIntent.getActivity(this, 0, intentNotif, PendingIntent.FLAG_ONE_SHOT)

        val uriDefaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notifyImage = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)

        val builder =
            NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)//use a white icon with transparency inside. Just like an outline icon
             //   .setColor(resources.getColor(R.color.material_blue_gray_800))
                .setLargeIcon(notifyImage)
                .setContentTitle(title)
                .setContentText(body)
                // .setContentText(remoteMessage.getNotification().getBody())
                .setContentInfo("")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setTicker(getString(R.string.app_name))
                .setSound(uriDefaultSound)
                .setContentIntent(intentPending)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "CHANNEL_NAME",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "${body.toUpperCase()}"
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, builder.build())
*/
    }

}
