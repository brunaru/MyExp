package br.usp.myexp.ui;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import br.usp.myexp.Constants;
import br.usp.myexp.QuestionnaireManager;
import br.usp.myexp.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* Schedule questionnaires. */
        QuestionnaireManager manager = new QuestionnaireManager();
        manager.scheduleAlarms(getApplicationContext());
        
        ImageView image = (ImageView) findViewById(R.id.imageView1);
        image.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
                test();
            }
        });        
    }
    
    private void test() {
        final QuestionnaireManager manager = new QuestionnaireManager();
        List<String> list = manager.getAllQuestionnairesFiles();
        final CharSequence[] items = list.toArray(new CharSequence[list.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Escolha o questionário:");
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String file = items[item].toString();
                
                //Questionnnaire ques = manager.readQuestionnaire(file);
                String title = getResources().getString(R.string.app_name);
                String text = getResources().getString(R.string.answer_questionnaire);
                NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.drawable.ic_stat_notify_qst)
                        .setContentTitle(title).setContentText(text);
                notification.setPriority(NotificationCompat.PRIORITY_MAX);
                notification.setDefaults(Notification.DEFAULT_ALL);
                //Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + File.separator
                //        + R.raw.notification_sound);
                //notification.setSound(alarmSound);
                //long[] vibrate = new long[]{100, 1000, 100, 1000};
                //notification.setVibrate(vibrate);
                notification.setAutoCancel(true);
                Intent intent = new Intent(file, Uri.parse(file), getApplicationContext(), QuestionnaireActivity.class);
                intent.putExtra(Constants.QUESTIONNAIRE_FILE, file);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setContentIntent(pendingIntent);
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(2, notification.build());
                
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
