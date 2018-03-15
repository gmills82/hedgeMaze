package millscraft.executor;

import millscraft.mazeGenerator.Grid;
import millscraft.mazeGenerator.generator.GeneratorAlgorithm;
import millscraft.mazeGenerator.generator.Sidewinder;
import millscraft.render.MinecraftRendererImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Grant Mills
 * @since 3/11/18
 */
public class HedgeMazeExecutor implements CommandExecutor {

	private static final Logger logger = LoggerFactory.getLogger(HedgeMazeExecutor.class);
	/**
	 * Executes the given command
	 * @param commandSender - source of the command
	 * @param command - the command
	 * @param s - alias of the command which was used
	 * @param strings - passed command args
	 * @return
	 */
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		//Parse arguments and use defaults if arguments are missing

		//Create the maze
		Grid maze = new Grid(20,20);
		GeneratorAlgorithm algorithm = new Sidewinder();
		Grid preparedMaze = algorithm.prepareMaze(maze);

		//Print the maze
		millscraft.mazeGenerator.render.Renderer<Location> renderer = new MinecraftRendererImpl(Bukkit.getWorlds().get(0).getSpawnLocation());
		logger.info("Creating a hedge maze at " + Bukkit.getWorlds().get(0).getSpawnLocation().toString());
		Location startingLocation = renderer.render(preparedMaze);

		return true;
	}
}
