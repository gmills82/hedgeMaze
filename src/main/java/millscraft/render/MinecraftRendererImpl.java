package millscraft.render;

import millscraft.mazeGenerator.Cell;
import millscraft.mazeGenerator.Grid;
import millscraft.mazeGenerator.generator.GeneratorAlgorithm;
import millscraft.mazeGenerator.render.Renderer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.Leaves;
import org.bukkit.material.MaterialData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Renders a given maze as the height and wall material given
 * @author Grant Mills
 * @since 3/11/18
 */
public class MinecraftRendererImpl implements Renderer<Location> {
	private static final Logger logger = LoggerFactory.getLogger(MinecraftRendererImpl.class);


	private static final Integer WALL_THICKNESS = 1;

	private Integer cellSize = 1;
	private Location startingLocation;
	private Integer wallHeight = 2;
	private Material wallMaterial = Material.LEAVES;
	private World world;
	private Integer yLevel;
	private GeneratorAlgorithm algorithm;

	public MinecraftRendererImpl(Location startingLocation) {
		this.startingLocation = startingLocation;

		this.world = startingLocation.getWorld();
		this.yLevel = startingLocation.getBlockY();
	}

	public MinecraftRendererImpl(Location startingLocation, Integer wallHeight) {
		this.startingLocation = startingLocation;
		this.wallHeight = wallHeight;

		this.world = startingLocation.getWorld();
		this.yLevel = startingLocation.getBlockY();
	}

	public MinecraftRendererImpl(Location startingLocation, Integer wallHeight, Material wallMaterial) {
		this.startingLocation = startingLocation;
		this.wallHeight = wallHeight;
		this.wallMaterial = wallMaterial;

		this.world = startingLocation.getWorld();
		this.yLevel = startingLocation.getBlockY();
	}

	public MinecraftRendererImpl(Location startingLocation, Integer wallHeight, Material wallMaterial, GeneratorAlgorithm algorithm) {
		this.startingLocation = startingLocation;
		this.wallHeight = wallHeight;
		this.wallMaterial = wallMaterial;
		this.algorithm = algorithm;

		this.world = startingLocation.getWorld();
		this.yLevel = startingLocation.getBlockY();
	}

	public MinecraftRendererImpl(Location startingLocation, Integer wallHeight, Integer cellSize, Material wallMaterial, GeneratorAlgorithm algorithm) {
		this.startingLocation = startingLocation;
		this.wallHeight = wallHeight;
		this.cellSize = cellSize;
		this.wallMaterial = wallMaterial;
		this.algorithm = algorithm;


		this.world = startingLocation.getWorld();
		this.yLevel = startingLocation.getBlockY();
	}

	@Override
	public Location render(Grid grid) {
		if(null == grid) {
			throw new IllegalArgumentException("Maze cannot be null.");
		}

		//Render the maze
		//Iterate over each cell
		Integer startingX = startingLocation.getBlockX();
		Integer startingZ = startingLocation.getBlockZ();
		logger.debug("Beginning rendering...");
		for(int x = 0; x < grid.getGrid().size(); x++) {
			java.util.List<Cell> row = grid.getGrid().get(x);
			logger.debug("-Printing row " + x);

			for(int y = 0; y < row.size(); y++) {
				Cell currentCell = row.get(y);
				logger.debug("--Printing cell " + y);
				
				//All mazes will be rendered south-east of the given location
				//as that is the positive direction for each axis
				Integer gridX = startingX;
				Integer gridZ = startingZ;
				if(currentCell.getColumn() != 0) {
					gridX = ((currentCell.getColumn()) * (cellSize + WALL_THICKNESS)) + startingX;
				}
				if(currentCell.getRow() != 0) {
					gridZ = ((currentCell.getRow()) * (cellSize + WALL_THICKNESS)) + startingZ;
				}
				Integer gridX2 = gridX + cellSize + WALL_THICKNESS;
				Integer gridZ2 = gridZ + cellSize + WALL_THICKNESS;

				// Check linked cells
				// We only have to check two directions for links as they will handle drawing all the walls
				if(currentCell.getNorth() == null) {
					drawWall(new Location(world, gridX, yLevel, gridZ), new Location(world, gridX2, yLevel, gridZ));
				}
				if(currentCell.getWest() == null) {
					drawWall(new Location(world, gridX, yLevel, gridZ), new Location(world, gridX, yLevel, gridZ2));
				}
				if(!currentCell.isLinked(currentCell.getEast())) {
					drawWall(new Location(world, gridX2, yLevel, gridZ), new Location(world, gridX2, yLevel, gridZ2));
				}
				if(!currentCell.isLinked(currentCell.getSouth())) {
					drawWall(new Location(world, gridX, yLevel, gridZ2), new Location(world, gridX2, yLevel, gridZ2));
				}
			}
		}
		logger.info("End rendering.");

		return startingLocation;
	}

	/**
	 * Renders a wall (line) in minecraft of the given height
	 * @param startingLocation - bottom block at the starting location
	 * @param endLocation - bottom block at the end location
	 */
	private void drawWall(Location startingLocation, Location endLocation) {

		//Get all blocks to be a part of the wall
		ArrayList<Block> wallBlocks = new ArrayList<>();

		Integer startingX = startingLocation.getBlockX();
		Integer startingZ = startingLocation.getBlockZ();
		Integer startingY = startingLocation.getBlockY();
		Integer endingX = endLocation.getBlockX();
		Integer endingZ = endLocation.getBlockZ();

		// End height of the wall
		Integer wallHeightY = (yLevel + wallHeight) - 1;

		// In Minecraft,
		// heading towards a negative Z value is North,
		// heading towards a positive Z value is South,
		// heading towards a negative X value is West,
		// and heading towards a positive X value is East.
		if(startingX.equals(endingX)) {
			//Builds a wall North to South
			if(startingZ > endingZ) {
				for(int a = endingZ; a <= startingZ; a++) {
					for(int c = startingY; c <= wallHeightY; c++) {
						wallBlocks.add(world.getBlockAt(startingX, c, a));
					}
				}
			}else if(startingZ < endingZ) {
				for(int b = startingZ; b <= endingZ; b++) {
					for(int d = startingY; d <= wallHeightY; d++) {
						wallBlocks.add(world.getBlockAt(startingX, d, b));
					}
				}
			}
		}else if(startingZ.equals(endingZ)) {
			//Builds a wall West to East
			if(startingX > endingX) {
				for(int a1 = startingX; a1 <= endingX; a1++) {
					for(int c1 = startingY; c1 <= wallHeightY; c1++) {
						wallBlocks.add(world.getBlockAt(a1, c1, startingZ));
					}
				}
			}else if(endingX > startingX) {
				for(int b1 = startingX; b1 <= endingX; b1++) {
					for(int d1 = startingY; d1 <= wallHeightY; d1++) {
						wallBlocks.add(world.getBlockAt(b1, d1, startingZ));
					}
				}
			}
		}else {
			throw new IllegalArgumentException("Cannot build a wall between these two locations. Locations are at a diagonal.");
		}

		//Convert their material type
		for(Block block : wallBlocks) {
			//Set the material to whatever they give us
			block.setType(wallMaterial);

			//If the block material's data is an instance of leaves then set to not decayable
			BlockState blockState = block.getState();
			MaterialData blockMaterialData = blockState.getData();
			if(blockMaterialData instanceof Leaves) {
				Leaves leaves = (Leaves) blockMaterialData;
				leaves.setDecayable(false);
				//Actually set the data
				blockState.setData(leaves);
				blockState.update(true);
			}
		}
	}


}
