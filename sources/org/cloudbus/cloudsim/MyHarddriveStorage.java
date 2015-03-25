package org.cloudbus.cloudsim;

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
		super(name, storageModelHdd.getCapacity() * 1000); // conversion from GBytes to MBytes.
		
		setId(id);
		setStorageModel(storageModelHdd);
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
	public void setReference(String reference) {
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
	public void setId(int id) {
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
	public void setStorageModel(StorageModelHdd storageModel) {
		this.storageModel = storageModel;
		
		double min = 0;
		double max = 2 * storageModel.getAvgSeekTime();
		ContinuousDistribution gen = new UniformDistr(min, max);
		
		setReference(storageModel.getReference());
		setLatency(storageModel.getLatency());
		setAvgSeekTime(storageModel.getAvgSeekTime(), gen); // Random seek time
		setMaxTransferRate((int) storageModel.getMaxTransferRate());
	}
	
	/**
	 * Adds a file to the storage. First, the method checks if there is enough space on the storage,
	 * then it checks if the file with the same name is already taken to avoid duplicate filenames. <br>
	 * The time taken (in seconds) for adding the file can also be found using
	 * {@link gridsim.datagrid.File#getTransactionTime()}.
	 * 
	 * @param file the file to be added
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

		// check if the same file name is alredy taken
		if (!contains(file.getName())) {
			double seekTime = getSeekTime(file.getSize());
			double transferTime = getTransferTime(file.getSize());
			double latency = getLatency();	// added by Baptiste Louis

			fileList.add(file);               // add the file into the HD
			nameList.add(file.getName());     // add the name to the name list
			currentSize += file.getSize();    // increment the current HD size
			result = seekTime + transferTime + latency;  // Latency added by Baptiste Louis
		}
		
		file.setTransactionTime(result);
		file.setResourceID(this.getId());
		
		return result;
	}
	
}
