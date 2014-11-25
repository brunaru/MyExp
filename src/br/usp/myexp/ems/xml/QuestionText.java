package br.usp.myexp.ems.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class QuestionText {

    @Element
    private Integer number;

    @Element
    private String text;

    public QuestionText() {

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

}
