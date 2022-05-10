package dungeon;

/**
 * Interface of the Turn, which is the most basic
 * element of time in the game. The turns give the order and
 * sequence to the game in terms of dungeon exploration
 * and treasure collection.
 *
 */
public interface Turn {
  
  /**
   * Returns the location where the player arrived
   * in this turn.
   * @return the location of the turn
   */
  public Location getLocation();
  
  /**
   * Returns the correlative number of the 
   * turn which indicates its position in the
   * sequences of turns of the game.
   * @return int with the temporal sequence number of this turn
   */
  public int getCorrelative();
  
  /**
   * Returns the turn played before this one.
   * @return Turn of the previous round
   */
  public Turn getPreviousTurn();
  
  /**
   * Returns true if a treasure was collected
   * during this turn.
   * @return true if a treasure was collected this turn.
   */
  public boolean gotTreasure();
  
  /**
   * Returns the treasure obtained during this turn,
   * if that happened.
   * @return the obtained treasure if a treasure was collected this turn.
   */
  public Item getObtainedTreasure();
  
  /**
   * Returns true if an arrow was collected
   * during this turn.
   * @return true if an arrow was collected this turn.
   */
  public boolean gotArrow();
  
  /**
   * Returns the arrow obtained during this turn,
   * if that happened.
   * @return the obtained arrow if an arrow was collected this turn.
   */
  public Arrow getObtainedArrow();
  
  /**
   * Getter to know if this is a losing game-over turn.
   * @return true if the player was killed by an attacking monster during this turn
   */
  public boolean deathByMonster();

}
