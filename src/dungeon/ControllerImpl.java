package dungeon;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import view.DungeonView;
import view.KeyBoardListener;
import view.TextInputForm;

/**
 * Class that implements the controller interface.
 * Mainly, creates an object with appendable and the scanner
 * already created in the driver to get the initial input.
 * I uses several helper methods to handle the different controls
 * of the game.
 *
 */
public class ControllerImpl implements Controller {

  private final DungeonView view;
  private final Game model;
  private TurnState state;
  private Direction attackDirection;
  private int attackReach;
  private boolean attackReady;
  private boolean collectTreasure;
  private boolean collectArrow;
  private Location l;
  
  /**
   * Constructor of the controller. It now uses only the model and view.
   * There is no need of appendable and scanner anymore.
   * @param model model of the dungeon game
   * @param view GUI to interact with the user
   * @throws IllegalArgumentException when input arguments are null.
   */
  public ControllerImpl(Game model, DungeonView view) throws IllegalArgumentException {
    if (model == null || view == null) {
      throw new IllegalArgumentException("Inputs can't be null");
    }
    this.model = model;
    this.view = view;
    
    // Setup the Keylistener of the view.
    this.configureKeyBoardListener();
  }
  
  @Override
  public void startGame(Game g) throws IllegalArgumentException {
    if (g == null) {
      throw new IllegalArgumentException("invalid null model");
    }
    
    StringBuilder builder = new StringBuilder();
    boolean finish = false;
    this.l = g.getDungeon().getStart();
    Player player = g.getPlayer();
    
    while (!finish) {       
      this.collectTreasure = false;
      this.collectArrow = false;
      this.attackReady = false;
      builder = new StringBuilder();
      builder.append("<html>");
      this.view.resetFocus();
      
      // Report smell
      Smell smell = g.getDungeon().checkSmell(l, true);
      if (smell.equals(Smell.MORE_PUNGENT)) {
        builder.append("It smells like something terrible is nearby!<br/>");
        builder.append("</html>");
        this.generalOutput(builder.toString());
        this.view.setDungeonLocationStench(this.l.getRow(), this.l.getColumn(), 2);
        this.sleep(2);
        builder = new StringBuilder();
        builder.append("<html>");
      }
      else if (smell.equals(Smell.LESS_PUNGENT)) {
        builder.append("There is a slight odor in the air...<br/>");
        builder.append("</html>");
        this.generalOutput(builder.toString());
        this.view.setDungeonLocationStench(this.l.getRow(), this.l.getColumn(), 1);
        this.sleep(2);
        builder = new StringBuilder();
        builder.append("<html>");
      }
      
      // In-location actions
      if (this.l.hasTreasure()) {
        this.state = TurnState.TREASURE_PICKUP;
        builder.append("The new location contains a treasure!<br/>");
        builder.append("Press 'c' if you want to pick it up, 'n' otherwise.<br/>");
        builder.append("</html>");
        this.generalOutput(builder.toString());
        
        // After sending message, we reset the String Builder
        builder = new StringBuilder();
        
        // We wait for the user input.... to be handled by the keyLitener
        while (this.state == TurnState.TREASURE_PICKUP) {
          this.sleep(2);
        }
        builder = new StringBuilder();
        builder.append("<html>");
      }
      
      if (this.l.hasArrow()) {
        this.state = TurnState.ARROW_PICKUP;
        builder.append("The new location contains a Crooked Arrow!<br/>");
        builder.append("Press 'c' if you want to pick it up, 'n' otherwise.<br/>");
        builder.append("</html>");
        this.generalOutput(builder.toString());
        
        // After sending message, we reset the String Builder
        builder = new StringBuilder();
        
        // We wait for the user input.... to be handled by the keyLitener
        while (this.state == TurnState.ARROW_PICKUP) {
          this.sleep(2);
        }
        builder = new StringBuilder();
        builder.append("<html>");
      }
      
      // Before finishing the turn, if a monster dwells here, he will appear...
      if (this.l.hasMonster()) {
        builder = new StringBuilder();
        builder.append("<html>");
        builder.append("Suddenly a sordid creature appears. Lets try to flee.<br/>");
        builder.append("</html>");
        this.generalOutput(builder.toString());
        this.sleep(2);
      }
      
      g.finishTurn(this.l, player.getLastTurn(), collectTreasure, collectArrow);
      this.view.setDungeonLocationVisible(this.l.getRow(), this.l.getColumn());
      
      if (g.getPlayer().getLastTurn().deathByMonster()) {
        this.view.setMonsterImage(this.l.getRow(), this.l.getColumn());
        this.reportLose(g);
        finish = true;
        continue;
      }
      
      // If we reach the finish cave, without being eaten by the monster. Player wins.
      if (g.checkFinish(player.getLastTurn().getLocation())) {
        this.reportWin(g);
        finish = true;
        continue;
      }  
      
      // Report and show new tile
      this.report(g);
      
      // General Instructions Phase, see implementation of this private helper method.
      this.move(g);
       
    }
  }
  
