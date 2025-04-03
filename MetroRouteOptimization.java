/*DS PROJECT    METRO ROUTE OPTIMIZATION
 * TEAM MEMBERS:
 * UCE2023528:Namrata Hirani
 * UCE2023537:Mitva Gami
 * UCE2023539:Mohini Kocheri
 * UCE2023542:Nishita Katepallewar

 * */

package MetroRouteOptimization;
import java.util.*;

class Passenger {
	private String name;
	private int age;
	private long phoneNo;
	private double billAmount;
	private boolean isStudent, isSenior;

	Passenger(String name, int age, long phoneNo) {
		this.name = name;
		this.age = age;
		this.phoneNo = phoneNo;
		this.billAmount = 0;
		this.isStudent = age < 25; // assuming student if age is below 25
		this.isSenior = age > 60; // assuming senior if age is above 60
	}

	public void setBillAmount(double billAmount) {
		this.billAmount = billAmount;
	}

	public double getBillAmount() {
		return billAmount;
	}

	public boolean isStudent() {
		return isStudent;
	}

	public boolean isSenior() {
		return isSenior;
	}

	public String getName() {
		return name;
	}

	public long getPhoneNo() {
		return phoneNo;
	}
	public int getAge() {
		return age;
	}
}


class MetroGraph {
	private int numStations;
	private List<List<Edge>> adjList;
	private Map<String, Integer> stationMap;
	private String[] stationNames;

	public MetroGraph(int numStations) {
		this.numStations = numStations;
		adjList = new ArrayList<>(numStations);
		for (int i = 0; i < numStations; i++) {
			adjList.add(new ArrayList<>());
		}
		stationMap = new HashMap<>();
		stationNames = new String[numStations];
	}

	public void addStation(String name, int id) {
		stationMap.put(name, id);
		stationNames[id] = name;
	}

	public void addEdge(int u, int v, int weight) {
		adjList.get(u).add(new Edge(v, weight));
		adjList.get(v).add(new Edge(u, weight)); // Bi-directional edges
	}

	// Path Information Display using Dijkstra's Algorithm
	public void displayRouteDetails(String startName, String endName, boolean timeOptimized) {
	    // Check if station names are valid
	    if (!stationMap.containsKey(startName) || !stationMap.containsKey(endName)) {
	        System.out.println("Invalid station name(s): " + startName + ", " + endName);
	        return;
	    }

	    int start = stationMap.get(startName);
	    int end = stationMap.get(endName);

	    List<Integer> path = findShortestPath(start, end, timeOptimized);

	    if (path == null || path.isEmpty()) {
	        System.out.println("No path found.");
	        return;
	    }

	    System.out.print("Route: ");
	    for (int station : path) {
	        System.out.print(stationNames[station] + " -> ");
	    }
	    System.out.println("End");
	}


	public List<Integer> findShortestPath(String startName, String endName, boolean timeOptimized) {
		return findShortestPath(stationMap.get(startName), stationMap.get(endName), timeOptimized);
	}

	// Dijkstra's Algorithm to find the shortest path
	private List<Integer> findShortestPath(int start, int end, boolean timeOptimized) {
		int[] distances = new int[numStations];
		int[] previous = new int[numStations];
		Arrays.fill(distances, Integer.MAX_VALUE);
		Arrays.fill(previous, -1);
		distances[start] = 0;

		PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
		pq.offer(new Edge(start, 0));

		while (!pq.isEmpty()) {
			Edge current = pq.poll();
			int u = current.to;

			if (u == end)
				break;

			for (Edge edge : adjList.get(u)) {
				int v = edge.to;
				int weight = edge.weight;

				if (distances[u] + weight < distances[v]) {
					distances[v] = distances[u] + weight;
					previous[v] = u;
					pq.offer(new Edge(v, distances[v]));
				}
			}
		}

		List<Integer> path = new ArrayList<>();
		for (int at = end; at != -1; at = previous[at]) {
			path.add(at);
		}
		Collections.reverse(path);
		return path.get(0) == start ? path : null;
	}

