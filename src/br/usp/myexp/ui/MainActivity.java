package br.usp.myexp.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import br.usp.myexp.AlarmReceiver;
import br.usp.myexp.Constants;
import br.usp.myexp.QuestionnaireManager;
import br.usp.myexp.R;
import br.usp.myexp.ems.xml.MultipleChoice;
import br.usp.myexp.ems.xml.OpenText;
import br.usp.myexp.ems.xml.Questionnnaire;
import br.usp.myexp.ems.xml.Questions;
import br.usp.myexp.ems.xml.Triggers;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button open = (Button) findViewById(R.id.buttonOpen);
        open.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
               // Intent intent = new Intent(getApplicationContext(), QuestionnaireActivity.class);
               // intent.putExtra(Constants.QUESTIONNAIRE_FILE, "example.xml");
                //startActivity(intent);
            }
        });
        
        scheduleAlarms();
    }
    
    private void scheduleAlarms() {
        QuestionnaireManager manager = new QuestionnaireManager();
        Questionnnaire ques = manager.readQuestionnaire("example.xml");
        List<String> times = ques.getTriggers().getTimes();

        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra(Constants.QUESTIONNAIRE_FILE, "example.xml");
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmMgr.cancel(alarmIntent);

        for (String time : times) {
            int dotsPos = time.indexOf(":");
            int hour = Integer.valueOf(time.substring(0, dotsPos));
            int min = Integer.valueOf(time.substring(dotsPos + 1));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, min);
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
        }        
    }
    
    @SuppressWarnings("unused")
    private void test() {
        Questionnnaire qstnr = new Questionnnaire();
        qstnr.setName("Teste");
        qstnr.setId("ID_teste");
        OpenText q1 = new OpenText();
        q1.setNumber(1);
        q1.setText("Esta eh uma questao teste");
        MultipleChoice q2 = new MultipleChoice();
        q2.setNumber(2);
        q2.setText("Esta eh uma questao teste de multipla escolha");
        List<String> options = new ArrayList<String>();
        options.add("Sim");
        options.add("Nao");
        options.add("Talvez");
        q2.setOptions(options);
        List<OpenText> ot = new ArrayList<OpenText>();
        ot.add(q1);
        List<MultipleChoice> mc = new ArrayList<MultipleChoice>();
        mc.add(q2);
        Questions qsts = new Questions();
        qsts.setMultipleChoiceQuestions(mc);
        qsts.setOpenTextQuestions(ot);
        qstnr.setQuestions(qsts);
        
        Triggers triggers = new Triggers();
        List<String> times = new ArrayList<String>();
        times.add("14:00");
        times.add("11:00");
        List<String> contexts = new ArrayList<String>();
        contexts.add("after_sleep");
        contexts.add("after_conversation");
        triggers.setContext(contexts);
        triggers.setTimes(times);
        qstnr.setTriggers(triggers);

        QuestionnaireManager manager = new QuestionnaireManager();
        
        //Questionnnaire nq = manager.read("example.xml");

        manager.writeQuestionnaire(qstnr, "q.xml");
    }
}
