package launchme;
import basicmethods.BasicPrintMsg;
import basicmethods.BasicString;
import basicmethods.BasicTime;
import basicmethods.LitUnFichierEnLignes;
import dir.RBFolderManager;
import findpaths.RBFindFilesToCopy;
import findpaths.RBFindMainDirectories;
import path.RBPathManager;
import staticdata.StaticRoboCopy;

public class RBManager {

	public RBManager() {
		
	}
	
	/*
	 * Data
	 */
	private static boolean IS_RUN_ONCE_ONLY = false;
	private RBFindMainDirectories pRBFindMainDirectories;
	private RBPathManager pRBPathManager;
	private RBFolderManager pRBDirManager;
	private RBFindFilesToCopy pRBFindFilesToCopy;
	private long pFrequencyCopying;
	
	/**
	 * 
	 */
	public final void run() {
		/*
		 * Instantiate
		 */
		BasicPrintMsg.displayTitle(this, "Start Robocopy");
		instantiateManagers();
		/*
		 * Initiate
		 */
		while (true) {
			displayAction("Start new round of checks");
			runManagers();
			BasicTime.sleep(pFrequencyCopying);
			if (IS_RUN_ONCE_ONLY) {
				return;
			}
		}
	}

	/**
	 * 
	 */
	private void instantiateManagers() {
		pRBFindMainDirectories = new RBFindMainDirectories(this);
		pRBFindFilesToCopy = new RBFindFilesToCopy(this);
		pRBPathManager = new RBPathManager(this);
		pRBDirManager = new RBFolderManager(this);
	}
	
	/**
	 * 
	 */
	private void runManagers() {
		/*
		 * Look for the Directory source (C:/, F:/, etc.) which contain a folder "ZZ_ROBOCOPY_SOURCE" (they will be the source) 
		 * and for the Directories back-up which contain a folder "ROBOCOPY_BACK_UP" (they will be the target where we copy the files)
		 */
		pRBFindMainDirectories.run();
		/*
		 * Load the frequency to check and copy the files from the CONF FILE in "ZZ_ROBOCOPY_SOURCE"
		 */
		loadFrequencyCopying();
		/*
		 * Load CONF files which gives the list of folders to copy. Interpret the '*' of the CONF file in order to list all the folders which we need to read
		 */
		pRBFindFilesToCopy.run();
		/*
		 * Create all the folders (not the files) which are missing in the directory target (the back-up)<br>
		 * We need to create the folders first, before copying the files, otherwise the copy method will not work (if some folders or sub folders are missing)
		 */
		pRBDirManager.createMissingDirectories();
		/*
		 * Prepare the object RBPath
		 */
		pRBPathManager.createPathInBackUpDrives();
		/*
		 * Copy the RBPath
		 */
		pRBPathManager.checkAndCopy();
		/*
		 * Communication
		 */
		pRBFindMainDirectories.writeDateLastCopyInFile();
		displayAction("Check finished successfully");
		displayAction("");
	}
	
	/**
	 * 
	 * @param _sMsg
	 */
	public final void display(Object _sObjectSender, String _sMsg) {
		if (StaticRoboCopy.getDISPLAY_ALL()) {
			BasicPrintMsg.display(_sObjectSender, "  " + _sMsg);
		}
	}

	/**
	 * 
	 * @param _sMsg
	 */
	public final void displayAction(String _sMsg) {
		BasicPrintMsg.display(this, _sMsg);
	}
	
	/**
	 * 
	 * @param _sMsg
	 */
	public final void displayError(String _sMsg) {
		BasicPrintMsg.printError(_sMsg);
	}
	
	/**
	 * loadFrequencyCopying
	 */
	private void loadFrequencyCopying() {
		/*
		 * By default the frequency is 15 minutes (in case we cannot read the CONF file, or in case the CONF file is not there)
		 */
		pFrequencyCopying = 15 * 60 * 1000;
		/*
		 * We try to read the CONF file where the frequency is written
		 */
		try {
			String lDir = pRBFindMainDirectories.getpDirectorySourceString();
			String lNameFile = StaticRoboCopy.getCONF_FREQUENCY_CHECK();
			LitUnFichierEnLignes lReadFile = new LitUnFichierEnLignes(lDir, lNameFile, true);
			/*
			 * Update the frequency from the CONF file
			 */
			pFrequencyCopying = BasicString.getInt(lReadFile.getmContenuFichierLignes().get(0)) * 1000;
		} catch (Exception lException) {
			BasicPrintMsg.error("I cannot read the frequency of updates\nFile= " + StaticRoboCopy.getCONF_FREQUENCY_CHECK());
		}
		/*
		 * Communication
		 */
		display(this, "The frequency to check and copy is " + BasicTime.getHeureTexteHHMMFromLong(pFrequencyCopying) + " (HH:mm)");
	}
	
	/*
	 * Getters & Setters
	 */
	public final RBPathManager getpRBPathManager() {
		return pRBPathManager;
	}
	public final RBFolderManager getpRBFolderManager() {
		return pRBDirManager;
	}
	public final RBFindMainDirectories getpRBFindMainDirectories() {
		return pRBFindMainDirectories;
	}
	public final RBFindFilesToCopy getpRBFindFilesToCopy() {
		return pRBFindFilesToCopy;
	}
	public static final void setIS_RUN_ONCE_ONLY(boolean iS_RUN_ONCE_ONLY) {
		IS_RUN_ONCE_ONLY = iS_RUN_ONCE_ONLY;
	}

}
