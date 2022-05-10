package dungeon;

import java.util.List;

/**
 * Represents a physical space with a specific and unique location within
 * the dungeon.
 * 
 */
public interface Location {

  /**
   * Returns the coordinates row, col of
   * the north neighbour if exists.
   * @return 2-dimensional int array with row, col index of 
   *     the neighbour location.
   */
  public int[] getNorth();
  
  /**
   * Returns the coordinates row, col of
   * the south neighbour if exists.
   * @return 2-dimensional int array with row, col index of 
   *     the neigbour location.
   */
  public int[] getSouth();
  
  /**
   * Returns the coordinates row, col of
   * the east neighbour if exists.
   * @return 2-dimensional int array with row, col index of 
   *     the neigbour location.
   */
  public int[] getEast();
  
  /**
   * Returns the coordinates row, col of
   * the west neighbour if exists.
   * @return 2-dimensional int array with row, col index of 
   *     the neigbour location.
   */
  public int[] getWest();
  
  /**
   * Returns if the location is of type
   * CAVE or TUNNEL as the enum type.
   * @return the formal enum type of the location
   */
  public LocationType getType();
  
  /**
   * Returns the treasure of the location
   * if exists.
   * @return the Tresure type placed in the location if
   *     exists.
   */
  public Item getTreasure();
  
  /**
   * Returns true if the location currently
   * holds a treasure.
   * @return true if the location contains a treasure not collected yet
   */
  public boolean hasTreasure();
  
  /**
   * Method to populate the location with a treasure.
   * @param t Item to be placed in this location
   * @throws IllegalArgumentException if t is null
   */
  public void setTreasure(Item t) throws IllegalArgumentException;
  
  /**
   * Method to get the treasure of a location and 
   * erase the reference from it.
   * @return the Tresure type placed in the location if
   *     exists and remove it from the location.
   */
  public Item retrieveTreasure();
  
  /**
   * Get the coordinate row of the location.
   * @return int indicating the row index of the location
   */
  public int getRow();
  
  /**
   * Get the coordinate columns of the location.
   * @return int indicating the column index of the location
   */
  public int getColumn();
  
  /**
   * Returns a list of enums Directions possible
   * to be taken from the location.
   * @return list of possible directions of movement from this location.
   */
  public List<Direction> getPossibleDirections();
  
  /**
   * Method to populate the location with a monster.
   * @param m monster to be placed in the location if possible
   * @throws IllegalArgumentException if the monster to be set is null
   */
  public void setMonster(Monster m) throws IllegalArgumentException;
  
  /**
   * Get a monster if location is cave and contains a living one.
   * @return Alive Monster of the location if exist. Doesn0t consider dead ones.
   */
  public Monster getMonster();
  
  /**
   * Checks if a monster is present and alive in the location.
   * @return true if the location has a living monster inside
   */
  public boolean hasMonster();
  
  /**
   * Returns the arrow of the location
   * if exists.
   * @return the Arrow type placed in the location if
   *     exists.
   */
  public Item getArrow();
  
  /**
   * Returns true if the location currently
   * holds an arrow.
   * @return true if the location contains an arrow not collected yet
   */
  public boolean hasArrow();
  
  /**
   * Method to populate the location with an arrow.
   * @param a arrow to be placed in the location
   * @throws IllegalArgumentException if a is null
   */
  public void setArrow(Arrow a) throws IllegalArgumentException;
  
  /**
   * Method to get the arrow of a location and 
   * erase the reference from it.
   * @return the Arrow type placed in the location if
   *     exists and remove it from the location.
   */
  public Arrow retrieveArrow();
  
  /**
   * Returns a neighbor location address located after an exit opposite of an entry
   * at the "dir" direction.
   * @param dir entry direction to the location used as reference to find its opposite exit.
   * @return pair of row, column index to be used in locations matrix.
   * @throws IllegalArgumentException if there is no opposite location, or entry from direction.
   */
  public int[] getOpossiteLocation(Direction dir) throws IllegalArgumentException;
  
  /**
   * Returns the direction towards the exit opposite of an entry
   * at the "dir" direction.
   * @param currentEntryPoint entry direction to the location used as reference to find its 
   *     opposite exit.
   * @return exit Direction.
   * @throws IllegalArgumentException if there is no opposite location, or entry from dir.
   *     direction.
   */
  public Direction getNextEntryPoint(Direction currentEntryPoint) throws IllegalArgumentException;
  
  public List<int[]> getNeighbours();
  
  public Direction getNeighbourDirection(int row, int col) throws IllegalArgumentException;
  
  public String getImagePath();
}
