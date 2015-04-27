package org.cloudbus.cloudsimdisk.core;

import org.cloudbus.cloudsim.core.CloudSimTags;

/**
 * My CloudSim Tags is used to declare ONE new Tags called CLOUDLET_FILES_DONE for the storage implementation.
 * 
 * @author Baptiste Louis
 */
public class MyCloudSimTags extends CloudSimTags {

	/** Confirmation that Cloudlet Files have been handle **/
	public static final int	CLOUDLET_FILE_DONE	= BASE + 49;

	/**
	 * Convert tagValue to TagText.
	 * 
	 * @author Anupinder Singh
	 * @author Baptiste Louis
	 * @param tagValue
	 *            the tag value number to convert in tag text
	 * @return tagText
	 */
	public static String TagText(int tagValue) {
		switch (tagValue) {
			case 100:
				return "NETBASE";
			case 0:
				return "BASE";
			case 9600:
				return "DEFAULT_BAUD_RATE";
			case -1:
				return "END_OF_SIMULATION";
			case -2:
				return "ABRUPT_END_OF_SIMULATION";
			case 1:
				return "EXPERIMENT";
			case 2:
				return "REGISTER_RESOURCE";
			case 3:
				return "REGISTER_RESOURCE_AR";
			case 4:
				return "RESOURCE_LIST";
			case 5:
				return "RESOURCE_AR_LIST";
			case 6:
				return "RESOURCE_CHARACTERISTICS";
			case 7:
				return "RESOURCE_DYNAMICS";
			case 8:
				return "RESOURCE_NUM_PE";
			case 9:
				return "RESOURCE_NUM_FREE_PE";
			case 10:
				return "RECORD_STATISTICS";
			case 11:
				return "RETURN_STAT_LIST";
			case 12:
				return "RETURN_ACC_STATISTICS_BY_CATEGORY";
			case 13:
				return "REGISTER_REGIONAL_GIS";
			case 14:
				return "REQUEST_REGIONAL_GIS";
			case 15:
				return "RESOURCE_CHARACTERISTICS_REQUEST";
			case 20:
				return "CLOUDLET_RETURN";
			case 21:
				return "CLOUDLET_SUBMIT";
			case 22:
				return "CLOUDLET_SUBMIT_ACK";
			case 23:
				return "CLOUDLET_CANCEL";
			case 24:
				return "CLOUDLET_STATUS";
			case 25:
				return "CLOUDLET_PAUSE";
			case 26:
				return "CLOUDLET_PAUSE_ACK";
			case 27:
				return "CLOUDLET_RESUME";
			case 28:
				return "CLOUDLET_RESUME_ACK";
			case 29:
				return "CLOUDLET_MOVE";
			case 30:
				return "CLOUDLET_MOVE_ACK";
			case 31:
				return "VM_CREATE";
			case 32:
				return "VM_CREATE_ACK";
			case 33:
				return "VM_DESTROY";
			case 34:
				return "VM_DESTROY_ACK";
			case 35:
				return "VM_MIGRATE";
			case 36:
				return "VM_MIGRATE_ACK";
			case 37:
				return "VM_DATA_ADD";
			case 38:
				return "VM_DATA_ADD_ACK";
			case 39:
				return "VM_DATA_DEL";
			case 40:
				return "VM_DATA_DEL_ACK";
			case 41:
				return "VM_DATACENTER_EVENT";
			case 42:
				return "VM_BROKER_EVENT";
			case 43:
				return "Network_Event_UP";
			case 44:
				return "Network_Event_send";
			case 45:
				return "RESOURCE_Register";
			case 46:
				return "Network_Event_DOWN";
			case 47:
				return "Network_Event_Host";
			case 48:
				return "NextCycle";
			case 49:
				return "Cloudlet_Files_Done";
			default:
				return "Invalid tag value";
		}
	}

	/** Private Constructor */
	private MyCloudSimTags() {
		super();
	}

}
