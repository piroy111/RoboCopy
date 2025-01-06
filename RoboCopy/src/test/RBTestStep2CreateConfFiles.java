package test;

import java.util.ArrayList;
import java.util.List;

import basicmethods.BasicFichiers;
import basicmethods.BasicPrintMsg;
import staticdata.StaticRoboCopy;

class RBTestStep2CreateConfFiles {

	protected RBTestStep2CreateConfFiles(RBTestManager _sRBTestManager) {
		pRBTestManager = _sRBTestManager;
	}

	/*
	 * Data
	 */
	private RBTestManager pRBTestManager;

	/**
	 * Create the CONF file with the test folders and the files to be copied
	 */
	public final void run() {
		BasicPrintMsg.displayTitle(this, this.getClass().getSimpleName());
		/*
		 * Initiate
		 */
		List<String> lListLineToWrite = new ArrayList<>();
		String lHeader;
		String lNameFile;
		String lPathStr = BasicFichiers.getDirectoryNameCorrect(StaticRoboCopy.getDRIVE() + StaticRoboCopy.getDIRECTORY_SOURCE());
		/*
		 * CONF Frequency check
		 */
		lNameFile = StaticRoboCopy.getCONF_FREQUENCY_CHECK();
		lHeader = "TEST: Frequency of updates in seconds";
		lListLineToWrite.clear();
		lListLineToWrite.add("1");
		BasicFichiers.writeFile(lPathStr, lNameFile, lHeader, lListLineToWrite);
		/*
		 * CONF Folder to copy
		 */
		lNameFile = StaticRoboCopy.getCONF_FILE();
		lHeader = "TEST: Folder(s) and files to synchronize";
		lListLineToWrite.clear();
		String lPathTest = RBTestStep1CreateFolderTest.getPathTestsFolderToCopy();
		lListLineToWrite.add(lPathTest + "test0/test_1.csv");
		lListLineToWrite.add(lPathTest + "test0/*/test8/*");
		lListLineToWrite.add(lPathTest + "test0/test1/test9/*");
		BasicFichiers.writeFile(lPathStr, lNameFile, lHeader, lListLineToWrite);
	}

	/*
	 * Getters & Setters
	 */
	public final RBTestManager getpRBTestManager() {
		return pRBTestManager;
	}
	
}
