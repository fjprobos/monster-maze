package dungeon;

/**
 * Interface representing a Monster object being able to live in a cave od the dungeon
 * and kill the player if faced with him.
 * It also is able to update its health when receiving an arrow shoot.
 *
 */
public interface Monster {
  
  /**
   * Discount one of the two points of health if reached by an arrow.
   */
  public void receiveDamage();
  
  /**
   * Getter of the current living cave of the monster.
   * @return its location if exist
   */
  public Location getLocation();
  
  /**
   * Return the current points of health remaining to the monster.
   * @return int with points of health remaining (0 to 2)
   */
  public int getHealth();

}
