package staticdata;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import basicmethods.BasicFichiers;
import basicmethods.BasicFichiersNioRaw;
import basicmethods.BasicPrintMsg;

public class StaticRoboCopy {

	/*
	 * Drives
	 */
	private static String DIRECTORY_SOURCE = "ZZ_ROBOCOPY_SOURCE";
	private static String DRIVE = getAndComputeMasterDrive(false);
	private static String DIRECTORY_BACK_UP = "ROBOCOPY_BACK_UP";
	private static String DIRECTORY_BACK_UP_BIJECTION = "/BIJECTION/";
	private static String DIRECTORY_BACK_UP_INJECTION = "/INJECTION/";
	/*
	 * Files
	 */
	private static String CONF_FILE = "Folders to duplicate and to skip.csv";
	private static String CONF_FREQUENCY_CHECK = "Sleeping time between two loops.csv";
	private static String CONF_FREQUENCY_COPY = "Frequency of copying in days.csv";
	private static String DATE_LAST_COPY = "Date of last copy.csv";
	/*
	 * Miscellaneous
	 */
	private static boolean DISPLAY_ALL = true;

	/**
	 * Find out the drive on which we store the files
	 */
	public static String getAndComputeMasterDrive(boolean _sIsCom) {
		/*
		 * Look for all drives
		 */
		Map<String, String> lMapDriveToType = BasicFichiers.getDrivesValid();
		/*
		 * Find the drive with the name 
		 */
		for (String lDrive : lMapDriveToType.keySet()) {
			String lTypeStr = lMapDriveToType.get(lDrive);
			if (lTypeStr.equals("Local Disk") || lTypeStr.equals("USB Drive")) {
				List<Path> lListPaths = BasicFichiersNioRaw.getListPath(Paths.get(lDrive));
				for (Path lPath : lListPaths) {
					if (lPath.getFileName().toString().equals(StaticRoboCopy.getDIRECTORY_SOURCE())) {
						if (_sIsCom) {
							System.out.println("I found the main drive= '" + lDrive + "', because it contains a folder '" + StaticRoboCopy.getDIRECTORY_SOURCE() + "'");
						}
						return lDrive;
					}
				}
			}
		}
		BasicPrintMsg.error("I cannot find the drive which contains the DIRECTORY '" 
				+ StaticRoboCopy.getDIRECTORY_SOURCE() + "'");
		return null;
	}

	/*
	 * Getters & Setters
	 */
	public static String getDIRECTORY_SOURCE() {
		return DIRECTORY_SOURCE;
	}
	public static final String getDRIVE() {
		return DRIVE;
	}
	public static final String getCONF_FILE() {
		return CONF_FILE;
	}
	public static final String getDIRECTORY_BACK_UP() {
		return DIRECTORY_BACK_UP;
	}
	public static final boolean getDISPLAY_ALL() {
		return DISPLAY_ALL;
	}
	public static final String getCONF_FREQUENCY_CHECK() {
		return CONF_FREQUENCY_CHECK;
	}
	public static final String getCONF_FREQUENCY_COPY() {
		return CONF_FREQUENCY_COPY;
	}
	public static final String getDATE_LAST_COPY() {
		return DATE_LAST_COPY;
	}
	public static final String getDIRECTORY_BACK_UP_BIJECTION() {
		return DIRECTORY_BACK_UP_BIJECTION;
	}
	public static final String getDIRECTORY_BACK_UP_INJECTION() {
		return DIRECTORY_BACK_UP_INJECTION;
	}

	public static final void setDIRECTORY_SOURCE(String dIRECTORY_SOURCE) {
		DIRECTORY_SOURCE = dIRECTORY_SOURCE;
	}

	public static final void setDRIVE(String dRIVE) {
		DRIVE = dRIVE;
	}

	public static final void setDIRECTORY_BACK_UP(String dIRECTORY_BACK_UP) {
		DIRECTORY_BACK_UP = dIRECTORY_BACK_UP;
	}

	public static final void setDIRECTORY_BACK_UP_BIJECTION(String dIRECTORY_BACK_UP_BIJECTION) {
		DIRECTORY_BACK_UP_BIJECTION = dIRECTORY_BACK_UP_BIJECTION;
	}

	public static final void setDIRECTORY_BACK_UP_INJECTION(String dIRECTORY_BACK_UP_INJECTION) {
		DIRECTORY_BACK_UP_INJECTION = dIRECTORY_BACK_UP_INJECTION;
	}

	public static final void setCONF_FILE(String cONF_FILE) {
		CONF_FILE = cONF_FILE;
	}

	public static final void setCONF_FREQUENCY_CHECK(String cONF_FREQUENCY_CHECK) {
		CONF_FREQUENCY_CHECK = cONF_FREQUENCY_CHECK;
	}

	public static final void setCONF_FREQUENCY_COPY(String cONF_FREQUENCY_COPY) {
		CONF_FREQUENCY_COPY = cONF_FREQUENCY_COPY;
	}

	public static final void setDATE_LAST_COPY(String dATE_LAST_COPY) {
		DATE_LAST_COPY = dATE_LAST_COPY;
	}

	public static final void setDISPLAY_ALL(boolean dISPLAY_ALL) {
		DISPLAY_ALL = dISPLAY_ALL;
	}

	
	
	
	

	
}
