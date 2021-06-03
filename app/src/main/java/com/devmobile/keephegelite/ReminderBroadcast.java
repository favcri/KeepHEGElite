package com.devmobile.keephegelite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
//		NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyDate")
//				.setSmallIcon(R.drawable.ic_google_keep_icon)
//				.setContentTitle("Salut")
//				.setContentText("C'est une notif")
//				.setPriority(NotificationCompat.PRIORITY_DEFAULT);
//		NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
//		notificationManagerCompat.notify(200, builder.build());
//		Toast.makeText(context, "Fin !", Toast.LENGTH_SHORT).show();
	}
}