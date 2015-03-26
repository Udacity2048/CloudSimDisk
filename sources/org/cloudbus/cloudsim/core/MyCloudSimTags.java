package org.cloudbus.cloudsim.core;

/**
 * New Tags for Storage implementation by Baptiste Louis.
 * 
 * @author baplou
 */
public class MyCloudSimTags {
	
	/** Starting constant value for cloud-related tags **/
	private static final int	BASE				= 0;
	
	/** Confirmation that Cloudlet Files have been handle **/
	public static final int		CLOUDLET_FILES_DONE	= BASE + 49;
	
	/**
	 * Convert tagValue to TagText.
	 * 
	 * @author Anupinder Singh
	 * @author baplou
	 * @param tagValue
	 * @return tagText
	 */
	public static String TagText(
			int tagValue) {
		switch (tagValue) {
			case 49:
				return "Cloudlet_Files_Done";
			default:
				return "Invalid tag value";
		}
		
	}
	
	/** Private Constructor */
	private MyCloudSimTags() {
		throw new UnsupportedOperationException(
				"My CloudSim Tags cannot be instantiated");
	}
	
}
