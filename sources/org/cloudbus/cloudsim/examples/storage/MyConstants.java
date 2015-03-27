package org.cloudbus.cloudsim.examples.storage;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.power.models.PowerModel;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerHpProLiantMl110G4Xeon3040;
import org.cloudbus.cloudsim.power.models.harddrives.PowerModeHddSeagateEnterpriseST6000VN0001;
import org.cloudbus.cloudsim.provisioners.BwProvisioner;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.storage.models.harddrives.StorageModelHdd;
import org.cloudbus.cloudsim.storage.models.harddrives.StorageModelHddSeagateEnterpriseST6000VN0001;

/**
 * Constants used by the Storage examples.
 *
 * @author Baptiste Louis
 * @since March, 25 2015
 */
public class MyConstants {

	// Default Datacenter Characteristics
	public final static String DATACENTER_NAME = "datacenter";
	public final static String DATACENTER_ARCHITECTURE = "x86";
	public final static String DATACENTER_OS = "Linux";
	public final static String DATACENTER_VMM = "Xen";
	public final static double DATACENTER_TIME_ZONE = 10.0;
	public final static double DATACENTER_COST_PER_SEC = 3.0;
	public final static double DATACENTER_COST_PER_MEM = 0.05;
	public final static double DATACENTER_COST_PER_STORAGE = 0.001;
	public final static double DATACENTER_COST_PER_BW = 0.0;
	public final static double DATACENTER_SCHEDULING_INTERVAL = 100;
	
	// Default VM parameters
	public final static int VM_NUMBER = 1;
	public final static double VM_MIPS = 50;
	public final static int VM_PES_NUMBER = 1;
	public final static int VM_RAM = 512;
	public final static long VM_BW = 1000;
	public final static long VM_SIZE = 10000;
	public final static int VM_PRIORITY = 1;
	public final static String VM_VMM = "Xen";
	public final static CloudletScheduler VM_CLOUDLET_SCHEDULER = new CloudletSchedulerTimeShared();
	public final static double VM_SCHEDULING_INTERVVAL = 1;
	
	// Default Host parameters
	public final static int HOST_NUMBER = 1;
	public final static int HOST_RAM = 2048;
	public final static long HOST_BW = 10000;
	public final static RamProvisioner HOST_RAM_PROVISIONER = new RamProvisionerSimple(HOST_RAM);
	public final static BwProvisioner HOST_BW_PROVISIONER = new BwProvisionerSimple(HOST_BW);
	public final static long HOST_STORAGE = 1000000;
	public final static long HOST_MIPS = 1000;
	public final static List<? extends Pe> HOST_PE_LIST = new ArrayList<Pe>();
	public final static VmScheduler HOST_VM_SCHEDULER = new VmSchedulerTimeShared(HOST_PE_LIST);
	public final static PowerModel HOST_POWER_MODEL = new PowerModelSpecPowerHpProLiantMl110G4Xeon3040();
	
	// Default Persistent storage parameters
	public final static StorageModelHdd STORAGE_MODEL_HDD = new StorageModelHddSeagateEnterpriseST6000VN0001();
	public final static PowerModel STORAGE_POWER_MODEL_HDD = new PowerModeHddSeagateEnterpriseST6000VN0001();
	
	// Default Cloudlet parameters
	public final static int CLOUDLET_LENGHT = 1;
	public final static int CLOUDLET_PES_NUMBER = 1;
	public final static int CLOUDLET_FILE_SIZE = 300;
	public final static int CLOUDLET_OUTPUT_SIZE = 300;
	public final static UtilizationModel CLOUDLET_UTILIZATION_MODEL_CPU = new UtilizationModelFull();
	public final static UtilizationModel CLOUDLET_UTILIZATION_MODEL_RAM = new UtilizationModelFull();
	public final static UtilizationModel CLOUDLET_UTILIZATION_MODEL_BW = new UtilizationModelFull();
	public final static int CLOUDLET_NUMBER_WIKI = 5000;
}

