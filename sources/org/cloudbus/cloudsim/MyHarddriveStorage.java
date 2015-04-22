package org.cloudbus.cloudsim;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cloudbus.cloudsim.distributions.ContinuousDistribution;
import org.cloudbus.cloudsim.distributions.MySeekTimeDistr;
import org.cloudbus.cloudsim.distributions.UniformDistr;
import org.cloudbus.cloudsim.storage.models.harddrives.StorageModelHdd;
import org.cloudbus.cloudsim.util.WriteToLogFile;
import org.cloudbus.cloudsim.util.WriteToResultFile;

/**
 * My HarddriveStorage is based on HarddriveStorage.java CloudSim Toolkit 1.0 written by Uros Cibej and Anthony Sulistio
 * but modifications are necessary for the new storage implementation. Mainly, it adds variable concerning the Waiting
 * Queue, the mode and the Active End Time with their corresponding GETTER and SETTER. Additionally, only one
 * constructor is available based on a HDD storage model in order to simplify things. Some HDD storage models are
 * provided and more can be easily implemented by the user.s
 * 
 * @author Baptiste Louis
 * 
 */
public class MyHarddriveStorage implements Storage {

	/** a list storing the names of all the files on the harddrive. */
	protected List<String>				nameList;

	/** a list storing all the files stored on the harddrive. */
	protected List<File>				fileList;

	/** the name of the harddrive. */
	protected final String				name;

	/** a generator required to randomize the seek time. */
	protected ContinuousDistribution	genSeekTime;

	/** a generator required to randomize the rotation latency. */
	protected ContinuousDistribution	genRotLatency;

	/** the space used by of files on the harddrive. */
	protected double					usedSpace;

	/** the total capacity of the harddrive in MB. */
	protected double					capacity;

	/** the maximum internal data transfer rate in MB/sec. */
	protected final double				maxInternalDataTransferRate;

	/** the average rotation latency of the harddrive in seconds. */
	protected final double				avgRotLatency;

	/** the average seek time in seconds. */
	protected double					avgSeekTime;

	/** unique ID of the Hard Drive */
	private final int					Id;

	/** unique reference of the Hard Drive */
	private final String				reference;

	/** the storage model of the Hard Drive */
	private final StorageModelHdd		storageModel;

	/** time (ms) until when the disk is in Active mode */
	protected double					activeEndAt;

	/** mode of the HDD. If 0, the disk is idle mode. If 1, the disk is in Active mode. */
	protected int						mode;

	/** the length of the requests waiting queue */
	protected int						queueLength;

	/** the history of the requests waiting queue lengths */
	protected List<Integer>				queueLengthHistory	= new ArrayList<Integer>();

	/**
	 * The constructor.
	 * 
	 * @param id
	 *            unique id for this drive
	 * @param name
	 *            the name of the new Hard Drive storage
	 * @param storageModelHdd
	 *            the model of a specific hard drive
	 * @throws ParameterException
	 *             when the name and the capacity are not valid
	 */
	public MyHarddriveStorage(int id, String name, StorageModelHdd storageModelHdd) throws ParameterException {

		// store id & name
		this.Id = id;
		this.name = name;

		// initializes final variables
		this.storageModel = storageModelHdd;
		this.capacity = storageModelHdd.getCapacity();
		this.reference = storageModelHdd.getModelNumber();
		this.maxInternalDataTransferRate = storageModelHdd.getMaxInternalDataTransferRate();
		this.avgRotLatency = storageModelHdd.getAvgRotationLatency();

		// initializes global variables
		this.activeEndAt = 0.0;
		this.fileList = new ArrayList<File>();
		this.nameList = new ArrayList<String>();
		this.genSeekTime = null;
		this.usedSpace = 0;
		setMode(0);
		setQueueLength(0);

		// randomize seekTime & rotation latency
		setSeekTime(storageModelHdd.getAvgSeekTime());
		setRotLatency(storageModelHdd.getAvgRotationLatency());
	}

