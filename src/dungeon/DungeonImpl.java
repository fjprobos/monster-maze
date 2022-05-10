package dungeon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Probably the largest class of this program. Holds most of the elements together: 
 * locations, edges, and important algorithms of the system.
 * It makes all the physical elements work together.
 *
 */
public class DungeonImpl implements Dungeon {
  
  private final int rows;
  private final int columns;
  private final boolean wrapped;
  private final int interConnectivity;
  private final boolean[][][][] finalConnections;
  private Location[][] locations;
  private final List<Edge> potentialEdges;
  private final List<Edge> selectedEdges;
  private final int[][][][] shortestDistances;
  private final List<Location> initialTreasureCaves;
  private final Location start;
  private final Location finish;
  private final Random randomGenerator;
  private final List<Monster> monsters;
  private final List<Location> monsterLocations;
  private final List<Location> initialArrowsLocations;
  
  
  /**
   * Constructor of the dungeon. It uses several helper methods to sequentially create the
   * dungeon given the required connectivity. It uses first potential edges as a start point
   * for Kruskal's algorithm and then gets the final edges after that part is runned.
   * @param rows number of rows of the dungeon
   * @param columns number of columns of the dungeon
   * @param interConnectivity connectivity level of the dungeon
   * @param wrapped boolean that indicates if the dungeon can have wrapped connections
   * @param treasurePercentage number between 0 and 100 indicating the percentage 
   *     of caves containing
   *     treasures and the percentage of locations containing crooked arrows.
   * @param monsterNumber int that indicates the initial number of monsters in 
   *     the dungeon. It cannot be less than 1.
   * @param randomSeed long used to initialize pseudorandom number series to be
   *     used for testing purposes
   * @param isTest indicates if the instance of the game is for testing purposes
   */
  public DungeonImpl(int rows, int columns, boolean wrapped, 
      int interConnectivity, int treasurePercentage, int monsterNumber, long randomSeed,
      boolean isTest) throws IllegalArgumentException {
    this.rows = rows;
    this.columns = columns;
    this.wrapped = wrapped;
    this.interConnectivity = interConnectivity;
    boolean[][][][] initialConnections;
    this.randomGenerator = new Random();
    if (isTest) {
      this.randomGenerator.setSeed(randomSeed);
    }
    initialConnections = this.createConnections(rows, columns, wrapped, interConnectivity);
    this.locations = this.createLocations(rows, columns, wrapped, 
        interConnectivity, initialConnections);
    this.potentialEdges = this.createEdges();
    ArrayList<Edge>[] aux = this.implemtKruskal();
    //ArrayList<Edge> discardedEdges;
    this.selectedEdges = aux[0];
    //discardedEdges = aux[1];
    this.finalConnections = this.updateConnections(rows, columns, wrapped, interConnectivity);
    this.locations = this.createLocations(rows, columns, wrapped, 
        interConnectivity, this.finalConnections);
    this.shortestDistances = this.calculateShortestDistances();
    this.initialTreasureCaves = this.locateTreasures(this.locations, treasurePercentage);
    Location[] startFinish = this.setStartEnd();
    this.start = startFinish[0];
    this.finish = startFinish[1];
    this.monsters = this.setMonsters(monsterNumber);
    this.monsterLocations = new ArrayList<Location>();
    this.monsters.stream().forEach(m -> this.monsterLocations.add(m.getLocation()));
    this.initialArrowsLocations = this.locateArrows(this.locations, treasurePercentage);
    
  }

  @Override
  public Location[][] getLocations() {
    return this.locations;
  }

  @Override
  public boolean checkConnections(Location a, Location b) throws IllegalArgumentException {
    if (a == null || b == null) {
      throw new IllegalArgumentException("Locations cannot be null.");
    }
    return this.finalConnections[a.getRow()][a.getColumn()][b.getRow()][b.getColumn()];
  }

  @Override
  public int getRows() {
    return this.rows;
  }

  @Override
  public int getColumns() {
    return this.columns;
  }

  @Override
  public int getInterconnectivityIndex() {
    return this.interConnectivity;
  }

  @Override
  public boolean getWrapped() {
    return this.wrapped;
  }
  
