package view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;

/**
 * Class to setup keyboard listeners in our view. It has been taken
 * from the class lectures.
 */
public class KeyBoardListener implements KeyListener {
  private Map<Character, Runnable> keyTypedMap;
  private Map<Integer, Runnable> keyReleasedMap;


  /**
   * Default constructor.
   */
  public KeyBoardListener() {
    // fields get set by their mutators
    keyTypedMap = null;
    keyReleasedMap = null;
  }

  /**
   * Set the map for key typed events. Key typed events in Java Swing are
   * characters.
   * 
   * @param map the actions for keys typed
   */
  public void setKeyTypedMap(Map<Character, Runnable> map) {
    keyTypedMap = map;
  }
  
  /**
   * Set the map for key released events. Key released events in Java Swing are
   * integer codes.
   * 
   * @param map the actions for keys released
   */
  public void setKeyReleasedMap(Map<Integer, Runnable> map) {
    keyReleasedMap = map;
  }


  @Override
  public void keyTyped(KeyEvent e) {
    if (keyTypedMap.containsKey(e.getKeyChar())) {
      keyTypedMap.get(e.getKeyChar()).run();
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (keyReleasedMap.containsKey(e.getKeyCode())) {
      keyReleasedMap.get(e.getKeyCode()).run();
    }
    
  }

}