package org.cloudbus.cloudsimdisk;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.DataCloudTags;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.File;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsimdisk.core.MyCloudSimTags;
import org.cloudbus.cloudsimdisk.power.MyPowerHarddriveStorage;
import org.cloudbus.cloudsimdisk.util.WriteToResultFile;

/**
 * My Datacenter extends Datacenter.java by overwriting some of its methods. The modifications are necessary for the new
 * storage implementation. Further development of CloudSimDisk will implement Host and VMs management.
 * 
 * @author Baptiste Louis
 * 
 */
public class MyDatacenter extends Datacenter {

	/** Round Robin Algorithm temp variable */
	private int	tempRR	= -1;

	/**
	 * The constructor.
	 * 
	 * @param name
	 *            name
	 * @param characteristics
	 *            characteristics
	 * @param vmAllocationPolicy
	 *            VM allocation Policy
	 * @param storageList
	 *            list of Persistent Storage
	 * @param schedulingInterval
	 *            Scheduling Interval
	 * @throws Exception
	 */
	public MyDatacenter(String name, DatacenterCharacteristics characteristics, VmAllocationPolicy vmAllocationPolicy,
			List<? extends Storage> storageList, double schedulingInterval) throws Exception {
		super(name, characteristics, vmAllocationPolicy, storageList, schedulingInterval);
	}

	/* (non-Javadoc)
	 * 
	 * @see org.cloudbus.cloudsim.Datacenter#processEvent(org.cloudbus.cloudsim.core.SimEvent) */
	@SuppressWarnings("javadoc")
	@Override
	public void processEvent(SimEvent ev) {

		// Handle my new Tag Event.
		if (ev.getTag() == MyCloudSimTags.CLOUDLET_FILE_DONE) {
			processCloudletFilesDone(ev);
		} else {
			super.processEvent(ev);
		}
	}

	@Override
	protected void processCloudletSubmit(SimEvent ev, boolean ack) {
		/**
		 * When a cloudlet is received by the Datacenter, First (and once for all) it retrieves requiredFiles and adds
		 * dataFiles by interacting with the persistent storage available. Note: as a first step of CloudSimDisk
		 * development, data center are not handling Host and VMs processing. However, the extension have been
		 * implemented to easily integrate these functionalities in future development of CloudSimDisk.
		 */
		processCloudletFiles(ev, ack);
	}

	/**
	 * Process Cloudlet Required Files and Data Files.
	 * 
	 * @param ev
	 * @param ack
	 */
	public void processCloudletFiles(SimEvent ev, boolean ack) {

		// retrieve Cloudlet object
		Cloudlet cl = (Cloudlet) ev.getData();
		WriteToResultFile.setTempRowNum(cl.getCloudletId());

		// print out Cloudlet reception
		Log.printLine();
		Log.formatLine("\n%.6f: %s: Cloudlet # %d has been successfully received. ", CloudSim.clock(), getName(),
				cl.getCloudletId());

		// Handle dataFiles
		List<File> dataFiles = new ArrayList<File>();
		if (cl instanceof MyCloudlet) {
			MyCloudlet mycl = (MyCloudlet) cl;
			dataFiles = mycl.getDataFiles();
		}

		if (dataFiles != null) {
			Iterator<File> iter = dataFiles.iterator();

			while (iter.hasNext()) {
				File tempFile = iter.next();

				// Might need to move this in Datacenter.java
				int answerTag = this.addFile(tempFile);

				// test if tempFile has been added
				if (answerTag == DataCloudTags.FILE_ADD_SUCCESSFUL) {

					// find where the file has been added
					for (MyPowerHarddriveStorage storage : this.<MyPowerHarddriveStorage> getStorageList()) {

						// test if the storage id EQUAL the file ResourceID
						if (storage.getId() == tempFile.getResourceID()) {

							// update HDD variables
							processOperationWithStorage(storage, tempFile, cl, "added");
						}
					}
				} else if (answerTag == DataCloudTags.FILE_ADD_ERROR_EXIST_READ_ONLY) {
					Log.printLine(tempFile.getName() + ".addFile(): Warning - This file named <" + tempFile.getName()
							+ "> is already stored");
				}
			}
		}

		// Handle requiredFiles
		List<String> requiredFiles = new ArrayList<String>();
		if (cl instanceof MyCloudlet) {
			MyCloudlet mycl = (MyCloudlet) cl;
			requiredFiles = mycl.getRequiredFiles();
		}

		if (requiredFiles != null) {
			Iterator<String> iter = requiredFiles.iterator();

			while (iter.hasNext()) {
				String fileName = iter.next();

				for (MyPowerHarddriveStorage storage : this.<MyPowerHarddriveStorage> getStorageList()) {
					File tempFile = storage.getFile(fileName);

					if (tempFile != null) {

						// update HDD variables
						processOperationWithStorage(storage, tempFile, cl, "retrieved");
						break;
					}
				}
			}
		}
	}

