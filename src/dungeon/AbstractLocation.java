package dungeon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Abstract class that encapsulates common code of Cave 
 * and Tunnel implementations of Location.
 */
public abstract class AbstractLocation implements Location {
  
  private final int rowPos;
  private final int colPos;
  private final int northIndex;
  private final int southIndex;
  private final int eastIndex;
  private final int westIndex;
  private final String imagePath;
  protected final boolean connectionNorth;
  protected final boolean connectionSouth;
  protected final boolean connectionEast;
  protected final boolean connectionWest;
  protected Item treasure;
  protected LocationType type;
  protected Monster monster;
  protected Arrow arrow;
  static Map<String, String> imagesMap;
  
  // Instantiating the static map
  static {
    imagesMap = new HashMap<String, String>();
    imagesMap.put("[WEST]", "W.png");
    imagesMap.put("[EAST]", "E.png");
    imagesMap.put("[SOUTH, WEST]", "SW.png");
    imagesMap.put("[SOUTH, EAST, WEST]", "SEW.png");
    imagesMap.put("[SOUTH, EAST]", "SE.png");
    imagesMap.put("[SOUTH]", "S.png");
    imagesMap.put("[NORTH, WEST]", "NW.png");
    imagesMap.put("[NORTH, SOUTH, WEST]", "NSW.png");
    imagesMap.put("[NORTH, SOUTH, EAST, WEST]", "NSEW.png");
    imagesMap.put("[NORTH, SOUTH, EAST]", "NSE.png");
    imagesMap.put("[NORTH, SOUTH]", "NS.png");
    imagesMap.put("[NORTH, EAST, WEST]", "NEW.png");
    imagesMap.put("[NORTH, EAST]", "NE.png");
    imagesMap.put("[NORTH]", "N.png");
    imagesMap.put("[EAST, WEST]", "EW.png");
  }
  
  
  /**
   *  Common part of the constructor used by Tunnel and Cave. 
   *  It requires its location, the indexes of their
   *  neighbors (that might be different in wrapped and unwrapped) 
   *  and its connections. Starts empty, but
   *  can later be populated with a treasure if it is a cave.
   *  @param row int index of row location reference inside the dungeon
   *  @param col int index of column location reference inside the dungeon
   *  @param north boolean indicating if this location has a connection with its north neighbour
   *  @param south boolean indicating if this location has a connection with its south neighbour
   *  @param east boolean indicating if this location has a connection with its east neighbour
   *  @param west boolean indicating if this location has a connection with its west neighbour
   *  @param northIndex reference helper to build position of north neighbour
   *  @param southIndex reference helper to build position of south neighbour
   *  @param eastIndex reference helper to build position of east neighbour
   *  @param westIndex reference helper to build position of west neighbour
   *  @throws IllegalArgumentException when col or row are non positive
   */
  public AbstractLocation(int row, int col, boolean north, boolean south, 
      boolean east, boolean west, int northIndex, 
      int southIndex, int eastIndex, int westIndex) throws IllegalArgumentException {
    if ( row < 0 || col < 0) {
      throw new IllegalArgumentException("Row and columns indexes have to be non-negative");
    }
    
    this.rowPos = row;
    this.colPos = col;
    this.northIndex = northIndex;
    this.southIndex = southIndex;
    this.eastIndex = eastIndex;
    this.westIndex = westIndex;
    this.connectionNorth = north;
    this.connectionSouth = south;
    this.connectionEast = east;
    this.connectionWest = west;
    this.treasure = null;
    this.type = null;
    this.imagePath = imagesMap.get(this.getPossibleDirections().toString());
    
  }
  

  @Override
  public int[] getNorth() {
    if (this.connectionNorth) {     
      int[] result = {this.northIndex, this.colPos};
      return result;
    }
    else {
      return null;
    }
    
  }

  @Override
  public int[] getSouth() {
    if (this.connectionSouth) {
      int[] result = {this.southIndex, this.colPos};
      return result;
    }
    else {
      return null;
    }
  }

