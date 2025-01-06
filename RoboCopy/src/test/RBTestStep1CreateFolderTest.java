package test;

import java.util.ArrayList;

import basicmethods.BasicFichiers;
import basicmethods.BasicFichiersNio;
import basicmethods.BasicPrintMsg;
import staticdata.StaticRoboCopy;

class RBTestStep1CreateFolderTest {

	protected RBTestStep1CreateFolderTest(RBTestManager _sRBTestManager) {
		pRBTestManager = _sRBTestManager;
	}

	/*
	 * Data
	 */
	private RBTestManager pRBTestManager;
	private static String FOLDER_TO_COPY = "Files to be copied/";
	private String pNameFileTest;
	private int pIdxFileTest;

	/**
	 * Create all the folders test and create 2 files inside it
	 */
	public final void run() {
		BasicPrintMsg.displayTitle(this, this.getClass().getSimpleName());
		createFolderAndAdd2Files("test0/");
		createFolderAndAdd2Files("test0/test1/");
		createFolderAndAdd2Files("test0/test2/");
		createFolderAndAdd2Files("test0/test3/");
		createFolderAndAdd2Files("test0/test1/test8/");
		createFolderAndAdd2Files("test0/test1/test9/test10/");
		createFolderAndAdd2Files("test0/test2/test8/");
		createFolderAndAdd2Files("test0/test2/test11/");
	}

	/**
	 * Create the folder test and create 2 files inside it
	 * @param _sPathStr
	 */
	private void createFolderAndAdd2Files(String _sPathStr) {
		/*
		 * Create folder
		 */
		String lPathStr = BasicFichiers.getDirectoryNameCorrect(getPathTestsFolderToCopy() + _sPathStr);
		BasicFichiersNio.createDirectory(lPathStr);
		pRBTestManager.display(this, "Created fodler= '" + lPathStr + "'");
		/*
		 * Create 2 files
		 */
		for (int lIdx = 0; lIdx < 2; lIdx++) {
			pIdxFileTest++;
			pNameFileTest = "test_" + pIdxFileTest + ".csv";
			BasicFichiers.writeFile(lPathStr, pNameFileTest, null, new ArrayList<>());
			
			////////////////////////////////////////////////////////////////////////////////////
			
//			pRBTestManager.display(this, "Created file test= '" + lPathStr + pNameFileTest + "'");
			
			System.err.println("pListExpectedFolders.add(\"" + lPathStr + "\");");
			
			
			
			////////////////////////////////////////////////////////////////////////////////////			
		}
	}
	
	/**
	 * 
	 * @return
	 */
	protected static String getPathTestsFolderToCopy() {
		return BasicFichiers.getDirectoryNameCorrect(BasicFichiers
				.getDirectoryNameCorrect(StaticRoboCopy.getDRIVE() + StaticRoboCopy.getDIRECTORY_SOURCE())
				+ FOLDER_TO_COPY);
	}
	

	/*
	 * Getters & Setters
	 */
	public final RBTestManager getpRBTestManager() {
		return pRBTestManager;
	}

	public static final String getFOLDER_TO_COPY() {
		return FOLDER_TO_COPY;
	}

}
