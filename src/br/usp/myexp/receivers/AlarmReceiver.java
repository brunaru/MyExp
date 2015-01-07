package br.usp.myexp.receivers;

import java.io.File;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import br.usp.myexp.Constants;
import br.usp.myexp.R;
import br.usp.myexp.ui.QuestionnaireActivity;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent data) {
        String fileName = data.getStringExtra(Constants.QUESTIONNAIRE_FILE);
        String questionnaireName = data.getStringExtra(Constants.QUESTIONNAIRE_NAME);
        Log.d("AlarmReceiver", "fileName: " + fileName + ", questionnaireName: " + questionnaireName);
        setNotification(context, fileName, questionnaireName);
    }
    
    private void setNotification(Context context, String fileName, String questionnaireName) {
        int notificationId = 1;
        String title = context.getResources().getString(R.string.app_name);
        String text = context.getResources().getString(R.string.answer_questionnaire);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_stat_notify_qst)
                .setContentTitle(title).setContentText(text);
        notification.setPriority(NotificationCompat.PRIORITY_MAX);
        notification.setDefaults(Notification.DEFAULT_ALL);
        notification.setAutoCancel(true);
        Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + File.separator
                + R.raw.notification_sound);
        notification.setSound(alarmSound);
        
        Intent intent = new Intent(fileName, Uri.parse(fileName), context, QuestionnaireActivity.class);
        intent.putExtra(Constants.QUESTIONNAIRE_FILE, fileName);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);
                
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationId, notification.build());
    }

}
