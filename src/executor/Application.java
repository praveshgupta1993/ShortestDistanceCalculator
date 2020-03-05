package executor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

import models.Location;
import models.LocationLink;

public class Application {

	static final String CURRENT_LOCATION_NAME = "Current";

	public static void main(String[] args) {
		// new Application().executeTest();
		new Application().execute();
	}

	@SuppressWarnings("unused")
	private void executeTest() {
		int destinationCount = 5;
		Set<Location> locations = new HashSet<>(destinationCount);

		Location currentLocation = new Location(CURRENT_LOCATION_NAME);
		locations.add(currentLocation);

		Location walmart = new Location("walmart");
		Location superC = new Location("super c");
		Location jeanCoute = new Location("jean coute");
		Location iga = new Location("iga");
		locations.add(walmart);
		locations.add(superC);
		locations.add(jeanCoute);
		locations.add(iga);

		Map<Location, Set<LocationLink>> locationLinksMapping = new HashMap<>();

		Set<LocationLink> currentLinks = new HashSet<>();
		LocationLink currentToWalmart = new LocationLink(currentLocation, walmart, 36);
		LocationLink currentToSuperC = new LocationLink(currentLocation, superC, 17);
		LocationLink currentToJean = new LocationLink(currentLocation, jeanCoute, 5);
		LocationLink currentToIga = new LocationLink(currentLocation, iga, 6);
		currentLinks.add(currentToWalmart);
		currentLinks.add(currentToSuperC);
		currentLinks.add(currentToJean);
		currentLinks.add(currentToIga);
		locationLinksMapping.put(currentLocation, currentLinks);

		Set<LocationLink> walmartLinks = new HashSet<>();
		LocationLink walmartToCurrent = new LocationLink(walmart, currentLocation, 36);
		LocationLink walmartToSuperC = new LocationLink(walmart, superC, 35);
		LocationLink walmartToJean = new LocationLink(walmart, jeanCoute, 31);
		LocationLink walmartToIGA = new LocationLink(walmart, iga, 38);
		walmartLinks.add(walmartToCurrent);
		walmartLinks.add(walmartToSuperC);
		walmartLinks.add(walmartToJean);
		walmartLinks.add(walmartToIGA);
		locationLinksMapping.put(walmart, walmartLinks);

		Set<LocationLink> superCLinks = new HashSet<>();
		LocationLink superCToCurrent = new LocationLink(superC, currentLocation, 36);
		LocationLink superCToWalmart = new LocationLink(superC, walmart, 35);
		LocationLink superCToJean = new LocationLink(superC, jeanCoute, 17);
		LocationLink superCToIGA = new LocationLink(superC, iga, 17);
		superCLinks.add(superCToCurrent);
		superCLinks.add(superCToWalmart);
		superCLinks.add(superCToJean);
		superCLinks.add(superCToIGA);
		locationLinksMapping.put(superC, superCLinks);

		Set<LocationLink> jeanCouteLinks = new HashSet<>();
		LocationLink jeanCouteToCurrent = new LocationLink(jeanCoute, currentLocation, 6);
		LocationLink jeanCouteToWalmart = new LocationLink(jeanCoute, walmart, 32);
		LocationLink jeanCouteToSuperC = new LocationLink(jeanCoute, superC, 17);
		LocationLink jeanCouteToIGA = new LocationLink(jeanCoute, iga, 11);
		jeanCouteLinks.add(jeanCouteToCurrent);
		jeanCouteLinks.add(jeanCouteToWalmart);
		jeanCouteLinks.add(jeanCouteToSuperC);
		jeanCouteLinks.add(jeanCouteToIGA);
		locationLinksMapping.put(jeanCoute, jeanCouteLinks);

		Set<LocationLink> igaLinks = new HashSet<>();
		LocationLink igaToCurrent = new LocationLink(iga, currentLocation, 6);
		LocationLink igaToWalmart = new LocationLink(iga, walmart, 41);
		LocationLink igaToSuperC = new LocationLink(iga, superC, 17);
		LocationLink igaTojeanCoute = new LocationLink(iga, jeanCoute, 11);
		igaLinks.add(igaToCurrent);
		igaLinks.add(igaToWalmart);
		igaLinks.add(igaToSuperC);
		igaLinks.add(igaTojeanCoute);
		locationLinksMapping.put(iga, igaLinks);

		executeInternal(locations, currentLocation, locationLinksMapping);

	}

