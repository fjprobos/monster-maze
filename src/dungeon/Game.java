package dungeon;

/**
 * This interface represent the information needed to play the game. It stores 
 * important indexes and organizes the main general methods of this project.
 *
 */
public interface Game extends GameReadOnly {
  
  /**
   * Call the constructor of the dungeon.
   * @param rows number of rows of the dungeon
   * @param columns number of columns of the dungeon
   * @param interconnectivityIndex connectivity level of the dungeon
   * @param wrapped boolean that indicates if the dungeon can have wrapped connections
   * @param treasurePercentage number between 0 and 100 indicating the percentage of caves 
   *     containing treasures and the percentage of locations containing crooked arrows.
   * @param monsterNumber int that indicates the initial number of monsters in the dungeon. 
   *     It cannot be less than 1.
   * @param randomSeed long used to initialize pseudorandom number series to be used for 
   *     testing purposes
   * @param isTest indicates if the instance of the game is for testing purposes
   * @return Dungeon created by the constructor.
   * @throws IllegalArgumentException Exception when rows or columns are non-positive
   */
  public Dungeon createDungeon(int rows, int columns, int interconnectivityIndex, 
      boolean wrapped, int treasurePercentage,int monsterNumber, long randomSeed, 
      boolean isTest) throws IllegalArgumentException;
  
  /**
   * Calls the constructor of the player.
   * @param playerName is the name of our player
   * @param d is the dungeon that the player will explore
   * @return Player created by the constructor.
   * @throws IllegalArgumentException when the dungeon is null
   */
  public Player createPlayer(String playerName, Dungeon d) throws IllegalArgumentException;
  
  
  /**
   * Makes the player move from its current location
   * to a new adjacent one.
   * @return Location the player entered to.
   * @throws IllegalArgumentException when the desired direction is null
   */
  public Location move(Direction dir) throws IllegalArgumentException;
  
  /**
   * Finish a players turn after receiving the input of the user
   * saying if he/she wants to pick up a treasure found.
   * @param l location of the turn
   * @param previousTurn is a reference to the turn that happened
   *     before this in terms of the temporal turn sequence.
   * @param collectTreasure indicates if the player wants to collect 
   *     a possible treasure of the location
   * @param collectArrow indicates if the player wants to collect 
   *     a possible arrow of the location
   * @return Turn finished.
   * @throws IllegalArgumentException when the location of the turn is null
   */
  public Turn finishTurn(Location l, Turn previousTurn, boolean collectTreasure, 
      boolean collectArrow) throws IllegalArgumentException;
  
  /**
   * Method used to make the player attack a monster.
   * Given current's player position, the player uses its bow and one
   * crooked arrow to attack the monster in a direction dir at a distance
   * of exactly "distance" locations away.
   * @param p Player that is attacking the monster
   * @param dir initial direction of the shoot
   * @param distance is an int indicating the number of caves away where the target is
   *     located.
   * @return true if the attack hits the monster, reducing its health to halve or killing it
   *     if it was already hit before.
   * @throws IllegalArgumentException if direction is illegal or if distance is non positive.
   * @throws IllegalStateException if the player has o remaining arrows to shoot.
   */
  public boolean attackMonster(Player p, Direction dir, int distance) 
      throws IllegalArgumentException, IllegalStateException;  
}
