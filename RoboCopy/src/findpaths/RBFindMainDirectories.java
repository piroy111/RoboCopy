package findpaths;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
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

public class RBFindMainDirectories {

	public RBFindMainDirectories(RBManager _sRBManager) {
		pRBManager = _sRBManager;
		/*
		 * Initiate
		 */
		pMapNameToRBDirBackUp = new HashMap<>();
		pListRBDirBackUp = new ArrayList<>();
	}


	/*
	 * Data
	 */
	private Path pDirectorySource;
	private String pDirectorySourceString;
	private String pDriveSource;
	private RBManager pRBManager;
	private Map<String, RBDirBackUp> pMapNameToRBDirBackUp;
	private List<RBDirBackUp> pListRBDirBackUp;


	/**
	 * Look for the Directory source (C:/, F:/, etc.) which contain a folder "ZZ_ROBOCOPY_SOURCE" (they will be the source) 
	 * and for the Directories back-up which contain a folder "ROBOCOPY_BACK_UP" (they will be the target where we copy the files)
	 */
	public final void run() {
		/*
		 * Load all the drives
		 */
		Map<String, String> lMapDriveToType = BasicFichiers.getDrivesValid();
		/*
		 * Keep the drives where we can write and where there is already a back up Directory
		 */
		List<Path> lListPathBackUp = new ArrayList<>();
		for (String lDrive : lMapDriveToType.keySet()) {
			String lTypeStr = lMapDriveToType.get(lDrive);
			if (lTypeStr.equals("Local Disk") || lTypeStr.equals("USB Drive")) {
				List<Path> lListPath = BasicFichiersNioRaw.getListPath(Paths.get(lDrive));
				for (Path lPath : lListPath) {
					if (BasicFichiersNio.getIsDirectory(lPath)) {
						String lPathStr = lPath.getFileName().toString();
						/*
						 * Case of a Directory target = a back-up
						 */
						if (lPathStr.startsWith(StaticRoboCopy.getDIRECTORY_BACK_UP())) {
							boolean lIsKeepPath = true;
							/*
							 * Check if there is a frequency, if yes check if we need to add this directory
							 */
							LitUnFichierEnLignes lReadFileFrequency = new LitUnFichierEnLignes(lPath.toString(), 
									StaticRoboCopy.getCONF_FREQUENCY_COPY(), false);
							int lFrequency = -1;
							int lDateLastCopy = -1;
							if (lReadFileFrequency.getmIsFichierLuCorrectement()) {
								lFrequency = BasicString.getInt(lReadFileFrequency.getmContenuFichierLignes().get(0));
								/*
								 * Read last date of copy
								 */
								LitUnFichierEnLignes lReadFileLastCopy = new LitUnFichierEnLignes(lPath.toString(), 
										StaticRoboCopy.getDATE_LAST_COPY(), false);
								if (lReadFileLastCopy.getmIsFichierLuCorrectement()) {
									lDateLastCopy = BasicString.getInt(lReadFileLastCopy.getmContenuFichierLignes().get(0));
									lIsKeepPath = BasicDateInt.getmNumberDays(lDateLastCopy, BasicDateInt.getmToday()) > lFrequency;
								}
							}
							/*
							 * Communication. We keep the directory if the last date check is before today minus the frequency
							 */
							String lMsgFound = "I found a new directory back-up= '" + lDrive + "', because it contains the folder '" 
									+ StaticRoboCopy.getDIRECTORY_BACK_UP() + "'";
							String lMsgIsKeep = "";
							if (lIsKeepPath) {
								lMsgIsKeep = "I keep the drive as a back-up";
							} else {
								lMsgIsKeep += " I dont keep the drive as a back-up";
							}
							lMsgIsKeep += ", because the last time it was copied is " + lDateLastCopy
									+ " and the frequency in days to copy is " + lFrequency + " days (0 means real time every 15 minutes)";
							pRBManager.display(this, lMsgFound + ". " + lMsgIsKeep);
							/*
							 * Create the directory back-up
							 */
							if (lIsKeepPath) {
								lListPathBackUp.add(lPath);
							}
						}
						/*
						 * Case of a Directory source
						 */
						else if (StaticRoboCopy.getDIRECTORY_SOURCE().startsWith(lPathStr)) {
							pDirectorySourceString = lDrive + StaticRoboCopy.getDIRECTORY_SOURCE() + "/";
							pDirectorySource = Paths.get(pDirectorySourceString);
							pDriveSource = lDrive;
							pRBManager.display(this, "I found the drive source '" + lDrive + "', because it contains the folder '" + StaticRoboCopy.getDIRECTORY_SOURCE() + "'");
						}
					}
				}
			}
		}
		/*
		 * Check if the path back-up has the sub folder bijection and injection. Create them if they are not here, and add new path to the list 
		 */
		List<Path> lListPathBackUpFullAndClean = new ArrayList<>();
		for (Path lPathBackUp : lListPathBackUp) {
			String lNamePathBackUp = lPathBackUp.getFileName().toString();
			if (!lNamePathBackUp.endsWith(StaticRoboCopy.getDIRECTORY_BACK_UP_BIJECTION())
					&& lNamePathBackUp.endsWith(StaticRoboCopy.getDIRECTORY_BACK_UP_INJECTION())) {
				lListPathBackUpFullAndClean.add(BasicFichiersNio.createDirectory(Paths.get(lNamePathBackUp + StaticRoboCopy.getDIRECTORY_BACK_UP_BIJECTION())));
				lListPathBackUpFullAndClean.add(BasicFichiersNio.createDirectory(Paths.get(lNamePathBackUp + StaticRoboCopy.getDIRECTORY_BACK_UP_INJECTION())));
			} else {
				lListPathBackUpFullAndClean.add(lPathBackUp);
			}
		}
		/*
		 * Create the object RBDirBackUp
		 */
		for (Path lPathBackUp : lListPathBackUpFullAndClean) {
			getpOrCreateRBDirBackUp(lPathBackUp);
		}
	}