  /**
   * Helper method to create the locations objects and locate them in 
   * a two dimensional matrix that indicates their physical position in the dungeon,
   * in terms of rows and columns.
   * @param rows int number with the number of rows of the dungeon.
   * @param columns int number with the number of columns of the dungeon.
   * @param wrapped boolean that indicates if the dungeon can have wrapped connections
   * @param interConnectivity connectivity level of the dungeon
   * @param connections fourth dimensional array already set indicating for each pair of locations
   *     if there will be a connection between them.
   * @return a two dimensional matrix that indicates their physical position in the dungeon,
   *     in terms of rows and columns.
   * @throws IllegalArgumentException when connections are null or rows, cols non-positive
   */
  private Location[][] createLocations(int rows, int columns, 
      boolean wrapped, int interConnectivity,  boolean[][][][] connections) 
          throws IllegalArgumentException {
    
    if (rows < 1 || columns < 1) {
      throw new IllegalArgumentException("Rows and columns need to be positive integers.");
    }
    if (connections == null) {
      throw new IllegalArgumentException("Connections cannot be null.");
    }
    
    Location[][] locations = new Location[rows][columns];
    
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        int connectionsNumber = 0;
        int south;
        int north;
        int east;
        int west;
        
        // North-south boundary control
        
        if (i == rows - 1) {
          south = 0;
          north = i - 1;
        }
        else if (i == 0) {
          south = i + 1;
          north = rows - 1;
        }
        else {
          south = i + 1;
          north = i - 1;
        }
        
        //East-West boundary control
        
        if (j == columns - 1) {
          east = 0;
          west = j - 1;
        }
        else if (j == 0) {
          east = j + 1;
          west = columns - 1;
        }
        else {
          east = j + 1;
          west = j - 1;
        }
        
        
        boolean southConnection = connections[i][j][south][j];
        boolean northConnection = connections[i][j][north][j];
        boolean eastConnection = connections[i][j][i][east];
        boolean westConnection = connections[i][j][i][west];

        connectionsNumber += southConnection ? 1 : 0;
        connectionsNumber += northConnection ? 1 : 0;
        connectionsNumber += eastConnection ? 1 : 0;
        connectionsNumber += westConnection ? 1 : 0;
        
        if (connectionsNumber == 2) {
          locations[i][j] = new Tunnel(i, j, northConnection, 
              southConnection, eastConnection, westConnection,
              north, south, east, west);
        }
        else {
          locations[i][j] = new Cave(i, j, northConnection, 
              southConnection, eastConnection, westConnection,
              north, south, east, west);
        }
        
      }
    }
    
    return locations;
  }
  
  /**
   * Helper method that judges for each pair of nodes if they should be connected.
   * This depends on randomness but also the logic of the dungeon creation, if it is wrapped 
   * and the interconnectivity index. This connections are created BEFORE running Kruskal's 
   * algorithm.
   * @param rows int number with the number of rows of the dungeon.
   * @param columns int number with the number of columns of the dungeon.
   * @param wrapped boolean that indicates if the dungeon can have wrapped connections
   * @param interConnectivity connectivity level of the dungeon
   * @return a four dimensional matrix that indicates their a pair of edges referenced
   *     by their numerical position indexes, are connected.
   * @throws IllegalArgumentException if rows or cols are non positive
   */
  private boolean[][][][] createConnections(int rows, int columns, 
      boolean wrapped, int interConnectivity) throws IllegalArgumentException {
    
    if (rows < 1 || columns < 1) {
      throw new IllegalArgumentException("Rows and columns need to be positive integers.");
    }
    
    // We we start with a preset matrix entirely connected.
    boolean[][][][] connections = new boolean[rows][columns][rows][columns];
    Random rd = new Random();
    
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        int[][] neighbourIndexes = this.getNeighbourIndexes(i, j, wrapped);
        boolean[] aux = new boolean[4];
        for (int k = 0; k < 4; k++) {
          int neighbourRow = neighbourIndexes[k][0];
          int neighbourCol = neighbourIndexes[k][1];
          if (neighbourRow == -1 || neighbourCol == -1) {
            continue;
          }
          else {
            aux[k] = rd.nextBoolean();
            connections[i][j][neighbourRow][neighbourCol] = aux[k];
          }
        }
        // If no connection was generated, we add one.
        if (!Arrays.asList(aux).contains(true)) {
          boolean aux2 = true;
          while (aux2) {
            int randomNum = this.randomGenerator.nextInt(3 + 1);
            int neighbourRow = neighbourIndexes[randomNum][0];
            int neighbourCol = neighbourIndexes[randomNum][1];
            if (neighbourRow == -1 || neighbourCol == -1) {
              continue;
            }
            else {
              connections[i][j][neighbourRow][neighbourCol] = true;
              aux2 = false;
            }
            
          }
        }
      }
    }
    
    return connections;
  }
  
  /**
   * Helper method that create edge objects for each pair of locations that are connected.
   * @return an ArrayList with all edges inside.
   */
  private ArrayList<Edge> createEdges() {
    ArrayList<Edge> edges = new ArrayList<Edge>();
    
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        if (this.locations[i][j].getNorth() != null) {
          int[] destIndex = locations[i][j].getNorth();
          int row = destIndex[0];
          int col = destIndex[1];
          Edge edge = new EdgeImpl(locations[i][j], locations[row][col]);
          edges.add(edge);
        }
        if (this.locations[i][j].getSouth() != null) {
          int[] destIndex = locations[i][j].getSouth();
          int row = destIndex[0];
          int col = destIndex[1];
          Edge edge = new EdgeImpl(locations[i][j], locations[row][col]);
          edges.add(edge);
        }
        if (this.locations[i][j].getEast() != null) {
          int[] destIndex = locations[i][j].getEast();
          int row = destIndex[0];
          int col = destIndex[1];
          Edge edge = new EdgeImpl(locations[i][j], locations[row][col]);
          edges.add(edge);
        }
        if (this.locations[i][j].getWest() != null) {
          int[] destIndex = locations[i][j].getWest();
          int row = destIndex[0];
          int col = destIndex[1];
          Edge edge = new EdgeImpl(locations[i][j], locations[row][col]);
          edges.add(edge);
        }
      }
    }
    
    return edges;
  }
  
  /**
   * Floyd-Warshall algorithm implementation to obtain shortest path between all nodes of a graph.
   * @return a 4 dimension matrix indicating the distance (number of steps) between each pair
   *     of nodes referenced by their phisical position indexes.
   */
  private int[][][][] calculateShortestDistances() {
    int[][][][] dist = new int[this.rows][this.columns][this.rows][this.columns];
    
    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.columns; j++) {
        for (int k = 0; k < this.rows; k++) {
          Arrays.fill(dist[i][j][k], 9999999);
        }
      }
    }
    
    // We iterate over the edges and include those connections in the distance matrix. 
    // We consider edges to be non-directed.
    
    this.selectedEdges.forEach((edge) -> {
      dist[edge.getOrigin().getRow()][edge.getOrigin().getColumn()]
          [edge.getDestination().getRow()][edge.getDestination().getColumn()] = 1;
      dist[edge.getDestination().getRow()][edge.getDestination().getColumn()]
          [edge.getOrigin().getRow()][edge.getOrigin().getColumn()] = 1;
    });
    
    // We also set the diagonal of the matrix to be zero.
    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.columns; j++) {
        dist[i][j][i][j] = 0;
      }
    }
    
    // Now the main part of the loop, where we have to examine all possible nodes trios.
    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.columns; j++) {
        for (int k = 0; k < this.rows; k++) {
          for (int l = 0; l < this.columns; l++) {
            for (int m = 0; m < this.rows; m++) {
              for (int n = 0; n < this.columns; n++) {
                int distOld = dist[i][j][k][l];
                int distNew = dist[i][j][m][n] + dist[m][n][k][l];
                if (distOld > distNew) {
                  dist[i][j][k][l] = dist[i][j][m][n] + dist[m][n][k][l];
                }
              }
            }
          }
        }
      }
    }
    
    return dist;
  }
  
  @Override
  public int getPathDistance(Location origin, Location destination) 
      throws IllegalArgumentException {
    if (origin == null || destination == null) {
      throw new IllegalArgumentException("Locations cannot be null.");
    }
    int response  = Math.min(this.shortestDistances[origin.getRow()]
        [origin.getColumn()][destination.getRow()][destination.getColumn()],
        this.shortestDistances[destination.getRow()][destination.getColumn()]
            [origin.getRow()][origin.getColumn()]);
    return response;
  }
  
  /**
   * Helper method that return the reference indexes of neighbours of a given location.
   * It handles wrapping around edges if that is the case.
   * @param r int with row reference of the input location
   * @param c int with column reference of the input location
   * @param wrapped boolean that mention if this location/dungeon is wrapped
   * @return two dimensional matrix with four rows, each of them indicating the reference
   *     to all the four direction's neighbours. If the re is no neighbour in one direction
   *     a null is returned.
   */
  private int[][] getNeighbourIndexes(int r, int c, boolean wrapped) {
    int[][] indexes =  new int[4][2];
    
    //East-West boundary control index 2 and 3.
    
    indexes[2][0] = r;
    indexes[3][0] = r;
    if (c == this.columns - 1) {
      if (wrapped) {
        indexes[2][1] = 0;
        indexes[3][1] = c - 1;
      }
      else {
        indexes[2][1] = - 1;
        indexes[3][1] = c - 1;
      }
    }
    else if (c == 0) {
      if (wrapped) {
        indexes[2][1] = c + 1;
        indexes[3][1] = this.columns - 1;
      }
      else {
        indexes[2][1] = c + 1;
        indexes[3][1] = - 1;
      }
      
    }
    else {
      indexes[2][1] = c + 1;
      indexes[3][1] = c - 1;
    }
    
    // North-south boundary control
    
    indexes[0][1] = c;
    indexes[1][1] = c;
    if (r == rows - 1) {
      if (wrapped) {
        indexes[0][0] = 0;
        indexes[1][0] = r - 1;
      }
      else {
        indexes[0][0] = - 1;
        indexes[1][0] = r - 1;
      }
      
    }
    else if (r == 0) {
      if (wrapped) {
        indexes[0][0] = r + 1;
        indexes[1][0] = columns - 1; 
      }
      else {
        indexes[0][0] = r + 1;
        indexes[1][0] = - 1;
      }
    }
    else {
      indexes[0][0] = r + 1;
      indexes[1][0] = r - 1;
    }
    
    return indexes;
  }
  
  /**
   * Helper method that judges for each pair of nodes if they should be connected 
   * to form a minimum spanning tree.
   * This algorithm starts with the previous implementation of all potential connections obtained
   * in the createConnections helper method.
   * @return an arrayList with all the new edges. It considers the interConnectivityIndex
   */
  private ArrayList<Edge>[] implemtKruskal() {
    
    // We have already created randomly a series of potential edges.
    // We will need two edge lists to store desired and undesired edges.
    ArrayList<Edge> selectedEdges = new ArrayList<Edge>();
    ArrayList<Edge> discardedEdges = new ArrayList<Edge>();
    
    // We also need a list of sets containing nodes
    ArrayList<HashSet<Location>> nodeSets = new ArrayList<HashSet<Location>>();
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        HashSet<Location> aux = new HashSet<Location>();
        aux.add(this.locations[i][j]);
        nodeSets.add(aux);
      }
    }
    
    // Now we iterate over the edges adding them to join disjunct subsets in case they do so.
    
    for (Edge e : this.potentialEdges) {
      Location o = e.getOrigin();
      Location d = e.getDestination();
      
      for (Iterator<HashSet<Location>> r = nodeSets.iterator(); r.hasNext(); ) {
        HashSet<Location> subset = r.next();
        if (subset.contains(o)) {
          if (subset.contains(d)) {
            discardedEdges.add(e);
          }
          else if (subset.size() > 0) {
            selectedEdges.add(e);
            //subset.add(d);
            for (Iterator<HashSet<Location>> r2 = nodeSets.iterator(); r2.hasNext(); ) {
              HashSet<Location> subset2 = r2.next();
              if (subset2.contains(d) && !subset2.equals(subset)) {
                subset.addAll(subset2);
                subset2.clear();
                //subset2.remove(d);
                //r2.remove();
              }
            }
          }
        }
      }  
    }
         
    int counter = -1;
    for (int i = 0; i < this.interConnectivity; i++) {
      boolean uselessEdge = true;
      while (uselessEdge) {
        counter = counter + 1;
        uselessEdge = false;
        Edge e = discardedEdges.get(counter);
        String dir1 = e.getDir1();
        String dir2 = e.getDir2();
        for (Edge ed: selectedEdges) {
          if (ed.getDir1().equals(dir1) || ed.getDir1().equals(dir2)) {
            uselessEdge = true;
            break;
          }
        }
      }
      if (!uselessEdge) {
        selectedEdges.add(discardedEdges.get(counter));
        discardedEdges.remove(counter);
      }
      
    }
    
    ArrayList<Edge>[] result = new ArrayList[2];
    result[0] = selectedEdges;
    result[1] = discardedEdges;
    
    return result;
  }
  
  /**
   * Helper method to update the connections of the Dungeon object after Kruskal's
   * algorithm has runned.
   * @return a four dimensional matrix that indicates their a pair of edges referenced
   *     by their numerical position indexes, are connected.
   */
  private boolean[][][][] updateConnections(int rows, int columns, 
      boolean wrapped, int interConnectivity) throws IllegalArgumentException {
    
    // take the edges and update the connections matrix from that information.
    boolean[][][][] connections = new boolean[rows][columns][rows][columns];
    
    for (Edge e : this.selectedEdges) {
      int originRow = e.getOrigin().getRow();
      int originColumn = e.getOrigin().getColumn();
      int destinationRow = e.getDestination().getRow();
      int destinationColumn = e.getDestination().getColumn();
      
      connections[originRow][originColumn][destinationRow][destinationColumn] = true;
      connections[destinationRow][destinationColumn][originRow][originColumn] = true;
    }
        
    return connections;
  }
  
  /**
   * Helper method to locate Treasures in the caves of the dungeon.
   * @param locations all potential locations to be considered
   * @param treasurePercentage 0 to 100 int number indicating the percentage of caves that will
   *     store treasures
   * @return a list with the locations selected to host treasures, with their treasures already
   *     placed.
   */
  private List<Location> locateTreasures(Location[][] locations, int treasurePercentage) {
    List<Location> caves = new ArrayList<Location>();
    
    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.columns; j++) {
        if (locations[i][j].getType().equals(LocationType.CAVE)) {
          caves.add(locations[i][j]);
        }
      }
    }
    
    int cavesNum = caves.size();
    for (int i = 0; i < cavesNum; i++) {
      int randomNum = this.randomGenerator.nextInt(100 - 1) + 1; 
      if (randomNum < treasurePercentage) {
        int trasureType = randomNum % 3;
        if (trasureType == 0) {
          caves.get(i).setTreasure(new TreasureImpl(ItemType.DIAMOND));
        }
        else if (trasureType == 1) {
          caves.get(i).setTreasure(new TreasureImpl(ItemType.RUBY));
        }
        else if (trasureType == 2) {
          caves.get(i).setTreasure(new TreasureImpl(ItemType.SAPPHIRE));
        }
      }
    }
        
    return caves;
  }
  
  /**
   * Helper method to locate Arrows in the locations (caves and tunnels) of the dungeon.
   * @param locations all potential locations to be considered
   * @param treasurePercentage 0 to 100 int number indicating the percentage of locations that will
   *     store treasures. It is the same number used for locateTreasure
   * @return a list with the locations selected to host arrows, with their arrows already placed.
   */
  private List<Location> locateArrows(Location[][] locations, int treasurePercentage) {
    List<Location> locationsList = new ArrayList<Location>();
    List<Location> result = new ArrayList<Location>();
    
    // Putting locations in a list.
    for (Location[] array : this.locations) {
      locationsList.addAll(Arrays.asList(array));
    }
    
    // Shuffling the list of locations.
    Random r1 = new Random();
    
    for (int i = locationsList.size() - 1; i >= 1; i--) {
      // swapping current index value
      // with random index value
      Collections.swap(locationsList, i, r1.nextInt(i + 1));
    }

    // Adding arrows at a random pace.
    for (int i = 0; i < locationsList.size(); i++) {
      int randomNum = this.randomGenerator.nextInt(100 - 1) + 1; 
      if (randomNum < treasurePercentage) {
        locationsList.get(i).setArrow(new ArrowImpl());
        result.add(locationsList.get(i));
      }
    }
        
    return result;
  }
  
  /**
   * Helper method to tag two locations (CAVES) as start and end
   * complying with their distance requirement.
   * @return two dimension array with both locations 0->start 1->end
   */
  private Location[] setStartEnd() {
    boolean found = false;
    Location[] result = new Location[2];

    while (!found) {
      int startRow = this.randomGenerator.nextInt(this.rows);
      int startColumn = this.randomGenerator.nextInt(this.columns);
      int endRow = this.randomGenerator.nextInt(this.rows);
      int endColumn = this.randomGenerator.nextInt(this.columns);
      
      Location start = this.locations[startRow][startColumn];
      Location end = this.locations[endRow][endColumn];
      
      int distance = this.getPathDistance(start, end);
      
      if ((distance >= 5 && distance < 999999) & end.getType().equals(LocationType.CAVE)) {
        found = true;
        result[0] = start;
        result[1] = end;
      }
    }
    
    return result;  
  }
  
  /**
   * Helper method to create and locate monsters in caves of the dungeon.
   * The first monster is set in the finish location and the rest in 
   * a random cave.
   * @param monsterNumber int with the number of monsters of the dungeon
   * @return list with created monster objects
   */
  private List<Monster> setMonsters(int monsterNumber) {
    int monsterCounter = 0;
    List<Monster> monsters = new ArrayList<Monster>();
    
    // Base case, first monster goes in the finish location.
    Monster firstMonster = new MonsterImpl(this.finish);
    monsters.add(firstMonster);
    monsterCounter ++;
    
    // Rest of the monsters.
    while (monsterCounter < monsterNumber) {
      int locationRow = this.randomGenerator.nextInt(this.rows);
      int locationColumn = this.randomGenerator.nextInt(this.columns);

      Location loc = this.locations[locationRow][locationColumn];
      
      if ((loc.equals(this.start) || loc.getType() == LocationType.TUNNEL) || loc.hasMonster()) {
        continue;
      }
      else {
        Monster m = new MonsterImpl(loc);
        monsters.add(m);
        monsterCounter ++;
        
      }
    }
    
    return monsters;
       
  }
  
  @Override
  public List<Location> getInitialTreasureCaves() {
    return this.initialTreasureCaves;
  }
  
  @Override
  public Location getStart() {
    return this.start;
  }
  
  @Override
  public Location getFinish() {
    return this.finish;
  }
  
  @Override
  public List<Monster> getMonsters() {
    return this.monsters;
  }
  
  @Override
  public List<Location> getInitialArrowLocations() {
    return this.initialArrowsLocations;
  }
  
  @Override
  public Smell checkSmell(Location l, boolean primarySearch) throws IllegalArgumentException {
    if (l == null) {
      throw new IllegalArgumentException("The base location cannot be null.");
    }
    List<Location> monsterLocations = new ArrayList<Location>();
    this.monsters.stream().forEach(m -> monsterLocations.add(m.getLocation()));
    List<Location> neighbors = new ArrayList<Location>();
    int r;
    int c;
    
    // First if we are inside a monster cave, we report no smell.
    if (monsterLocations.contains(l)) {
      return Smell.NO_SMELL;
    }
    
    if (l.getNorth() != null) {
      r = l.getNorth()[0];
      c = l.getNorth()[1];
      neighbors.add(this.locations[r][c]);
    }
    if (l.getSouth() != null) {
      r = l.getSouth()[0];
      c = l.getSouth()[1];
      neighbors.add(this.locations[r][c]);
    }
    if (l.getEast() != null) {
      r = l.getEast()[0];
      c = l.getEast()[1];
      neighbors.add(this.locations[r][c]);
    }
    if (l.getWest() != null) {
      r = l.getWest()[0];
      c = l.getWest()[1];
      neighbors.add(this.locations[r][c]);
    }
    
    if (this.checkMonster(monsterLocations, neighbors)) {
      return Smell.MORE_PUNGENT;
    }
    else if (primarySearch) {
      //Check in every neighbor if we get a MORE_PUNGENT return
      Smell aux = Smell.NO_SMELL;
      for (Location n : neighbors) {
        aux = this.checkSmell(n, false);
        if (aux.equals(Smell.MORE_PUNGENT)) {
          return Smell.LESS_PUNGENT;
        }
      }
    }
    return Smell.NO_SMELL;
    
  }
  
  
  /**
   * Helper method to figure out if a monster is present in a list of locations.
   * @param monsterLocations list of locations where monsters dwell
   * @param checkingLocations list of locations to be checked
   * @return true if a monster dwells inside any of checkingLocations
   */
  private boolean checkMonster(List<Location> monsterLocations, List<Location> checkingLocations) {
    for (Location l : checkingLocations) {
      if (l.hasMonster()) {
        return true;
      }
    }
    return false;
  }
  

}
