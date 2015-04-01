package org.cloudbus.cloudsim;

import java.util.List;

/**
 * My Cloudlet extends Cloudlet.java by adding RequiredFiles and DataFiles parameters. RequiredFiles is a list of
 * fileNames that is required by the Cloudlet. DataFiles is a list of Files that need to be added on the datacenter.
 * 
 * @author Baptiste Louis
 * 
 */
public class MyCloudlet extends Cloudlet {
	
	/** The required files. */
	private List<String>	requiredFiles	= null;
	
	/** The data files. */
	private List<File>		dataFiles		= null;
	
	/**
	 * The constructor.
	 * 
	 * @param cloudletId
	 *            id
	 * @param cloudletLength
	 *            length in Million Instruction(s)
	 * @param pesNumber
	 *            number of Processing Element(s)
	 * @param cloudletFileSize
	 *            size in MB
	 * @param cloudletOutputSize
	 *            output size in MB
	 * @param utilizationModelCpu
	 *            CPU model
	 * @param utilizationModelRam
	 *            RAM model
	 * @param utilizationModelBw
	 *            BW model
	 * @param requiredFiles
	 *            list of required filenames
	 * @param dataFiles
	 *            list of "to storage" files
	 */
	public MyCloudlet(
			int cloudletId,
			long cloudletLength,
			int pesNumber,
			long cloudletFileSize,
			long cloudletOutputSize,
			UtilizationModel utilizationModelCpu,
			UtilizationModel utilizationModelRam,
			UtilizationModel utilizationModelBw,
			List<String> requiredFiles,
			List<File> dataFiles) {
		super(
				cloudletId,
				cloudletLength,
				pesNumber,
				cloudletFileSize,
				cloudletOutputSize,
				utilizationModelCpu,
				utilizationModelRam,
				utilizationModelBw,
				false);
		
		setRequiredFiles(requiredFiles);
		setDataFiles(dataFiles);
	}
	
	// GETTER AND SETTER
	
	/**
	 * Gets the required files.
	 * 
	 * @return the required files
	 */
	public List<String> getRequiredFiles() {
		return requiredFiles;
	}
	
	/**
	 * Sets the required files.
	 * 
	 * @param requiredFiles
	 *            the new required files
	 */
	protected void setRequiredFiles(
			final List<String> requiredFiles) {
		this.requiredFiles = requiredFiles;
	}
	
	/**
	 * @return the dataFiles
	 */
	public List<File> getDataFiles() {
		return dataFiles;
	}
	
	/**
	 * @param dataFiles
	 *            the dataFiles to set
	 */
	public void setDataFiles(
			List<File> dataFiles) {
		this.dataFiles = dataFiles;
	}
	
}
