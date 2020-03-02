package io.github.erictanggit.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class DescriptionRules {

	private List<String> should = new ArrayList<>();
	private String name;

	public DescriptionRules(String name, List<String> list) {
		this.name = name;
		should = list;
	}

	public List<String> getRules() {
		return should;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("Name :", name).append("List rules :", Arrays.asList(should))
				.toString();
	}

}
