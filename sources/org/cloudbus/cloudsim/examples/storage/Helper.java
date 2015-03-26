package org.cloudbus.cloudsim.examples.storage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.CloudletSchedulerDynamicWorkload;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.File;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.MyCloudlet;
import org.cloudbus.cloudsim.ParameterException;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.VmSchedulerTimeSharedOverSubscription;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.examples.power.Constants;
import org.cloudbus.cloudsim.power.MyPowerDatacenter;
import org.cloudbus.cloudsim.power.MyPowerDatacenterBroker;
import org.cloudbus.cloudsim.power.PowerDatacenter;
import org.cloudbus.cloudsim.power.PowerDatacenterBroker;
import org.cloudbus.cloudsim.power.MyPowerHarddriveStorage;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerHostUtilizationHistory;
import org.cloudbus.cloudsim.power.PowerVm;
import org.cloudbus.cloudsim.power.models.PowerModel;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerHpProLiantMl110G4Xeon3040;
import org.cloudbus.cloudsim.power.models.harddrives.*;
import org.cloudbus.cloudsim.provisioners.BwProvisioner;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.storage.models.harddrives.StorageModelHdd;
import org.cloudbus.cloudsim.storage.models.harddrives.StorageModelHddSeagateEnterpriseST6000VN0001;

/**
 * Helper for the CloudSim examples of cloudsim.examples.storage package.
 * 
 * @author baplou
 * 
 */
public class Helper {
	
	/**
	 * an index incrementing Names and IDs of HDDs created.
	 */
	private int									hddId			= 0;
	
	/**
	 * an index incrementing IDs of Cloudlets created.
	 */
	private int									cloudletId		= 0;
	
	/**
	 * an index incrementing IDs of VMs created.
	 */
	private int									vmId			= 0;
	
	/**
	 * an index incrementing IDs of Hosts created.
	 */
	private int									hostId			= 0;
	
	/**
	 * the cloudlet list.
	 */
	public List<MyCloudlet>						cloudletList	= new ArrayList<MyCloudlet>();
	
	/**
	 * the Power-VM List.
	 */
	public List<PowerVm>						vmlist			= new ArrayList<PowerVm>();
	
	/**
	 * the Power-host List.
	 */
	public List<PowerHost>						hostList		= new ArrayList<PowerHost>();
	
	/**
	 * the Pe List.
	 */
	public List<Pe>								peList			= new ArrayList<Pe>();
	
	/**
	 * the persistent storage List.
	 */
	public LinkedList<MyPowerHarddriveStorage>	storageList		= new LinkedList<MyPowerHarddriveStorage>();
	
	/**
	 * the Broker.
	 */
	public MyPowerDatacenterBroker				broker;
	
	/**
	 * the Datacenter.
	 */
	public MyPowerDatacenter					datacenter;
	
	/**
	 * the Datacenter Characteristics
	 */
	public DatacenterCharacteristics			datacenterCharacteristics;
	
	// Methods
	/**
	 * Initialize CloudSim.
	 */
	public void initCloudSim() {
		CloudSim.init(1,
				Calendar.getInstance(),
				false);
	}
	
