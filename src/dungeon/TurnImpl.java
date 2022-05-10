package dungeon;

/**
 * Class implementing the Turn interface, all its elements are final
 * and references to the previous turn to ensure the order in a final
 * variable.
 *
 */
public class TurnImpl implements Turn {

  private final Location location;
  private final boolean gotTreasure;
  private final boolean gotArrow;
  private final Item obtainedTreasure;
  private final Arrow obtainedArrow;
  private final int correlative;
  private final Turn previousTurn;
  private final boolean deathByMonster;
  
  /**
   * Constructor that requires the location, the previous turn that is null
   * in the first turn and a boolean that indicates if the player wants to
   * collect the treasure in that location (if exists).
   * @param l location the player reaches in that turn, and where all actions of the
   *     turn happen.
   * @param previousTurn reference to the previous turn (same type) in order to have a linked 
   *     reference with the temporal order of the game sequence.
   * @param collectTreasure boolean that indicates if the player wants to collect the 
   *     treasure of the location if there is one there
   * @param collectArrow boolean that indicates if the player wants to collect the 
   *     arrow of the location if there is one there
   * @throws IllegalArgumentException if the input location is null
   *
   */
  public TurnImpl(Location l, Turn previousTurn, boolean collectTreasure, boolean collectArrow) 
      throws IllegalArgumentException {
    this.location = l;
    if (l.hasTreasure() && collectTreasure) {
      this.gotTreasure = true;
      this.obtainedTreasure = l.retrieveTreasure();
    }
    else {
      this.gotTreasure = false;
      this.obtainedTreasure = null;
    }
    
    if (l.hasArrow() && collectArrow) {
      this.gotArrow = true;
      this.obtainedArrow = l.retrieveArrow();
    }
    else {
      this.gotArrow = false;
      this.obtainedArrow = null;
    }
    
    if (previousTurn == null) {
      this.correlative = 1;
    }
    else {
      this.correlative = previousTurn.getCorrelative() + 1;
    }
    this.previousTurn = previousTurn;
    this.deathByMonster = false;
  }
  
  /**
   * Version for finishing turn when eaten by a monster.
   * @param l Location of the turn. Place where all the turn's activity occurs.
   * @param previousTurn reference to the previous same type object
   */
  public TurnImpl(Location l, Turn previousTurn) throws IllegalArgumentException {
    if (l == null) {
      throw new IllegalArgumentException("The location cannot be null");
    }
    this.location = l;
    this.gotTreasure = false;
    this.obtainedTreasure = null;
    this.gotArrow = false;
    this.obtainedArrow = null;
    this.correlative = previousTurn.getCorrelative() + 1;
    this.previousTurn = previousTurn;
    this.deathByMonster = true;
  }
  
  @Override
  public Location getLocation() {
    return this.location;
  }

  @Override
  public int getCorrelative() {
    return this.correlative;
  }

  @Override
  public Turn getPreviousTurn() {
    return this.previousTurn;
  }
  
  @Override
  public boolean gotTreasure() {
    return this.gotTreasure;
  }
  
  @Override
  public Item getObtainedTreasure() {
    return this.obtainedTreasure;
  }
  
  @Override
  public boolean gotArrow() {
    return this.gotArrow;
  }
  
  @Override
  public Arrow getObtainedArrow() {
    return this.obtainedArrow;
  }
  
  @Override
  public boolean deathByMonster() {
    return this.deathByMonster;
  }

}
