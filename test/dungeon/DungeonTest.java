package dungeon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;


/**
 * Class to test all elements of a game. It mainly uses the game object as it is the
 * interface capable of calling all important unit elements of the game.
 *
 */
public class DungeonTest {
  
  private Game gEasy;
  private Game gHard;
  private Game gUnwrapped;
  private int rows;
  private int columns;
  
  @Before
  public void setup() {
    this.rows = 5;
    this.columns = 5;
    int interConnectivity = 1;
    int treasurePercentage = 30;
    String playerName = "Link";
    
    gEasy = new GameImpl(rows, columns, interConnectivity, true, 
        treasurePercentage, playerName, 1, 1, true);
    gHard = new GameImpl(rows, columns, interConnectivity, true, 
        treasurePercentage, playerName, 5, 1, true);
    gUnwrapped = new GameImpl(rows, columns, interConnectivity, false, 
        treasurePercentage, playerName, 1, 1, true);
  }
  
  
  @Test
  public void testWrappedDungeon() {
    
    Dungeon d = this.gEasy.getDungeon();
    boolean wrapped = false;
        
    try {
      
      for (int i = 0; i < this.rows; i++) {
        if ( d.getLocations()[i][0].getWest() != null) {
          wrapped = true;
          break;
        }
        if ( d.getLocations()[i][this.columns - 1].getEast() != null) {
          wrapped = true;
          break;
        }
      }
      
      for (int j = 0; j < this.columns; j++) {
        if ( d.getLocations()[0][j].getNorth() != null) {
          wrapped = true;
          break;
        }
        if ( d.getLocations()[this.rows - 1][j].getSouth() != null) {
          wrapped = true;
          break;
        }
      }
      
      boolean expectedValue = true;
      
      assertEquals(expectedValue, wrapped);
    }
    catch (IllegalArgumentException e) {
      fail("An exception should not have been thrown.");
    }
  }
  
  
  @Test
  public void testUnwrappedDungeon() {
    
    Dungeon d = this.gUnwrapped.getDungeon();
    boolean wrapped = false;
        
    try {
      
      for (int i = 0; i < this.rows; i++) {
        if ( d.getLocations()[i][0].getWest() != null) {
          wrapped = true;
          break;
        }
        if ( d.getLocations()[i][this.columns - 1].getEast() != null) {
          wrapped = true;
          break;
        }
      }
      
      for (int j = 0; j < this.columns; j++) {
        if ( d.getLocations()[0][j].getNorth() != null) {
          wrapped = true;
          break;
        }
        if ( d.getLocations()[this.rows - 1][j].getSouth() != null) {
          wrapped = true;
          break;
        }
      }
      
      boolean expectedValue = false;
      
      assertEquals(expectedValue, wrapped);
    }
    catch (IllegalArgumentException e) {
      fail("An exception should not have been thrown.");
    }
  }
  
  
  @Test
  public void testNodesConnectivity() {
    
    Dungeon d = this.gEasy.getDungeon();
    boolean unreachable = false;
    
    try {
      
      for (int i = 0; i < this.rows; i++) {
        for (int j = 0; j < this.columns; j++) {
          for (int k = 0; k < this.rows; k++) {
            for (int l = 0; l < this.columns; l++) {
              
              Location origin = d.getLocations()[i][j];
              Location destination = d.getLocations()[k][l];
              
              if ( d.getPathDistance(origin, destination) > 1000) {
                unreachable = true;
                break;
              }
            }
          }
        }
      }
      boolean expectedValue = false;
    
      assertEquals(expectedValue, unreachable);
    }
    catch (IllegalArgumentException e) {
      fail("An exception should not have been thrown.");
    }
  }
  
  
  @Test
  public void testStartFinishDistance() {
    
    Dungeon d = this.gEasy.getDungeon();
    
    try {
      
      int distance = d.getPathDistance(d.getStart(), d.getFinish());
    
      assertTrue(distance >= 5);
    }
    catch (IllegalArgumentException e) {
      fail("An exception should not have been thrown.");
    }
  }
  
