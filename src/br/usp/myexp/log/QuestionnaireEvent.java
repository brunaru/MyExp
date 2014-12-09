package br.usp.myexp.log;

public class QuestionnaireEvent {

    private String questionnaireId;

    private String time;

    private String event;

    private String questionNumber;

    private String viewText;

    private String answer;

    public QuestionnaireEvent(String questionnaireId, String time, String event, String questionNumber, String viewText, String answer) {
        this.questionnaireId = questionnaireId;
        this.time = time;
        this.event = event;
        this.questionNumber = questionNumber;
        this.viewText = viewText;
        this.answer = answer;
    }
    
    public QuestionnaireEvent(String questionnaireId, String time, String event, String questionNumber, String viewText) {
        this.questionnaireId = questionnaireId;
        this.time = time;
        this.event = event;
        this.questionNumber = questionNumber;
        this.viewText = viewText;
        this.answer = "-";
    }
    
    public QuestionnaireEvent(String questionnaireId, String time, String event, String questionNumber) {
        this.questionnaireId = questionnaireId;
        this.time = time;
        this.event = event;
        this.questionNumber = questionNumber;
        this.viewText = "-";
        this.answer = "-";
    }

    public String getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(String questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(String questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getViewText() {
        return viewText;
    }

    public void setViewText(String viewText) {
        this.viewText = viewText;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
