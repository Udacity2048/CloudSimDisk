package org.cloudbus.cloudsimdisk.power;

import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.File;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsimdisk.MyDatacenter;
import org.cloudbus.cloudsimdisk.core.MyCloudSimTags;
import org.cloudbus.cloudsimdisk.util.WriteToResultFile;

/**
 * My Power-aware Datacenter is a Datacenter which overwrites all methods to be in accordance with power-aware ability.
 * 
 * @author Baptiste Louis
 * 
 */
public class MyPowerDatacenter extends MyDatacenter {

	/** The Total Storage Energy in Joule(s). */
	private double	totalStorageEnergy;

	/** The Total Datacenter Energy in Joule(s). */
	private double	totalDatacenterEnergy;

	/**
	 * Creates a new datacenter.
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
	public MyPowerDatacenter(String name, DatacenterCharacteristics characteristics,
			VmAllocationPolicy vmAllocationPolicy, List<MyPowerHarddriveStorage> storageList, double schedulingInterval)
			throws Exception {
		super(name, characteristics, vmAllocationPolicy, storageList, schedulingInterval);

		// sets initial variables
		setTotalDatacenterEnergy(0.0);
		setTotalStorageEnergy(0.0);
	}

	@Override
	protected void processOperationWithStorage(MyPowerHarddriveStorage storage, File tempFile, Cloudlet cl,
			String action) {

		// retrieve the transaction time for this operation
		double transTime = tempFile.getTransactionTime();

		// add the transaction time to the "total active time" of this disk
		storage.setInActiveDuration(storage.getInActiveDuration() + transTime);

		// update the storage state
		double waitingTime = 0.0;
		double eventDelay = 0.0;
		double idleInterval = 0.0;
		if (storage.isIdle()) {
			// handle Idle intervals
			idleInterval = CloudSim.clock() - storage.getLastIdleStartTime();
			storage.getIdleIntervalsHistory().add(idleInterval);
			// handle Active End Time
			storage.setActiveEndAt(CloudSim.clock() + transTime);
			// handle Event for Operation completion
			eventDelay = transTime;
			// handle mode to Active (key = 1)
			storage.setMode(1);
		} else if (storage.isActive()) {
			// handle waiting time
			waitingTime = storage.getActiveEndAt() - CloudSim.clock();
			// handle Time at which all operation will be done)
			storage.setActiveEndAt(storage.getActiveEndAt() + transTime);
			// handle the event delay to schedule the event COUDLET_FILE_DONE
			eventDelay = waitingTime + transTime;
			// Note: an event delay is not a specific Time, it is a duration.
			// also, eventDelay = storage.getActiveEndAt() - CloudSim.clock();
		}

		// compute energy of the operation
		double tempPowerActive = storage.getPowerActive();
		double tempEnergyActive = tempPowerActive * transTime;
		
		// compute energy of previous Idle interval (if the disk was previously in Idle mode)
		double tempPowerIdle = storage.getPowerIdle();
		double tempEnergyIdle =  tempPowerIdle * idleInterval;

		// update total energy of data center
		setTotalStorageEnergy(getTotalStorageEnergy() + tempEnergyActive + tempEnergyIdle);
		setTotalDatacenterEnergy(getTotalDatacenterEnergy() + tempEnergyActive + tempEnergyIdle);
		
		// update total energy of the target hard drive
		storage.setTotalEnergyActive(storage.getTotalEnergyActive() + tempEnergyActive);
		storage.setTotalEnergyIdle(storage.getTotalEnergyIdle() + tempEnergyIdle);

		// handle queue
		storage.setQueueLength(storage.getQueueLength() + 1);

		// Prepare data for the event
		Object[] data = new Object[8];
		data[0] = action; // the action of the operation
		data[1] = cl; // the cloudlet subject to the operation
		data[2] = tempFile; // the file subject to the operation
		data[3] = tempPowerActive; // the power needed during the operation
		data[4] = transTime; // the transaction time of the operation
		data[5] = tempEnergyActive; // the energy consumed by the operation
		data[6] = storage; // the disk subject to the operation
		data[7] = waitingTime; // the waiting time for the operation

		// Schedule an Event confirming that the read/write operation has been done.
		send(this.getId(), eventDelay, MyCloudSimTags.CLOUDLET_FILE_DONE, data);
	}

	@Override
	protected void processCloudletFilesDone(SimEvent ev) {

		// Retrieves data from the ev
		Object[] data = (Object[]) ev.getData();
		String action = (String) data[0];
		Cloudlet cl = (Cloudlet) data[1];
		File tempFile = (File) data[2];
		double tempPower = (double) data[3];
		double transTime = (double) data[4];
		double tempEnergy = (double) data[5];
		MyPowerHarddriveStorage storage = (MyPowerHarddriveStorage) data[6];
		double waitingTime = (double) data[7];

		// store results/information
		WriteToResultFile.AddValueToSheetTab(waitingTime, cl.getCloudletId(), 2);
		WriteToResultFile.AddValueToSheetTab(transTime, cl.getCloudletId(), 3);
		WriteToResultFile.AddValueToSheetTab(CloudSim.clock(), cl.getCloudletId(), 7);
		WriteToResultFile.AddValueToSheetTab(tempFile.getName(), cl.getCloudletId(), 8);
		WriteToResultFile.AddValueToSheetTab(tempFile.getSize(), cl.getCloudletId(), 9);
		WriteToResultFile.AddValueToSheetTab(tempEnergy, cl.getCloudletId(), 12);

		// Print out confirmation that Files have been handled
		Log.formatLine("\n%.6f: %s: Cloudlet # %d: <%s> %s on %s.", CloudSim.clock(), getName(), cl.getCloudletId(),
				tempFile.getName(), action, storage.getName());
		Log.formatLine("%10s Power  consumption of %6.3f Watt(s).", "", tempPower);
		Log.formatLine("%10s Queue Waiting time of %9.6f Seconds(s).", "", waitingTime);
		Log.formatLine("%10s Transaction time   of %9.6f Seconde(s).", "", transTime);
		Log.formatLine("%10s Energy consumption of %6.3f Joule(s).", "", tempEnergy);
		Log.printLine();

		// handle queue
		storage.setQueueLength(storage.getQueueLength() - 1);

		// if there is no further operation on the disk
		if (storage.getActiveEndAt() <= CloudSim.clock()) {
			storage.setMode(0); // switch to idle mode
			storage.setLastIdleStartTime(CloudSim.clock()); // handle Idle intervals
			storage.setActiveEndAt(0.0); // reset EndAt time
		}
	}

	// GETTERs and SETTERs

	/**
	 * Gets the total Energy related to Storage.
	 * 
	 * @return the totalStorageEnergy
	 */
	public double getTotalStorageEnergy() {
		return totalStorageEnergy;
	}

	/**
	 * Sets the total Energy related to Storage.
	 * 
	 * @param totalStorageEnergy
	 *            the totalStorageEnergy to set
	 */
	public void setTotalStorageEnergy(double totalStorageEnergy) {
		this.totalStorageEnergy = totalStorageEnergy;
	}

	/**
	 * Gets the total energy related to the Datacenter.
	 * 
	 * @return the totalDatacenterEnergy
	 */
	public double getTotalDatacenterEnergy() {
		return totalDatacenterEnergy;
	}

	/**
	 * Sets the total energy related to the Datacenter.
	 * 
	 * @param totalDatacenterEnergy
	 *            the totalDatacenterEnergy to set
	 */
	public void setTotalDatacenterEnergy(double totalDatacenterEnergy) {
		this.totalDatacenterEnergy = totalDatacenterEnergy;
	}
}
