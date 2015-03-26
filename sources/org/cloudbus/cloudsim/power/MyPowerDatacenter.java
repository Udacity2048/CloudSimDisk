package org.cloudbus.cloudsim.power;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DataCloudTags;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.File;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.MyCloudlet;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEvent;

/**
 * @author baplou
 * 
 */
public class MyPowerDatacenter extends PowerDatacenter {
	
	// Variables
	
	/** The Total Storage Energy in Watt-second. */
	private double	totalStorageEnergy;
	
	/**
	 * the total simulation time to calculate the Idle Power consumption (might be need to implement directly in Hard
	 * drive)
	 */
	public double	TotalOperationTime	= 0;
	
	/**
	 * The previous time (when we updated the storage energy consumption)
	 */
	public double	previousTime		= 0;
	
	// Constructor
	
	/**
	 * Instantiates a new datacenter.
	 * 
	 * @param name
	 *            the name
	 * @param characteristics
	 *            the res configurations
	 * @param schedulingInterval
	 *            the scheduling interval
	 * @param vmAllocationPolicy
	 *            the vm provisioner
	 * @param storageList
	 *            the storage list
	 * @throws Exception
	 *             the exception
	 */
	public MyPowerDatacenter(
			String name,
			DatacenterCharacteristics characteristics,
			VmAllocationPolicy vmAllocationPolicy,
			List<MyPowerHarddriveStorage> storageList,
			double schedulingInterval) throws Exception {
		super(
				name,
				characteristics,
				vmAllocationPolicy,
				storageList,
				schedulingInterval);
	}
	
	// Methods
	
	/*
	 * (non-Javadoc)
	 * @see cloudsim.Datacenter#processCloudletSubmit(cloudsim.core.SimEvent, boolean)
	 */
	@Override
	protected void processCloudletSubmit(
			SimEvent ev,
			boolean ack) {
		// Handle Cloudlets' Processing, VMs and host updates regarding CPUs utilization
		// (it is not our focus for this simulation).
		// super.processCloudletSubmit(ev, ack);
		// setCloudletSubmitted(CloudSim.clock());
		
		/**
		 * When a cloudlet is received by the Datacenter, First (and once for all) it gets required files and adds data
		 * Files by interacting with the persistent storage available.
		 */
		processCloudletFiles(ev,
				ack);
	}
	
	/**
	 * Process Cloudlet Required Files and Data Files
	 * 
	 * @param ev
	 * @param ack
	 */
	public void processCloudletFiles(
			SimEvent ev,
			boolean ack) {
		// gets the Cloudlet object
		Cloudlet cl = (Cloudlet) ev.getData();
		
		// handle time frame
		double timeFrameStorageEnergy = 0.0;
		
		// Confirm reception Cloudlet
		Log.printLine();
		Log.formatLine("%.3f: %s: Cloudlet # %d has been successfully received. ",
				CloudSim.clock(),
				getName(),
				cl.getCloudletId());
		
		// energy to get the files
		double tempPower;
		double tempTime;
		double tempEnergy;
		
		List<String> requiredFiles = cl.getRequiredFiles();
		Iterator<String> iter = requiredFiles.iterator();
		while (iter.hasNext()) {
			String fileName = iter.next();
			
			for (MyPowerHarddriveStorage storage : this.<MyPowerHarddriveStorage> getStorageList()) {
				File tempFile = storage.getFile(fileName);
				if (tempFile != null) {
					int mode = 1; // mode if either 0(idle) or 1(operating). Can be improve later on.
					tempPower = storage.getPower(mode);
					tempTime = tempFile.getTransactionTime();
					tempEnergy = tempPower * tempTime;
					
					Log.formatLine("%.3f: %s about Cloudlet # %d : <%s> retrieved on %s.",
							CloudSim.clock(),
							getName(),
							cl.getCloudletId(),
							tempFile.getName(),
							storage.getName());
					Log.formatLine("%10s Power  consumption of %6.3f Watt(s)  according to mode %3d.",
							"",
							tempPower,
							mode);
					Log.formatLine("%10s Transaction time   of %6.3f Seconde(s) according to %s specifications.",
							"",
							tempTime,
							storage.getName());
					Log.formatLine("%10s Energy consumption of %6.3f Joule(s) according to mode %3d.",
							"",
							tempEnergy,
							mode);
					Log.printLine();
					
					TotalOperationTime += tempFile.getTransactionTime();
					totalStorageEnergy += tempEnergy;
					timeFrameStorageEnergy = tempEnergy;
					break;
				}
			}
		}
		
		// energy to add the files
		tempPower = 0;
		tempTime = 0;
		tempEnergy = 0;
		
		List<File> dataFiles = new ArrayList<File>();
		if (cl instanceof MyCloudlet) {
			MyCloudlet mycl = (MyCloudlet) cl;
			dataFiles = mycl.getDataFiles();
		}
		
		Iterator<File> iter2 = dataFiles.iterator();
		while (iter2.hasNext()) {
			File tempFile = iter2.next();
			
			// Might need to move this in Datacenter.java
			int answerTag = this.addFile(tempFile);
			
			// test if tempFile has been added
			if (answerTag == DataCloudTags.FILE_ADD_SUCCESSFUL) {
				// find where the file has been added
				for (MyPowerHarddriveStorage storage : this.<MyPowerHarddriveStorage> getStorageList()) {
					// test if the storage id EQUAL the file ResourceID
					if (storage.getId() == tempFile.getResourceID()) {
						int mode = 1; // mode if either 0(idle) or 1(operating). Can be improve later on.
						tempPower = storage.getPower(mode);
						tempTime = tempFile.getTransactionTime();
						tempEnergy = tempPower * tempTime;
						
						Log.formatLine("%.3f: %s about Cloudlet # %d: <%s> added on %s.",
								CloudSim.clock(),
								getName(),
								cl.getCloudletId(),
								tempFile.getName(),
								storage.getName());
						Log.formatLine("%10s Power  consumption of %6.3f Watt(s)  according to mode %3d.",
								"",
								tempPower,
								mode);
						Log.formatLine("%10s Transaction time   of %6.3f Seconde(s) according to %s specifications.",
								"",
								tempTime,
								storage.getName());
						Log.formatLine("%10s Energy consumption of %6.3f Joule(s) according to mode %3d.",
								"",
								tempEnergy,
								mode);
						Log.printLine();
						
						TotalOperationTime += tempFile.getTransactionTime();
						totalStorageEnergy += tempEnergy;
						timeFrameStorageEnergy = tempEnergy;
					}
				}
			} else if (answerTag == DataCloudTags.FILE_ADD_ERROR_EXIST_READ_ONLY) {
				Log.printLine(tempFile.getName() + ".addFile(): Warning - File named <" + tempFile.getName() + "> is already stored");
			}
			
		}
		
		setPower(getPower() + timeFrameStorageEnergy);
	}
	
	/**
	 * @return the totalStorageEnergy
	 */
	public double getTotalStorageEnergy() {
		return totalStorageEnergy;
	}
	
	/**
	 * @param totalStorageEnergy
	 *            the totalStorageEnergy to set
	 */
	public void setTotalStorageEnergy(
			double totalStorageEnergy) {
		this.totalStorageEnergy = totalStorageEnergy;
	}
	
	/**
	 * @return the totalOperationTime
	 */
	public double getTotalOperationTime() {
		return TotalOperationTime;
	}
	
	/**
	 * @param totalOperationTime
	 *            the totalOperationTime to set
	 */
	public void setTotalOperationTime(
			double totalOperationTime) {
		TotalOperationTime = totalOperationTime;
	}
}
