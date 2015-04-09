package org.cloudbus.cloudsim;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cloudbus.cloudsim.core.PrintFile;
import org.cloudbus.cloudsim.distributions.ContinuousDistribution;
import org.cloudbus.cloudsim.distributions.UniformDistr;
import org.cloudbus.cloudsim.storage.models.harddrives.StorageModelHdd;

/**
 * My HarddriveStorage extends HarddriveStorage.java by overwriting some of its methods. The modifications are not
 * important but necessary for the new storage implementation. Mainly, it adds variable concerning the Waiting Queue,
 * the mode and the Active End Time with their corresponding GETTER and SETTER. Additionally, only one constructor is
 * available based on a HDD storage model in order to simplify things. Some models are provided and more can be easily
 * implemented by the user.
 * 
 * @author Baptiste Louis
 * 
 */
public class MyHarddriveStorage extends HarddriveStorage {

	/** unique ID of the Hard Drive */
	private int				Id;

	/** unique reference of the Hard Drive */
	private String			reference;

	/** the storage model of the Hard Drive */
	private StorageModelHdd	storageModel;

	/** time (ms) until when the disk is in Active mode */
	protected double		activeEndAt;

	/** mode of the HDD. If 0, the disk is idle mode. If 1, the disk is in Active mode. */
	protected int			mode;

	/** the length of the requests waiting queue */
	protected int			queueLength;

	/** the history of the requests waiting queue lengths */
	protected List<Integer>	queueLengthHistory	= new ArrayList<Integer>();

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
		super(name, storageModelHdd.getCapacity());

		// initializes global variables
		setId(id);
		setStorageModel(storageModelHdd);
		setActiveEndAt(0.0);
		setMode(0);
		setQueueLength(0);
	}

	// GETTER and SETTER

	/**
	 * Sets the storage Model.
	 * 
	 * @param storageModel
	 *            the storageModel to set
	 */
	public void setStorageModel(StorageModelHdd storageModel) {
		this.storageModel = storageModel;

		// randomize SeekTime
		ContinuousDistribution gen = new UniformDistr(0, 2 * storageModel.getAvgSeekTime());

		// SCALABILITY: define your own Distribution algorithm for the seekTime.
		//
		// ContinuousDistribution gen = new YOUR_PERSO_DISTR(...);

		// set HDD characteristics
		setReference(storageModel.getReference());
		setLatency(storageModel.getLatency());
		setAvgSeekTime(storageModel.getAvgSeekTime(), gen);
		setMaxTransferRate((int) storageModel.getMaxTransferRate());

		// SCALABILITY: define your own Distribution algorithm for the seekTime.
		//
		// ContinuousDistribution generator = new YOUR_PERSO_DISTR(...);
	}

	/* (non-Javadoc)
	 * 
	 * @see org.cloudbus.cloudsim.HarddriveStorage#setAvgSeekTime(double,
	 * org.cloudbus.cloudsim.distributions.ContinuousDistribution) */
	@SuppressWarnings("javadoc")
	@Override
	public boolean setAvgSeekTime(double seekTime, ContinuousDistribution generator) {
		if (seekTime <= 0.0) {
			return false;
		}

		avgSeekTime = seekTime;
		gen = generator;
		return true;
	}

	@Override
	protected double getSeekTime(int fileSize) {
		double result = 0;

		if (gen != null) {
			result += gen.sample();
		}

		if (fileSize > 0 && capacity != 0) {
			result += (fileSize / capacity);
		}

		// SCALABILITY: right your algorithm to GET a seekTime.

		return result;
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

		PrintFile.AddtoFile("OBSERVATION>> Hard disk drive \"" + this.name + "\" is now in " + result + " mode.\n");
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
		PrintFile.AddtoFile("OBSERVATION>> #QueueLenght <" + this.name + "> is now => " + queueLength);
	}

	/**
	 * Gets the transfer time of a given file.
	 * 
	 * @param fileSize
	 *            the size of the transferred file
	 * @return the transfer time in seconds
	 */
	protected double getTransferTime(int fileSize) {
		double result = 0;
		if (fileSize > 0 && maxTransferRate != 0) {
			result = fileSize / maxTransferRate;
		}

		return result;
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
	 * Sets the reference.
	 * 
	 * @param reference
	 *            the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
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
	 * Sets the id.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		Id = id;
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
		int size = 0;
		int index = 0;
		boolean found = false;
		File tempFile = null;

		// find the file in the disk
		while (it.hasNext()) {
			tempFile = it.next();
			size += tempFile.getSize();
			if (tempFile.getName().equals(fileName)) {
				found = true;
				obj = tempFile;
				break;
			}

			index++;
		}

		// if the file is found, then determine the time taken to get it
		if (found) {
			obj = fileList.get(index);
			double seekTime = getSeekTime(size);
			double transferTime = getTransferTime(obj.getSize());
			double latency = getLatency();

			// total time for this operation
			obj.setTransactionTime(seekTime + transferTime + latency);

			// log the observation
			String msg = String.format(
					"OBSERVATION>> Reading \"%s\" on \"%s\" will take:" + "\n" + "%13s" + "%9.6f"
							+ " second(s) for SeekTime;" + "\n" + "%13s" + "%9.6f" + " second(s) for TransferTime;"
							+ "\n" + "%13s" + "%9.6f" + " second(s) for Latency;" + "\n" + "%13s" + "%9.6f"
							+ " second(s) in TOTAL.\n", obj.getName(), this.getName(), "", seekTime, "", transferTime,
					"", latency, "", seekTime + transferTime + latency);
			PrintFile.AddtoFile(msg);
		}

		return obj;
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
		if (file.getSize() + currentSize > capacity) {
			Log.printLine(name + ".addFile(): Warning - not enough space" + " to store " + file.getName());
			return result;
		}

		// check if the same file name is already taken
		if (!contains(file.getName())) {
			double seekTime = getSeekTime(file.getSize());
			double transferTime = getTransferTime(file.getSize());
			double latency = getLatency(); // added by Baptiste Louis

			fileList.add(file); // add the file into the HD
			nameList.add(file.getName()); // add the name to the name list
			currentSize += file.getSize(); // increment the current HD size
			result = seekTime + transferTime + latency; // Latency added by Baptiste Louis

			// Log the observation
			String msg = String.format(
					"OBSERVATION>> Writting \"%s\" on \"%s\" will take:" + "\n" + "%13s" + "%9.6f"
							+ " second(s) for SeekTime;" + "\n" + "%13s" + "%9.6f" + " second(s) for TransferTime;"
							+ "\n" + "%13s" + "%9.6f" + " second(s) for Latency;" + "\n" + "%13s" + "%9.6f"
							+ " second(s) in TOTAL.\n", file.getName(), this.getName(), "", seekTime, "", transferTime,
					"", latency, "", seekTime + transferTime + latency);
			PrintFile.AddtoFile(msg);
		}

		file.setTransactionTime(result);
		file.setResourceID(this.getId());

		return result;
	}

}
