package io.github.erictanggit.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class DescriptionAnonymizationRules {
	private String changeTo;
	private String name;

	public DescriptionAnonymizationRules(String name, String functionAnonymization) {
		this.name = name;
		changeTo = functionAnonymization;
	}

	public String getAnonymazation() {
		return changeTo;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return new ToStringBuilder(this).append("Name :", name).append("Anonyization rule :", changeTo).toString();
	}
}
