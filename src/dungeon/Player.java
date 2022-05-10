package dungeon;

import java.util.List;

/**
 * One of the most important elements of the game, its player.
 * It defines how the player can move across a given dungeon,
 * the relation with the turns dynamics and the treasure collection.
 *
 */
public interface Player {
  
  /**
   * Returns the name of the player.
   * @return string with the name of the player
   */
  public String getName();
  
  /**
   * Returns a list with the treasures that the player
   * has already collected.
   * @return list of items of type treasure collected by the player
   */
  public List<Item> getTreasures();
  
  /**
   * Returns a list with all the turns that the player
   * has played, in order.
   * @return list of turns finished by the player.
   *     List is not necessarily ordered.
   */
  public List<Turn> getTurns();
  
  /**
   * Returns the last turn played.
   * @return current Turn of the player
   */
  public Turn getLastTurn();
  
  /**
   * Adds a treasure to the players bag of
   * treasures (list of treasures).
   * @param t Treasure type to be added to its bag.
   * @throws IllegalArgumentException if t is null.
   */
  public void addTreasure(Item t) throws IllegalArgumentException;
  
  /**
   * Mode the player from its current location in dungeon d
   * in direction dir after a previous turn t.
   * @param d dungeon where the player is moving
   * @param t turn when the player is being moved
   * @param dir is the direction in which the player attempts to move
   * @return the new Location if the movement was succesful
   * @throws IllegalArgumentException if d, t or dir are null
   */
  public Location move(Dungeon d, Turn t, Direction dir) throws IllegalArgumentException;
  
  /**
   * After moving a player and collecting input from the user, 
   * this method is able to finish the creation of
   * a turn, knowing if the user wants to pickup a found treasure.
   * @param l location of the turn
   * @param previousTurn is a reference to the turn that happened
   *     before this in terms of the temporal turn sequence.
   * @param collectTreasure indicates if the player wants to collect 
   *     a possible treasure of the location
   * @param collectArrow indicates if the player wants to collect 
   *     a possible arrow of the location
   * @param deathByMonster if the player was attacked and killed by a monster
   * @return Turn finished.
   * @throws IllegalArgumentException if the location of the turn is null
   */
  public Turn finishTurn(Location l, Turn previousTurn, boolean collectTreasure, 
      boolean collectArrow, boolean deathByMonster) throws IllegalArgumentException;
  
  /**
   * Reports a string with a list of all treasures collected
   * so far.
   * @return String containing all the report
   */
  public String reportTreasures();
  
  /**
   * Returns the number of arrows unused remaining.
   * @return int with the number of remaining arrows to kill monsters
   */
  public int getRemainingArrows();
  
  /**
   * Use one of the remaining arrows.
   * @throws IllegalStateException if no arrow is available
   */
  public void useArrow() throws IllegalStateException;

}