	/**
	 * Process operation with the storage.
	 * 
	 * @param storage
	 * @param tempFile
	 * @param cl
	 * @param action
	 */
	protected void processOperationWithStorage(MyPowerHarddriveStorage storage, File tempFile, Cloudlet cl,
			String action) {

		// retrieve the transaction time for this operation
		double transTime = tempFile.getTransactionTime();

		// add the transaction time to the "total active time" of this disk
		storage.setInActiveDuration(storage.getInActiveDuration() + transTime);

		// update the storage state
		double waitingTime = 0.0;
		double eventDelay = 0.0;
		if (storage.isIdle()) {
			// handle Active End Time
			storage.setActiveEndAt(CloudSim.clock() + transTime);
			// handle Event for Operation completion
			eventDelay = transTime;
			// handle mode to Active (key = 1)
			storage.setMode(1);
		} else if (storage.isActive()) {
			// handle waiting time
			waitingTime = storage.getActiveEndAt() - CloudSim.clock();
			// handle Time at which the Active mode end (Time at which all operation will be done)
			storage.setActiveEndAt(storage.getActiveEndAt() + transTime);
			// handle the event delay to schedule the event confirmation COUDLET_FILE_DONE
			eventDelay = waitingTime + transTime;
			// Note: an event delay is not a specific Time, it is a duration.
			// Note: similarly, eventDelay = storage.getActiveEndAt() - CloudSim.clock();
		}

		// handle queue
		storage.setQueueLength(storage.getQueueLength() + 1);

		// Prepare data for the event
		Object[] data = new Object[6];
		data[0] = action; // the action of the operation
		data[1] = cl; // the cloudlet subject to the operation
		data[2] = tempFile; // the file subject to the operation
		data[3] = transTime; // the transaction time of the operation
		data[4] = storage; // the disk subject to the operation
		data[5] = waitingTime; // the waiting time for the operation

		// Schedule an Event confirming that the read/write operation has been done.
		send(this.getId(), eventDelay, MyCloudSimTags.CLOUDLET_FILE_DONE, data);
	}

