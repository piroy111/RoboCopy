package test;

import java.nio.file.Path;
import java.util.List;

import basicmethods.BasicPrintMsg;
import findpaths.RBDirBackUp;

public class RBTestStep4CheckInjection {

	protected RBTestStep4CheckInjection(RBTestManager _sRBTestManager) {
		pRBTestManager = _sRBTestManager;
	}

	/*
	 * Data
	 */
	private RBTestManager pRBTestManager;

	/**
	 * 
	 */
	public final void run() {
		BasicPrintMsg.displayTitle(this, this.getClass().getSimpleName());
		
		
		
		
		
		
		
		
		
		
		List<RBDirBackUp> lListRBDirBackUp = pRBTestManager.getpRBTestStep3RunRoboCopy().getpRBManager().getpRBFindMainDirectories().getpListRBDirBackUp();
		
		
		
		
		
		for (RBDirBackUp lRBDirBackUp : lListRBDirBackUp) {
			Path[] lArraySubBAckUp = new Path[]{lRBDirBackUp.getpPathBijection(), lRBDirBackUp.getpPathInjection()};
			for (Path lPathBackUp : lArraySubBAckUp) {
				String lPathBackUpStr = lPathBackUp.getFileName().toString();
				
				
				
				
				
			}
		}
		
		
	}

	/*
	 * Getters & Setters
	 */
	public final RBTestManager getpRBTestManager() {
		return pRBTestManager;
	}

	
}
