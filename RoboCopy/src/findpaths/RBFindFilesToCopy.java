package findpaths;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import basicmethods.BasicFichiersNioRaw;
import basicmethods.LitUnFichierEnLignes;
import launchme.RBManager;
import staticdata.StaticRoboCopy;

public class RBFindFilesToCopy {

	public RBFindFilesToCopy(RBManager _sRBManager) {
		pRBManager = _sRBManager;
	}


	/*
	 * Data
	 */
	private List<Path> pListFilesToCopy;
	private RBManager pRBManager;
	
	/**
	 * 
	 */
	public final void run() {
		/*
		 * Read configuration file with the list of the folders which we want to copy
		 */
		String lNameFile = StaticRoboCopy.getCONF_FILE();
		String lDir = pRBManager.getpRBFindMainDirectories().getpDirectorySourceString();
		LitUnFichierEnLignes lReadFile = new LitUnFichierEnLignes(lDir, lNameFile, true);
		/*
		 * Initiate
		 */
		pListFilesToCopy = new ArrayList<Path>();
		String lDriveSource = pRBManager.getpRBFindMainDirectories().getpDriveSource();
		/*
		 * Fill the list of path to copy
		 */
		for (List<String> lLineStr : lReadFile.getmContenuFichierListe()) {
			String lDirToCopy = lLineStr.get(0);
			/*
			 * Case the directory is the drive source
			 */
			if (lDirToCopy.charAt(1) != ':') {
				lDirToCopy = lDriveSource + lDirToCopy;
			}
			/*
			 * Store all the files to copy
			 */
			List<Path> lListPathToCopy = getAndComputeListPath(lDirToCopy);
			pListFilesToCopy.addAll(lListPathToCopy);
		}
	}

	/**
	 * 
	 * @param _sDir
	 * @return
	 */
	public final List<Path> getAndComputeListPath(String _sDir) {
		String[] lArrayDirectory = _sDir.split("/", -1);
		/*
		 * Initiate the first list of Path at the level of the drive
		 */
		String lDrive = lArrayDirectory[0] + "/";
		List<Path> lListPathPrevious = BasicFichiersNioRaw.getListPathNoError(Paths.get(lDrive));
		lListPathPrevious.clear();
		lListPathPrevious.add(Paths.get(lDrive));
		/*
		 * Loop on all the Directories, and keep the Directories where we want all the files
		 */
		for (int lIdxDepthDirectorys = 1; lIdxDepthDirectorys < lArrayDirectory.length - 1; lIdxDepthDirectorys++) {
			String lDirectory = lArrayDirectory[lIdxDepthDirectorys];
			List<Path> lListPathNew = new ArrayList<Path>();
			for (Path lPathPrevious : lListPathPrevious) {
				List<Path> lListPathGeneratedByPrevious = BasicFichiersNioRaw.getListPathNoError(lPathPrevious);
				for (Path lPathNew : lListPathGeneratedByPrevious) {
					boolean lIsKeepPathNew;
					/*
					 * Case we are not finished and the path is not a Directory
					 */
					if (!BasicFichiersNioRaw.getIsDirectory(lPathNew)) {
						lIsKeepPathNew = false;
					}
					/*
					 * Case the Path is a Directory
					 */
					else {
						/*
						 * Case we want to keep all Directories versus we want only a specific Directory
						 */
						if (lDirectory.equals("*")) {
							lIsKeepPathNew = true;
						} else {
							lIsKeepPathNew = lPathNew.getFileName().toString().equals(lDirectory);
						}
					}
					/*
					 * Case we want to keep the path
					 */
					if (lIsKeepPathNew) {
						lListPathNew.add(lPathNew);
					}
				}
			}
			lListPathPrevious = new ArrayList<Path>(lListPathNew);
		}
		/*
		 * Case of the last choice 
		 */
		String lLastChoice = lArrayDirectory[lArrayDirectory.length - 1];
		List<Path> lListPath = new ArrayList<Path>();
		/*
		 * Case we have chosen files --> we look for the file
		 */
		if (!lLastChoice.equals("*")) {
			for (Path lPathPrevious : lListPathPrevious) {
				List<Path> lListPathGeneratedByPrevious = BasicFichiersNioRaw.getListPathNoError(lPathPrevious);
				for (Path lNewPath : lListPathGeneratedByPrevious) {
					if (!BasicFichiersNioRaw.getIsDirectory(lNewPath)
							&& lNewPath.getFileName().toString().equals(lLastChoice)) {
						lListPath.add(lPathPrevious);
					}
				}
			}
		}
		/*
		 * Case we have chosen everything ("*") --> we add everything
		 */
		else {
			for (Path lPathPrevious : lListPathPrevious) {
				lListPath.addAll(getListFilesAndCreateFolders(lPathPrevious));
			}
		}
		/*
		 * Return
		 */
		return lListPath;
	}

	/**
	 * @return a List of all the files contained in the Directory and in the sub-directories.<br>
	 * Does not contain the sub-directories themselves<br>
	 * create the RBDirectories which are at the end (i.e. which don't contain other directories)<br>
	 * computed recursively<br>
	 * @param _sPathSource
	 */
	private final List<Path> getListFilesAndCreateFolders(Path _sPathSource) {
		/*
		 * Initiate
		 */
		List<Path> lListSubPath = BasicFichiersNioRaw.getListPathNoError(_sPathSource);
		List<Path> lListPathToKeep = new ArrayList<Path>();
		boolean lIsSubDirExists = false;
		/*
		 * Loop on all sub-path
		 */
		for (Path lSubPath : lListSubPath) {
			if (BasicFichiersNioRaw.getIsDirectory(lSubPath)) {
				lIsSubDirExists = true;
				lListPathToKeep.addAll(getListFilesAndCreateFolders(lSubPath));
			} else {
				lListPathToKeep.add(lSubPath);
				/*
				 * Create RBPath
				 */
				pRBManager.getpRBPathManager().getpOrCreateRBPath(lSubPath);
			}
		}
		/*
		 * Create RBDir only if there is no sub-directory
		 */
		if (!lIsSubDirExists) {
			pRBManager.getpRBFolderManager().getpOrCreateRBFolder(_sPathSource);
		}
		/*
		 * Return
		 */
		return lListPathToKeep;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
