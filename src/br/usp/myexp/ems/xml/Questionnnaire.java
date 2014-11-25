package br.usp.myexp.ems.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Questionnnaire {
    
    @Element
    private String name;
    
    @Element
    private String id;

    @Element
    private Questions questions;
    
    @Element(name = "triggers")
    private Triggers triggers;

    @Attribute(required = false)
    private String noNamespaceSchemaLocation = "questions_model.xsd";

    public Questionnnaire() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Questions getQuestions() {
        return questions;
    }

    public void setQuestions(Questions questions) {
        this.questions = questions;
    }

    public Triggers getTriggers() {
        return triggers;
    }

    public void setTriggers(Triggers triggers) {
        this.triggers = triggers;
    }

}
