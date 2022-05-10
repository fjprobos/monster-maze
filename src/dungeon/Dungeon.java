package dungeon;

import java.util.List;

/**
 * This interface represent a set of Locations that are connected together and
 * conform a common system called dungeon.
 *
 */
public interface Dungeon {
  
  /**
   * Getter of all locations in a matrix fashion (row and column).
   * @return 2-dimensional (row, cols) array of locations.
   */
  public Location[][] getLocations();
  
  /**
   * Method to check if two locations are connected.
   * @param a Location at one extreme.
   * @param b Location at the other extreme.
   * @return Returns true if there is an edge between locations a and b.
   * @throws IllegalArgumentException when a or b are null
   */
  public boolean checkConnections(Location a, Location b) throws IllegalArgumentException;
  
  /**
   * Returns the number of rows of the dungeon.
   * @return int with the row number of the location.
   */
  public int getRows();
  
  /**
   * Returns the number of columns of the dungeon.
   * @return int with the column number of the location.
   */
  public int getColumns();
  
  /**
   * Returns the general interconnectivity number
   * we input when building the dungeon.
   * @return int with the amount of paths that should exist to connect two pairs of nodes.
   */
  public int getInterconnectivityIndex();
  
  /**
   * Method to check if a dungeon is "wrapped" or not.
   * @return boolean true if the dungeon is wrapped. 
   */
  public boolean getWrapped();
  
  /**
   * Return the number of steps required to get from
   * origin to destination through the shortest
   * distance path.
   * @param origin Location at one extreme.
   * @param destination Location at the other extreme.
   * @return int with the cost of edges needed to go from origin to destination.
   * @throws IllegalArgumentException when origin or destination are null
   */
  public int getPathDistance(Location origin, Location destination) throws IllegalArgumentException;
  
  /**
   * Returns the original location of treasures
   * before they are collected by the player.
   * @return list of locations where treasures were placed.
   */
  public List<Location> getInitialTreasureCaves();
  
  /**
   * Returns the starting location of the dungeon.
   * @return staring Location type.
   */
  public Location getStart();
  
  /**
   * Returns the finishing location of the dungeon.
   * @return finishing location type.
   */
  public Location getFinish();
  
  /**
   * Returns the surviving Monsters of the dungeon.
   */
  public List<Monster> getMonsters();
  
  /**
   * Returns the original location of arrows
   * before they are collected by the player.
   * @return list of locations where arrows were placed.
   */
  public List<Location> getInitialArrowLocations();
  
  /**
   * Checks if there is a monster nearby location l.
   * Uses a boolean to tell if this is a primary or secondary search in terms
   * of distance to the player.
   * @param l location where we want to check the smell level.
   * @param primarySearch boolean that indicates if method is being used
   *     to check smell in location l directly or as a two connections degree
   *     location of a third location.
   * @return level of smell indicated in enumeration.
   * @throws IllegalArgumentException when l is null
   */
  public Smell checkSmell(Location l, boolean primarySearch) throws IllegalArgumentException;

}
