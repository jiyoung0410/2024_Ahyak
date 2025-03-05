package com.example.ahyak.Alarm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.ahyak.FullScreenAlarmActivity
import com.example.ahyak.MainActivity
import com.example.ahyak.R

//class AlarmReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context, intent: Intent?) {
//        val fullScreenIntent = Intent(context,FullScreenAlarmActivity::class.java)
//        fullScreenIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//        context.startActivity(fullScreenIntent)
//    }
//}

class AlarmReceiver : BroadcastReceiver() { // Alarm이 발생했을 때의 동작 정의
    private lateinit var manager: NotificationManager
    private lateinit var builder1: NotificationCompat.Builder
    private lateinit var builder2: NotificationCompat.Builder

    companion object {
        const val CHANNEL_ID = "channel"
        const val CHANNEL_NAME = "channel1"
    }

//    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onReceive(context: Context?, intent: Intent?) {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH).apply {
            description = "test notification"
        }

        manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

//        val intent2 = Intent(context, FullScreenAlarmActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
        val intent2 = Intent(context, MainActivity::class.java)
        val requestCode = intent?.extras!!.getInt("alarm_rqCode")
        val title = intent.extras!!.getString("content")

//        Toast.makeText(context,"Alarm test",Toast.LENGTH_SHORT).show()

        val pendingIntent = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(context,requestCode,intent2,PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(context,requestCode,intent2,PendingIntent.FLAG_UPDATE_CURRENT)
        }

//        builder1 = NotificationCompat.Builder(context, CHANNEL_ID).apply {
//            setSmallIcon(R.drawable.ic_logo)
//            setContentTitle(title)
//            setContentText("SCHEDULE MANAGER")
//            priority = NotificationCompat.PRIORITY_DEFAULT
//            setContentIntent(pendingIntent)
//            setAutoCancel(true)
//        }

        builder1 = NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon((R.drawable.ic_logo))
            setContentTitle(title)
            setContentText("약을 복용하실 시간입니다!")
            priority = NotificationCompat.PRIORITY_HIGH
            setContentIntent(pendingIntent)
            setCategory(NotificationCompat.CATEGORY_ALARM)
            setAutoCancel(true)
            setStyle(NotificationCompat.BigTextStyle().bigText("약을 복용하실 시간입니다!"))
//            addAction(R.drawable.ic_logo,"Snooze",snoozeP)
            //setFullScreenIntent(pendingIntent,true)
        }

//        builder2 = NotificationCompat.Builder(context, CHANNEL_ID).apply {
//            setSmallIcon(R.drawable.ic_calender_point)
//            setContentTitle("new content")
//            setContentText("SCHEDULE MANAGER")
//            priority = NotificationCompat.PRIORITY_DEFAULT
//            setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.ic_logo))
//            setStyle(NotificationCompat.BigPictureStyle()
//                .bigPicture(BitmapFactory.decodeResource(resources,R.drawable.ic_logo))
//                .bigLargeIcon(null)
//                .setBigContentTitle("확장"))
//            setContentIntent(pendingIntent)
//        }

//        val notification = builder.setContentTitle(title)
//            .setContentText("SCHEDULE MANAGER")
//            .setSmallIcon(R.drawable.ic_calender_point)
//            .setAutoCancel(true)
//            .setContentIntent(pendingIntent)
//            .build()

        with(NotificationManagerCompat.from(context)) {
            manager.notify(5, builder1.build())
        }
    }
}