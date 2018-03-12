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

/**
 * @author Grant Mills
 * @since 3/11/18
 */
public class HedgeMazeExecutor implements CommandExecutor {

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
		Grid maze = new Grid(30,30);
		GeneratorAlgorithm algorithm = new Sidewinder();
		Grid preparedMaze = algorithm.prepareMaze(maze);

		//Print the maze
		millscraft.mazeGenerator.render.Renderer<Location> renderer = new MinecraftRendererImpl(Bukkit.getWorlds().get(0).getSpawnLocation());
		Location startingLocation = renderer.render(preparedMaze);

		return true;
	}
}