	// Alternative Routes by finding all paths
	public void findAlternativeRoutes(String startName, String endName) {
		int start = stationMap.get(startName);
		int end = stationMap.get(endName);

		List<List<Integer>> allRoutes = new ArrayList<>();
		List<Integer> currentPath = new ArrayList<>();
		boolean[] visited = new boolean[numStations];

		findAllPathsDFS(start, end, visited, currentPath, allRoutes);

		int routeNum = 1;
		for (List<Integer> route : allRoutes) {
			System.out.print("Alternative Route " + routeNum++ + ": ");
			for (int station : route) {
				System.out.print(stationNames[station] + " -> ");
			}
			System.out.println("End");
		}
	}

	// DFS helper method to find all paths
	private void findAllPathsDFS(int current, int destination, boolean[] visited, List<Integer> currentPath,
			List<List<Integer>> allRoutes) {
		visited[current] = true;
		currentPath.add(current);

		if (current == destination) {
			allRoutes.add(new ArrayList<>(currentPath));
		} else {
			for (Edge edge : adjList.get(current)) {
				if (!visited[edge.to]) {
					findAllPathsDFS(edge.to, destination, visited, currentPath, allRoutes);
				}
			}
		}

		currentPath.remove(currentPath.size() - 1);
		visited[current] = false;
	}

	// Transfer Minimization
	public void findMinimalTransferPath(Passenger passenger, String startName, String endName) {
		int start = stationMap.get(startName);
		int end = stationMap.get(endName);

		List<Integer> path = findShortestPath(start, end, false); // Simple Dijkstra, further customization needed
		System.out.print("Minimal Transfer Path: ");
		for (int station : path) {
			System.out.print(stationNames[station] + " -> ");
		}
		System.out.println("End");

		generateBill(passenger, startName, endName);
	}
	
	public void generateBill(Passenger passenger, String startName, String endName) {
		double fare = calculateFareWithDiscounts(startName, endName, passenger.isStudent(), passenger.isSenior());
		passenger.setBillAmount(fare);

	    // Print detailed bill receipt
	    System.out.println("\n=========== METRO BILL RECEIPT ===========");
	    System.out.printf("%-25s: %s\n", "Passenger Name", passenger.getName());
	    System.out.printf("%-25s: %d\n", "Age", passenger.getAge());
	    System.out.printf("%-25s: %d\n", "Phone Number", passenger.getPhoneNo());
	    System.out.println("------------------------------------------");
	    System.out.printf("%-25s: %s\n", "From Station", startName);
	    System.out.printf("%-25s: %s\n", "To Station", endName);
	    System.out.println("------------------------------------------");
	    if (passenger.isStudent()) {
	        System.out.printf("%-25s: %.0f%%\n", "Student Discount", 20.0);
	    }
	    if (passenger.isSenior()) {
	        System.out.printf("%-25s: %.0f%%\n", "Senior Citizen Discount", 25.0);
	    }
	    
	    System.out.println("------------------------------------------");
	    System.out.printf("%-25s: ₹%.2f\n", "TOTAL AMOUNT", fare);
	    System.out.println("==========================================\n");
	
		
	}
 
	public void changeRoute(Passenger passenger, String startName, String midWayName, String newDestName) {
		if (!isOnPath(startName, midWayName, newDestName)) {
			System.out.println("Midway station is not on the path from start to destination.");
			
		}
		findMinimalTransferPath(passenger, midWayName, newDestName);
		passenger.setBillAmount(calculateFareWithDiscounts(startName, midWayName, passenger.isStudent(),
				passenger.isSenior())
				+ calculateFareWithDiscounts(midWayName, newDestName, passenger.isStudent(), passenger.isSenior()));
	}

