package org.cloudbus.cloudsim.power;

import java.util.ArrayList;
import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.MyCloudlet;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.distributions.ContinuousDistribution;
import org.cloudbus.cloudsim.distributions.ExponentialDistr;
import org.cloudbus.cloudsim.distributions.UniformDistr;
import org.cloudbus.cloudsim.lists.VmList;

/**
 * @author baplou
 * 
 */
public class MyPowerDatacenterBroker extends PowerDatacenterBroker {
	
	/**
	 * History of requests (cloudlets) arrival rate.
	 */
	private List<Double>	History	= new ArrayList<Double>();
	
	private ContinuousDistribution distri;
	
	/**
	 * Created a new DatacenterBroker object.
	 * 
	 * @param name
	 *            name to be associated with this entity (as required by Sim_entity class from simjava package)
	 * @throws Exception
	 *             the exception
	 * @pre name != null
	 * @post $none
	 */
	public MyPowerDatacenterBroker(
			String name,String RequestArrivalDistri) throws Exception {
		super(name);
		
		setDistri(RequestArrivalDistri);
	}
	
	@Override
	protected void submitCloudlets() {
		
		// Initialize local variable
		int vmIndex = 0;
		double tempDelay = 0;
		
		// For each Cloudlet of the Cloudlet list...
		for (Cloudlet cloudlet : getCloudletList()) {
			MyCloudlet myCloudlet = (MyCloudlet) cloudlet;
			
			// Vm binding check 
			Vm vm;
			if (cloudlet.getVmId() == -1) { // if user didn't bind this cloudlet and it has not been
											// executed yet
				vm = getVmsCreatedList().get(vmIndex);
			} else { // submit to the specific vm
				vm = VmList.getById(getVmsCreatedList(), cloudlet.getVmId());
				if (vm == null) { // vm was not created
					Log.printLine(CloudSim.clock() + ": " + getName() + ": Postponing execution of cloudlet "
							+ cloudlet.getCloudletId() + ": bount VM not available");
					continue;
				}
			}
			
			// set VM ID to the Cloudlet
			cloudlet.setVmId(vm.getId());
			
			// Request arrival rate: each Cloudlet are delayed according to a specific distribution algorithm.
			tempDelay = distri.sample();
			History.add(tempDelay);
			send(getVmsToDatacentersMap().get(vm.getId()), tempDelay, CloudSimTags.CLOUDLET_SUBMIT, myCloudlet);
			Log.formatLine("%.3f: %s: Cloudlet #%3d is scheduled to be sent to VM #%3d in %7.3f second(s)", CloudSim.clock(), getName(),
					cloudlet.getCloudletId(), vm.getId(), tempDelay);
			
			cloudletsSubmitted++;
			vmIndex = (vmIndex + 1) % getVmsCreatedList().size();
			getCloudletSubmittedList().add(cloudlet);
		}
		
		// remove submitted cloudlets from waiting list
		for (Cloudlet cloudlet : getCloudletSubmittedList()) {
			getCloudletList().remove(cloudlet);
		}
	}
	
	/**
	 * @return the delays
	 */
	public List<Double> getDelayHistory() {
		return History;
	}
	
	public void setDistri(String RequestArrivalDistri) {
		switch (RequestArrivalDistri) {
			case "expo":
				distri = new ExponentialDistr(60);
				break;
			
			default:
				distri = new UniformDistr(1, 2);
				break;
		}
	}
}
