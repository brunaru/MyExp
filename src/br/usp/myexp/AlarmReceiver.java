package br.usp.myexp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import br.usp.myexp.ui.QuestionnaireActivity;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent data) {
        String fileName = data.getStringExtra(Constants.QUESTIONNAIRE_FILE);
        setNotification(context, fileName);
        //Intent intent = new Intent(context, QuestionnaireActivity.class);
        //intent.putExtra(Constants.QUESTIONNAIRE_FILE, fileName);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //context.startActivity(intent);
    }
    
    private void setNotification(Context context, String fileName) {
        int notificationId = 001;
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Questionnaire").setContentText("Please, answer this questionnaire. :)");
        notification.setPriority(NotificationCompat.PRIORITY_MAX);
        notification.setDefaults(Notification.DEFAULT_ALL);
        
        Intent intent = new Intent(context, QuestionnaireActivity.class);
        intent.putExtra(Constants.QUESTIONNAIRE_FILE, fileName);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        notification.setContentIntent(pendingIntent);       
                
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationId, notification.build());
    }

}
