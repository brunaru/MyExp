package br.usp.myexp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import br.usp.myexp.ems.json.AnswersGroup;
import br.usp.myexp.ems.json.QuestionnaireAnswers;
import br.usp.myexp.ems.xml.Questionnnaire;

import com.google.gson.Gson;

public class QuestionnaireManager {

    private static String MY_EXP_DIR = "MyExp";

    public Questionnnaire readQuestionnaire(String fileName) {
        File dir = getDir();
        if (dir != null) {
            Serializer serializer = new Persister();
            File src = new File(dir, fileName);
            try {
                Questionnnaire qs = serializer.read(Questionnnaire.class, src);
                return qs;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean writeQuestionnaire(Questionnnaire obj, String fileName) {
        File dir = getDir();
        if (dir != null) {
            Serializer serializer = new Persister();
            File src = new File(dir, fileName);
            try {
                serializer.write(obj, src);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private File getDir() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            String path = Environment.getExternalStorageDirectory().getPath() + File.separator + MY_EXP_DIR;
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdir();
            }
            return dir;
        }
        return null;
    }
    
    private boolean writeQuestionAnswerXml(String fileName, String questionNumber, String answer) {
        try {
            FileWriter writer = new FileWriter(fileName, true);
            writer.append(questionNumber);
            writer.append(',');
            writer.append(answer);
            writer.append('\n');
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @SuppressWarnings("unused")
    private boolean reWriteQuestionAnswerXml(String fileName, String questionNumber, String answer) {
        try {
            File file = new File(fileName);
            List<String> lines = FileUtils.readLines(file);
            lines.remove(lines.size()-1);
            FileUtils.writeLines(file, lines);
            writeQuestionAnswerXml(fileName, questionNumber, answer);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @SuppressWarnings("unused")
    private String getAnswersFileNameXml(String questionnaireId) {
        File dir = getDir();
        if (dir != null) {            
            DateFormat dateFormat = new SimpleDateFormat("HH-mm-ss_dd-MM-yyyy", Locale.getDefault());
            Date date = new Date();
            String fileName = dir + File.separator + questionnaireId + "_" + String.valueOf(dateFormat.format(date)) + ".csv";
            String head = "Number,Answer";            
            try {
                FileWriter writer = new FileWriter(fileName);
                writer.append(head);
                writer.append('\n');
                writer.flush();
                writer.close();
                return fileName;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public boolean writeQuestionAnswers(AnswersGroup answersGroup, String questionnaireId) {
        File dir = getDir();
        if (dir != null) {
            String fileName = dir + File.separator + questionnaireId + ".json";
            File file = new File(fileName);
            QuestionnaireAnswers jsonObj = null;
            Gson gson = new Gson();
            try {
                if (file.exists()) {
                    BufferedReader reader = new BufferedReader(new FileReader(fileName));
                    jsonObj = gson.fromJson(reader, QuestionnaireAnswers.class);
                    jsonObj.getAnswersGroupList().add(answersGroup);
                    reader.close();
                } else {
                    jsonObj = new QuestionnaireAnswers();
                    jsonObj.setQuestionnaireId(questionnaireId);
                    List<AnswersGroup> groups = new ArrayList<AnswersGroup>();
                    groups.add(answersGroup);
                    jsonObj.setAnswersGroups(groups);
                }

                String json = gson.toJson(jsonObj);
                FileWriter writer = new FileWriter(fileName);
                writer.write(json);
                writer.close();

                return true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
    
    public void scheduleAlarms(Context context) {
        File dir = getDir();
        File[] files = dir.listFiles(new MyFileNameFilter(".xml"));
        int requestCode = 0;
        for (File file : files) {
            String fileName = file.getName();
            Questionnnaire ques = readQuestionnaire(fileName);
            if (ques != null) {                
                requestCode = scheduleAlarms(context, fileName, ques, requestCode);
            }
        }
    }
    
    @SuppressLint("DefaultLocale")
    public static class MyFileNameFilter implements FilenameFilter {
        
        private String extension;

        public MyFileNameFilter(String extension) {
            this.extension = extension.toLowerCase();
        }

        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(extension);
        }
        
    }
    
    private int scheduleAlarms(Context context, String fileName, Questionnnaire ques, int requestCode) {
        List<String> times = ques.getTriggers().getTimes();
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(fileName, Uri.parse(fileName), context, AlarmReceiver.class);
        intent.putExtra(Constants.QUESTIONNAIRE_FILE, fileName);
        
        Log.d("QuestionnaireManager", "requestCode: " + requestCode + ", fileName: " + fileName);

        for (String time : times) {
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            requestCode++;
            int dotsPos = time.indexOf(":");
            int hour = Integer.valueOf(time.substring(0, dotsPos));
            int min = Integer.valueOf(time.substring(dotsPos + 1));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, min);
            // if alarm is in the past add one day
            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);            
            Log.d("QuestionnaireManager", "alarmset: " + hour + ":"+ min);
        }
        
        return requestCode;
    }

}
