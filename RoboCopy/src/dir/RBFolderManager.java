package dir;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import findpaths.RBDirBackUp;
import launchme.RBManager;
import staticdata.StaticRoboCopy;

public class RBFolderManager {

	public RBFolderManager(RBManager _sRBManager) {
		pRBManager = _sRBManager;
		/*
		 * Initiate
		 */
		pMapNameToRBFolder = new HashMap<>();
	}

	/*
	 * Data
	 */
	private RBManager pRBManager;
	private Map<String, RBFolder> pMapNameToRBFolder;


	/**
	 * @return 
	 * @param _sDirStr
	 */
	public final RBFolder getpOrCreateRBFolder(Path _sPathDir) {
		String lDriStr = _sPathDir.toString();
		RBFolder lRBDir = pMapNameToRBFolder.get(lDriStr);
		if (lRBDir == null) {
			lRBDir = new RBFolder(_sPathDir);
			pMapNameToRBFolder.put(lDriStr, lRBDir);
			pRBManager.display(this, "Create " + RBFolder.class.getSimpleName() + ": " + _sPathDir.toString());
		}
		return lRBDir;
	}

	/**
	 * @return Create all the folders  which are missing in the directory target (the back-up)<br>
	 * We need to create the folders first, before copying the files, otherwise the copy method will not work (if some folders are missing)
	 */
	public final void createMissingDirectories() {
		/*
		 * Initiate
		 */
		List<RBDirBackUp> lListPathBackUp = pRBManager.getpRBFindMainDirectories().getpListRBDirBackUp();
		String[] lArrayTypeCopy = new String[]{StaticRoboCopy.getDIRECTORY_BACK_UP_BIJECTION(), StaticRoboCopy.getDIRECTORY_BACK_UP_INJECTION()};
		/*
		 * Loop on all the folders we identified as to be copied
		 */
		for (RBFolder lRBDir : pMapNameToRBFolder.values()) {
			/*
			 * Loop on all the drives on which we will copy
			 */
			for (RBDirBackUp lRBDirBackUp : lListPathBackUp) {
				Path lPathBackUp = lRBDirBackUp.getpPath();
				/*
				 * Loop on all the sub type of copy (bijection, injection)
				 */
				for (String lTypeCopy : lArrayTypeCopy) {
					String lNameDirectory = lPathBackUp.toString()
							+ lTypeCopy	+ lRBDir.getpPath().toString().substring(3);
					File lFile = new File(lNameDirectory);
					if (!lFile.exists()) {
						if (!lFile.mkdirs()) {
							pRBManager.displayError("ERROR: cannot create directory: " + lNameDirectory);
						} else {
							pRBManager.displayAction("CREATED Directory: " + lNameDirectory);
						}
					}
				}
			}
		}
	}




}
