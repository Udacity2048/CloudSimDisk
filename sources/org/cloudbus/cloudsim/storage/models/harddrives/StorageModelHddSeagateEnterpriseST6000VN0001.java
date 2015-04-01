/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */
package org.cloudbus.cloudsim.storage.models.harddrives;

/**
 * Storage Model based on Seagate Enterprise NAS HDD Review .
 * 
 * Link: http://www.storagereview.com/seagate_enterprise_nas_hdd_review
 * 
 * @author Baptiste Louis
 */
public class StorageModelHddSeagateEnterpriseST6000VN0001 extends StorageModelHdd {
	
	/**
	 * The characteristics of the hard drive: capacity (MB), latency (s), seekTime (s), maxTransferRate (MB/s).
	 */
	private final double[]	parameters	= { 6000000, 0.00416, 0.0085, 216 };
	
	/**
	 * The characteristics of the hard drive: capacity, latency, seekTime, maxTransferRate.
	 */
	private final String[]	nominative	= { "Seagate Enterprise", "ST6000VN0001" };
	
	/*
	 * (non-Javadoc)
	 * @see org.cloudbus.cloudsim.power.models.PowerModelSpecPower#getPowerData(int)
	 */
	@SuppressWarnings("javadoc")
	@Override
	protected double getParameter(
			int index) {
		return parameters[index];
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.cloudbus.cloudsim.power.models.PowerModelSpecPower#getPowerData(int)
	 */
	@SuppressWarnings("javadoc")
	@Override
	protected String getnominative(
			int index) {
		return nominative[index];
	}
}
