package io.github.erictanggit.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class DescriptionCSV {

	private String name;
	private Type dataType; // Data dataType for this column

	public DescriptionCSV(String name, Type dataType) {
		this.name = name;
		this.dataType = dataType;
	}

	public Type getType() {
		return dataType;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("Name :", name).append("Column :", dataType).toString();
	}

}
