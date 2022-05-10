package dungeon;

/**
 * Interface to represent edges connecting two locations of the dungeon.
 * They only store positions and provide it in both directions, as these edges
 * are not directed.
 *
 */
public interface Edge {
  
  /**
   * Get one of the extremes of the edge.
   * @return Location of origin of the edge.
   */
  public Location getOrigin();
  
  /**
   * Get the other extreme of the edge.
   * @return Location of destination of the edge.
   */
  public Location getDestination();
  
  /**
   * Returns the hashcode string of both
   * locations in one of the valid directions
   * of the edge.
   * @return String with concat of hashes.
   */
  public String getDir1();
  
  /**
   * Returns the hashcode string of both
   * locations in the other valid directions
   * of the edge.
   * @return String with concat of hashes.
   */
  public String getDir2();

}
