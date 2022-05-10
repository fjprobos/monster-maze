package dungeon;

import java.util.LinkedList;

/**
 * Location defined by two connections.
 * It cannot store a treasure.
 *
 */
public class Tunnel extends AbstractLocation {
  
  /**
   * Constructor similar to cave, but this time ensures that only
   * two connections are given as argument.
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
  public Tunnel(int row, int col, boolean north, boolean south, boolean east, boolean west,
      int northIndex, int southIndex, int eastIndex, int westIndex) 
          throws IllegalArgumentException {
    super(row, col, north, south, east, west, northIndex, southIndex, eastIndex, westIndex);
    this.type = LocationType.TUNNEL;
    int connections = north ? 1 : 0;
    connections += south ? 1 : 0;
    connections += east ? 1 : 0;
    connections += west ? 1 : 0;
    if (connections != 2) {
      throw new IllegalArgumentException("Just two connections in tunnels.");
    }
    
  }
  
  @Override
  public void setTreasure(Item t) throws IllegalArgumentException {
    throw new IllegalArgumentException("Tunnels cann't hold treasures.");
    
  }
  
  @Override
  public void setMonster(Monster m) throws IllegalStateException {
    throw new IllegalStateException("Monsters can only be located in caves.");
  }
  
  @Override
  public int[] getOpossiteLocation(Direction entryPoint) throws IllegalArgumentException {
    if (entryPoint == null) {
      throw new IllegalArgumentException("entryPoint argument cannot be null.");
    }
    
    // Default return.
    int[] result = new int[2];
    
    // We get the exit of the tunnel coming from "dir" entry.
    Direction nextEntryPoint = this.getNextEntryPoint(entryPoint);
    
    // We return the corresponding exit neighbor row and column indexes.
    if (nextEntryPoint.equals(Direction.NORTH)) {
      return this.getSouth();
    }
    else if (nextEntryPoint.equals(Direction.SOUTH)) {
      return this.getNorth();
    }
    else if (nextEntryPoint.equals(Direction.EAST)) {
      return this.getWest();
    }
    else if (nextEntryPoint.equals(Direction.WEST)) {
      return this.getEast();
    }

    return result;
  }
  
  @Override
  public Direction getNextEntryPoint(Direction currentEntryPoint) throws IllegalArgumentException {
    if (currentEntryPoint == null) {
      throw new IllegalArgumentException("currentEntryPoint argument cannot be null.");
    }
    LinkedList<Direction> possibleDirections = this.getPossibleDirections();
    // Default return.
    Direction oppositeCrookedDirection = null;
    Direction nextEntryPoint = null;
    
    // We get the exit direction of the current tunnel.
    if (possibleDirections.get(0).equals(currentEntryPoint)) {
      oppositeCrookedDirection = possibleDirections.get(1);
    }
    else {
      oppositeCrookedDirection = possibleDirections.get(0);
    }
    
    //We get the entry of the next location
    if (oppositeCrookedDirection.equals(Direction.NORTH)) {
      nextEntryPoint = Direction.SOUTH;
    }
    else if (oppositeCrookedDirection.equals(Direction.SOUTH)) {
      nextEntryPoint = Direction.NORTH;
    }
    else if (oppositeCrookedDirection.equals(Direction.EAST)) {
      nextEntryPoint = Direction.WEST;
    }
    else if (oppositeCrookedDirection.equals(Direction.WEST)) {
      nextEntryPoint = Direction.EAST;
    }
    
    
    return nextEntryPoint;
  }
  
}