	// GETTER and SETTER

	/**
	 * Sets the seek Time.
	 * 
	 * @param avgSeekTime
	 *            the average seek Time to set
	 * @return <tt>true</tt> if the setting succeeded, <tt>false</tt> otherwise
	 */
	public boolean setSeekTime(double avgSeekTime) {

		this.avgSeekTime = avgSeekTime;

		// check that avgSeekTime > 0
		if (avgSeekTime <= 0.0) {
			return false;
		}

		// randomize SeekTime
		ContinuousDistribution generator = new UniformDistr(0.0002, 3 * avgSeekTime);

		// SCALABILITY: define your own Distribution algorithm for the seekTime.
		//
		// ContinuousDistribution generator = new YOUR_PERSO_DISTR(...);

		// store variable
		this.genSeekTime = generator;

		return true;
	}

	/**
	 * Get the seek time for a file with the defined size. Given a file size in MB, this method returns a seek time for
	 * the file in seconds.
	 * 
	 * @return the seek time in seconds
	 */
	protected double getSeekTime() {
		double result = 0;

		// Seek Time will be between a "Track-to-Track seek time" of 0.2ms and "3 x avgSeekTime".
		if (genSeekTime != null) {
			result += genSeekTime.sample();
		}

		// SCALABILITY: right your algorithm to GET a seekTime. You can add parameter to the method like the fileSize.

		return result;
	}

	/**
	 * Sets the rotation latency.
	 * 
	 * @param avgRotationLatency
	 *            the average rotation latency to set
	 * @return <tt>true</tt> if the setting succeeded, <tt>false</tt> otherwise
	 */
	public boolean setRotLatency(double avgRotationLatency) {

		// check that avgRotationLatency > 0
		if (avgRotationLatency <= 0.0) {
			return false;
		}

		// randomize rotation latency
		ContinuousDistribution generator = new UniformDistr(0, 2 * avgRotationLatency);

		// SCALABILITY: define your own Distribution algorithm for the rotation latency.
		//
		// ContinuousDistribution generator = new YOUR_PERSO_DISTR(...);

		// store variable
		this.genRotLatency = generator;
		
		return true;
	}

	/**
	 * Get a sample of rotation latency. This method returns a rotation latency between 0 and 2 x the average rotation
	 * latency.
	 * 
	 * @return the rotation latency in seconds
	 */
	protected double getRotLatency() {
		double result = 0;

		if (genRotLatency != null) {
			result += genRotLatency.sample();
		}

		// SCALABILITY: right your algorithm to GET the rotation latency. You can add parameter to the method.

		return result;
	}

	/**
	 * Gets the transfer time of a given file. Note than "Maximum Internal Data Transfer Rate" is taken into account
	 * which is rarely the case in real world but manufacturers usually provide only this mesurement.
	 * 
	 * @param fileSize
	 *            the size of the transferred file
	 * @return the transfer time in seconds
	 */
	protected double getTransferTime(int fileSize) {
		double result = 0;
		if (fileSize > 0 && maxInternalDataTransferRate != 0) {
			result = fileSize / maxInternalDataTransferRate;
		}

		return result;
	}

	/**
	 * Sets the mode.
	 * 
	 * @param mode
	 *            the mode to set
	 */
	public void setMode(int mode) {
		this.mode = mode;

		// log the observation.
		String result = "Unknown";

		if (mode == 0) {
			result = "Idle";
		} else if (mode == 1) {
			result = "Active";
		}

		WriteToLogFile
				.AddtoFile("OBSERVATION>> Hard disk drive \"" + this.name + "\" is now in " + result + " mode.\n");
	}

	/**
	 * Sets the length of the waiting queue.
	 * 
	 * @param queueLength
	 *            the queueLength to set
	 */
	public void setQueueLength(int queueLength) {
		this.queueLength = queueLength;

		// store history
		queueLengthHistory.add(queueLength);

		// log the observation
		WriteToLogFile.AddtoFile("OBSERVATION>> #QueueLenght <" + this.name + "> is now => " + queueLength);
	}