	private void execute() {
		Scanner sc = new Scanner(System.in);

		System.out.print("Enter number of destinations to reach: ");
		int destinationCount = Integer.parseInt(sc.nextLine()) + 1;

		Set<Location> locations = new HashSet<>(destinationCount);

		Location currentLocation = new Location(CURRENT_LOCATION_NAME);
		locations.add(currentLocation);

		for (int i = 1; i < destinationCount; i++) {
			System.out.print("Enter destination name: ");
			locations.add(new Location(sc.nextLine()));
		}

		Map<Location, Set<LocationLink>> locationLinksMapping = new HashMap<>();

		for (Location source : locations) {
			for (Location target : locations) {
				if (!source.equals(target)) {
					System.out.print("Enter time in minutest from " + source.name + " to " + target.name + ": ");
					LocationLink locationLink = new LocationLink(source, target, sc.nextInt());
					Set<LocationLink> locationLinks = locationLinksMapping.get(source);
					if (Objects.isNull(locationLinks)) {
						locationLinks = new HashSet<>();
					}
					locationLinks.add(locationLink);
					locationLinksMapping.put(source, locationLinks);
				}
			}
		}
		sc.close();

		executeInternal(locations, currentLocation, locationLinksMapping);

	}

	void executeInternal(Set<Location> locations, Location currentLocation,
			Map<Location, Set<LocationLink>> locationLinksMapping) {
		Map<Location, Boolean> locationVisistedMappings = new HashMap<>();

		for (Location loc : locations) {
			locationVisistedMappings.put(loc, Boolean.FALSE);
		}

		Location currentNode = currentLocation;

		List<Location> orderedLocationsToTraverse = new LinkedList<>();
		Map<Location, LocationLink> orderedLocationsToTraverseLinks = new HashMap<>();

		long unvisitedNodesNumber = locationVisistedMappings.values().stream().filter(p -> Boolean.FALSE.equals(p))
				.count();
		while (unvisitedNodesNumber > 0) {
			Set<LocationLink> locationLinks = locationLinksMapping.get(currentNode);
			int preferredLinkTime = -1;
			Location preferredTargetLocation = null;
			LocationLink preferredLink = null;
			for (LocationLink link : locationLinks) {
				if (link.target.name.equals(CURRENT_LOCATION_NAME) && unvisitedNodesNumber > 1) {
					continue;
				} else {
					if (Boolean.FALSE.equals(locationVisistedMappings.get(link.target))) {
						boolean isTargetApplicable = false;
						if (preferredLinkTime != -1) {
							if (link.timeInMinutes < preferredLinkTime) {
								isTargetApplicable = true;
							}
						} else {
							isTargetApplicable = true;
						}
						if (isTargetApplicable) {
							preferredTargetLocation = link.target;
							preferredLink = link;
							preferredLinkTime = link.timeInMinutes;
						}
					}
				}
			}
			orderedLocationsToTraverse.add(preferredTargetLocation);
			orderedLocationsToTraverseLinks.put(preferredTargetLocation, preferredLink);
			locationVisistedMappings.put(preferredTargetLocation, Boolean.TRUE);
			currentNode = preferredTargetLocation;
			unvisitedNodesNumber = locationVisistedMappings.values().stream().filter(p -> Boolean.FALSE.equals(p))
					.count();
		}

		System.out.print("[" + CURRENT_LOCATION_NAME + "]");
		for (Location loc : orderedLocationsToTraverse) {
			System.out.print(
					" --- (" + orderedLocationsToTraverseLinks.get(loc).timeInMinutes + ") ---> [" + loc.name + "]");
		}
	}

}
