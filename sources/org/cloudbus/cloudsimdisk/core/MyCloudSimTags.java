package org.cloudbus.cloudsimdisk.core;

import org.cloudbus.cloudsim.core.CloudSimTags;

/**
 * My CloudSim Tags is used to declare ONE new Tags called CLOUDLET_FILES_DONE for the storage implementation.
 * 
 * @author Baptiste Louis
 */
public class MyCloudSimTags extends CloudSimTags {

	/** Starting constant value for cloud-related tags **/
	private static final int	BASE				= 0;

	/** Confirmation that Cloudlet Files have been handle **/
	public static final int		CLOUDLET_FILE_DONE	= BASE + 49;

	/**
	 * Convert tagValue to TagText.
	 * 
	 * @param tagValue
	 * @return tagText
	 */
	public static String TagText(int tagValue) {
		switch (tagValue) {
			case 49:
				return "Cloudlet_Files_Done";
			default:
				return "none";
		}

	}

	/** Private Constructor */
	private MyCloudSimTags() {
		throw new UnsupportedOperationException("My CloudSim Tags cannot be instantiated");
	}

}