	public boolean isOnPath(String startName, String midWayName, String endName) {
		List<Integer> originalPath = findShortestPath(startName, endName, false);
		int mid = stationMap.get(midWayName);
		for (int station : originalPath) {
			if (station == mid) {
				return true;
			}
		}
		return false;
	}
	
	// Fare Calculation with Discounts
	public double calculateFareWithDiscounts(String startName, String endName, boolean isStudent, boolean isSenior) {
		 if (!stationMap.containsKey(startName) || !stationMap.containsKey(endName)) {
		        return 0;
		    }
		int start = stationMap.get(startName);
		int end = stationMap.get(endName);
		
		List<Integer> path = findShortestPath(start, end, true);
		if (path == null || path.isEmpty()) {
	        return 0;
	    }
		
		double baseFare = 20; // Example base fare per segment
		double distanceFactor=10; // Fare multiplier based on distance
		
		double totalFare = baseFare + path.size() * distanceFactor;
		System.out.println("Total Fare:"+totalFare);
		// Apply discounts
		if (isStudent) {
			totalFare =totalFare-totalFare*0.20; // 20% discount for students
		}
		if (isSenior) {
			totalFare =totalFare-totalFare* 0.25; // 25% discount for senior citizens
		}

		return totalFare;
	}


	// Helper Class for Edges
	static class Edge {
		int to, weight;

		public Edge(int to, int weight) {
			this.to = to;
			this.weight = weight;
		}
	}
}

public class MetroRouteOptimization {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		MetroGraph graph = new MetroGraph(22); // Adjust number of stations as needed

		// Add stations

		graph.addStation("CENTRAL SECRETARIAT", 0);
		graph.addStation("PATEL CHOWK", 1);
		graph.addStation("RAJIV CHOWK", 2);
		graph.addStation("MANDI HOUSE", 3);
		graph.addStation("SUPREME COURT", 4);
		graph.addStation("INDRAPRASTHA", 5);
		graph.addStation("YAMUNA BANK", 6);
		graph.addStation("AKSHARDHAM", 7);
		graph.addStation("MAYUR VIHAR", 8);
		graph.addStation("NIZAMUDDIN", 9);
		graph.addStation("ASHRAM", 10);
		graph.addStation("VINOBAPURI", 11);
		graph.addStation("LAJPAT NAGAR", 12);
		graph.addStation("SOUTH EXTENTION", 13);
		graph.addStation("DILLI HAAT", 14);
		graph.addStation("JOR BAGH", 15);
		graph.addStation("LOK KALYAN MARG", 16);
		graph.addStation("UDYOG BHAWAN", 17);
		graph.addStation("KHAN MARKET", 18);
		graph.addStation("JLN STADIUM", 19);
		graph.addStation("JANGPURA", 20);
		graph.addStation("JANPATH", 21);


		// Add other stations similarly...

		// Add edges between metro stations with their respective distances

		graph.addEdge(0, 1, 2);

		graph.addEdge(0, 21, 1);

		graph.addEdge(0, 17, 2);

		graph.addEdge(0, 18, 4);

		graph.addEdge(1, 2, 2);

		graph.addEdge(2, 3, 4);

		graph.addEdge(3, 4, 2);

		graph.addEdge(3, 21, 3);

		graph.addEdge(4, 5, 2);

		graph.addEdge(5, 6, 3);

		graph.addEdge(6, 7, 3);

		graph.addEdge(7, 8, 3);

		graph.addEdge(8, 9, 5);

		graph.addEdge(9, 10, 3);

		graph.addEdge(10, 11, 3);

		graph.addEdge(11, 12, 3);

		graph.addEdge(12, 13, 3);

		graph.addEdge(12, 20, 3);

		graph.addEdge(13, 14, 2);

		graph.addEdge(14, 15, 2);

		graph.addEdge(15, 16, 2);

		graph.addEdge(16, 17, 2);

		graph.addEdge(18, 19, 3);

		graph.addEdge(19, 20, 2);
        
