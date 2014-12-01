package br.usp.myexp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import br.usp.myexp.ui.QuestionnaireActivity;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent data) {
        String fileName = data.getStringExtra(Constants.QUESTIONNAIRE_FILE);
        Log.d("AlarmReceiver", "fileName: " + fileName);
        setNotification(context, fileName);
    }
    
    private void setNotification(Context context, String fileName) {
        int notificationId = 1;
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Questionnaire").setContentText("Please, answer this questionnaire. :)");
        notification.setPriority(NotificationCompat.PRIORITY_MAX);
        notification.setDefaults(Notification.DEFAULT_ALL);
        notification.setAutoCancel(true);
        
        Intent intent = new Intent(fileName, Uri.parse(fileName), context, QuestionnaireActivity.class);
        intent.putExtra(Constants.QUESTIONNAIRE_FILE, fileName);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);
                
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationId, notification.build());
    }

}