	/**
	 * Processes a "Cloudlet Files Done" Event.
	 * 
	 * @param ev
	 *            the SimEvent
	 */
	protected void processCloudletFilesDone(SimEvent ev) {

		// Retrieves data from the ev
		Object[] data = (Object[]) ev.getData();
		String action = (String) data[0];
		Cloudlet cl = (Cloudlet) data[1];
		File tempFile = (File) data[2];
		double transTime = (double) data[3];
		MyHarddriveStorage storage = (MyHarddriveStorage) data[4];
		double waitingTime = (double) data[5];

		// store results/information
		WriteToResultFile.AddValueToSheetTab(waitingTime, cl.getCloudletId(), 2);
		WriteToResultFile.AddValueToSheetTab(transTime, cl.getCloudletId(), 3);
		WriteToResultFile.AddValueToSheetTab(CloudSim.clock(), cl.getCloudletId(), 7);
		WriteToResultFile.AddValueToSheetTab(tempFile.getName(), cl.getCloudletId(), 8);
		WriteToResultFile.AddValueToSheetTab(tempFile.getSize(), cl.getCloudletId(), 9);

		// Print out confirmation that Files have been handled
		Log.formatLine("\n%.6f: %s: Cloudlet # %d: <%s> %s on %s.", CloudSim.clock(), getName(), cl.getCloudletId(),
				tempFile.getName(), action, storage.getName());
		Log.formatLine("%10s Queue Waiting time of %9.6f Seconds(s).", "", waitingTime);
		Log.formatLine("%10s Transaction time   of %9.6f Seconde(s).", "", transTime);
		Log.printLine();

		// handle queue
		storage.setQueueLength(storage.getQueueLength() - 1);

		// Test if there is further operation on the disk
		if (storage.getActiveEndAt() <= CloudSim.clock()) {
			storage.setMode(0); // switch to idle mode
			storage.setActiveEndAt(0.0); // reset EndAt time
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see org.cloudbus.cloudsim.Datacenter#addFile(org.cloudbus.cloudsim.File) */
	@SuppressWarnings("javadoc")
	@Override
	public int addFile(File file) {

		/************ HDD POOL MANAGEMENT ******/
		/* Select the storage algorithm */
		int key = 2;
		/*************************************/

		// test if the file is NULL
		if (file == null) {
			return DataCloudTags.FILE_ADD_ERROR_EMPTY;
		}

		// test if the File is already stored
		if (contains(file.getName())) {
			return DataCloudTags.FILE_ADD_ERROR_EXIST_READ_ONLY;
		}

		// test if some persistent storage is available
		if (getStorageList().size() <= 0) {
			return DataCloudTags.FILE_ADD_ERROR_STORAGE_FULL;
		}

		// prepare algorithms
		int msg = DataCloudTags.FILE_ADD_ERROR_STORAGE_FULL;
		Storage tempStorage = null;

		switch (key) {
			case 1:
				/* ****************************************************************
				 * FIRST-FOUND (DEFAULT): scan the list of available HDD storage 
				 * and add the file on the first one which have enough free space 
				 * for the file. */
				for (int i = 0; i < getStorageList().size(); i++) {
					tempStorage = getStorageList().get(i);
					if (tempStorage.getFreeSpace() >= file.getSize()) {
						tempStorage.addFile(file);
						msg = DataCloudTags.FILE_ADD_SUCCESSFUL;
						break;
					}
				}
				// ****************************************************************
				break;

			case 2:
				/* ****************************************************************
				 * ROUND-ROBIN: adding the first file on the first disk, the second 
				 * file on the second disk, etc. When no more disk are in the pool, 
				 * restart from the first disk. */
				int numberOfTries = 0;

				// if we arrived to the end of the list, restart at the beginning.
				if (tempRR + 1 >= getStorageList().size()) {
					tempRR = -1;
				}

				do {
					// select the next HDD
					tempRR++;
					tempStorage = getStorageList().get(tempRR);

					// count the number of tries
					numberOfTries++;

					// while "no space on the selected HDD" or "all HDD tested"
				} while ((tempStorage.getFreeSpace() < file.getSize()) 
						|| (numberOfTries > getStorageList().size()));

				// if the algorithm found one HDD with enough space, add the file.
				if (numberOfTries <= getStorageList().size()) {
					tempStorage.addFile(file);
					msg = DataCloudTags.FILE_ADD_SUCCESSFUL;
				} else {
					msg = DataCloudTags.FILE_ADD_ERROR_STORAGE_FULL;
				}

				// ****************************************************************
				break;

			/*--------------------------------------------------------------------------------------
			 |SCALABILITY: write your own algorithm to manage request to the persistent storage
			 *--------------------------------------------------------------------------------------*/

			default:
				System.out.println("ERROR: no algorithm corresponding to this key.");
				break;
		}

		return msg;
	}
}
