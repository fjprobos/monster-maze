package dungeon;

import java.util.List;

/**
 * This interface represent a read only way to interact with the game model.
 * It provides all the information required to represent the game in the
 * view.
 */
public interface GameReadOnly {
  
  /**
   * Returns a string with the possible directions
   * the player can take given its current location.
   * @return list of directions available in text format.
   */
  public String getPossibleDirections();
  
  /**
   * Returns a string report with the most important
   * information of the last turn played by the player.
   * @return String with required report of a turn.
   */
  public String reportLastTurn();
  
  /**
   * Returns a string report of the current
   * treasures collected by the player and
   * its name.
   * @return String with current information of player.
   */
  public String reportPlayer();
  
  /**
   * Gives a full report of the current location
   * where the player currently is.
   * @return String with current location information.
   */
  public String reportLocation();
  
  /**
   * Returns all the turns played in order.
   * @return List of all turns played.
   */
  public List<Turn> getPlayerSequence();
  
  /**
   * Returns a string with a visual representation
   * of the dungeon.
   * @return String with use of line space to create a 2D image.
   */
  public String getVisualMap();
  
  /**
   * Reference to the unique player of the game.
   * @return the player of the game.
   */
  public Player getPlayer();
  
  /**
   * Checks if the given location is the 
   * one defined as the endpoint of the dungeon.
   * @param l location that will be checked if it is equal to the finish of the dungeon
   * @return boolean that checks if finished is reached.
   * @throws IllegalArgumentException when the location to be checked is null
   */
  public boolean checkFinish(Location l)  throws IllegalArgumentException;
  
  /**
   * Returns the whole dungeon.
   * @return Dungeon object of the game.
   */
  public Dungeon getDungeon();
  
  /**
   * Returns a string with the information
   * of the treasure just found.
   * @return Treasure object.
   */
  public String reportObtainedTreasure();
  
  /**
   * Getter of the isTest boolean that says if this game instance is for
   * testing purposes.
   * @return true if the game instance is a test one.
   */
  public boolean getIsTest();
}