	/**
	 * Gets the total capacity of the storage in MB.
	 * 
	 * @return the capacity of the storage in MB
	 */
	@Override
	public double getCapacity() {
		return capacity;
	}

	/**
	 * Gets the space used by the stored files in MB.
	 * 
	 * @return the space used of the stored files in MB
	 */
	@Override
	public double getUsedSpace() {
		return usedSpace;
	}

	/**
	 * Gets the name of the storage.
	 * 
	 * @return the name of this storage
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Gets the average rotation latency of this harddrive.
	 * 
	 * @return the average rotation latency in seconds
	 */
	public double getAvgRotLatency() {
		return avgRotLatency;
	}

	/**
	 * Gets the maximum internal data transfer rate of the storage.
	 * 
	 * @return the maximum internal transfer rate in MB/sec
	 */
	public double getMaxInternalDataTransferRate() {
		return maxInternalDataTransferRate;
	}

	/**
	 * Gets the average seek time of the harddrive in seconds.
	 * 
	 * @return the average seek time in seconds
	 */
	public double getAvgSeekTime() {
		return avgSeekTime;
	}

	/**
	 * Gets the list of file names located on this storage.
	 * 
	 * @return a List of file names
	 */
	@Override
	public List<String> getFileNameList() {
		return nameList;
	}

	/**
	 * Gets the storage model.
	 * 
	 * @return the storageModel
	 */
	public StorageModelHdd getStorageModel() {
		return storageModel;
	}

	/**
	 * Gets the mode.
	 * 
	 * @return the mode
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * Gets the length of the waiting queue.
	 * 
	 * @return the queueLength
	 */
	public int getQueueLength() {
		return queueLength;
	}

	/**
	 * Gets the reference.
	 * 
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public int getId() {
		return Id;
	}

	/**
	 * Get the "Active End At" time.
	 * 
	 * @return the activeEndAt
	 */
	public double getActiveEndAt() {
		return activeEndAt;
	}

	/**
	 * Sets the "Active End At" time.
	 * 
	 * @param activeEndAt
	 *            the time to set
	 */
	public void setActiveEndAt(double activeEndAt) {
		this.activeEndAt = activeEndAt;
	}

	/**
	 * Gets the waiting queue history.
	 * 
	 * @return the queueLengthHistory
	 */
	public List<Integer> getQueueLengthHistory() {
		return queueLengthHistory;
	}

	// CHECK STATE METHODS

	/**
	 * Is the HDD in Active mode?
	 * 
	 * @return the answer
	 */
	public boolean isActive() {
		if (mode == 1) return true;
		return false;
	}

	/**
	 * Is the HDD in Idle mode?
	 * 
	 * @return the answer
	 */
	public boolean isIdle() {
		if (mode == 0) return true;
		return false;
	}

	// SPACE METHODS

	/**
	 * Gets the free space on this storage in MB.
	 * 
	 * @return the free space in MB
	 */
	@Override
	public double getFreeSpace() {
		return capacity - usedSpace;
	}

	/**
	 * Checks if the storage is full or not.
	 * 
	 * @return <tt>true</tt> if the storage is full, <tt>false</tt> otherwise
	 */
	@Override
	public boolean isFull() {
		if (Math.abs(usedSpace - capacity) < .0000001) { // currentSize == capacity
			return true;
		}
		return false;
	}

	/**
	 * Gets the number of files stored on this storage.
	 * 
	 * @return the number of stored files
	 */
	@Override
	public int getNumStoredFile() {
		return fileList.size();
	}

	/**
	 * Makes a reservation of the space on the storage to store a file.
	 * 
	 * @param fileSize
	 *            the size to be reserved in MB
	 * @return <tt>true</tt> if reservation succeeded, <tt>false</tt> otherwise
	 */
	@Override
	public boolean reserveSpace(int fileSize) {
		if (fileSize <= 0) {
			return false;
		}

		if (usedSpace + fileSize >= capacity) {
			return false;
		}

		usedSpace += fileSize;
		return true;
	}