		System.out.println("Enter name of passenger:");
		String name = scanner.nextLine();
		System.out.println("Enter age:");
		int age = scanner.nextInt();
		System.out.println("Enter phone number:");
		long phoneNo = scanner.nextLong();
		scanner.nextLine();
		Passenger passenger = new Passenger(name, age, phoneNo);

		// Add other edges similarly...

		System.out.println("1.Central Secretariat");

		System.out.println("2.Patel Chowk");

		System.out.println("3.Rajiv Chowk");

		System.out.println("4. Mandi House");

		System.out.println("5.Supreme Court");

		System.out.println("6.Indraprastha");

		System.out.println("7.Yamuna Bank");

		System.out.println("8.Akshardham");

		System.out.println("9.Mayur Vihar");

		System.out.println("10.Nizamuddin");

		System.out.println("11.Ashram");

		System.out.println("12.Vinobapuri");

		System.out.println("13.Lajpat Nagar");

		System.out.println("14.South Extention");

		System.out.println("15.Dilli Haat");

		System.out.println("16.Jor Bagh");

		System.out.println("17.Lok Kalyan Marg");

		System.out.println("18.Udyog Bhawan");

		System.out.println("19.Khan Market");

		System.out.println("20.JLN Stadium");

		System.out.println("21.Jangpura");

		System.out.println("22.Janpath");

		System.out.println("Enter start station name:");
		String start = scanner.nextLine().toUpperCase();
		System.out.println("Enter end station name:");
		String end = scanner.nextLine().toUpperCase();

		int choice;
		while(true) {
			System.out.println("\nChoose an option:");
			System.out.println("1. Display Route Details");
			System.out.println("2. Find Alternative Routes");
			System.out.println("3. Find Minimal Transfer Path");
			System.out.println("4. Change Route");
			System.out.println("5. exit");
			

			choice = scanner.nextInt();
			scanner.nextLine(); // Consume newline

			switch (choice) {
			case 1:
				graph.displayRouteDetails(start, end, true);
				break;
			case 2:
				graph.findAlternativeRoutes(start, end);
				break;
			case 3:
				graph.findMinimalTransferPath(passenger,start, end);
				break;
			
			case 4:
				System.out.println("Enter station that you want to get off at:");
				String midWayChange = scanner.nextLine().toUpperCase();
				if (!graph.isOnPath(start, midWayChange, end)) {
					System.out.println("Station " + midWayChange + " does not lie on your path");
				} else {
					System.out.println("Enter new destination station:");
					String newDestChange = scanner.nextLine().toUpperCase();
					graph.changeRoute(passenger, start, midWayChange, newDestChange);
				}
				break;
			case 5:
				System.out.println("Exiting...");
				System.exit(0);
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		}

		
	}
}



