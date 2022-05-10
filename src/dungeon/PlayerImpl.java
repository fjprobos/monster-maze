package dungeon;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of the player interface. It stores lists of treasures collected until
 * the last turn played, all the turns played in order and its name.
 *
 */
public class PlayerImpl implements Player {
  
  private final String name;
  private final ArrayList<Turn> turns;
  private final LinkedList<Item> treasures;
  private final ArrayList<Arrow> arrows;
  
  /**
   * Player constructor with its name and an already
   * created initial turn.
   * @param name String that indicates the way of calling this character.
   * @param initialTurn first turn of the game
   */
  public PlayerImpl(String name, Turn initialTurn) throws IllegalArgumentException {
    if (initialTurn == null) {
      throw new IllegalArgumentException("The initial turn cannot be null.");
    }
    this.name = name;
    this.turns = new ArrayList<Turn>();
    this.treasures = new LinkedList<Item>();
    this.turns.add(initialTurn);
    this.arrows = new ArrayList<Arrow>();
    for (int i = 0; i < 3; i++) {
      Arrow a = new ArrowImpl();
      a.collect(this);
      this.arrows.add(a);
    }
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public List<Item> getTreasures() {
    return this.treasures;
  }
  
  @Override
  public List<Turn> getTurns() {
    return this.turns;
  }

  @Override
  public void addTreasure(Item t) {
    this.treasures.add(t);

  }

  @Override
  public Location move(Dungeon d, Turn t, Direction dir) throws IllegalArgumentException {
    if (d == null || t == null || dir == null) {
      throw new IllegalArgumentException("d,t or dir cannot be null.");
    }
    
    // We first double check if it is possible to move to the north.
    Turn currentTurn = this.turns.get(this.turns.size() - 1);
    Location currentLocation = currentTurn.getLocation();
    List<Direction> possibleDirections = currentLocation.getPossibleDirections();
    int[] newLocationIndex;
    Location newLocation;
    
    if (possibleDirections.contains(dir)) {
      // We obtain the new location
      switch (dir.name()) {
        case "NORTH":
          newLocationIndex =  currentLocation.getNorth();
          break;
        case "SOUTH":
          newLocationIndex =  currentLocation.getSouth();
          break;
        case "EAST":
          newLocationIndex =  currentLocation.getEast();
          break;
        case "WEST":
          newLocationIndex =  currentLocation.getWest();
          break;
        default:
          newLocationIndex =  currentLocation.getNorth();
      }
      
      newLocation = d.getLocations()[newLocationIndex[0]][newLocationIndex[1]];
      
    }
    else {
      throw new IllegalArgumentException("Direction is not allowed in current location.");
    }
    
    return newLocation;
  }
  
  @Override
  public Turn finishTurn(Location l, Turn previousTurn, boolean collectTreasure,
      boolean collectArrow, boolean deathByMonster) 
      throws IllegalArgumentException {
    if (l == null) {
      throw new IllegalArgumentException("The location of the turn cannot be null");
    }
    
    Turn newTurn;
    
    if (deathByMonster) {
      // Turn constructor when we know player has bee eaten by monster.
      newTurn = new TurnImpl(l, previousTurn);
    }
    else {
      // New turn with new location is created. Constructor will retrieve 
      // the treasure and arrow if exists and indicated by the user.
      newTurn = new TurnImpl(l, previousTurn, collectTreasure, collectArrow);
    }
    
    // Turn, treasures and arrows are added to list.
    this.turns.add(newTurn);
    if ( newTurn.gotTreasure()) {
      newTurn.getObtainedTreasure().collect(this);
      this.treasures.add(newTurn.getObtainedTreasure());
    }
    if ( newTurn.gotArrow()) {
      newTurn.getObtainedArrow().collect(this);
      this.arrows.add(newTurn.getObtainedArrow());
    }
    return newTurn;
  }
  
  @Override
  public Turn getLastTurn() {
    int last = this.turns.size() - 1;
    return this.turns.get(last);
  }
  
  @Override
  public String reportTreasures() {
    StringBuilder builder = new StringBuilder();
    builder.append("Player name: ").append(this.name).append("<br/>");
    builder.append("Available arrows: ").append(this.getRemainingArrows()).append("<br/>");
    builder.append("Current treasures collected:<br/>");
    
    for (Item t: this.treasures) {
      builder.append(t.getType().toString()).append("<br/>");
    }
    
    return builder.toString();
  }
  
  @Override
  public int getRemainingArrows() {
    return (int) this.arrows.stream().filter(a -> !a.isUsed()).count();
  }
  
  @Override
  public void useArrow() {
    if (this.getRemainingArrows() < 1) {
      throw new IllegalStateException("No arrows to use.");
    }
    else {
      for (Arrow a : this.arrows) {
        if (!a.isUsed()) {
          a.useArrow();
          break;
        }
      }
    }
  }


}
