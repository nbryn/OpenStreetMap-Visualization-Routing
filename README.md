# OpenStreetMap Routing Service

## Summary
Java desktop application which can load user(or default) specified data(zip, .osm) in XML format from <link> www.openstreetmap.org</link>.

The application parses the data and creates a visual representation of it in the GUI after which the user can zoom, search for locations, specify interest points
and get the fastest(cars) or shortest(bicycle) route between two locations.

The application also provides search suggestions based on the users input.
The application only paints the area which are currently visible to the user. This improve performance in situations where an user loads e.g. Denmark and zooms in on a specific area. 

In order to achieve all of this the application includes custom implementations of well known Algorithms and Data Structures such as Dijkstra's Shortest Path Algorithm,
KD-Tree and Ternary Search Tree.

<h3 align="center">Picture of the app showing a route</h3>
<p align="center">
<img  src="https://user-images.githubusercontent.com/44057369/94069316-a591de00-fdf0-11ea-93c6-dec6a9921bc0.png"  width="50%" height="475"/> 
</p>

 ## Algorithms and Data Structures
 * Dijkstra's Algorithm - Algorithm for finding the shortest paths between nodes in a graph. Used for routing.
 * A* - Extension of Dijkstra's Algorithm. A* achieves better performance by using heuristics to guide its search. Also used for routing.
 * KD-Tree - k-d tree (short for k-dimensional tree) is a space-partitioning data structure for organizing points in a k-dimensional space.
 * Ternary Search Tree(TST) - TST is a type of trie (prefix tree) where nodes are arranged in a manner similar to a binary search tree.
 
 ## Tech 
* Java - Class-based, object-oriented and general-purpose programming language used to develop the application.
* JavaFX - Java GUI library for creating applications that can run across multiple platforms and devices.
* JUnit - Unit testing framework for Java.
* Gradle - Build automation tool that manages the build and it's dependencies. 

## Setup
1. Clone the repo
```sh
git clone https://github.com/nbryn/osm-visualization-routing.git
```
2. Open an IDE like e.g. Intellij
3. Click 'Import Project' and choose the root folder of the cloned project
4. Select 'Gradle' when the 'Import Project' screen opens
5. Run the application using the following command 
```sh
./gradlew run
```
