package dungeon;

import java.util.List;
import java.util.Random;

/**
 * The most upper level class that oversees all the rest. It is the one used to
 * interact with the driver class.
 *
 */
public class GameImpl implements Game {
  
  private final Dungeon dungeon;
  private final Player player;
  private final Random randomGenerator;
  private final boolean isTest;
  
  /**
   * Constructor of the game that calls at the same time
   * the constructors of the player and dungeon. Hence
   * it requires all the arguments they need.
   *
   */
  public GameImpl(int rows, int columns, int interconnectivityIndex, 
      boolean wrapped, int treasurePercentage, String playerName,
      int monsterNumber, long randomSeed, boolean isTest) throws IllegalArgumentException {
    this.dungeon = this.createDungeon(rows, columns, 
        interconnectivityIndex, wrapped, treasurePercentage,
        monsterNumber, randomSeed, isTest);
    this.player = this.createPlayer(playerName, dungeon);
    this.randomGenerator = new Random();
    this.isTest = isTest;
    if (isTest) {
      this.randomGenerator.setSeed(randomSeed);
    }
  }

  @Override
  public Dungeon createDungeon(int rows, int columns, int interconnectivityIndex, boolean wrapped,
      int treasurePercentage,int monsterNumber, long randomSeed, boolean isTest)
          throws IllegalArgumentException {
    if (rows < 1 || columns < 1) {
      throw new IllegalArgumentException("Columns or rows must be positive");
    }
    Dungeon newDungeon = new DungeonImpl(rows, columns, wrapped, interconnectivityIndex, 
        treasurePercentage, monsterNumber, randomSeed, isTest); 
    return newDungeon;
  }

  @Override
  public Player createPlayer(String playerName, Dungeon d) throws IllegalArgumentException {
    if (d == null) {
      throw new IllegalArgumentException("The input dungeon ccannot be null");
    }
    // The first turn is created in the origin location of the dungeon.
    Turn firstTurn = new TurnImpl(d.getStart(), null, true, true);
    Player newPlayer = new PlayerImpl(playerName, firstTurn);
    return newPlayer;
  }

  @Override
  public String getPossibleDirections() {
    return this.player.getLastTurn().getLocation().getPossibleDirections().toString(); 
  }

  @Override
  public String reportLastTurn() {
    return this.player.getLastTurn().toString();
  }

  @Override
  public Location move(Direction dir) throws IllegalArgumentException {
    if (dir == null) {
      throw new IllegalArgumentException("The desired direction cannot be null");
    }
    return this.player.move(this.dungeon, this.player.getLastTurn(), dir);
  }

  @Override
  public Turn finishTurn(Location l, Turn previousTurn, boolean collectTreasure, 
      boolean collectArrow) 
      throws IllegalArgumentException {
    if (l == null) {
      throw new IllegalArgumentException("The turn's location cannot be null");
    }
    // Before being able to finish the turn, we have to check if there is a monster
    // and handle the possible death being eaten.
    boolean eaten = this.deathByMonster(l);
    
    if (eaten) {
      return this.player.finishTurn(l, previousTurn, collectTreasure, collectArrow, eaten);
    }
    else {
      return this.player.finishTurn(l, previousTurn, collectTreasure, collectArrow, eaten);
    }
  }

  @Override
  public List<Turn> getPlayerSequence() {
    return this.player.getTurns();
  }
  
  @Override
  public String getVisualMap() {
    StringBuilder builder = new StringBuilder();
    builder.append("");
    Location[][] locations = this.dungeon.getLocations();
    
    // First line if the dungeon is wrapped:
    if (this.dungeon.getWrapped()) {
      builder.append(" ");
      for (int i = 0; i < this.dungeon.getColumns(); i++) {
        List<Direction> directions = locations[0][i].getPossibleDirections();
        if (directions.contains(Direction.NORTH)) {
          builder.append("# ");
        }
        else {
          builder.append("  ");
        }
      }
      
      builder.append(" \n");
      
    }
    
    
    for (int i = 0; i < this.dungeon.getRows(); i++) {
      
      // Main line containing Locations and horizontal connections.
      for (int j = 0; j < this.dungeon.getColumns(); j++) {
        List<Direction> directions = locations[i][j].getPossibleDirections();
        if (directions.contains(Direction.WEST)) {
          builder.append("#");
        }
        else {
          builder.append(" ");
        }
        
        if (this.player.getLastTurn().getLocation().equals(locations[i][j])) {
          builder.append("@");
        }
        else if (this.dungeon.getStart().equals(locations[i][j])) {
          builder.append("S");
        }
        else if (this.dungeon.getFinish().equals(locations[i][j])) {
          builder.append("F");
        }
        else if (locations[i][j].getType().equals(LocationType.CAVE)) {
          builder.append("C");
        }
        else {
          builder.append("T");
        }
      }
      
      List<Direction> directions = locations[i]
          [this.dungeon.getColumns() - 1].getPossibleDirections();
      
      if (directions.contains(Direction.EAST)) {
        builder.append("#");
      }
      else {
        builder.append(" ");
      }
      
      // End of main iterative line.
      builder.append(" \n");
      
      // Secondary main line containing vertical connections.
      builder.append(" ");
      for (int j = 0; j < this.dungeon.getColumns(); j++) {
        List<Direction> directions2 = locations[i][j].getPossibleDirections();
        if (directions2.contains(Direction.SOUTH)) {
          builder.append("#");
        }
        else {
          builder.append(" ");
        }
        builder.append(" ");
        
      }
      // End of secondary iterative line.
      builder.append(" \n");
    }
    
    return builder.toString();
    
  }
  