  @Test
  public void testStartingPoint() {
    
    Dungeon d = this.gEasy.getDungeon();
    Location start = d.getStart();
    Location playerStart = gEasy.getPlayer().getLastTurn().getLocation();
    
    try {
      assertEquals(start, playerStart);
    }
    catch (IllegalArgumentException e) {
      fail("An exception should not have been thrown.");
    }
  }
  
  @Test
  public void testPlayerDescription() {
    
    String playerDescription = gEasy.reportPlayer();
    String expectedDescription = "Player name: Link\nAvailable arrows: "
        + "3\nCurrent treasures collected:\n";
    
    try {
      assertEquals(playerDescription, expectedDescription);
    }
    catch (IllegalArgumentException e) {
      fail("An exception should not have been thrown.");
    }
  }
  
  @Test
  public void testLocationDescription() {
    Location current = gEasy.getPlayer().getLastTurn().getLocation();
    StringBuilder builderCoordinates = new StringBuilder();
    builderCoordinates.append(Integer.toString(current.getRow()));
    builderCoordinates.append(" ,");
    builderCoordinates.append(Integer.toString(current.getColumn()));
    List<Direction> possibleDirections = current.getPossibleDirections();
    StringBuilder builderDirections = new StringBuilder();
    for (Direction dir : possibleDirections) {
      builderDirections.append(dir.toString());
      builderDirections.append("\n");
    }
    
    StringBuilder builderExpected = new StringBuilder();
    builderExpected.append("You are currently in a ");
    builderExpected.append(current.getType().toString());
    builderExpected.append(" located in coordinates ");
    builderExpected.append(builderCoordinates.toString());
    builderExpected.append(".\n");
    String expected = builderExpected.toString();
    
    String locationDescription = gEasy.reportLocation();
    
    try {
      assertEquals(locationDescription, expected);
    }
    catch (IllegalArgumentException e) {
      fail("An exception should not have been thrown.");
    }
  }
  
  @Test
  public void testMovement() {
    
    try {
      Direction[] posibleDirections = {Direction.NORTH, 
          Direction.SOUTH, Direction.EAST, Direction.WEST};
      Boolean[] test = {false, false, false, false};
      
      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 30; j++) {
          Game specialGame = new GameImpl(rows, columns, 10, true, 80, "Link", 1, 1, true);
          Location current = specialGame.getPlayer().getLastTurn().getLocation();
          if (current.getPossibleDirections().contains(posibleDirections[i])) {
            specialGame.move(posibleDirections[i]);
            test[i] = true;
            break;
          }
        }
      }
      
