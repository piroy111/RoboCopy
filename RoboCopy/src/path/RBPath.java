package path;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RBPath {

	/**
	 * File which we want to copy (in the form of a Path), and the list of the folders in which we want to copy it
	 * @param _sPathSource
	 */
	protected RBPath(Path _sPathSource) {
		pPathSource = _sPathSource;
		/*
		 * Initiate
		 */
		pListDirTarget = new ArrayList<>();
	}
	
	/*
	 * Data
	 */
	private Path pPathSource;
	private List<String> pListDirTarget;
	
	/**
	 * Add a new PathTarget
	 * @param _sPathStr
	 */
	public final void addNewPathTarget(String _sDirTarget) {
		if (!pListDirTarget.contains(_sDirTarget)) {
			pListDirTarget.add(_sDirTarget);
		}
	}

	/*
	 * Getters & Setters
	 */
	public final Path getpPathSource() {
		return pPathSource;
	}
	public final List<String> getpListDirTarget() {
		return pListDirTarget;
	}
	
	
	
	
}
