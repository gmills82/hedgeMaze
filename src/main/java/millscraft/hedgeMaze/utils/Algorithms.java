package millscraft.hedgeMaze.utils;

import millscraft.mazeGenerator.generator.BinaryTree;
import millscraft.mazeGenerator.generator.GeneratorAlgorithm;
import millscraft.mazeGenerator.generator.Sidewinder;

/**
 * @author Grant Mills
 * @since 3/16/18
 */
public enum Algorithms {
	SIDEWINDER(Sidewinder.class),
	BINARYTREE(BinaryTree.class);

	private Class<? extends GeneratorAlgorithm> algorithmClazz;

	Algorithms(Class<? extends GeneratorAlgorithm> algorithmClazz) {
		this.algorithmClazz = algorithmClazz;
	}

	public Class<? extends GeneratorAlgorithm> getAlgorithmClazz() {
		return this.algorithmClazz;
	}
}
