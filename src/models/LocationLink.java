package models;
public class LocationLink {

	Location source;
	public Location target;
	public int timeInMinutes;

	public LocationLink(Location source, Location target, int timeInMinutes) {
		this.source = source;
		this.target = target;
		this.timeInMinutes = timeInMinutes;
	}

	@Override
	public String toString() {
		return super.toString() + " [" + this.source.name + ", " + this.target.name + ", " + this.timeInMinutes + "]";
	}

}
