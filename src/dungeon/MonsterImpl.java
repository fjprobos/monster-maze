package dungeon;

/**
 * Implementation of the monster interface which is a being able to live in
 * a cave od the dungeon and kill the player if faced with him.
 * It also is able to update its health when receiving an arrow shoot.
 *
 */
public class MonsterImpl implements Monster {
  
  private final Location cave;
  private int health;
  
  /**
   * Only monster constructor. It receives a location as its den and
   * its health is set to be fully two points.
   * @param l Location den of the monster.
   */
  public MonsterImpl(Location l) {
    this.cave = l;
    this.health = 2;
    l.setMonster(this);
  }

  @Override
  public void receiveDamage() {
    this.health = Math.max(0, this.health - 1);
  }

  @Override
  public Location getLocation() {
    return this.cave;
  }

  @Override
  public int getHealth() {
    return this.health;
  }

}