	/**
	 * Adds a file for which the space has already been reserved. The time taken (in seconds) for adding the file can
	 * also be found using {@link gridsim.datagrid.File#getTransactionTime()}.
	 * 
	 * @param file
	 *            the file to be added
	 * @return the time (in seconds) required to add the file
	 */
	@Override
	public double addReservedFile(File file) {
		if (file == null) {
			return 0;
		}

		usedSpace -= file.getSize();
		double result = addFile(file);

		// if add file fails, then set the current size back to its old value
		if (result == 0.0) {
			usedSpace += file.getSize();
		}

		return result;
	}

	/**
	 * Checks whether there is enough space on the storage for a certain file.
	 * 
	 * @param fileSize
	 *            a FileAttribute object to compare to
	 * @return <tt>true</tt> if enough space available, <tt>false</tt> otherwise
	 */
	@Override
	public boolean hasPotentialAvailableSpace(int fileSize) {
		if (fileSize <= 0) {
			return false;
		}

		// check if enough space left
		if (getFreeSpace() > fileSize) {
			return true;
		}

		Iterator<File> it = fileList.iterator();
		File file = null;
		int deletedFileSize = 0;

		// if not enough space, then if want to clear/delete some files
		// then check whether it still have space or not
		boolean result = false;
		while (it.hasNext()) {
			file = it.next();
			if (!file.isReadOnly()) {
				deletedFileSize += file.getSize();
			}

			if (deletedFileSize > fileSize) {
				result = true;
				break;
			}
		}

		return result;
	}

	// FILE METHODS

	/**
	 * Check if the file is valid or not. This method checks whether the given file or the file name of the file is
	 * valid. The method name parameter is used for debugging purposes, to output in which method an error has occured.
	 * 
	 * @param file
	 *            the file to be checked for validity
	 * @param methodName
	 *            the name of the method in which we check for validity of the file
	 * @return <tt>true</tt> if the file is valid, <tt>false</tt> otherwise
	 */
	protected boolean isFileValid(File file, String methodName) { // Modified privacy for Baptiste Louis test

		if (file == null) {
			Log.printLine(name + "." + methodName + ": Warning - the given file is null.");
			return false;
		}

		String fileName = file.getName();
		if (fileName == null || fileName.length() == 0) {
			Log.printLine(name + "." + methodName + ": Warning - invalid file name.");
			return false;
		}

		return true;
	}

	/**
	 * Removes a file from the storage. The time taken (in seconds) for deleting the file can also be found using
	 * {@link gridsim.datagrid.File#getTransactionTime()}.
	 * 
	 * @param fileName
	 *            the name of the file to be removed
	 * @return the deleted file
	 */
	@Override
	public File deleteFile(String fileName) {
		if (fileName == null || fileName.length() == 0) {
			return null;
		}

		Iterator<File> it = fileList.iterator();
		File file = null;
		while (it.hasNext()) {
			file = it.next();
			String name = file.getName();

			// if a file is found then delete
			if (fileName.equals(name)) {
				double result = deleteFile(file);
				file.setTransactionTime(result);
				break;
			} else {
				file = null;
			}
		}
		return file;
	}

	/**
	 * Removes a file from the storage. The time taken (in seconds) for deleting the file can also be found using
	 * {@link gridsim.datagrid.File#getTransactionTime()}.
	 * 
	 * @param fileName
	 *            the name of the file to be removed
	 * @param file
	 *            the file which is removed from the storage is returned through this parameter
	 * @return the time taken (in seconds) for deleting the specified file
	 */
	@Override
	public double deleteFile(String fileName, File file) {
		return deleteFile(file);
	}