      for (int i = 0; i < 4; i++) {
        assertTrue(test[i]);
      }
    }
    catch (IllegalArgumentException e) {
      fail("An exception should not have been thrown.");
    }
  }
  
  @Test
  public void testTreasure() {
    
    try {
      Direction[] posibleDirections = {Direction.SOUTH};
      Boolean test = false;
      
      for (int j = 0; j < 60; j++) {
        Game specialGame = new GameImpl(rows, columns, 10, true, 90, "Link", 1, 1, true);
        Location current = specialGame.getPlayer().getLastTurn().getLocation();
        if (current.getPossibleDirections().contains(posibleDirections[0])) {
          Location newLoc = specialGame.move(posibleDirections[0]);
          if (newLoc.hasTreasure()) {
            Turn thisTurn = specialGame.getPlayer().getLastTurn();
            specialGame.getPlayer().finishTurn(newLoc, thisTurn, true, true, false);
          }
          test = true;
          break;
        }
      }
      
      assertTrue(test);
    }
    catch (IllegalArgumentException e) {
      fail("An exception should not have been thrown.");
    }
  }
  
  
  @Test
  public void testMonsterEndCave() {
    
    Location endCave = this.gEasy.getDungeon().getFinish();
    try {
      assertTrue(endCave.hasMonster());
    }
    catch (IllegalArgumentException e) {
      fail("An exception should not have been thrown.");
    }
  }
  
  @Test
  public void testCorrectMonsterNumber() {
    
    List<Monster> monsters = this.gHard.getDungeon().getMonsters();
    int expectedValue = 5;
    int observedValue = monsters.size();
    try {
      assertEquals(expectedValue, observedValue);
    }
    catch (IllegalArgumentException e) {
      fail("An exception should not have been thrown.");
    }
  }
  
  @Test
  public void testMonstersInCaves() {
    
    List<Monster> monsters = this.gHard.getDungeon().getMonsters();
    
    try {
      for (Monster m : monsters) {
        assertEquals(m.getLocation().getType(), LocationType.CAVE);
      }
    }
    catch (IllegalArgumentException e) {
      fail("An exception should not have been thrown.");
    }
  }
  
  @Test
  public void testNoArrowsRemaining() {
    
    try {
      Player p = this.gHard.getPlayer();
      Direction dir = this.gHard.getPlayer().getLastTurn().getLocation()
          .getPossibleDirections().get(0);
      for (int i = 0; i < 4; i++) {
        this.gHard.attackMonster(p, dir, 1);
      }
      fail("An exception should have been thrown.");
    }
    catch (IllegalStateException e) {
      System.out.println("IllegalStateException thrown correctly when "
          + "no arrows are remaining to attack.");
    }
  }
  
  @Test
  public void TestArrowsTunnel() {
    boolean found = false;
    try {
      for (int i = 0; i < 100; i++) {
        Game specialGame = new GameImpl(rows, columns, 1, true, 100, "Link", 1, 1, true);
        Direction dir = specialGame.getPlayer().getLastTurn().getLocation()
            .getPossibleDirections().get(0);
        Location newLocation = specialGame.move(dir);
        if (newLocation.hasArrow() & newLocation.getType().equals(LocationType.TUNNEL)) {
          found = true;
          break;
        }
      }
      assertTrue(found);
    }
    catch (IllegalStateException e) {
      fail("No tunnels with arrows were found.");
    }
  }
  
  @Test
  public void TestMonsterKilling() {
    boolean found = false;
    try {
      for (int i = 0; i < 100; i++) {
        Game specialGame = new GameImpl(rows, columns, 1, true, 100, "Link", 10, 1, true);
        Player p = this.gHard.getPlayer();
        Location currentLocation = p.getLastTurn().getLocation();
        Direction dir = currentLocation.getPossibleDirections().get(0);
        Location newLocation = p.move(specialGame.getDungeon(), p.getLastTurn(), dir);
        if (newLocation.hasMonster()) {
          for (int j = 0; j < 2; j++) {
            specialGame.attackMonster(p, dir, 1);
          }
          if (!newLocation.hasMonster()) {
            found = true;
            break;
          }
        }
      }
      assertTrue(found);
    }
    catch (IllegalStateException e) {
      fail("No monster killing assertion was met.");
    }
  }
  
  @Test
  public void TestArrowsCollection() {
    int observedValue = 0;
    int expectedValue = 4;
    try {
      for (int i = 0; i < 100; i++) {
        Game specialGame = new GameImpl(rows, columns, 1, true, 100, "Link", 1, 1, true);
        Player p = this.gHard.getPlayer();
        Turn currentTurn = specialGame.getPlayer().getLastTurn();
        Direction dir = specialGame.getPlayer().getLastTurn().getLocation()
            .getPossibleDirections().get(0);
        Location newLocation = specialGame.move(dir);
        if (newLocation.hasArrow()) {
          p.finishTurn(newLocation, currentTurn, false, true, false);
          observedValue = p.getRemainingArrows();
          break;
        }
      }
      assertEquals(expectedValue, observedValue);
    }
    catch (IllegalStateException e) {
      fail("Arrows were not collected as they should have been.");
    }
  }
}
