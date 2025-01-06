package findpaths;

import java.nio.file.Path;

import basicmethods.BasicFichiersNio;
import staticdata.StaticRoboCopy;

public class RBDirBackUp {

	/**
	 * The target directory (or back-up) in which we are going to copy the files
	 * @param _sPathDir
	 */
	protected RBDirBackUp(Path _sPathDir) {
		pPath = _sPathDir;
		/*
		 * 
		 */
		pPathBijection = BasicFichiersNio.createDirectory(pPath.getFileName().toString() + StaticRoboCopy.getDIRECTORY_BACK_UP_BIJECTION());
		pPathBijection = BasicFichiersNio.createDirectory(pPath.getFileName().toString() + StaticRoboCopy.getDIRECTORY_BACK_UP_BIJECTION());
	}
	
	/*
	 * Data
	 */
	private Path pPath;
	private Path pPathInjection;
	private Path pPathBijection;
	
	/*
	 * Getters & Setters
	 */
	public final Path getpPath() {
		return pPath;
	}
	public final Path getpPathInjection() {
		return pPathInjection;
	}
	public final Path getpPathBijection() {
		return pPathBijection;
	}
	
	
}
