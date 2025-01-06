package test;

import basicmethods.BasicPrintMsg;

class RBTestManager {

	protected RBTestManager() {
		pRBTestStep0ChangeStatic = new RBTestStep0ChangeStatic(this);
		pRBTestStep1CreateFolderTest = new RBTestStep1CreateFolderTest(this);
		pRBTestStep2CreateConfFiles = new RBTestStep2CreateConfFiles(this);
		pRBTestStep3RunRoboCopy = new RBTestStep3RunRoboCopy(this);
	}
	
	/*
	 * Data
	 */
	private RBTestStep0ChangeStatic pRBTestStep0ChangeStatic;
	private RBTestStep1CreateFolderTest pRBTestStep1CreateFolderTest;
	private RBTestStep2CreateConfFiles pRBTestStep2CreateConfFiles;
	private RBTestStep3RunRoboCopy pRBTestStep3RunRoboCopy;
	
	/**
	 * Run twice RBManager
	 */
	public final void run() {
		/*
		 * Change all the static folders so that when we run RBManager, it will take the test
		 */
		pRBTestStep0ChangeStatic.changeStaticToTest();
		/*
		 * Create all the folders test and create 2 files inside it
		 */
		pRBTestStep1CreateFolderTest.run();
		/*
		 * Create the CONF file with the test folders and the files to be copied
		 */
		pRBTestStep2CreateConfFiles.run();
		/*
		 * Run ROBOCOPY 2 times
		 */
		pRBTestStep3RunRoboCopy.run();
		/*
		 * Change all the static back to what they were
		 */
		pRBTestStep0ChangeStatic.changeStaticToNormal();
	}

	/**
	 * 
	 * @param _sSender
	 * @param _sMsg
	 */
	protected final void display(Object _sSender, String _sMsg) {
		BasicPrintMsg.display(_sSender, _sMsg);
	}
	
	/*
	 * Getters & Setters
	 */
	public final RBTestStep0ChangeStatic getpRBTestStep0ChangeStatic() {
		return pRBTestStep0ChangeStatic;
	}
	public final RBTestStep1CreateFolderTest getpRBTestStep1CreateFolderTest() {
		return pRBTestStep1CreateFolderTest;
	}
	public final RBTestStep2CreateConfFiles getpRBTestStep2CreateConfFiles() {
		return pRBTestStep2CreateConfFiles;
	}
	public final RBTestStep3RunRoboCopy getpRBTestStep3RunRoboCopy() {
		return pRBTestStep3RunRoboCopy;
	}
	
}
