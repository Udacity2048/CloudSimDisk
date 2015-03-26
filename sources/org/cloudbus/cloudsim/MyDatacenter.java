package org.cloudbus.cloudsim;

import java.util.Iterator;
import java.util.List;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.MyCloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;

/**
 * @author baplou
 * 
 */
public class MyDatacenter extends Datacenter {
	
	/**
	 * @param name
	 * @param characteristics
	 * @param vmAllocationPolicy
	 * @param storageList
	 * @param schedulingInterval
	 * @throws Exception
	 */
	public MyDatacenter(
			String name,
			DatacenterCharacteristics characteristics,
			VmAllocationPolicy vmAllocationPolicy,
			List<? extends Storage> storageList,
			double schedulingInterval) throws Exception {
		super(
				name,
				characteristics,
				vmAllocationPolicy,
				storageList,
				schedulingInterval);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.cloudbus.cloudsim.Datacenter#processEvent(org.cloudbus.cloudsim.core.SimEvent)
	 */
	@Override
	public void processEvent(
			SimEvent ev) {
		
		if (ev.getTag() == MyCloudSimTags.CLOUDLET_FILES_DONE) {
			processCloudletFilesDone(ev);
		} else {
			super.processEvent(ev);
		}
	}
	
	/**
	 * Processes a Cloudlet Files Done notification
	 */
	protected void processCloudletFilesDone(
			SimEvent ev) {
		
		// Retrieves data from the ev
		Object[] data = (Object[]) ev.getData();
		String action = (String) data[0];
		Cloudlet cl = (Cloudlet) data[1];
		File tempFile = (File) data[2];
		double tempTime = (double) data[4];
		Storage storage = (Storage) data[7];
		
		// Print out confirmation that Files have been handled
		Log.formatLine("%.3f: %s: Cloudlet # %d: <%s> %s on %s.",
				CloudSim.clock(),
				getName(),
				cl.getCloudletId(),
				tempFile.getName(),
				action,
				storage.getName());
		Log.formatLine("%10s Transaction time of %6.3f Seconde(s) according to %s specifications.",
				"",
				tempTime,
				storage.getName());
		Log.printLine();
	}
	
	/**
	 * Processes a Cloudlet submission.
	 * 
	 * @param ev
	 *            a SimEvent object
	 * @param ack
	 *            an acknowledgment
	 */
	@Override
	protected void processCloudletSubmit(
			SimEvent ev,
			boolean ack) {
		updateCloudletProcessing();
		
		try {
			// gets the Cloudlet object
			Object obj = ev.getData();
			
			// Test is the Object <obj> is a Cloudlet
			if (obj instanceof Cloudlet) {
				Cloudlet cl = (Cloudlet) obj;
				
				// checks whether this Cloudlet has finished or not
				if (cl.isFinished()) {
					String name = CloudSim.getEntityName(cl.getUserId());
					Log.printLine(getName() + ": Warning - Cloudlet #" + cl.getCloudletId() + " owned by " + name
							+ " is already completed/finished.");
					Log.printLine("Therefore, it is not being executed again");
					Log.printLine();
					
					// NOTE: If a Cloudlet has finished, then it won't be processed.
					// So, if ack is required, this method sends back a result.
					// If ack is not required, this method don't send back a result.
					// Hence, this might cause CloudSim to be hanged since waiting
					// for this Cloudlet back.
					if (ack) {
						int[] data = new int[3];
						data[0] = getId();
						data[1] = cl.getCloudletId();
						data[2] = CloudSimTags.FALSE;
						
						// unique tag = operation tag
						int tag = CloudSimTags.CLOUDLET_SUBMIT_ACK;
						sendNow(cl.getUserId(),
								tag,
								data);
					}
					
					sendNow(cl.getUserId(),
							CloudSimTags.CLOUDLET_RETURN,
							cl);
					
					return;
				}
				
				// process this Cloudlet to this CloudResource
				cl.setResourceParameter(getId(),
						getCharacteristics().getCostPerSecond(),
						getCharacteristics().getCostPerBw());
				
				int userId = cl.getUserId();
				int vmId = cl.getVmId();
				
				// time to transfer the files
				double fileTransferTime = predictRequiredFilesTransferTime(cl.getRequiredFiles());
				
				// Test if the Cloudlet <cl> is a MyCloudlet.
				if (cl instanceof MyCloudlet) {
					MyCloudlet mycl = (MyCloudlet) cl;
					fileTransferTime += predictDataFilesTransferTime(mycl.getDataFiles());
				}
				
				Host host = getVmAllocationPolicy().getHost(vmId,
						userId);
				Vm vm = host.getVm(vmId,
						userId);
				CloudletScheduler scheduler = vm.getCloudletScheduler();
				double estimatedFinishTime = scheduler.cloudletSubmit(cl,
						fileTransferTime);
				
				// if this cloudlet is in the execution queue
				if (estimatedFinishTime > 0.0 && !Double.isInfinite(estimatedFinishTime)) {
					estimatedFinishTime += fileTransferTime;
					send(getId(),
							estimatedFinishTime,
							CloudSimTags.VM_DATACENTER_EVENT); // Generate
																// Event.
				}
				
				if (ack) {
					int[] data = new int[3];
					data[0] = getId();
					data[1] = cl.getCloudletId();
					data[2] = CloudSimTags.TRUE;
					
					// unique tag = operation tag
					int tag = CloudSimTags.CLOUDLET_SUBMIT_ACK;
					sendNow(cl.getUserId(),
							tag,
							data);
				}
			}
		} catch (ClassCastException c) {
			Log.printLine(getName() + ".processCloudletSubmit(): " + "ClassCastException error.");
			c.printStackTrace();
		} catch (Exception e) {
			Log.printLine(getName() + ".processCloudletSubmit(): " + "Exception error.");
			e.printStackTrace();
		}
		
		checkCloudletCompletion();
	}
	
	/**
	 * Predict Required Files transfer time.
	 * 
	 * @param requiredFiles
	 *            the required files
	 * @return the double
	 */
	protected double predictRequiredFilesTransferTime(
			List<String> requiredFiles) {
		double time = 0.0;
		
		Iterator<String> iter = requiredFiles.iterator();
		while (iter.hasNext()) {
			String fileName = iter.next();
			for (int i = 0; i < getStorageList().size(); i++) {
				Storage tempStorage = getStorageList().get(i);
				File tempFile = tempStorage.getFile(fileName);
				if (tempFile != null) {
					time += tempFile.getSize() / tempStorage.getMaxTransferRate();
					break;
				}
			}
		}
		return time;
	}
	
	/**
	 * Predict data files transfer time.
	 * 
	 * @param requiredFiles
	 *            the required files
	 * @return the double
	 */
	protected double predictDataFilesTransferTime(
			List<File> dataFiles) {
		double time = 0.0;
		
		// handle the case where there is no persistent storage
		if (getStorageList().size() == 0) {
			return time;
		}
		
		Iterator<File> iter = dataFiles.iterator();
		while (iter.hasNext()) {
			File fileName = iter.next();
			time += fileName.getSize() / getStorageList().get(0).getMaxTransferRate();
			// for the prediction, it is assumed that the maximum transfer rate
			// of the target hard drive will be approximately the same than the
			// first hard drive in the storageList of this Datacenter.
			
		}
		return time;
	}
}
