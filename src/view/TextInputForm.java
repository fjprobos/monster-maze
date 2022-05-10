package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dungeon.Controller;

/**
 * Class to create an extra window to get information from the player.
 * It is used to get the reach of the arrow shoot.
 */
public class TextInputForm extends JFrame implements ActionListener {
  // JTextField
  private static JTextField t;

  // JFrame
  private static JFrame f;

  // JButton
  private static JButton b;

  // label to display text
  private static JLabel l;
  
  //Reference to the controller to mutate its state after entering shoot reach
  private Controller c;
  

  // default constructor
  TextInputForm(Controller c) {
    this.c = c;
  }

  /**
   * Static class used to create all the elements needed for this window.
   * @param c reference to the controller to pass information to it.
   * @param title title to be displayed a the top of the window.
   * @param message message to be given to the player in the middle of the frame.
   * @param button message contained in the only button of this form.
   */
  public static void setup(Controller c, String title, String message, String button) {
    // create a new frame to store text field and button
    f = new JFrame(title);
    f.setLocation(300, 300);
    
    // create a label to display text
    l = new JLabel(message);
    
    // create a new button
    b = new JButton(button);
    
    // create a object of the text class
    TextInputForm te = new TextInputForm(c);
    
    // addActionListener to button
    b.addActionListener(te);
    
    // create a object of JTextField with 16 columns
    t = new JTextField(16);
    
    // create a panel to add buttons and textfield
    JPanel p = new JPanel();
    
    // add buttons and textfield to panel
    p.add(t);
    p.add(b);
    p.add(l);
    
    // add panel to frame
    f.add(p);
    
    // set the size of frame
    f.setSize(300, 200);
    
    f.show();
  }

  // if the button is pressed
  @Override
  public void actionPerformed(ActionEvent e) {
    String s = e.getActionCommand();
    if (s.equals("Shoot")) {
      try {
        int reach = Integer.parseInt(t.getText());
        c.setValidAttackReach(reach);
        f.setVisible(false);
        f.dispose();
      } catch (NumberFormatException nfe) {
        l.setText("Invalid integer input. Try again.");
      }
    }
    else if (s.equals("Submit")) {
      try {
        int reach = Integer.parseInt(t.getText());
        c.setValidAttackReach(reach);
        f.setVisible(false);
        f.dispose();
      } catch (NumberFormatException nfe) {
        l.setText("Invalid integer input. Try again.");
      }
    }
  }
}