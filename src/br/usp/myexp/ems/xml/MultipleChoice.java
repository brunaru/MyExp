package br.usp.myexp.ems.xml;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class MultipleChoice extends QuestionText {

    @ElementList(entry = "option", inline = true)
	private List<String> options;
	
	@Attribute
	private Boolean onlyone = true;
	
	public MultipleChoice() {
		
	}
	
	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	public Boolean getOnlyone() {
		return onlyone;
	}

	public void setOnlyone(Boolean onlyone) {
		this.onlyone = onlyone;
	}

}
