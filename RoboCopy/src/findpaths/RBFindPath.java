package findpaths;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import basicmethods.BasicDateInt;
import basicmethods.BasicFichiers;
import basicmethods.BasicFichiersNio;
import basicmethods.BasicFichiersNioRaw;
import basicmethods.BasicString;
import basicmethods.LitUnFichierEnLignes;
import launchme.RBManager;
import staticdata.StaticRoboCopy;


public class RBFindPath {

	public RBFindPath(RBManager _sRBManager) {
		pRBManager = _sRBManager;
	}


	/*
	 * Data
	 */
	private List<Path> pListDirectoryBackUp;
	private List<Path> pListFilesToCopy;
	private Path pDirectorySource;
	private String pDriveSource;
	private RBManager pRBManager;

	/**
	 * 
	 */
	public final void run() {
		identifyDirectorys();
		loadFilesToCopy();
	}

	/**
	 * Look for the Directory source "ZZ_ROBOCOPY_SOURCE" and for the Directories back-up "ROBOCOPY_BACK_UP"
	 */
	private void identifyDirectorys() {
		pListDirectoryBackUp = new ArrayList<Path>();
		/*
		 * Load all the drives
		 */
		Map<String, String> lMapDriveToType = BasicFichiers.getDrivesValid();
		/*
		 * Keep the drives where we can write and where there is already a back up Directory
		 */
		for (String lDrive : lMapDriveToType.keySet()) {
			String lTypeStr = lMapDriveToType.get(lDrive);
			if (lTypeStr.equals("Local Disk") || lTypeStr.equals("USB Drive")) {
				List<Path> lListPath = BasicFichiersNioRaw.getListPath(Paths.get(lDrive));
				for (Path lPath : lListPath) {
					if (BasicFichiersNio.getIsDirectory(lPath)) {
						String lPathName = lPath.getFileName().toString();
						/*
						 * Case of a Directory target = a back-up
						 */
						if (lPathName.equals(StaticRoboCopy.getDIRECTORY_BACK_UP())) {
							boolean lIsKeepPath = true;
							/*
							 * Check if there is a frequency, if yes check if we need to add this directory
							 */
							LitUnFichierEnLignes lReadFileFrequency = new LitUnFichierEnLignes(lPath.toString(), 
									StaticRoboCopy.getCONF_FREQUENCY_COPY(), false);
							if (lReadFileFrequency.getmIsFichierLuCorrectement()) {
								int lFrequency = BasicString.getInt(lReadFileFrequency.getmContenuFichierLignes().get(0));
								/*
								 * Read last date of copy
								 */
								LitUnFichierEnLignes lReadFileLastCopy = new LitUnFichierEnLignes(lPath.toString(), 
										StaticRoboCopy.getCONF_FREQUENCY_COPY(), false);
								if (lReadFileLastCopy.getmIsFichierLuCorrectement()) {
									int lDateLastCopy = BasicString.getInt(lReadFileLastCopy.getmContenuFichierLignes().get(0));
									lIsKeepPath = BasicDateInt.getmNumberDays(lDateLastCopy, BasicDateInt.getmToday()) >= lFrequency;
								}
							}
							if (lIsKeepPath) {
								pListDirectoryBackUp.add(lPath);
								pRBManager.display(this, "Directory back-up= " + lPath.toString());
							}
						}
						/*
						 * Case of a Directory source
						 */
						else if (lPathName.equals(StaticRoboCopy.getDIRECTORY_SOURCE())) {
							pDirectorySource = lPath;
							pDriveSource = lDrive;
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 */
	private void loadFilesToCopy() {
		/*
		 * Read configuration file
		 */
		String lNameFile = StaticRoboCopy.getCONF_FILE();
		String lDir = pDirectorySource.toString().replaceAll("\\\\", "/") + "/";
		LitUnFichierEnLignes lReadFile = new LitUnFichierEnLignes(lDir, lNameFile, true);
		/*
		 * Fill the list of path to copy
		 */
		pListFilesToCopy = new ArrayList<Path>();
		for (List<String> lLineStr : lReadFile.getmContenuFichierListe()) {
			String lDirToCopy = lLineStr.get(0);
			/*
			 * Case the directory is the drive source
			 */
			if (lDirToCopy.charAt(1) != ':') {
				lDirToCopy = pDriveSource + lDirToCopy;
			}
			/*
			 * Store all the files to copy
			 */
			List<Path> lListPathToCopy = getAndComputeListPath(lDirToCopy);
			pListFilesToCopy.addAll(lListPathToCopy);
			for (Path lPath : lListPathToCopy) {
				pRBManager.display(this, "To be Copied: " + lPath.toString());
			}
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
						/*
						 * If we want the Directory, then we need to check it does exist in the back-up
						 */
						if (lIsKeepPathNew) {
							addDirectoryBakcUp(lPathNew);
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
				lListPath.addAll(getListFilesAndCreateDirectories(lPathPrevious));
			}
		}
		/*
		 * Return
		 */
		return lListPath;
	}

	/**
	 * @return create directories if they don't exist
	 * @param _sPath
	 */
	private void addDirectoryBakcUp(Path _sPath) {
		if (BasicFichiersNio.getIsDirectory(_sPath)) {
			for (Path lPathBackUp : pListDirectoryBackUp) {
				String lNameDirectory = lPathBackUp.toString()
						+ "/" + _sPath.toString().substring(3);
				File lFile = new File(lNameDirectory);
				if (!lFile.exists()) {
					if (!lFile.mkdirs()) {
						pRBManager.displayAction("ERROR: cannot create directory: " + lNameDirectory);
					} else {
						pRBManager.displayAction("CREATED Directory: " + lNameDirectory);
					}
				}
			}
		}
	}

	/**
	 * @return a List of all the files contains in the Folder and in the sub-folders.<br>
	 * Does not contain the sub-folders themselves<br>
	 * computed recursively<br>
	 * @param _sPathSource
	 */
	private final List<Path> getListFilesAndCreateDirectories(Path _sPathSource) {
		List<Path> lListSubPath = BasicFichiersNioRaw.getListPathNoError(_sPathSource);
		List<Path> lListPathToKeep = new ArrayList<Path>();
		for (Path lSubPath : lListSubPath) {
			if (BasicFichiersNioRaw.getIsDirectory(lSubPath)) {
				addDirectoryBakcUp(lSubPath);
				lListPathToKeep.addAll(getListFilesAndCreateDirectories(lSubPath));
			} else {
				lListPathToKeep.add(lSubPath);
			}
		}
		return lListPathToKeep;
	}


	/*
	 * Getters & Setters
	 */
	public final List<Path> getpListDirectoryBackUp() {
		return pListDirectoryBackUp;
	}
	public final List<Path> getpListFilesToCopy() {
		return pListFilesToCopy;
	}
	public final Path getpDirectorySource() {
		return pDirectorySource;
	}
	public final String getpDriveSource() {
		return pDriveSource;
	}
	public final RBManager getpRBManager() {
		return pRBManager;
	}


}