	/**
	 * Removes a file from the storage. The time taken (in seconds) for deleting the file can also be found using
	 * {@link gridsim.datagrid.File#getTransactionTime()}.
	 * 
	 * @param file
	 *            the file which is removed from the storage is returned through this parameter
	 * @return the time taken (in seconds) for deleting the specified file
	 */
	@Override
	public double deleteFile(File file) {
		double result = 0.0;
		// check if the file is valid or not
		if (!isFileValid(file, "deleteFile()")) {
			return result;
		}
		double seekTime = getSeekTime();
		double transferTime = getTransferTime(file.getSize());

		// check if the file is in the storage
		if (contains(file)) {
			fileList.remove(file);            // remove the file HD
			nameList.remove(file.getName());  // remove the name from name list
			usedSpace -= file.getSize();    // decrement the current HD space
			result = seekTime + transferTime;  // total time
			file.setTransactionTime(result);
		}
		return result;
	}

	/**
	 * Checks whether a certain file is on the storage or not.
	 * 
	 * @param fileName
	 *            the name of the file we are looking for
	 * @return <tt>true</tt> if the file is in the storage, <tt>false</tt> otherwise
	 */
	@Override
	public boolean contains(String fileName) {
		boolean result = false;
		if (fileName == null || fileName.length() == 0) {
			Log.printLine(name + ".contains(): Warning - invalid file name");
			return result;
		}
		// check each file in the list
		Iterator<String> it = nameList.iterator();
		while (it.hasNext()) {
			String name = it.next();
			if (name.equals(fileName)) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * Checks whether a certain file is on the storage or not.
	 * 
	 * @param file
	 *            the file we are looking for
	 * @return <tt>true</tt> if the file is in the storage, <tt>false</tt> otherwise
	 */
	@Override
	public boolean contains(File file) {
		boolean result = false;
		if (!isFileValid(file, "contains()")) {
			return result;
		}

		result = contains(file.getName());
		return result;
	}

	/**
	 * Renames a file on the storage. The time taken (in seconds) for renaming the file can also be found using
	 * {@link gridsim.datagrid.File#getTransactionTime()}.
	 * 
	 * @param file
	 *            the file we would like to rename
	 * @param newName
	 *            the new name of the file
	 * @return <tt>true</tt> if the renaming succeeded, <tt>false</tt> otherwise
	 */
	@Override
	public boolean renameFile(File file, String newName) {
		// check whether the new filename is conflicting with existing ones
		// or not
		boolean result = false;
		if (contains(newName)) {
			return result;
		}

		// replace the file name in the file (physical) list
		File obj = getFile(file.getName());
		if (obj == null) {
			return result;
		} else {
			obj.setName(newName);
		}

		// replace the file name in the name list
		Iterator<String> it = nameList.iterator();
		while (it.hasNext()) {
			String name = it.next();
			if (name.equals(file.getName())) {
				file.setTransactionTime(0);
				nameList.remove(name);
				nameList.add(newName);
				result = true;
				break;
			}
		}

		return result;
	}

	/**
	 * Adds a set of files to the storage. Runs through the list of files and save all of them. The time taken (in
	 * seconds) for adding each file can also be found using {@link gridsim.datagrid.File#getTransactionTime()}.
	 * 
	 * @param list
	 *            the files to be added
	 * @return the time taken (in seconds) for adding the specified files
	 */
	@Override
	public double addFile(List<File> list) {
		double result = 0.0;
		if (list == null || list.isEmpty()) {
			Log.printLine(name + ".addFile(): Warning - list is empty.");
			return result;
		}

		Iterator<File> it = list.iterator();
		File file = null;
		while (it.hasNext()) {
			file = it.next();
			result += addFile(file);    // add each file in the list
		}
		return result;
	}

	/**
	 * Adds a file to the storage. First, the method checks if there is enough space on the storage, then it checks if
	 * the file with the same name is already taken to avoid duplicate filenames.
	 * 
	 * @param file
	 *            the file to be added
	 * @return the time taken (in seconds) for adding the specified file
	 */
	@Override
	public double addFile(File file) {
		double result = 0.0;
		// check if the file is valid or not
		if (!isFileValid(file, "addFile()")) {
			return result;
		}

		// check the capacity
		if (file.getSize() + usedSpace > capacity) {
			Log.printLine(name + ".addFile(): Warning - not enough space to store " + file.getName());
			return result;
		}

		// check if the same file name is already taken
		if (!contains(file.getName())) {
			
			// calculate the transaction time
			double seekTime = getSeekTime();
			double rotlatency = getRotLatency();
			double transferTime = getTransferTime(file.getSize());
			result = seekTime + rotlatency + transferTime;
			
			// update global variables
			fileList.add(file); // add the file into the HD
			nameList.add(file.getName()); // add the name to the name list
			usedSpace += file.getSize(); // increment the current HD size
			

			// store results/information
			WriteToResultFile.AddValueToSheetTabSameRow(seekTime, 4);
			WriteToResultFile.AddValueToSheetTabSameRow(rotlatency, 5);
			WriteToResultFile.AddValueToSheetTabSameRow(transferTime, 6);

			// Log the observation
			String msg = String.format("OBSERVATION>> Writting \"%s\" on \"%s\" will take:" + "\n" + "%13s" + "%9.6f"
					+ " second(s) for SeekTime;" + "\n" + "%13s" + "%9.6f" + " second(s) for TransferTime;" + "\n"
					+ "%13s" + "%9.6f" + " second(s) for rotation Latency;" + "\n" + "%13s" + "%9.6f"
					+ " second(s) in TOTAL.\n", file.getName(), this.getName(), "", seekTime, "", transferTime, "",
					rotlatency, "", seekTime + transferTime + rotlatency);
			WriteToLogFile.AddtoFile(msg);
		}

		// store transactionTime and HDDid as attributes on this file.
		file.setTransactionTime(result);
		file.setResourceID(this.getId());

		return result;
	}

	/**
	 * Gets the file with the specified name.
	 * 
	 * @param fileName
	 *            the name of the needed file
	 * @return the file with the specified filename
	 */
	@Override
	public File getFile(String fileName) {
		// check first whether file name is valid or not
		File obj = null;
		if (fileName == null || fileName.length() == 0) {
			Log.printLine(name + ".getFile(): Warning - invalid " + "file name.");
			return obj;
		}

		Iterator<File> it = fileList.iterator();
		int index = 0;
		boolean found = false;
		File tempFile = null;

		// find the file in the disk
		while (it.hasNext()) {
			tempFile = it.next();
			if (tempFile.getName().equals(fileName)) {
				found = true;
				obj = tempFile;
				break;
			}

			index++;
		}

		// if the file is found, then determine the time taken to get it
		if (found) {
			
			// calculate the transaction time
			obj = fileList.get(index);
			double seekTime = getSeekTime();
			double rotlatency = getRotLatency();
			double transferTime = getTransferTime(obj.getSize());

			// total time for this operation
			obj.setTransactionTime(seekTime + rotlatency + transferTime);

			// store results/information
			WriteToResultFile.AddValueToSheetTabSameRow(seekTime, 4);
			WriteToResultFile.AddValueToSheetTabSameRow(rotlatency, 5);
			WriteToResultFile.AddValueToSheetTabSameRow(transferTime, 6);

			// log the observation
			String msg = String.format("OBSERVATION>> Reading \"%s\" on \"%s\" will take:" + "\n" + "%13s" + "%9.6f"
					+ " second(s) for SeekTime;" + "\n" + "%13s" + "%9.6f" + " second(s) for TransferTime;" + "\n"
					+ "%13s" + "%9.6f" + " second(s) for rotation Latency;" + "\n" + "%13s" + "%9.6f"
					+ " second(s) in TOTAL.\n", obj.getName(), this.getName(), "", seekTime, "", transferTime, "",
					rotlatency, "", seekTime + transferTime + rotlatency);
			WriteToLogFile.AddtoFile(msg);
		}

		return obj;
	}
}
