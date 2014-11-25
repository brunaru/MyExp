package br.usp.myexp.ems.xml;

import java.util.List;

import org.simpleframework.xml.ElementList;

public class Questions {
    
    @ElementList(entry = "opentext", inline = true, required = false)
    private List<OpenText> openTextQuestions;

    @ElementList(entry = "multiplechoice", inline = true, required = false)
    private List<MultipleChoice> multipleChoiceQuestions;
    
    public Questions() {
        
    }
    
    public List<OpenText> getOpenTextQuestions() {
        return openTextQuestions;
    }

    public void setOpenTextQuestions(List<OpenText> openTextQuestions) {
        this.openTextQuestions = openTextQuestions;
    }

    public List<MultipleChoice> getMultipleChoiceQuestions() {
        return multipleChoiceQuestions;
    }

    public void setMultipleChoiceQuestions(List<MultipleChoice> multipleChoiceQuestions) {
        this.multipleChoiceQuestions = multipleChoiceQuestions;
    }

}
