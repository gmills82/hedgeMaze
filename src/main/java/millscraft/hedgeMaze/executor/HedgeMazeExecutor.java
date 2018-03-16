package millscraft.hedgeMaze.executor;

import millscraft.hedgeMaze.utils.Algorithms;
import millscraft.mazeGenerator.Grid;
import millscraft.mazeGenerator.generator.GeneratorAlgorithm;
import millscraft.mazeGenerator.generator.Sidewinder;
import millscraft.hedgeMaze.render.MinecraftRendererImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author Grant Mills
 * @since 3/11/18
 */
public class HedgeMazeExecutor implements CommandExecutor {

	private static final Logger logger = LoggerFactory.getLogger(HedgeMazeExecutor.class);
	private static final Integer DEFAULT_WIDTH = 30;
	private static final Integer DEFAULT_HEIGHT = 30;
	private static final Integer DEFAULT_WALL_HEIGHT = 2;
	private static final Integer DEFAULT_CELL_SIZE = 1;
	private static final Material DEFAULT_WALL_MATERIAL = Material.LEAVES;
	private static final GeneratorAlgorithm DEFAULT_ALGORITHM = new Sidewinder();

	/**
	 * Executes the given command
	 * @param commandSender - source of the command
	 * @param command - the command
	 * @param alias - alias of the command which was used
	 * @param arguments - passed command args
	 * @return
	 */
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] arguments) {
		//Create the maze using defaults
		//Arguments should be in following order:
		//[0]X, [1]Y, [2]Z, [3]Height, [4]width, [5]wallHeight, [6]cellSize, [7]wallMaterial, [8]algorithm
		Integer argXValue = null;
		Integer argYValue = null;
		Integer argZValue = null;
		Integer argMazeHeight = DEFAULT_HEIGHT;
		Integer argMazeWidth = DEFAULT_WIDTH;
		Integer argWallHeight = DEFAULT_WALL_HEIGHT;
		Integer argCellSize = DEFAULT_CELL_SIZE;
		Material argWallMaterial = DEFAULT_WALL_MATERIAL;
		GeneratorAlgorithm argAlgorithm = DEFAULT_ALGORITHM;

		//Give help dialog
		if(arguments.length == 1 && arguments[0].toLowerCase().equals("help")) {
			printUsage(commandSender, command);
			return false;
		}

		//Check for required args
		if(arguments.length < 3) {
			commandSender.sendMessage(command.toString() + " must be given at the very least a location for the hedge maze. Try entering an X, Y, and Z value.");
			return false;
		}

		//Parse arguments from strings to appropriate types
		for(int x = 0; x < arguments.length; x++) {
			if(x == 0) {
				argXValue = Integer.getInteger(arguments[0]);
			}else if(x == 1){
				argYValue = Integer.getInteger(arguments[1]);
			}else if(x == 2){
				argZValue = Integer.getInteger(arguments[2]);
			}else if(x == 3) {
				argMazeHeight = Integer.getInteger(arguments[3]);
			}else if(x == 4) {
				argMazeWidth = Integer.getInteger(arguments[4]);
			}else if(x == 5) {
				argWallHeight = Integer.getInteger(arguments[5]);
			}else if(x == 6) {
				argCellSize = Integer.getInteger(arguments[6]);
			}else if(x == 7) {
				String formattedMaterialString = arguments[7].toUpperCase();
				formattedMaterialString = formattedMaterialString.replace(" ", "_");
				if(Material.valueOf(formattedMaterialString) != null) {
					argWallMaterial = Material.valueOf(formattedMaterialString);
				}else {
					commandSender.sendMessage("Trouble parsing the desired material for the maze walls. Please use a value from this page, https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html and try your command again.");
					return false;
				}
			}else if(x == 8) {
				//Parse user input and try to match to algorithm class
				String formattedAlgorithmString = arguments[8].toUpperCase();
				formattedAlgorithmString = formattedAlgorithmString.replace(" ", "_");
				if(Algorithms.valueOf(formattedAlgorithmString) != null) {
					try {
						//Try to instantiate the algorithm class
						argAlgorithm = Algorithms.valueOf(formattedAlgorithmString).getAlgorithmClazz().newInstance();
					} catch (InstantiationException e) {
						commandSender.sendMessage("There was an error creating the maze with that algorithm.");
						logger.error("Error instantiating algorithm " + Algorithms.valueOf(formattedAlgorithmString).getAlgorithmClazz().getName());
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						commandSender.sendMessage("There was an error creating the maze with that algorithm.");
						logger.error("Error instantiating algorithm " + Algorithms.valueOf(formattedAlgorithmString).getAlgorithmClazz().getName());
						e.printStackTrace();
					}
				}
			}
		}

		//Construct the maze
		Grid maze = new Grid(argMazeHeight, argMazeWidth);
		Grid preparedMaze = argAlgorithm.prepareMaze(maze);

		//Print the maze
		Location mazeLocation = null;
		if(argXValue != null && argYValue != null && argZValue != null) {
			mazeLocation = new Location(Bukkit.getWorlds().get(0), argXValue, argYValue, argZValue);
		}
		if(mazeLocation != null) {
			//Setup the renderer with the user args/defaults
			millscraft.mazeGenerator.render.Renderer<Location> renderer = new MinecraftRendererImpl(mazeLocation, argWallHeight, argCellSize, argWallMaterial, argAlgorithm);

			//Log this for admins
			logger.info("Creating a hedge maze at " + Bukkit.getWorlds().get(0).getSpawnLocation().toString() + ". Created by " + commandSender.getName());

			//The actual render command
			Location startingLocation = renderer.render(preparedMaze);

			commandSender.sendMessage("Your maze is ready at " + mazeLocation.toString());
		}else {
			commandSender.sendMessage("Could not get location in world: " + Bukkit.getWorlds().get(0).getName() + " using coords X: " + argXValue + ", Y: " + argYValue + ", Z: " + argZValue + ".");
			return false;
		}

		return true;
	}

	private void printUsage(CommandSender commandSender, Command command) {
		commandSender.sendMessage("Usage for " + command.toString() + " [Xcoord] [Ycoord] [Zcoord] [mazeHeight] [mazeWidth] [wallHeight] [cellSize] [wallMaterial] [mazeAlgorithm]");
		commandSender.sendMessage("Mazes are always printed south-east of the given location coordinates.");
		commandSender.sendMessage("Xcoord - The X value of the location of the maze (Required)");
		commandSender.sendMessage("Ycoord - The Y value of the location of the maze (Required)");
		commandSender.sendMessage("Zcoord - The Z value of the location of the maze (Required)");
		commandSender.sendMessage("The maze is an array of arrays of cells, or a 2D array.");
		commandSender.sendMessage("mazeHeight - The number of rows of the maze");
		commandSender.sendMessage("mazeWidth - The number of columns of the maze");
		commandSender.sendMessage("WARNING: Creating extremely large mazes may degrade server performance.");
		commandSender.sendMessage("wallHeight - How many blocks high you want the maze walls");
		commandSender.sendMessage("wallMaterial - The block to build the maze with, select from here https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html");

		String algorithmOptions = Arrays.asList(Algorithms.values()).toString();
		commandSender.sendMessage("mazeAlgorithm - " + algorithmOptions);
		commandSender.sendMessage("TIP: Make sure the area for the maze is clear as the plugin will overwrite any blocks in the area wherever it prints walls. Also existing blocks in cells will block maze paths.");
	}
}
