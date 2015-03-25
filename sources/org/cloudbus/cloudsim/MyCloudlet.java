package org.cloudbus.cloudsim;

import java.util.List;

/**
 * @author baplou
 *
 */
public class MyCloudlet extends Cloudlet {
	
	// Data cloudlet
	/** The required files. */
	private List<String>	requiredFiles	= null; // list of required filenames
													
	/** The data files. */
	private List<File>		dataFiles		= null; // list of "to storage" files
													
	/**
	 * @param cloudletId
	 * @param cloudletLength
	 * @param pesNumber
	 * @param cloudletFileSize
	 * @param cloudletOutputSize
	 * @param utilizationModelCpu
	 * @param utilizationModelRam
	 * @param utilizationModelBw
	 * @param requiredFiles
	 * @param dataFiles
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
		super(cloudletId, cloudletLength, pesNumber, cloudletFileSize, cloudletOutputSize,
				utilizationModelCpu, utilizationModelRam, utilizationModelBw, false);
		
		setRequiredFiles(requiredFiles);
		setDataFiles(dataFiles);
	}
	
	// Data cloudlet methods
	
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
	protected void setRequiredFiles(final List<String> requiredFiles) {
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
	public void setDataFiles(List<File> dataFiles) {
		this.dataFiles = dataFiles;
	}
	
}