  @Override
  public Player getPlayer() {
    return this.player;
  }
  
  @Override
  public String reportPlayer() {
    return this.player.reportTreasures();
  }
  
  @Override
  public String reportLocation() {
    return this.player.getLastTurn().getLocation().toString();
  }
  
  @Override
  public boolean checkFinish(Location l) throws IllegalArgumentException {
    if (l == null) {
      throw new IllegalArgumentException("The location to be checked cannot be null.");
    }
    Location finish = this.dungeon.getFinish();
    return finish.equals(l);
  }
  
  @Override
  public Dungeon getDungeon() {
    return this.dungeon;
  }
  
  @Override
  public String reportObtainedTreasure() {
    return this.player.getLastTurn().getObtainedTreasure().getType().toString();
  }
  
  @Override
  public boolean attackMonster(Player p, Direction dir, int distance) 
      throws IllegalArgumentException, IllegalStateException {
    if (dir == null ) {
      throw new IllegalArgumentException("Direction cannot be null.");
    }
    if (distance < 1) {
      throw new IllegalArgumentException("Distance has to be positive.");
    }
    if (p.getRemainingArrows() < 1) {
      throw new IllegalStateException("No arrows remaining to attack.");
    }
    else {
      // Use the arrow
      p.useArrow();
    }
    
    Location shotDestination = this.getArrowDestination(p.getLastTurn()
        .getLocation(), dir, distance);
    
    
    // Notice that hasMonster and getMonster methods consider only "living" monsters. 
    if (shotDestination.hasMonster()) {
      Monster damagedMonster = shotDestination.getMonster();
      damagedMonster.receiveDamage();
      return true;
    }
    else {
      return false;
    }
  }
  
  
  /**
   * Helper method to get the destination cave of an attack shot.
   * @param shotStart location where the shoot starts
   * @param dir Direction of the shoot
   * @param distance number of caves distance
   * @return final Location the arrow reaches
   */
  private Location getArrowDestination(Location shotStart, Direction dir, int distance) {
    int passedCaves = 0;
    Direction currentEntryPoint = null;
    Location currentLocation = shotStart;
    if (dir.equals(Direction.NORTH)) {
      currentEntryPoint = Direction.SOUTH;
    }
    if (dir.equals(Direction.SOUTH)) {
      currentEntryPoint = Direction.NORTH;
    }
    if (dir.equals(Direction.EAST)) {
      currentEntryPoint = Direction.WEST;
    }
    if (dir.equals(Direction.WEST)) {
      currentEntryPoint = Direction.EAST;
    }
    while (passedCaves < distance) {
      // We check if we have hit a wall
      if (currentEntryPoint == null) {
        return currentLocation;
      }
      else {
        int[] nextLocationIndexes = currentLocation.getOpossiteLocation(currentEntryPoint);
        int row = nextLocationIndexes[0];
        int column = nextLocationIndexes[1];
        currentEntryPoint = currentLocation.getNextEntryPoint(currentEntryPoint);
        currentLocation = this.dungeon.getLocations()[row][column];
        if (currentLocation.getType().equals(LocationType.CAVE)) {
          passedCaves ++;
        }
      }
    }
    return currentLocation;
  }
  
  /**
   * Helper to check if a monster is in player's current location and
   * resolve if the player is eaten by it.
   * @return true if player is eaten and the game is over. False, otherwise.
   */
  private boolean deathByMonster(Location l) {
    if (l.hasMonster()) {
      Monster m = l.getMonster();
      if (m.getHealth() == 2) {
        return true;
      }
      else {
        return this.randomGenerator.nextBoolean();
      }
    }
    else {
      return false;
    }
  }
  
  @Override
  public boolean getIsTest() {
    return this.isTest;
  }

}
