# Hedge Maze
A spigot plugin to allow the easy generation of hedge mazes in Minecraft

## Command Usage
After installing the plugin on a spigot server the player has access to the "hedgeMaze" command.

Usage for hedgeMaze \[Xcoord\] \[Ycoord\] \[Zcoord\] \[mazeHeight\] \[mazeWidth\] \[wallHeight\] \[cellSize\] \[wallMaterial\] \[mazeAlgorithm\]

- Ycoord - The Y value of the location of the maze (Required)
- Zcoord - The Z value of the location of the maze (Required)
- The maze is an array of arrays of cells, or a 2D array.
- mazeHeight - The number of rows of the maze
- mazeWidth - The number of columns of the maze
- wallHeight - How many blocks high you want the maze walls
- wallMaterial - The block to build the maze with, select from here [https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html]

**WARNING**: Creating extremely large mazes may degrade server performance.

**TIP**: Mazes are always printed south-east of the given location coordinates.

**TIP**: Make sure the area for the maze is clear as the plugin will overwrite any blocks in the area wherever it prints walls. Also existing blocks in cells will block maze paths.

## Building the project
The project makes use of another library for generating mazes from different algorithms. It can be found [here](https://github.com/gmills82/mazeGenerator). It is my attempt at porting over the code from [Mazes for Programmers](https://www.amazon.com/Mazes-Programmers-Twisty-Little-Passages/dp/1680500554) by Jamis Buck.

Mazes for Programmers is a wonderful book and I recommend buying it.

In general the project is built using `mvn package`. Please be sure to set the value of `outputDirectory` on the `maven-assembly-plugin` in the `pom.xml` to whatever folder you would like the jar built to. I set mine to my local server's plugin folder for spigot.