	/**
	 * Creates a Power-aware broker named "Broker".
	 * 
	 */
	public void createBroker(
			String RequestArrivalDistri) {
		try {
			broker = new MyPowerDatacenterBroker(
					"Broker",
					RequestArrivalDistri);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * create Pe List.
	 * 
	 * @param PesNumber
	 */
	public void createPeList(
			int PesNumber) {
		for (int i = 0; i < PesNumber; i++) {
			peList.add(new Pe(
					i,
					new PeProvisionerSimple(
							MyConstants.HOST_MIPS)));
		}
	}
	
	/**
	 * Creates the host list.
	 * 
	 * @param hostsNumber
	 *            the hosts number
	 */
	public void createHostList(
			int hostsNumber) {
		for (int i = 0; i < hostsNumber; i++) {
			hostList.add(new PowerHost(
					i,
					new RamProvisionerSimple(
							MyConstants.HOST_RAM),
					new BwProvisionerSimple(
							MyConstants.HOST_BW),
					MyConstants.HOST_STORAGE,
					peList,
					new VmSchedulerTimeSharedOverSubscription(
							peList),
					MyConstants.HOST_POWER_MODEL));
		}
	}
	
	/**
	 * Creates the vm list.
	 * 
	 * @param vmsNumber
	 *            the vms number
	 */
	public void createVmList(
			int vmsNumber) {
		for (int i = 0; i < vmsNumber; i++) {
			vmlist.add(new PowerVm(
					i,
					broker.getId(),
					MyConstants.VM_MIPS,
					MyConstants.VM_PES_NUMBER,
					MyConstants.VM_RAM,
					MyConstants.VM_BW,
					MyConstants.VM_SIZE,
					MyConstants.VM_PRIORITY,
					MyConstants.VM_VMM,
					MyConstants.VM_CLOUDLET_SCHEDULER,
					MyConstants.VM_SCHEDULING_INTERVVAL));
		}
		broker.submitVmList(vmlist);
	}
	
	/**
	 * create a defined number of defined storage type to the persistent storage of a power-aware datacenter.
	 * 
	 * @param storageNumber
	 * @throws ParameterException
	 */
	public void createPersistentStorage(
			int storageNumber) throws ParameterException {
		for (int i = 0; i < storageNumber; i++) {
			storageList.add(new MyPowerHarddriveStorage(
					i,
					"hdd" + i,
					MyConstants.STORAGE_MODEL_HDD,
					MyConstants.STORAGE_POWER_MODEL_HDD));
		}
	}
	
	/**
	 * Creates a power-aware Datacenter.
	 */
	public void createDatacenterCharacteristics() {
		datacenterCharacteristics = new DatacenterCharacteristics(
				MyConstants.DATACENTER_ARCHITECTURE,
				MyConstants.DATACENTER_OS,
				MyConstants.DATACENTER_VMM,
				hostList,
				MyConstants.DATACENTER_TIME_ZONE,
				MyConstants.DATACENTER_COST_PER_SEC,
				MyConstants.DATACENTER_COST_PER_MEM,
				MyConstants.DATACENTER_COST_PER_STORAGE,
				MyConstants.DATACENTER_COST_PER_BW);
	}
	
	/**
	 * Creates a power-aware Datacenter.
	 * 
	 * @throws Exception
	 */
	public void createDatacenter() throws Exception {
		datacenter = new MyPowerDatacenter(
				MyConstants.DATACENTER_NAME,
				datacenterCharacteristics,
				new VmAllocationPolicySimple(
						hostList),
				storageList,
				MyConstants.DATACENTER_SCHEDULING_INTERVAL);
	}
	
	/**
	 * @param CloudlerNumber
	 * @param requiredFiles
	 * @param dataFiles
	 * @throws ParameterException
	 */
	public void createCloudletList(
			int CloudlerNumber,
			List<String> requiredFiles,
			List<File> dataFiles) throws ParameterException {
		
		for (int i = 0; i < CloudlerNumber; i++) {
			cloudletList.add(new MyCloudlet(
					i,
					MyConstants.CLOUDLET_LENGHT,
					MyConstants.CLOUDLET_PES_NUMBER,
					MyConstants.CLOUDLET_FILE_SIZE,
					MyConstants.CLOUDLET_OUTPUT_SIZE,
					MyConstants.CLOUDLET_UTILIZATION_MODEL_CPU,
					MyConstants.CLOUDLET_UTILIZATION_MODEL_RAM,
					MyConstants.CLOUDLET_UTILIZATION_MODEL_BW,
					requiredFiles,
					dataFiles));
			cloudletList.get(i).setUserId(broker.getId());
			cloudletList.get(i).setVmId(vmlist.get(0).getId());
		}
		broker.submitCloudletList(cloudletList);
	}
	
	/**
	 * @param startingFilesList
	 */
	public void addFiles(
			String startingFilesList) {
		
		if (startingFilesList == "") {
			try {
				datacenter.addFile(new File(
						"shortFile",
						1));
			} catch (ParameterException e) {
				e.printStackTrace();
			}
		} else {
			try {
				// instantiate a reader
				BufferedReader input = new BufferedReader(
						new FileReader(
								"files/" + startingFilesList));
				
				// read line by line
				String line;
				String[] lineSplited;
				String fileName;
				String fileSize;
				while ((line = input.readLine()) != null) {
					
					// retrieve fileName and fileSize
					lineSplited = line.split("\\s+"); // regular expression quantifiers for whitespace
					fileName = lineSplited[0];
					Log.print(fileName);
					fileSize = lineSplited[1];
					
					// add file to datacenter
					datacenter.addFile(new File(
							fileName,
							Integer.parseInt(fileSize)));
				}
				
				// close the reader
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * print the persistent storage details of a power-aware datacenter.
	 */
	public void printPersistenStorageDetails() {
		List<MyPowerHarddriveStorage> tempList = datacenter.getStorageList();
		
		for (int i = 0; i < tempList.size(); i++) {
			Log.printLine((i + 1) + "/" + (tempList.size()) + " " + tempList.get(i).getName());
			Log.formatLine("\tCapacity        -> %10.0f %s",
					tempList.get(i).getCapacity(),
					"MB");
			Log.formatLine("\tUsedSpace       -> %10.0f %s",
					(tempList.get(i).getCapacity() - tempList.get(i).getAvailableSpace()),
					"MB");
			Log.formatLine("\tFreeSpace       -> %10.0f %s",
					tempList.get(i).getAvailableSpace(),
					"MB");
			Log.formatLine("\tlatency         -> %10.5f %s",
					tempList.get(i).getLatency(),
					"s");
			Log.formatLine("\tavgSeekTime     -> %10.5f %s",
					tempList.get(i).getAvgSeekTime(),
					"s");
			Log.formatLine("\tmaxTransferRate -> %10.0f %s",
					tempList.get(i).getMaxTransferRate(),
					"MB/s.");
		}
	}
	
	/**
	 * Prints a summary of the simulation.
	 */
	public void printResults() {
		int numberOfHosts = datacenter.getHostList().size();
		int numberOfVms = vmlist.size();
		int numberOfCloudlets = cloudletList.size();
		double TotalStorageEnergy = datacenter.getTotalStorageEnergy();
		
		Log.printLine();
		Log.printLine("*** RESULTS ***");
		Log.printLine();
		Log.formatLine("Number of Hosts      : %d",
				numberOfHosts);
		Log.formatLine("Number of Vms        : %d",
				numberOfVms);
		Log.formatLine("Number of Cloudlets  : %d",
				numberOfCloudlets);
		Log.printLine();
		Log.formatLine("Total Storage    Energy Consumed : %.3f Joule(s)",
				TotalStorageEnergy);
	}
	
	/**
	 * Prints the list of arrival request time.
	 */
	public void printArrivalRate() {
		Log.printLine("\n\n");
		Log.printLine("************** Arrival Rate in second(s) (not sorted) *************");
		
		for (Double delay : broker.getDelayHistory()) {
			Log.formatLine("%20.15f",
					delay);
		}
		
		Log.printLine("\n\n");
	}
}
