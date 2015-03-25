package org.cloudbus.cloudsim.examples.storage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.File;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.MyCloudlet;
import org.cloudbus.cloudsim.ParameterException;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.examples.power.Constants;
import org.cloudbus.cloudsim.power.MyPowerDatacenter;
import org.cloudbus.cloudsim.power.MyPowerDatacenterBroker;
import org.cloudbus.cloudsim.power.PowerDatacenter;
import org.cloudbus.cloudsim.power.PowerDatacenterBroker;
import org.cloudbus.cloudsim.power.MyPowerHarddriveStorage;
import org.cloudbus.cloudsim.power.PowerHost;
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
	 * an index incrementing IDs of Pe created.
	 */
	private int									peId			= 0;
	
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
	
	// Methods
	
	/**
	 * Initialize CloudSim.
	 * 
	 * @param numberOfUser
	 */
	public void initCloudSim(int numberOfUser) {
		Calendar calendar = Calendar.getInstance();
		boolean trace_flag = false; // mean trace events
		
		// Initialize the CloudSim library.
		CloudSim.init(numberOfUser, calendar, trace_flag);
	}
	
	/**
	 * Creates a power-aware Datacenter.
	 * 
	 * @param arch
	 * @param os
	 * @param vmm
	 * @param time_zone
	 * @param cost
	 * @param costPerMem
	 * @param costPerStorage
	 * @param costPerBw
	 * @return
	 */
	public PowerDatacenter createDatacenter(String arch, String os, String vmm, double time_zone, double cost, double costPerMem,
			double costPerStorage, double costPerBw) {
		
		// Hosts
		addPe(1000);
		addHost(new RamProvisionerSimple(2048), new BwProvisionerSimple(10000), 1000000, new VmSchedulerTimeShared(peList),
				new PowerModelSpecPowerHpProLiantMl110G4Xeon3040());
		
		// Persistent Storage
		addPersistentStorage(1, new StorageModelHddSeagateEnterpriseST6000VN0001(),
				new PowerModeHddSeagateEnterpriseST6000VN0001());
		Log.printLine("Persitent storage created.");
		
		// Datacenter Characteristics
		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(arch, os, vmm, hostList, time_zone, cost,
				costPerMem, costPerStorage, costPerBw);
		
		// Datacenter
		try {
			datacenter = new MyPowerDatacenter("datacenter", characteristics, new VmAllocationPolicySimple(hostList),
					storageList, 100);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datacenter;
	}
	
	/**
	 * Creates a Power-aware broker named "Broker".
	 * 
	 */
	public void createBroker() {
		try {
			broker = new MyPowerDatacenterBroker("Broker");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a power-aware VM.
	 * 
	 * @param mips
	 * @param pesNumber
	 * @param ram
	 * @param bw
	 * @param size
	 * @param priority
	 * @param vmm
	 * @param cloudletScheduler
	 * @param schedulingInterval
	 */
	public void createVM(double mips, int pesNumber, int ram, long bw, long size, int priority, String vmm,
			CloudletScheduler cloudletScheduler, double schedulingInterval) {
		
		// create one VM.
		PowerVm vm = new PowerVm(vmId, broker.getId(), mips, pesNumber, ram, bw, size, 1, vmm, cloudletScheduler,
				schedulingInterval);
		
		vmId++;
		
		// add the VM to the vmList.
		vmlist.add(vm);
		
		// submit the vmList to the Broker entity.
		broker.submitVmList(vmlist);
	}
	
	/**
	 * add cloudlet to the Cloudlet List. This constructor can create a define number of identical Cloudlets.
	 * 
	 * @param quantity
	 *            the number of Cloudlet to create.
	 * @param lenght
	 * @param pesNumber
	 * @param fileSize
	 * @param outputSize
	 * @param utilizationModelCpu
	 * @param utilizationModelRam
	 * @param utilizationModelBw
	 * @param requiredFiles
	 * @param dataFiles
	 * @throws ParameterException 
	 */
	public void addCloudlet(int quantity, long lenght, int pesNumber, long fileSize, long outputSize,
			UtilizationModel utilizationModelCpu, UtilizationModel utilizationModelRam, UtilizationModel utilizationModelBw,
			List<String> requiredFiles, List<File> dataFiles) throws ParameterException {
		
		int i;
		
		for (i = 0; i < quantity; i++) {
			
			// Data Files creation
			List<File> dF = new ArrayList<File>();
			dF.add(new File("video" + i, 100));
			
			MyCloudlet cloudlet = new MyCloudlet(cloudletId, lenght, pesNumber, fileSize, outputSize, utilizationModelCpu,
					utilizationModelRam, utilizationModelBw, requiredFiles, dF);
			cloudlet.setUserId(broker.getId());
			cloudlet.setVmId(vmlist.get(0).getId()); // assume there is only 1 VM
			cloudletId++;
			
			// add the cloudlet to the list.
			cloudletList.add(cloudlet);
		}
	}
	
	/**
	 * add cloudlet to the Cloudlet List. This constructor can create one specific cloudlet.
	 * 
	 * @param lenght
	 * @param pesNumber
	 * @param fileSize
	 * @param outputSize
	 * @param utilizationModelCpu
	 * @param utilizationModelRam
	 * @param utilizationModelBw
	 * @param requiredFiles
	 * @param dataFiles
	 */
	public void addCloudlet(long lenght, int pesNumber, long fileSize, long outputSize, UtilizationModel utilizationModelCpu,
			UtilizationModel utilizationModelRam, UtilizationModel utilizationModelBw, List<String> requiredFiles,
			List<File> dataFiles) {
		
		MyCloudlet cloudlet = new MyCloudlet(cloudletId, lenght, pesNumber, fileSize, outputSize, utilizationModelCpu,
				utilizationModelRam, utilizationModelBw, requiredFiles, dataFiles);
		cloudlet.setUserId(broker.getId());
		cloudlet.setVmId(vmlist.get(0).getId());
		cloudletId++;
		
		// add the cloudlet to the list.
		cloudletList.add(cloudlet);
	}
	
	/**
	 * add a Pe to the Pe List.
	 * 
	 * @param mips
	 */
	public void addPe(int mips) {
		peList.add(new Pe(peId, new PeProvisionerSimple(mips)));
		peId++;
	}
	
	/**
	 * add a host to the host List.
	 * 
	 * @param ramProvisioner
	 * @param bwProvisioner
	 * @param storage
	 * @param vmScheduler
	 * @param powerModel
	 */
	public void addHost(RamProvisioner ramProvisioner, BwProvisioner bwProvisioner, long storage, VmScheduler vmScheduler,
			PowerModel powerModel) {
		
		PowerHost Machine = new PowerHost(hostId, ramProvisioner, bwProvisioner, storage, peList, vmScheduler, powerModel);
		
		hostList.add(Machine);
		hostId++;
	}
	
	/**
	 * add a defined number of defined storage type to the persistent storage of a power-aware datacenter.
	 * 
	 * @param quantity
	 *            the quantity of disk created
	 * @param storageModelHdd
	 *            the storage HDD model
	 * @param powerModel
	 *            the power model
	 */
	public void addPersistentStorage(int quantity, StorageModelHdd storageModelHdd, PowerModel powerModel) {
		
		for (int i = 0; i < quantity; i++) {
			System.out.println("Hard drive <hdd" + hddId + "> manufacturing...");
			MyPowerHarddriveStorage tempHdd;
			try {
				tempHdd = new MyPowerHarddriveStorage(hddId, "hdd" + hddId, storageModelHdd,
						new PowerModeHddHGSTUltrastarC10K900());
				hddId++;
				storageList.add(tempHdd);
				System.out.println("Hard drive <hdd" + hddId + "> created.");
			} catch (ParameterException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * add a new power-aware hard drive to the persistent storage if a power-aware datacenter.
	 * 
	 * @param name
	 *            the name of the HDD
	 * @param storageModelHdd
	 *            the HDD-model of the HDD
	 * @param powerModelHdd
	 *            the power-model of the HDD
	 */
	public void addPersistentStorage(String name, StorageModelHdd storageModelHdd, PowerModelHdd powerModelHdd) {
		
		MyPowerHarddriveStorage tempHdd;
		try {
			tempHdd = new MyPowerHarddriveStorage(hddId, name, storageModelHdd, powerModelHdd);
			hddId++;
			storageList.add(tempHdd);
			Log.printLine("Hard drive <" + name + "> created.");
		} catch (ParameterException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * add a file to the persistent storage of a power-aware datacenter. This file will be added before the simulation
	 * start.
	 * 
	 * @param fileName
	 *            is the name of the file
	 * @param fileSize
	 *            is the size of the file
	 */
	public void addFile(String fileName, int fileSize) {
		File tempFile;
		try {
			tempFile = new File(fileName, fileSize);
			datacenter.addFile(tempFile);
		} catch (ParameterException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the cloudletList
	 */
	public List<MyCloudlet> getCloudletList() {
		return cloudletList;
	}
	
	/**
	 * @return the broker
	 */
	public PowerDatacenterBroker getBroker() {
		return broker;
	}
	
	/**
	 * print the persistent storage details of a power-aware datacenter.
	 * 
	 * @param powerDatacenter
	 *            the powerDatacenter
	 */
	public void printPersistenStorageDetails(PowerDatacenter powerDatacenter) {
		List<MyPowerHarddriveStorage> tempList = powerDatacenter.getStorageList();
		
		for (int i = 0; i < tempList.size(); i++) {
			Log.printLine((i + 1) + "/" + (tempList.size()) + " " + tempList.get(i).getName());
			Log.formatLine("\tCapacity        -> %10.0f %s", tempList.get(i).getCapacity(), "MB");
			Log.formatLine("\tUsedSpace       -> %10.0f %s",
					(tempList.get(i).getCapacity() - tempList.get(i).getAvailableSpace()), "MB");
			Log.formatLine("\tFreeSpace       -> %10.0f %s", tempList.get(i).getAvailableSpace(), "MB");
			Log.formatLine("\tlatency         -> %10.5f %s", tempList.get(i).getLatency(), "s");
			Log.formatLine("\tavgSeekTime     -> %10.5f %s", tempList.get(i).getAvgSeekTime(), "s");
			Log.formatLine("\tmaxTransferRate -> %10.0f %s", tempList.get(i).getMaxTransferRate(), "MB/s.");
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
		Log.formatLine("Number of Hosts      : %d", numberOfHosts);
		Log.formatLine("Number of Vms        : %d", numberOfVms);
		Log.formatLine("Number of Cloudlets  : %d", numberOfCloudlets);
		Log.printLine();
		Log.formatLine("Total Storage    Energy Consumed : %.3f Joule(s)", TotalStorageEnergy);
	}
	
	/**
	 * Prints the list of arrival request time.
	 */
	public void printArrivalRate() {
		Log.printLine("\n\n");
		Log.printLine("************** Arrival Rate in second(s) (not sorted) *************");
		
		for (Double delay : broker.getDelayHistory()) {
			Log.formatLine("%20.15f", delay);
		}
		
		Log.printLine("\n\n");
	}
}
