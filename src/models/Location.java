package models;

public class Location {

	public String name;

	public Location(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return super.toString() + " [ " + this.name + "]";
	}

}
