# Metro Route Optimization Program

## Overview
This Java-based application is a comprehensive Metro Route Optimization Program designed to assist users in navigating a metro system efficiently. The program offers various features such as shortest path calculation, alternative route suggestions, fare computation, and more. It is designed with modularity, user-friendliness, and scalability in mind.

## Features
- **Shortest Path Calculation**: Uses Dijkstra's Algorithm to find the most efficient route between two stations.
- **Alternative Routes**: Provides additional routes using Depth-First Search (DFS) for users who prefer alternatives.
- **Transfer Minimization**: Ensures fewer station transfers for a smoother journey.
- **Fare Calculation**: Calculates fares with built-in discount options for students, senior citizens, and others.
- **Dynamic Updates**: Supports changes to metro routes and fare structures.
- **User Interaction**: Friendly console-based interaction for entering station names, preferences, and more.

## Requirements
- **Java Development Kit (JDK)**: Version 8 or above
- **IDE (Optional)**: IntelliJ IDEA, Eclipse, or any Java IDE
- **Operating System**: Windows, macOS, or Linux

## Installation
1. Clone this repository:
   ```bash
   git clone <repository_url>
   ```
2. Navigate to the project directory:
   ```bash
   cd metro-route-optimization
   ```
3. Compile the program:
   ```bash
   javac MetroRouteOptimization.java
   ```
4. Run the program:
   ```bash
   java MetroRouteOptimization
   ```

## Usage
1. Run the program in a terminal or IDE.
2. Enter the names of the starting and destination stations.
3. Select options for additional features, such as alternative routes or fare discounts.
4. View the suggested routes, fare, and travel details.

## File Structure
- `MetroRouteOptimization.java`: Main entry point for the application.
- `Passenger.java`: Handles passenger-related operations such as fare discounts.
- `MetroGraph.java`: Manages the metro network graph, including stations and connections.

## Example Output
```plaintext
Welcome to Metro Route Optimization Program!
Enter the starting station: A
Enter the destination station: F
The shortest route is: A -> B -> D -> F
Total Fare: $5.00
Would you like to see alternative routes? (yes/no): yes
Alternative Route 1: A -> C -> E -> F
Total Fare: $6.00
Thank you for using the Metro Route Optimization Program!
```

## Customization
- **Adding Stations**: Update the `stationMap` in `MetroGraph`.
- **Adjusting Fares**: Modify the `calculateFareWithDiscounts` method in `Passenger`.
- **Changing Routes**: Use the `updateRoute` method in `MetroGraph` to add or remove connections.

## Future Enhancements
- Real-time metro status updates via APIs.
- Visual route mapping using a graphical interface.
- Integration with mobile applications for enhanced user experience.

## Contribution
Contributions are welcome! Follow these steps to contribute:
1. Fork the repository.
2. Create a new branch for your feature or bug fix:
   ```bash
   git checkout -b feature-name
   ```
3. Commit your changes:
   ```bash
   git commit -m "Add new feature"
   ```
4. Push to your branch:
   ```bash
   git push origin feature-name
   ```
5. Open a pull request.

## License
This project is licensed under the MIT License. See the `LICENSE` file for details.
