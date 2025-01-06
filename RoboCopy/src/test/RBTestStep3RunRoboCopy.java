package test;

import basicmethods.BasicPrintMsg;
import basicmethods.BasicTime;
import launchme.RBManager;

class RBTestStep3RunRoboCopy {
	
	protected RBTestStep3RunRoboCopy(RBTestManager _sRBTestManager) {
		pRBTestManager = _sRBTestManager;
	}

	/*
	 * Data
	 */
	private RBTestManager pRBTestManager;
	private RBManager pRBManager;

	/**
	 * 
	 */
	public final void run() {
		BasicPrintMsg.displayTitle(this, this.getClass().getSimpleName());
		/*
		 * Prepare RBManager. Force the static boolean in order to avoid the endless loop
		 */
		BasicTime.sleep(500);
		pRBManager = new RBManager();
		RBManager.setIS_RUN_ONCE_ONLY(true);
		/*
		 * Run
		 */
		pRBManager.run();
	}

	/*
	 * Getters & Setters
	 */
	public final RBTestManager getpRBTestManager() {
		return pRBTestManager;
	}

	public final RBManager getpRBManager() {
		return pRBManager;
	}
	
}
