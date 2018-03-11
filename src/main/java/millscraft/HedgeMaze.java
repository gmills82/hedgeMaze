package millscraft;

import millscraft.executor.HedgeMazeExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class HedgeMaze extends JavaPlugin {
    // This code is called after the server starts and after the /reload command
    @Override
    public void onEnable() {
        getLogger().log(Level.INFO, "{0}.onEnable()", this.getClass().getName());

        //Register command
        HedgeMazeExecutor executor = new HedgeMazeExecutor();
        this.getCommand("hedgeMaze").setExecutor(executor);
    }

    // This code is called before the server stops and after the /reload command
    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "{0}.onDisable()", this.getClass().getName());
    }
}
