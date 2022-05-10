package dungeon;

/**
 * This interface represent one crooked arrow to kill 
 * the monsters of the dungeon. They don't disappear when are
 * used, but are marked to not being able to be used again.
 *
 */
public interface Arrow extends Item {
  
  /**
   * Uses the arrow to attack if it is on hands of
   * a player and hasn't been used already. 
   * Throws IllegalStateException if it is still
   * not collected by a player.
   * Sets a bool var to false indicating the arrow cannot be
   * used in the future.
   * @throws IllegalStateException if the arrow has already been used.
   */
  public void useArrow() throws IllegalStateException;
  
  /**
   * Checks if the arrow has been already used. This is important,
   * because we keep track of all arrows regardless of their state.
   * @return true if the arrow has already been used to in a shot.
   */
  public boolean isUsed();

}
