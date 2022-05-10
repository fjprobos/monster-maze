package dungeon;

/**
 * Treasure implementation class, that has a cave when
 * located, a final type and a beholder when collected.
 *
 */
public class TreasureImpl implements Item {
  
  protected Location location;
  protected ItemType type;
  protected Player beholder;
  
  /**
   *Basic constructor that starts the treasure without location
   *nor player beholder.
   *@param t Itemtype of the treasure
   *@throws IllegalArgumentException if the Itemtype is null
   */
  public TreasureImpl(ItemType t) throws IllegalArgumentException {
    if (t == null) {
      throw new IllegalArgumentException("t cannot be null.");
    }
    this.type = t;
    this.location = null;
    this.beholder = null;
  }
  
  /**
   *Alternative constructor to be used by descendant class ArrowImpl.
   *
   */
  public TreasureImpl() {
    this.type = null;
    this.location = null;
    this.beholder = null;
  }
  
  
  @Override
  public void placeInLocation(Location l) throws IllegalArgumentException {
    if (!l.getType().equals(LocationType.CAVE)) {
      throw new IllegalArgumentException("Treasures can only be located in caves.");
    }
    this.location = l;

  }

  @Override
  public void collect(Player p) throws IllegalArgumentException {
    if (p == null) {
      throw new IllegalArgumentException("Player that is collecting cannot be null.");
    }
    this.beholder = p;
  }

  @Override
  public Player getBeholder() {
    return this.beholder;
  }

  @Override
  public ItemType getType() {
    return this.type;
  }

  @Override
  public Location getLocation() {
    return this.location;
  }

}
