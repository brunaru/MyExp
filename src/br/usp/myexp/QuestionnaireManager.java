package br.usp.myexp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.os.Environment;
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

}
