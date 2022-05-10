package dungeon;

/**
 * Enum that stores the stages of the turn cycle. It allows the controller
 * to coordinate the different meanings of keyboard input in different
 * parts of the turn.
 */
public enum TurnState {
  TREASURE_PICKUP,
  ARROW_PICKUP,
  ATTACK_DIRECTION,
  ATTACK_REACH,
  GENERAL_INSTRUCTIONS,
  MOVE_INSTRUCTIONS,
  TRANSITION
}
