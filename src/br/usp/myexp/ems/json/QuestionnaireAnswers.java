package br.usp.myexp.ems.json;

import java.util.List;

public class QuestionnaireAnswers {
    
    private String questionnaireId;
    
    private String userId;
    
    private List<AnswersGroup> answersGroupList;

    public QuestionnaireAnswers() {
    }

    public QuestionnaireAnswers(String questionnaireId, String userId, List<AnswersGroup> answersGroupList) {
        this.questionnaireId = questionnaireId;
        this.userId = userId;
        this.answersGroupList = answersGroupList;
    }

    public String getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(String questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public List<AnswersGroup> getAnswersGroupList() {
        return answersGroupList;
    }

    public void setAnswersGroups(List<AnswersGroup> answersGroupList) {
        this.answersGroupList = answersGroupList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
