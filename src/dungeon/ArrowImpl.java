package dungeon;

/**
 * This class extends the implementation of a Treasure using the Arrow
 * interface to add new particular functionalities of this item.
 * The most important element is being able to check if it has been used.
 *
 */
public class ArrowImpl extends TreasureImpl implements Arrow {
  
  private boolean used;
  
  /**
   * Constructor of the Arrow, uses most of the elements
   * of the treasure constructor. This time the type is
   * only one.
   */
  public ArrowImpl() {
    super();
    this.type = ItemType.ARROW;
    this.used = false;
  }
  
  
  @Override
  public void placeInLocation(Location l) {
    this.location = l;

  }

  @Override
  public void useArrow() throws IllegalStateException {
    if (this.beholder == null) {
      throw new IllegalStateException("Arrows can only been used when beholded by a player.");
    }
    else if (this.used) {
      throw new IllegalStateException("Arrows can only been used once.");
    }
    this.used = true;
  }
  
  @Override
  public boolean isUsed() {
    return this.used;
  }

}
