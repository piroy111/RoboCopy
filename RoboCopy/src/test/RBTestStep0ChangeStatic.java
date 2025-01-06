package test;

import basicmethods.BasicFichiers;
import basicmethods.BasicPrintMsg;
import staticdata.StaticRoboCopy;

class RBTestStep0ChangeStatic {

	protected RBTestStep0ChangeStatic(RBTestManager _sRBTestManager) {
		pRBTestManager = _sRBTestManager;
	}
	
	/*
	 * Data
	 */
	private RBTestManager pRBTestManager;
	private static String FOLDER_TEST = "Test";
	private String pDirectorySource;

	/**
	 * Change all the static folders so that when we run RBManager, it will take the test
	 */
	public final void changeStaticToTest() {
		BasicPrintMsg.displayTitle(this, "changeStaticToTest");
		pDirectorySource = StaticRoboCopy.getDIRECTORY_SOURCE();
		StaticRoboCopy.setDIRECTORY_SOURCE(BasicFichiers.getDirectoryNameCorrect(pDirectorySource)
				+ FOLDER_TEST);
	}
	
	/**
	 * Change back all the static folders
	 */
	public final void changeStaticToNormal() {
		BasicPrintMsg.displayTitle(this, "changeStaticToNormal");
		StaticRoboCopy.setDIRECTORY_SOURCE(pDirectorySource);
	}

	/*
	 * Getters & Setters
	 */
	public final RBTestManager getpRBTestManager() {
		return pRBTestManager;
	}
	
}
