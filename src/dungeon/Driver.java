package dungeon;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import view.DungeonView;
import view.DungeonViewImpl;

/**
 * Driver class to run the game and look it work.
 */
public class Driver {
  
  /**
   * Entry point to the game.
   */
  public static void main(String[] args) {
    
    Readable input = new InputStreamReader(System.in);
    Appendable output = System.out;
    Scanner scan = new Scanner(input);
    String playerName = "Link";
    int randomSeed = 1;
    boolean isTest = false;
    int rows;
    int columns;
    boolean wrapped;
    int interConnectivityIndex;
    int treasurePercentage;
    int monsterNumber;
    
    StringBuilder builder = new StringBuilder();
    builder.append("Welcome to the Dungeon Game!\n\n");
    
    //Row
    builder.append("Please select the number of rows of your dungeon.\n");
    try {
      output.append(builder.toString());
      builder.setLength(0);
    } catch (IOException ioe) {
      scan.close();
      throw new IllegalStateException("Append failed", ioe);
    }
    while (true) {
      int inputInt = scan.nextInt();
      if (inputInt > 1) {
        rows = inputInt;
        break;
      } 
    }
    
    //Column
    builder.append("Please select the number of columns of your dungeon.\n");
    try {
      output.append(builder.toString());
      builder.setLength(0);
    } catch (IOException ioe) {
      scan.close();
      throw new IllegalStateException("Append failed", ioe);
    }
    while (true) {
      int inputInt = scan.nextInt();
      if (inputInt > 1) {
        columns = inputInt;
        break;
      } 
    }
    
    //Wrapped
    builder.append("Please indicate with 1 if you want the dungeon to be wrapped.\n");
    builder.append("Otherwise, type 0.\n");
    try {
      output.append(builder.toString());
      builder.setLength(0);
    } catch (IOException ioe) {
      scan.close();
      throw new IllegalStateException("Append failed", ioe);
    }
    while (true) {
      int inputInt = scan.nextInt();
      if (inputInt == 1) {
        wrapped = true;
        break;
      }
      else if (inputInt == 0) {
        wrapped = false;
        break;
      }
    }
    
    //Interconnectivity
    builder.append("Please select the interconnectivity level of your dungeon.\n");
    try {
      output.append(builder.toString());
      builder.setLength(0);
    } catch (IOException ioe) {
      scan.close();
      throw new IllegalStateException("Append failed", ioe);
    }
    while (true) {
      int inputInt = scan.nextInt();
      if (inputInt > -1) {
        interConnectivityIndex = inputInt;
        break;
      } 
    }
    
    //treasurePercentage
    builder.append("Please select the treasure Percentage of your dungeon with"
        + " a 0 to 100 integer number.\n");
    try {
      output.append(builder.toString());
      builder.setLength(0);
    } catch (IOException ioe) {
      scan.close();
      throw new IllegalStateException("Append failed", ioe);
    }
    while (true) {
      int inputInt = scan.nextInt();
      if (inputInt > -1) {
        treasurePercentage = inputInt;
        break;
      } 
    }
    
    //Monsters
    builder.append("Please select the number of Otyughs in your dungeon. \n");
    try {
      output.append(builder.toString());
      builder.setLength(0);
    } catch (IOException ioe) {
      scan.close();
      throw new IllegalStateException("Append failed", ioe);
    }
    while (true) {
      int inputInt = scan.nextInt();
      if (inputInt > 0) {
        monsterNumber = inputInt;
        break;
      } 
    }
    
    Game g = new GameImpl(rows, columns, interConnectivityIndex, 
        wrapped, treasurePercentage, playerName,
        monsterNumber, randomSeed, isTest);
    
    DungeonView view = new DungeonViewImpl("Dungeon Adventure", g);
    
    scan.close();
    
    input = new InputStreamReader(System.in);
    Controller c = new ControllerImpl(g, view);
    view.addButtonListeners(c);
    
    c.startGame(g);
      
  }

}