	/**
	 * @return 
	 * @param _sDirStr
	 */
	public final RBDirBackUp getpOrCreateRBDirBackUp(Path _sPathDir) {
		String lDriStr = _sPathDir.toString();
		RBDirBackUp lRBDirBackUp = pMapNameToRBDirBackUp.get(lDriStr);
		if (lRBDirBackUp == null) {
			lRBDirBackUp = new RBDirBackUp(_sPathDir);
			pMapNameToRBDirBackUp.put(lDriStr, lRBDirBackUp);
			pListRBDirBackUp.add(lRBDirBackUp);
			pRBManager.display(this, "Create RBDirBackUp: " + _sPathDir.toString());
		}
		return lRBDirBackUp;
	}

	/**
	 * Write the date of last copy in a file in the directory
	 */
	public final void writeDateLastCopyInFile() {
		for (RBDirBackUp lRBDirBackUp : pListRBDirBackUp) {
			String lHeader = "Date of last back-up copy";
			List<String> lListLineToWrite = new ArrayList<String>();
			lListLineToWrite.add("" + BasicDateInt.getmToday());
			BasicFichiers.writeFile(lRBDirBackUp.getpPath().toString(), 
					StaticRoboCopy.getDATE_LAST_COPY(), lHeader, lListLineToWrite);
		}
	}
	
	/*
	 * Getters & Setters
	 */
	public final Path getpDirectorySource() {
		return pDirectorySource;
	}
	public final String getpDriveSource() {
		return pDriveSource;
	}
	public final RBManager getpRBManager() {
		return pRBManager;
	}
	public final String getpDirectorySourceString() {
		return pDirectorySourceString;
	}
	public final List<RBDirBackUp> getpListRBDirBackUp() {
		return pListRBDirBackUp;
	}
	
	
	
	
}
