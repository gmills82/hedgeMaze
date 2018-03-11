package millscraft.executor;

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

		//Print the maze

		return true;
	}
}
