## 1. About/Overview. 
Backend model + controller + view of a dungeon-maze game. 
Dungeons of several sizes and levels of connectivity can be created to be explored by the player.
Mighty monsters dwell in caves waiting to eat a lost human!
Equipped with a bow, the player can slay those monsters and reach the end of the dungeon.
Many treasures await!

## 2. List of features.
- Single Player Mode
- Dungeons ensure connectivity through Kruskal's Minimumm Spanning Tree algorithm.
- Floyd Algorithm used to obtain shortest paths distances between origin/destination pairs.
- Level of treasure abundance can be customized.
- Randomness added to create different dungeons after every game.
- Great visual interface ready to be used by players.
- Full report of the locations to be explored.
- Monsters added in different caves of the dungeon.
- Ability to attack monsters with a bow and crooked arrows.
- Crooked arrows can be found and collected in tunnels and caves of the dungeon.

## 3. How to run the jar file
1. Install Java in your computer.
2. Double click on the jar file to open it thorugh the console.
3. Input through the console the design elements of the game by following the prompt.
4. Play the game moving around the dungeon using keyboard arrows and s to select attack mode.
5. Use mouse click to move to neighbour locations if prefered.

## 4. Sample Images

Run 1 -- *dungeon_gui_1.png*:
Presents a 4 by 4 wrapped dungeon half explored by the player. In the current turn, the player is entering
a location where a strong stench is felt.

Run 2 -- *dungeon_gui_2.png*:
Presents a 4 by 4 wrapped dungeon almost entirely explored by the player. In the current turn, the player is entering
a location where he finds a crooked arrow to be collected.

Run 3 -- *dungeon_gui_3.png*:
Presents a 4 by 4 wrapped dungeon almost entirely explored by the player. In the current turn, the player enters the final
cave without having killed the monster. Hence he is eaten by it and the game is over.

## 5. The design was heavilly improved in terms of GUI introduced. Now we are able to
play the game using images of the dungeon and the elements involved, making it much
more engaging.

## 6. Assumptions.
- Tunnels do not contain treasures.
- Only caves can host monsters.
- There is always a monster at the end of the dungeon.
- By now, only Rubies, Diamnonds and Saphires can be found.
- Arrows can be found both in tunnels and caves.
- The player need to have unused arrows to shoot the bow.
- The game finishes when the player reaches the finish location.
- The player can avoid collecting a treasure if he wants.

## 7. Limitations. Dungeons should not be smaller than 4 by 4 locations. Only North, South, East, West movements are allowed.

## 8. Citations.
- Columbia Kruskal's Algorithm: http://www.columbia.edu/~cs2035/courses/ieor6614.S16/mst-linear.pdf
- Stanford Kruskal's Algorithm: https://web.stanford.edu/class/archive/cs/cs106b/cs106b.1138/lectures/24/Slides24.pdf
- Stanford Floyd-Warshall Algorithm: https://web.stanford.edu/class/cs97si/07-shortest-path-algorithms.pdf
