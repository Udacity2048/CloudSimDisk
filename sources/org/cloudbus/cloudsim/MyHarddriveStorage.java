package org.cloudbus.cloudsim;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cloudbus.cloudsim.core.PrintFile;
import org.cloudbus.cloudsim.distributions.ContinuousDistribution;
import org.cloudbus.cloudsim.distributions.UniformDistr;
import org.cloudbus.cloudsim.storage.models.harddrives.StorageModelHdd;

/**
 * @author baplou
 * 
 */
public class MyHarddriveStorage extends HarddriveStorage {
	
	/**
	 * the specific ID of the Hard Drive (mandatory parameter)
	 */
	private int				Id;
	
	/**
	 * the specific reference of the Hard Drive
	 */
	private String			reference;
	
	/**
	 * the storage model of the Hard Drive
	 */
	private StorageModelHdd	storageModel;
	
	/**
	 * time in millisecond until which the disk is in Operation mode. If 0, the disk is in Idle mode.
	 */
	protected double		EndAt;
	
	/**
	 * mode of the HDD: 0 is idle, 1 is operating.
	 */
	protected int			mode;
	
	/**
	 * Queue length
	 */
	protected int			queueLength;
	
	/**
	 * Queue length history
	 */
	protected List<Integer>	queueLengthHistory	= new ArrayList<Integer>();
	
	/**
	 * Creates a Hard Drive storage from a specific model.
	 * 
	 * @param id
	 * 
	 * @param name
	 *            the name of the new Hard Drive storage
	 * @param storageModelHdd
	 *            the model of a specific hard drive
	 * @throws ParameterException
	 *             when the name and the capacity are not valid
	 */
	public MyHarddriveStorage(
			int id,
			String name,
			StorageModelHdd storageModelHdd) throws ParameterException {
		super(
				name,
				storageModelHdd.getCapacity());
		
		setId(id);
		setStorageModel(storageModelHdd);
		setEndAt(0.0);
		setMode(0); // idle mode by default
		setQueueLength(0);
	}
	
	// GETTER and SETTER
	
	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}
	
	/**
	 * @param reference
	 *            the reference to set
	 */
	public void setReference(
			String reference) {
		this.reference = reference;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return Id;
	}
	
	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(
			int id) {
		Id = id;
	}
	
	/**
	 * @return the storageModel
	 */
	public StorageModelHdd getStorageModel() {
		return storageModel;
	}
	
	/**
	 * @param storageModel
	 *            the storageModel to set
	 */
	public void setStorageModel(
			StorageModelHdd storageModel) {
		this.storageModel = storageModel;
		
		double min = 0;
		double max = 2 * storageModel.getAvgSeekTime();
		ContinuousDistribution gen = new UniformDistr(
				min,
				max);
		
		setReference(storageModel.getReference());
		setLatency(storageModel.getLatency());
		setAvgSeekTime(storageModel.getAvgSeekTime(),
				gen); // Random seek time
		setMaxTransferRate((int) storageModel.getMaxTransferRate());
	}
	
	/**
	 * @return the EndAt
	 */
	public double getEndAt() {
		return EndAt;
	}
	
	/**
	 * @param EndAt
	 * @param opeEndAt
	 *            the EndAt to set
	 */
	public void setEndAt(
			double EndAt) {
		this.EndAt = EndAt;
	}
	
	/**
	 * @return the mode
	 */
	public int getMode() {
		return mode;
	}
	
	/**
	 * @param mode
	 *            the mode to set
	 */
	public void setMode(
			int mode) {
		this.mode = mode;
		
		String result = "Unknown";
		if (mode == 0) {
			result = "Idle";
		} else if (mode == 1) {
			result = "Operating";
		}
		
		PrintFile.AddtoFile("ACTION>> Hard disk drive \"" + this.name + "\" is now in " + result + " mode.\n");
	}
	
	/**
	 * @return the queueLength
	 */
	public int getQueueLength() {
		return queueLength;
	}
	
	/**
	 * @param queueLength
	 *            the queueLength to set
	 */
	public void setQueueLength(
			int queueLength) {
		this.queueLength = queueLength;

		// Store history
		queueLengthHistory.add(queueLength);
		
		PrintFile.AddtoFile("OBSERVATION>> #QueueLenght <" + this.name + "> is now => " + queueLength);
	}
	
	/**
	 * @return the queueLengthHistory
	 */
	public List<Integer> getQueueLengthHistory() {
		return queueLengthHistory;
	}

	/**
	 * Is the HDD in Operating mode?
	 * 
	 * @return the answer
	 */
	public boolean isOperating() {
		if (mode == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Is the HDD in Idle mode?
	 * 
	 * @return the answer
	 */
	public boolean isIdle() {
		if (mode == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Gets the transfer time of a given file.
	 * 
	 * @param fileSize
	 *            the size of the transferred file
	 * @return the transfer time in seconds
	 */
	protected double getTransferTime(
			int fileSize) {
		double result = 0;
		if (fileSize > 0 && maxTransferRate != 0) {
			result = fileSize / maxTransferRate;
		}
		
		return result;
	}
	
	/**
	 * Gets the file with the specified name. The time taken (in seconds) for getting the file can also be found using
	 * {@link gridsim.datagrid.File#getTransactionTime()}.
	 * 
	 * @param fileName
	 *            the name of the needed file
	 * @return the file with the specified filename
	 */
	@Override
	public File getFile(
			String fileName) {
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
			
			// Log in the file
			String msg = String.format("OBSERVATION>> Reading \"%s\" on \"%s\" will take:" + "\n" + "%13s" + "%9.6f"
					+ " second(s) for SeekTime;" + "\n" + "%13s" + "%9.6f" + " second(s) for TransferTime;" + "\n" + "%13s"
					+ "%9.6f" + " second(s) for Latency;" + "\n" + "%13s" + "%9.6f" + " second(s) in TOTAL.\n",
					obj.getName(),
					this.getName(),
					"",
					seekTime,
					"",
					transferTime,
					"",
					latency,
					"",
					seekTime + transferTime + latency);
			PrintFile.AddtoFile(msg);
		}
		
		return obj;
	}
	
	/**
	 * Adds a file to the storage. First, the method checks if there is enough space on the storage, then it checks if
	 * the file with the same name is already taken to avoid duplicate filenames. <br>
	 * The time taken (in seconds) for adding the file can also be found using
	 * {@link gridsim.datagrid.File#getTransactionTime()}.
	 * 
	 * @param file
	 *            the file to be added
	 * @return the time taken (in seconds) for adding the specified file
	 */
	@Override
	public double addFile(
			File file) {
		double result = 0.0;
		// check if the file is valid or not
		if (!isFileValid(file,
				"addFile()")) {
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
			
			// Log in the file
			String msg = String.format("OBSERVATION>> Writting \"%s\" on \"%s\" will take:" + "\n" + "%13s" + "%9.6f"
					+ " second(s) for SeekTime;" + "\n" + "%13s" + "%9.6f" + " second(s) for TransferTime;" + "\n" + "%13s"
					+ "%9.6f" + " second(s) for Latency;" + "\n" + "%13s" + "%9.6f" + " second(s) in TOTAL.\n",
					file.getName(),
					this.getName(),
					"",
					seekTime,
					"",
					transferTime,
					"",
					latency,
					"",
					seekTime + transferTime + latency);
			PrintFile.AddtoFile(msg);
		}
		
		file.setTransactionTime(result);
		file.setResourceID(this.getId());
		
		return result;
	}
	
}
