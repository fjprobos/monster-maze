package view;


import java.awt.event.KeyListener;

import dungeon.Controller;

/**
 * Interface of our Dungeon Game view.
 */
public interface DungeonView {

  /**
   * Set up the controller to handle dungeon buttons events in this view.
   * @param listener the controller
   */
  void addButtonListeners(Controller listener);
  
  /**
   * Shows a message in the main text box at the top of the View.
   */
  void mainTextBoxMessage(String message);
  
  /**
   * Shows a current location report in the text box located at the left-bottom
   * part of the view.
   */
  void locationTextBoxMessage(String message);
  
  /**
   * Shows a player report in the text box located at the right-bottom
   * part of the view.
   */
  void playerTextBoxMessage(String message);
  
  
  /**
   * Method to ad keyboard listeners in the view.
   * 
   * @param listener the listener to add
   */
  void addKeyListener(KeyListener listener);
  
  /**
   * Method to reset focus in the component storing they key listeners.
   * So, the player is able to input data by keyboard whenever is
   * necessary.
   */
  void resetFocus();
  
  /**
   * Method to show the image of a location in the view when the player
   * has already explored it.
   * 
   * @param i row index of the desired location.
   * @param j column index of the desired location.
   */
  void setDungeonLocationVisible(int i, int j);
  
  /**
   * Method to show the image of a certain level of stench felt.
   * 
   * @param i row index of the desired location.
   * @param j column index of the desired location.
   * @param level level of stench to be displayed.
   */
  void setDungeonLocationStench(int i, int j, int level);
  
  /**
   * Clears the stench images.
   */ 
  void clearDungeonLocationStench();
  
  /**
   * Method to show images of items collected by the player.
   * They last two seconds and then disappear.
   * 
   * @param i row index of the desired location.
   * @param j column index of the desired location.
   * @param path containing the name of the element to be displayed.
   */
  void setDungeonLocationFlashImage(int i, int j, String path);
  
  /**
   * Method to show the image of a monster in the desired locaiton.
   * 
   * @param i row index of the desired location.
   * @param j column index of the desired location.
   */
  void setMonsterImage(int i, int j);
  
}