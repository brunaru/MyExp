package br.usp.myexp.receivers;

import br.usp.myexp.QuestionnaireManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        QuestionnaireManager manager = new QuestionnaireManager();
        manager.scheduleAlarms(context);
    }

}
