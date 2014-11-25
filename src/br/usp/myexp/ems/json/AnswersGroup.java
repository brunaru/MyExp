package br.usp.myexp.ems.json;

import java.util.List;

public class AnswersGroup {
    
    private long dateMs;
    
    public AnswersGroup() {
    }

    private List<Answer> answers;

    public long getDateMs() {
        return dateMs;
    }

    public void setDateMs(long dateMs) {
        this.dateMs = dateMs;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

}
