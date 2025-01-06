package dir;

import java.nio.file.Path;

public class RBFolder {

	/**
	 * Folder which we want to copy. This allows to create folders in the target drives when it does not exist
	 * @param _sPathDir
	 */
	protected RBFolder(Path _sPathDir) {
		pPath = _sPathDir; 
	}
	
	/*
	 * Data
	 */
	private Path pPath;

	
	
	
	/*
	 * Getters & Setters
	 */
	public final Path getpPath() {
		return pPath;
	}
	
	
	
	
}