  @Override
  public int[] getEast() {
    if (this.connectionEast) {
      int[] result = {this.rowPos, this.eastIndex};
      return result;
    }
    else {
      return null;
    }
  }

  @Override
  public int[] getWest() {
    if (this.connectionWest) {
      int[] result = {this.rowPos, this.westIndex};
      return result;
    }
    else {
      return null;
    }
  }

  @Override
  public LocationType getType() {
    return this.type;
  }

  @Override
  public Item getTreasure() {
    return this.treasure;
  }

  @Override
  public int getRow() {
    return this.rowPos;
  }

  @Override
  public int getColumn() {
    return this.colPos;
  }
  
  @Override
  public LinkedList<Direction> getPossibleDirections() {
    LinkedList<Direction> result = new LinkedList<Direction>();
    
    if (this.connectionNorth) {
      result.add(Direction.NORTH);
    }
    
    if (this.connectionSouth) {
      result.add(Direction.SOUTH);
    }
    
    if (this.connectionEast) {
      result.add(Direction.EAST);
    }
    
    if (this.connectionWest) {
      result.add(Direction.WEST);
    }
    
    return result;
  }
  
  @Override
  public boolean hasTreasure() {
    return Objects.nonNull(this.treasure);
  }
  
  @Override
  public Item retrieveTreasure() {
    Item aux =  this.treasure;
    this.treasure = null;
    return aux;
  }
  
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("");
    builder.append("You are currently in a ").append(this.type.toString())
        .append(" located in coordinates ").append(String.valueOf(this.rowPos))
        .append(" ,").append(String.valueOf(this.colPos)).append(".<br/>");    
    
    return builder.toString();
  }
  
  @Override
  public Monster getMonster() {
    Monster m =  ((this.monster.getHealth() > 0) ? this.monster : null);
    return m;
  }
  
  @Override
  public boolean hasMonster() {
    if (this.monster != null) {
      return this.monster.getHealth() > 0;
    }
    else {
      return false;
    }
  }
  
  @Override
  public Item getArrow() {
    return this.arrow;
  }
  
  @Override
  public boolean hasArrow() {
    return Objects.nonNull(this.arrow);
  }
  
  @Override
  public void setArrow(Arrow a) throws IllegalArgumentException {
    if (a == null) {
      throw new IllegalArgumentException("Argument cannot be null.");
    }
    this.arrow = a;
    a.placeInLocation(this);
  }
  
  @Override
  public Arrow retrieveArrow() {
    Arrow aux =  this.arrow;
    this.arrow = null;
    return aux;
  }
  
  @Override
  public List<int[]> getNeighbours() {
    List<int[]> result = new ArrayList<int[]>();
    
    if (this.connectionNorth) {
      result.add(this.getNorth());
    }
    if (this.connectionSouth) {
      result.add(this.getSouth());
    }
    if (this.connectionEast) {
      result.add(this.getEast());
    }
    if (this.connectionWest) {
      result.add(this.getWest());
    }
    
    return result;
  }
  
  @Override
  public Direction getNeighbourDirection(int row, int col) throws IllegalArgumentException {
    
    if (row < 0 || col < 0 ) {
      throw new IllegalArgumentException("row and col arguments must be positive.");
    }
    
    if (this.connectionNorth) {
      if (this.getNorth()[0] == row & this.getNorth()[1] == col) {
        return Direction.NORTH;
      }
    }
    if (this.connectionSouth) {
      if (this.getSouth()[0] == row & this.getSouth()[1] == col) {
        return Direction.SOUTH;
      }
    }
    if (this.connectionEast) {
      if (this.getEast()[0] == row & this.getEast()[1] == col) {
        return Direction.EAST;
      }
    }
    if (this.connectionWest) {
      if (this.getWest()[0] == row & this.getWest()[1] == col) {
        return Direction.WEST;
      }
    }
    
    return null;
  }
  
  @Override
  public String getImagePath() {
    return this.imagePath;
  }
}
