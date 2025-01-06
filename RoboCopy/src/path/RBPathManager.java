package path;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import basicmethods.BasicFichiers;
import basicmethods.BasicFichiersNio;
import findpaths.RBDirBackUp;
import launchme.RBManager;
import staticdata.StaticRoboCopy;

public class RBPathManager {

	public RBPathManager(RBManager _sRBManager) {
		pRBManager = _sRBManager;
		/*
		 * Initiate
		 */
		pMapPathStrToPath = new HashMap<String, RBPath>();
	}
	
	/*
	 * Data
	 */
	private RBManager pRBManager;
	private Map<String, RBPath> pMapPathStrToPath;
	
	/**
	 * @return Classic get or create
	 * @param _sPath
	 */
	public final RBPath getpOrCreateRBPath(Path _sPathSource) {
		RBPath lRBPath = pMapPathStrToPath.get(_sPathSource.toString());
		if (lRBPath == null) {
			lRBPath = new RBPath(_sPathSource);
			pMapPathStrToPath.put(_sPathSource.toString(), lRBPath);
			pRBManager.display(this, "Created RBPath: " + _sPathSource.toString());
		}
		return lRBPath;
	}
	
	/**
	 * Create all the Paths which we will need to copy in
	 */
	public final void createPathInBackUpDrives() {
		/*
		 * Initiate
		 */
		List<RBDirBackUp> lListPathBackUp = pRBManager.getpRBFindMainDirectories().getpListRBDirBackUp();
		List<String> lListSubFolderBackUp = new ArrayList<>();
		lListSubFolderBackUp.add(StaticRoboCopy.getDIRECTORY_BACK_UP_BIJECTION());
		lListSubFolderBackUp.add(StaticRoboCopy.getDIRECTORY_BACK_UP_INJECTION());
		/*
		 * 
		 */
		for (RBPath lRBPath : pMapPathStrToPath.values()) {
			for (RBDirBackUp lRBDirBackUp : lListPathBackUp) {
				String lDirBackUp = lRBDirBackUp.getpPath().toString();
				String lNameFolderTarget = lRBPath.getpPathSource().getParent().toString().substring(3);
				for (String lSubFolderBackUp : lListSubFolderBackUp) {
					String lNameDir = BasicFichiers.getDirectoryNameCorrect(lDirBackUp + lSubFolderBackUp + lNameFolderTarget);
					lRBPath.addNewPathTarget(lNameDir);
				}
			}
		}
	}

	/**
	 * Copy all the files that don't exist yet or that have changed
	 */
	public final void checkAndCopy() {
		for (RBPath lRBPath : pMapPathStrToPath.values()) {
			for (String lDirTarget : lRBPath.getpListDirTarget()) {
				BasicFichiersNio.copyFileIfUpdate(lRBPath.getpPathSource(), lDirTarget, false, true);
			}
		}
	}

	/*
	 * Getters & Setters
	 */
	public final RBManager getpRBManager() {
		return pRBManager;
	}
	public final Map<String, RBPath> getpMapPathStrToPath() {
		return pMapPathStrToPath;
	}
	
	
	
	
}
