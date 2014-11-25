package br.usp.myexp.ems.xml;

import java.util.List;

import org.simpleframework.xml.ElementList;

public class Triggers {
    
    @ElementList(entry = "time", inline = true, required = false)
    private List<String> times;
    
    @ElementList(entry = "context", inline = true, required = false)
    private List<String> contexts;
    
    public Triggers () {
        
    }

    public List<String> getTimes() {
        return times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
    }

    public List<String> getContexts() {
        return contexts;
    }

    public void setContext(List<String> contexts) {
        this.contexts = contexts;
    }

}
