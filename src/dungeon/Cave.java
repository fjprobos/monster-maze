package dungeon;


/**
 * Concrete class to implement a location of CAVE type.
 * It has 1, 3 or 4 connections but not 2.
 * It can hold all type of items and host monsters.
 */
public class Cave extends AbstractLocation {
  
  /**
   * Constructor of Cave, extends the common constructor developed in AbstractLocation.
   * and controls that no object of this type has two connections.
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
  public Cave(int row, int col, boolean north, boolean south, boolean east, boolean west,
      int northIndex, int southIndex, int eastIndex, int westIndex) 
          throws IllegalArgumentException {
    super(row, col, north, south, east, west, northIndex, southIndex, eastIndex, westIndex);
    this.type = LocationType.CAVE;
    int connections = north ? 1 : 0;
    connections += south ? 1 : 0;
    connections += east ? 1 : 0;
    connections += west ? 1 : 0;
    if (connections == 2) {
      throw new IllegalArgumentException("Two connection locations cannot be Cave.");
    }
    
  }
  
  @Override
  public void setTreasure(Item t) throws IllegalArgumentException {
    if (t == null) {
      throw new IllegalArgumentException("Argument cannot be null.");
    }
    this.treasure = t;
    t.placeInLocation(this);
  }
  
  @Override
  public void setMonster(Monster m) throws IllegalArgumentException {
    if (m == null) {
      throw new IllegalArgumentException("Monster argument cannot be null.");
    }
    this.monster = m;
  }
  
  @Override
  public int[] getOpossiteLocation(Direction entryPoint) throws IllegalArgumentException {
    if (entryPoint == null) {
      throw new IllegalArgumentException("entryPoint argument cannot be null.");
    }
    
    
    // Default return.
    int[] result = new int[2];

    
    if (entryPoint.equals(Direction.NORTH) & this.connectionSouth) {
      return this.getSouth();
    }
    else if (entryPoint.equals(Direction.SOUTH) & this.connectionNorth) {
      return this.getNorth();
    }
    else if (entryPoint.equals(Direction.EAST) & this.connectionWest) {
      return this.getWest();
    }
    else if (entryPoint.equals(Direction.WEST) & this.connectionWest) {
      return this.getEast();
    }

    return result;
  }
  
  @Override
  public Direction getNextEntryPoint(Direction currentEntryPoint) throws IllegalArgumentException {
    if (currentEntryPoint == null) {
      throw new IllegalArgumentException("currentEntryPoint argument cannot be null.");
    }
    // Default return.
    Direction nextEntryPoint = null;

    if (currentEntryPoint.equals(Direction.NORTH) & this.connectionSouth) {
      nextEntryPoint = Direction.NORTH;
    }
    else if (currentEntryPoint.equals(Direction.SOUTH) & this.connectionNorth) {
      nextEntryPoint = Direction.SOUTH;
    }
    else if (currentEntryPoint.equals(Direction.EAST) & this.connectionWest) {
      nextEntryPoint = Direction.EAST;
    }
    else if (currentEntryPoint.equals(Direction.WEST) & this.connectionEast) {
      nextEntryPoint = Direction.WEST;
    }
    return nextEntryPoint; 
  }

}