/*  OUTPUT
 Enter name of passenger:
Mitva Gami
Enter age:
18
Enter phone number:
8452061962
1.Central Secretariat
2.Patel Chowk
3.Rajiv Chowk
4. Mandi House
5.Supreme Court
6.Indraprastha
7.Yamuna Bank
8.Akshardham
9.Mayur Vihar
10.Nizamuddin
11.Ashram
12.Vinobapuri
13.Lajpat Nagar
14.South Extention
15.Dilli Haat
16.Jor Bagh
17.Lok Kalyan Marg
18.Udyog Bhawan
19.Khan Market
20.JLN Stadium
21.Jangpura
22.Janpath
Enter start station name:
Ashram
Enter end station name:
Jor Bagh

Choose an option:
1. Display Route Details
2. Find Alternative Routes
3. Find Minimal Transfer Path
4. Change Route
5. exit
1
Route: ASHRAM -> VINOBAPURI -> LAJPAT NAGAR -> SOUTH EXTENTION -> DILLI HAAT -> JOR BAGH -> End

Choose an option:
1. Display Route Details
2. Find Alternative Routes
3. Find Minimal Transfer Path
4. Change Route
5. exit
2
Alternative Route 1: ASHRAM -> NIZAMUDDIN -> MAYUR VIHAR -> AKSHARDHAM -> YAMUNA BANK -> INDRAPRASTHA -> SUPREME COURT -> MANDI HOUSE -> RAJIV CHOWK -> PATEL CHOWK -> CENTRAL SECRETARIAT -> UDYOG BHAWAN -> LOK KALYAN MARG -> JOR BAGH -> End
Alternative Route 2: ASHRAM -> NIZAMUDDIN -> MAYUR VIHAR -> AKSHARDHAM -> YAMUNA BANK -> INDRAPRASTHA -> SUPREME COURT -> MANDI HOUSE -> RAJIV CHOWK -> PATEL CHOWK -> CENTRAL SECRETARIAT -> KHAN MARKET -> JLN STADIUM -> JANGPURA -> LAJPAT NAGAR -> SOUTH EXTENTION -> DILLI HAAT -> JOR BAGH -> End
Alternative Route 3: ASHRAM -> NIZAMUDDIN -> MAYUR VIHAR -> AKSHARDHAM -> YAMUNA BANK -> INDRAPRASTHA -> SUPREME COURT -> MANDI HOUSE -> JANPATH -> CENTRAL SECRETARIAT -> UDYOG BHAWAN -> LOK KALYAN MARG -> JOR BAGH -> End
Alternative Route 4: ASHRAM -> NIZAMUDDIN -> MAYUR VIHAR -> AKSHARDHAM -> YAMUNA BANK -> INDRAPRASTHA -> SUPREME COURT -> MANDI HOUSE -> JANPATH -> CENTRAL SECRETARIAT -> KHAN MARKET -> JLN STADIUM -> JANGPURA -> LAJPAT NAGAR -> SOUTH EXTENTION -> DILLI HAAT -> JOR BAGH -> End
Alternative Route 5: ASHRAM -> VINOBAPURI -> LAJPAT NAGAR -> SOUTH EXTENTION -> DILLI HAAT -> JOR BAGH -> End
Alternative Route 6: ASHRAM -> VINOBAPURI -> LAJPAT NAGAR -> JANGPURA -> JLN STADIUM -> KHAN MARKET -> CENTRAL SECRETARIAT -> UDYOG BHAWAN -> LOK KALYAN MARG -> JOR BAGH -> End

Choose an option:
1. Display Route Details
2. Find Alternative Routes
3. Find Minimal Transfer Path
4. Change Route
5. exit
3
Minimal Transfer Path: ASHRAM -> VINOBAPURI -> LAJPAT NAGAR -> SOUTH EXTENTION -> DILLI HAAT -> JOR BAGH -> End
Total Fare:80.0

=========== METRO BILL RECEIPT ===========
Passenger Name           : Mitva Gami
Age                      : 18
Phone Number             : 8452061962
------------------------------------------
From Station             : ASHRAM
To Station               : JOR BAGH
------------------------------------------
Student Discount         : 20%
------------------------------------------
TOTAL AMOUNT             : ₹64.00
==========================================


Choose an option:
1. Display Route Details
2. Find Alternative Routes
3. Find Minimal Transfer Path
4. Change Route
5. exit
4
Enter station that you want to get off at:
LAJPAT NAGAR
Enter new destination station:
Khan Market
Minimal Transfer Path: LAJPAT NAGAR -> JANGPURA -> JLN STADIUM -> KHAN MARKET -> End
Total Fare:60.0

=========== METRO BILL RECEIPT ===========
Passenger Name           : Mitva Gami
Age                      : 18
Phone Number             : 8452061962
------------------------------------------
From Station             : LAJPAT NAGAR
To Station               : KHAN MARKET
------------------------------------------
Student Discount         : 20%
------------------------------------------
TOTAL AMOUNT             : ₹48.00
==========================================

Total Fare:50.0
Total Fare:60.0

Choose an option:
1. Display Route Details
2. Find Alternative Routes
3. Find Minimal Transfer Path
4. Change Route
5. exit
5
Exiting...

 */