package dungeon;

/**
 * Interface to represent a controller that is able to
 * get the complete model of the dungeon game and grant
 * this object the power to interacter with the user.
 *
 */
public interface Controller {
  
  /**
   * Starts the controller to play a single instance of the
   * Dungeon Adventure Game.
   * @param g non null model of the dungeon game.
   * @throws IllegalArgumentException when the game is null.
   */
  public void startGame(Game g) throws IllegalArgumentException;
  
  /**
   * Method called by the dungeon button's actionlisteners to attempt a move or
   * attack in the direction of the location indicated by the row and col indexes.
   * For efficiency purposes simply does nothing if the pressed button is not a neighbour
   * of the current location of the player.
   * @param row row index of the desired action direction.
   * @param col column index of the desired action direction.
   * @throws IllegalArgumentException indexes are out of bounds
   */
  public void handleDungeonButton(int row, int col) throws IllegalArgumentException;
  
  public void setValidAttackReach(int reach) throws IllegalArgumentException;

}