  @Override
  public void setValidAttackReach(int reach) throws IllegalArgumentException {
    if (reach < 1) {
      throw new IllegalArgumentException("Reach must be positive");
    }
    this.attackReach = reach;
    this.attackReady = true;
  }
  
  /**
   * Helper method to manage the movement and willingness to attack of the player.
   * @param msgs series of Strings that are appended to our Controller appendable object.
   */
  private void generalOutput(String... msgs) {   
    for (String msg : msgs) {
      this.view.mainTextBoxMessage(msg);
    }
  }
  
  private void locationOutput(String... msgs) {   
    for (String msg : msgs) {
      this.view.locationTextBoxMessage(msg);
    }
  }
  
  private void playerOutput(String... msgs) {   
    for (String msg : msgs) {
      this.view.playerTextBoxMessage(msg);
    }
  }
  
  /**
   * Helper method to manage the movement and willingness to attack of the player.
   * @param g Game model
   * @return location after the player's movement has been processed.
   * @throws InterruptedException when sleep time is interrupted
   */
  private void move(Game g) {
    //Movement
    
    this.state = TurnState.GENERAL_INSTRUCTIONS;
    StringBuilder builder = new StringBuilder();
    builder.append("<html>");
    builder.append("It is time to keep going...<br/>");
    builder.append("Press 's' if you want to shoot the arrow. <br/>");
    builder.append("Otherwise, use mouse or keyboard arrows to move to a neighbour location.<br/>");
    builder.append("</html>");
    this.generalOutput(builder.toString());
    
    while (this.state == TurnState.GENERAL_INSTRUCTIONS) {
      try {
        this.view.resetFocus();
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    
    // If the player selected to attack, we handle that situation
    while (this.state == TurnState.ATTACK_DIRECTION || this.state == TurnState.ATTACK_REACH) {
      try {
        TimeUnit.SECONDS.sleep(1);
        if (this.attackReady) {
          this.attack(g);
          this.state = TurnState.GENERAL_INSTRUCTIONS;
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
  
  /**
   * Helper method to report status of the turn.
   * @param g Full dungeon game model
   */
  private void report(Game g) {
    //Print the status of the turn when starting it.
    StringBuilder builder = new StringBuilder();
    builder.append("<html>");
    builder.append(g.reportLocation());
    builder.append("Available directions in current location:<br/>");
    builder.append(g.getPossibleDirections());
    builder.append("<br/><br/>");
    if (g.getIsTest()) {
      builder.append(g.getVisualMap());
      builder.append("<br/>");
    }
    builder.append("</html>");
    this.locationOutput(builder.toString());
    
    builder = new StringBuilder();
    builder.append("<html>");
    builder.append(g.reportPlayer());
    builder.append("<br/>");
    builder.append("</html>");
    this.playerOutput(builder.toString());
  }
  
  /**
   * Helper method to report when the player wins the game.
   * @param g Full dungeon game model
   */
  private void reportWin(Game g) {
    StringBuilder builder = new StringBuilder();
    builder.append("<html>");
    builder.append("You have reached the final location.<br/>");
    builder.append("The game has finished! YOU WON<br/>");
    builder.append("</html>");
    this.generalOutput(builder.toString());
  }
  
  /**
  * Helper method to report when the player loses the game.
  * @param g Full dungeon game model
  */
  private void reportLose(Game g) {
    StringBuilder builder = new StringBuilder();
    builder.append("<html>");
    builder.append("You were slaughtered by a might monster... "
        + "there is nothing you can do now.<br/>");
    builder.append("GAME OVER<br/>");
    builder.append("</html>");
    this.generalOutput(builder.toString());
  }
  
  /**
  * Helper method shot a bow to a monster in the darkness.
  * @param g Full dungeon game model
  */
  private boolean attack(Game g) {
    
    StringBuilder builder = new StringBuilder(); 
    builder.append("<html>");
    builder.append("With strong focus, you shoot an arrow to the ");
    builder.append(this.attackDirection.toString());
    builder.append(".<br/>");
    try {
      TimeUnit.SECONDS.sleep(2);
      if (g.attackMonster(g.getPlayer(), this.attackDirection, this.attackReach)) {
        builder.append("Something groaned at the distance...<br/>");
      }
      builder.append("</html>");
      this.generalOutput(builder.toString());
      TimeUnit.SECONDS.sleep(3);
      return true;
    }
    catch (IllegalArgumentException e) {
      builder = new StringBuilder(); 
      builder.append(e.getMessage());
      builder.append("<br/>");
      this.generalOutput(builder.toString());
      return false;
    }
    catch (IllegalStateException e) {
      builder = new StringBuilder(); 
      builder.append(e.getMessage());
      builder.append("<br/>");
      this.generalOutput(builder.toString());
      return false;
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return false;
  }
  
  private void configureKeyBoardListener() {
    Map<Character, Runnable> keyTypes = new HashMap<>();
    Map<Integer, Runnable> keyReleases = new HashMap<>();

    keyReleases.put(KeyEvent.VK_LEFT, () -> {
      Location currentLocation = this.model.getPlayer().getLastTurn().getLocation();
      int[] desiredNeighbour = currentLocation.getWest();
      if (desiredNeighbour != null) {
        this.directionInstruction(desiredNeighbour[0], desiredNeighbour[1]);
      }
    });
    
    keyReleases.put(KeyEvent.VK_RIGHT, () -> {
      Location currentLocation = this.model.getPlayer().getLastTurn().getLocation();
      int[] desiredNeighbour = currentLocation.getEast();
      if (desiredNeighbour != null) {
        this.directionInstruction(desiredNeighbour[0], desiredNeighbour[1]);
      }
    });
    
    keyReleases.put(KeyEvent.VK_UP, () -> {
      Location currentLocation = this.model.getPlayer().getLastTurn().getLocation();
      int[] desiredNeighbour = currentLocation.getNorth();
      if (desiredNeighbour != null) {
        this.directionInstruction(desiredNeighbour[0], desiredNeighbour[1]);
      }
    });
    
    keyReleases.put(KeyEvent.VK_DOWN, () -> {
      Location currentLocation = this.model.getPlayer().getLastTurn().getLocation();
      int[] desiredNeighbour = currentLocation.getSouth();
      if (desiredNeighbour != null) {
        this.directionInstruction(desiredNeighbour[0], desiredNeighbour[1]);
      }
    });
    
    keyTypes.put('s', () -> {
      this.attackInstruction();
    });
    
    keyTypes.put('c', () -> {
      this.collectInstruction();
    });
    
    keyTypes.put('n', () -> {
      this.negativeInstruction();
    });


    KeyBoardListener kbd = new KeyBoardListener();
    kbd.setKeyTypedMap(keyTypes);
    kbd.setKeyReleasedMap(keyReleases);

    this.view.addKeyListener(kbd);
    this.view.resetFocus();
  }
  
  
  @Override
  public void handleDungeonButton(int row, int col) throws IllegalArgumentException {
    
    if (row < 0 || col < 0 ) {
      throw new IllegalArgumentException("Row and col arguments must be positive.");
    }
    else {
      this.directionInstruction(row, col);
    }
  }
  
  private void directionInstruction(int row, int col) {
    if (this.state == TurnState.GENERAL_INSTRUCTIONS) {
      Location currentLocation = this.model.getPlayer().getLastTurn().getLocation();
      Direction desiredDirection =  currentLocation.getNeighbourDirection(row, col);
      
      if (desiredDirection != null) {
        
        boolean allowedMovement = this.model.getPossibleDirections()
            .contains(desiredDirection.name());
        
        if (!allowedMovement) {
          this.generalOutput("Invalid input, please try again.<br/>");
          return;
        }
        else {
          Location newLoc = this.model.move(desiredDirection);
          
          if (newLoc != null) {
            this.state = TurnState.TRANSITION;
            this.l = newLoc;
          }
        }
      }
      
    }
    
    if (this.state == TurnState.ATTACK_DIRECTION) {
      Location currentLocation = this.model.getPlayer().getLastTurn().getLocation();
      Direction desiredDirection =  currentLocation.getNeighbourDirection(row, col);
      
      if (desiredDirection != null) {
        
        boolean allowedMovement = this.model.getPossibleDirections()
            .contains(desiredDirection.name());
        
        if (!allowedMovement) {
          this.generalOutput("Invalid input, please try again.<br/>");
          return;
        }
        else {
          this.attackDirection = desiredDirection;
          this.state = TurnState.ATTACK_REACH;
          TextInputForm.setup(this, "Shoot distance", 
              "Please submit a number with the reach of the shoot.", "Shoot");
        }
      }
      
    }
  }
  
  private void attackInstruction() {
    if (this.state == TurnState.GENERAL_INSTRUCTIONS) {
      StringBuilder b = new StringBuilder();
      b.append("Select keyboard arrows to choose an attack direction.");
      this.state = TurnState.ATTACK_DIRECTION;
      this.generalOutput(b.toString());
    }
  }
  
  private void collectInstruction() {
    if (this.state == TurnState.TREASURE_PICKUP) {
      this.collectTreasure = true;
      StringBuilder b = new StringBuilder();
      b.append("<html>");
      b.append("You found a ");
      Location l = this.l;
      b.append(l.getTreasure().getType().toString());
      b.append("!<br/>");
      b.append("</html>");
      this.generalOutput(b.toString());
      this.view.setDungeonLocationFlashImage(this.l.getRow(), this.l.getColumn(), 
          l.getTreasure().getType().toString());
      try {
        TimeUnit.SECONDS.sleep(3);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      this.state = TurnState.TRANSITION;
    }
    if (this.state == TurnState.ARROW_PICKUP) {
      this.collectArrow = true;
      StringBuilder b = new StringBuilder();
      b.append("<html>");
      b.append("You found a croocked arrow!<br/>");
      b.append("</html>");
      this.generalOutput(b.toString());
      this.view.setDungeonLocationFlashImage(this.l.getRow(), this.l.getColumn(), "ARROW");
      try {
        TimeUnit.SECONDS.sleep(3);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      this.state = TurnState.TRANSITION;
    }
  }
  
  private void negativeInstruction() {
    if (this.state == TurnState.TREASURE_PICKUP) {
      this.collectTreasure = false;
      this.state = TurnState.TRANSITION;
    }
    if (this.state == TurnState.ARROW_PICKUP) {
      this.collectArrow = false;
      this.state = TurnState.TRANSITION;
    }
  }
  
  private void sleep(int seconds) {
    try {
      TimeUnit.SECONDS.sleep(seconds);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  

}
