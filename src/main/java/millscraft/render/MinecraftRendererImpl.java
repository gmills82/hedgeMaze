package millscraft.render;

import millscraft.mazeGenerator.Grid;
import millscraft.mazeGenerator.render.Renderer;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 * @author Grant Mills
 * @since 3/11/18
 */
public class MinecraftRendererImpl implements Renderer<Location> {
	private Location startingLocation;

	public MinecraftRendererImpl(Location startingLocation) {
		this.startingLocation = startingLocation;
	}

	@Override
	public Location render(Grid grid) {
		if(null == grid) {
			throw new IllegalArgumentException("Maze cannot be null.");
		}

		//Render the maze
		//Iterate over each cell
		//Call drawWall where appropriate

		return startingLocation;
	}

	/**
	 * Renders a wall (line) in minecraft of the given height
	 * @param startingLocation - bottom block at the starting location
	 * @param endLocation - bottom block at the end location
	 * @param wallMaterial - material to change the blocks to
	 * @param height - number of blocks high the wall should be
	 */
	private void drawWall(Location startingLocation, Location endLocation, Material wallMaterial, Integer height) {

	}
}
