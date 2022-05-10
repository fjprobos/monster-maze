package dungeon;

/**
 * Class implementation of the edge interface.
 * Store origin and destination as location interfaces
 * of the places connected by it.
 *
 */
public class EdgeImpl implements Edge {

  private final Location origin;
  private final Location destination;
  
  /**
   * Simple constructor that defines the final origin and destination of an edge.
   * This means, indicates both connecting locations of the edge object.
   *
   */
  public EdgeImpl(Location o, Location d) throws IllegalArgumentException {
  
    this.origin = o;
    this.destination = d;
  }
  
  @Override
  public Location getOrigin() {
    return this.origin;
  }
  
  @Override
  public Location getDestination() {
    return this.destination;
  }
  
  @Override
  public String getDir1() {
    StringBuilder builder = new StringBuilder();
    builder.append(String.valueOf(this.origin.hashCode()));
    builder.append(String.valueOf(this.destination.hashCode()));
    return builder.toString();
  }
  
  @Override
  public String getDir2() {
    StringBuilder builder = new StringBuilder();
    builder.append(String.valueOf(this.destination.hashCode()));
    builder.append(String.valueOf(this.origin.hashCode()));
    return builder.toString();
  }
  
}
