package dungeon;

/**
 * Interface of the treasure type that can be located in caves
 * and collected by the player.
 *
 */
public interface Item {
  
  /**
   * Adds the treasure to a location l.
   * @param l location where the treasure will be placed
   * @throws IllegalArgumentException depends on the implementation
   */
  public void placeInLocation(Location l) throws IllegalArgumentException;
  
  /**
   * Transfers the treasure to a beholder p.
   * @param p player that will store the treasure
   * @throws IllegalArgumentException if the player that will collect the arrow is null
   */
  public void collect(Player p) throws IllegalArgumentException;
  
  /**
   * Returns current beholder if it has one.
   * @return player that is currently holding the item
   */
  public Player getBeholder();
  
  /**
   * Returns the type of the item:
   * Diamond, Saphire, Ruby or Arrow.
   * @return the type of the item
   */
  public ItemType getType();
  
  /**
   * Returns the place where a treasure is placed.
   * @return the location where the item is, if it hasn't been collected
   *     by a player.
   */
  public Location getLocation();
  
}
