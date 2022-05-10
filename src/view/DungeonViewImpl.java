package view;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import dungeon.Controller;
import dungeon.GameReadOnly;
import dungeon.Location;
import dungeon.Driver;

/**
 * Class implementing the DungeonView interface. It represents the
 * general frame of our window view and all elements are contained within.
 *
 */
public class DungeonViewImpl extends JFrame implements DungeonView {

  private static JLabel mainDisplay;
  private static JLabel locationDisplay;
  private static JLabel playerDisplay;
  private List<DungeonJButton> dungeonButtons;


  /**
   * The constructor of View. It uses a read only interface of the model that provides
   * all the information needed to create this view.
   *
   * @param caption window title
   * @param game the read-only model that only provides data, 
   *     but the view has no access to modify it
   */
  public DungeonViewImpl(String caption, GameReadOnly game) {
    super(caption);
    int cols = game.getDungeon().getColumns();
    int rows = game.getDungeon().getRows();
    this.dungeonButtons = new ArrayList<DungeonJButton>();
    setSize(800, 800);
    setLocation(150, 100);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel mainPanel = createMainPanel(550, 500);
    JPanel gameBoard = createGameBoard(rows, cols);
        
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        StringBuilder builder = new StringBuilder();
        builder.append("res/dungeon-images-bw/");
        Location l = game.getDungeon().getLocations()[i][j];
        builder.append(l.getImagePath());
        
        
        DungeonJButton location = new DungeonJButton(i, j, builder.toString());
        location.setPreferredSize(new Dimension(100, 100));
        location.setBackground(Color.BLACK);
        gameBoard.add(location); 
        this.dungeonButtons.add(location);
      }
    }
    
    gameBoard.setLayout(new GridLayout(0, cols));
    
    
    mainPanel.add(gameBoard);
    locationDisplay = new JLabel("Location details:");
    mainPanel.add(locationDisplay);
    playerDisplay = new JLabel("Player details:");
    mainPanel.add(playerDisplay);
    JButton exitButton = new JButton("Restart");
    exitButton.addActionListener(l -> {
      this.setVisible(false);
      this.removeAll();
      //this.dispose();
      Driver.main(new String[0]);
    });
    mainPanel.add(exitButton);
    
    JScrollPane scrPane = new JScrollPane(mainPanel);
    this.getContentPane().add(scrPane);
    
    pack();
    
    this.setVisible(true);
  }

  /**
   * Helper method to draw game board cubes.
   *
   */
  private static JPanel createGameBoard(int rows, int cols) {
    JPanel gameBoard = new JPanel();
    gameBoard.setPreferredSize(new Dimension(90 * rows, 90 * cols));
    return gameBoard;
  }

  /**
   * Helper method to create the main panel of this game.
   *
   */
  private static JPanel createMainPanel(int height, int width) {
    mainDisplay = new JLabel("Welcome to the Dungeon Adventure.");
    JPanel mainPanel = new JPanel();
    mainPanel.setPreferredSize(new Dimension(height, width));
    mainPanel.add(mainDisplay);
    JButton exitButton = new JButton("Exit");
    exitButton.addActionListener(l -> {
      System.exit(0);
    });
    mainPanel.add(exitButton);
    mainPanel.setBackground(new Color(186, 215, 222));
    return mainPanel;
  }
  
  @Override
  public void addButtonListeners(Controller listener) {
    
    for (DungeonJButton b :  this.dungeonButtons) {
      b.addActionListener(e -> listener.handleDungeonButton(b.row, b.col));
    }

  }
  
  @Override
  public void mainTextBoxMessage(String message) {
    mainDisplay.setText(message);
  }
  
  @Override
  public void locationTextBoxMessage(String message) {
    locationDisplay.setText(message);
  }
  
  @Override
  public void playerTextBoxMessage(String message) {
    playerDisplay.setText(message);
  }
  
  @Override
  public void resetFocus() {
    this.setFocusable(true);
    this.requestFocus();
  }
  
  @Override
  public void setDungeonLocationVisible(int i, int j) {
    
    for (DungeonJButton b : this.dungeonButtons) {
      if (b.row == i & b.col == j) {
        b.setIconVisible();
      }
    }
  }
  
  @Override
  public void setDungeonLocationStench(int i, int j, int level) {
    String ending = "";
    if (level == 1) {
      ending = "stench01.png";
    }
    else if (level == 2) {
      ending = "stench02.png";
    }
    for (DungeonJButton b : this.dungeonButtons) {
      if (b.row == i & b.col == j) {
        StringBuilder builder = new StringBuilder();
        builder.append("res/dungeon-images-bw/");
        builder.append(ending);
        b.setStenchIcon(builder.toString());
      }
    }
  }
  
  @Override
  public void clearDungeonLocationStench() {
    for (DungeonJButton b : this.dungeonButtons) {
      b.clearStenchIcon();
    }
  }
  
  @Override
  public void setDungeonLocationFlashImage(int i, int j, String pathEnding) {
    for (DungeonJButton b : this.dungeonButtons) {
      if (b.row == i & b.col == j) {
        StringBuilder builder = new StringBuilder();
        builder.append("res/dungeon-images-bw/");
        builder.append(pathEnding);
        builder.append(".png");
        b.setFlashIcon(builder.toString());
      }
    }
  }
  
  @Override
  public void setMonsterImage(int i, int j) {
    for (DungeonJButton b : this.dungeonButtons) {
      if (b.row == i & b.col == j) {
        StringBuilder builder = new StringBuilder();
        builder.append("res/dungeon-images-bw/");
        builder.append("otyugh");
        builder.append(".png");
        b.setMonsterIcon(builder.toString());
      }
    }
  }
  
  private static class DungeonJButton extends JButton {
    
    private final int row;
    private final int col;
    private BufferedImage locationImage;
    private BufferedImage stench;
    private ImageIcon stenchIcon;
    
    /**
     * Constructor that takes as main input the type of shape the sub-square is initially
     * holding.
     * @param t type of the figure that this JPanel will initially draw.
     */
    private DungeonJButton(int row, int col, String path) {
      super();
      this.col = col;
      this.row = row;
      BufferedImage aux;
      try {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
        aux = ImageIO.read(stream);
        aux = resize(aux, 90, 90);
        this.locationImage = aux;
      } catch (IOException e) {
        this.locationImage = null;
        e.printStackTrace();
      }
    }
    
    /**
     * Helper method to resize images to be used in the View's JPanels.
     *
     * @param img  image that will show on the panel before resizing
     * @param newW width of image
     * @param newH height of image
     * @return resized image
     */
    private static BufferedImage resize(BufferedImage img, int newW, int newH) {
      Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
      BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2d = dimg.createGraphics();
      g2d.drawImage(tmp, 0, 0, null);
      g2d.dispose();

      return dimg;
    }
    
    private void setIconVisible() {
      this.setIcon(new ImageIcon(this.locationImage));
    }
    
    private void setStenchIcon(String path) {
      BufferedImage aux;
      try {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
        aux = ImageIO.read(stream);
        aux = resize(aux, 50, 50);
        this.stench = aux;
      } catch (IOException e) {
        this.locationImage = null;
        e.printStackTrace();
      }
      this.stenchIcon = new ImageIcon(this.stench);
      this.setIcon(this.stenchIcon);
    }
    
    private void clearStenchIcon() {
      if (this.stenchIcon != null) {
        this.setDisabledIcon(this.stenchIcon);
        this.stench = null;
      }
    }
    
    private void setFlashIcon(String path) {
      BufferedImage aux;
      try {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
        aux = ImageIO.read(stream);
        ImageIcon flash = new ImageIcon(aux);
        this.setIcon(flash);
        try {
          TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        this.setDisabledIcon(flash);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    
    private void setMonsterIcon(String path) {
      BufferedImage aux = null;
      try {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
        aux = ImageIO.read(stream);
        aux = resize(aux, 50, 50);
      } catch (IOException e) {
        e.printStackTrace();
      }
      this.setIcon(new ImageIcon(aux));
    }
    
  }
    
}
