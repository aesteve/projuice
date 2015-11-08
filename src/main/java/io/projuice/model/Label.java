package io.projuice.model;

public class Label implements Comparable<Label> {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(Label other) {
		if (other == null) {
			return -1;
		}
		return name.compareTo(other.getName());
	}
}
