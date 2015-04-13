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
import org.cloudbus.cloudsim.distributions.MyBasicDistr;
import org.cloudbus.cloudsim.distributions.MyPoissonDistr;
import org.cloudbus.cloudsim.distributions.MyWikiDistr;
import org.cloudbus.cloudsim.distributions.UniformDistr;
import org.cloudbus.cloudsim.lists.VmList;
import org.cloudbus.cloudsim.util.WriteToResultFile;

/**
 * My Broker used for storage examples.
 * 
 * @author Baptiste Louis
 * 
 */
public class MyPowerDatacenterBroker extends PowerDatacenterBroker {

	/** History of requests (cloudlets) arrival rate. */
	private List<Double>			History	= new ArrayList<Double>();

	/** Distribution used for arrival rate. */
	private ContinuousDistribution	distri;

	/**
	 * Created a new DatacenterBroker object.
	 * 
	 * @param name
	 *            name to be associated with this entity (as required by Sim_entity class from simjava package)
	 * @param type
	 *            the distribution type
	 * @param RequestArrivalDistri
	 *            the file containing time distribution
	 * @throws Exception
	 *             the exception
	 */
	public MyPowerDatacenterBroker(String name, String type, String RequestArrivalDistri) throws Exception {
		super(name);

		setDistri(type, RequestArrivalDistri);
	}

	/* (non-Javadoc)
	 * 
	 * @see org.cloudbus.cloudsim.DatacenterBroker#submitCloudlets() */
	@SuppressWarnings("javadoc")
	@Override
	protected void submitCloudlets() {

		// Initialize local variable
		int vmIndex = 0;
		double tempArrivalTime = 0;

		// For each Cloudlet of the Cloudlet list...
		for (Cloudlet cloudlet : getCloudletList()) {
			MyCloudlet myCloudlet = (MyCloudlet) cloudlet;
			WriteToResultFile.AddValueToSheetTab(myCloudlet.getCloudletId(), myCloudlet.getCloudletId(), 0);

			// Vm binding check
			Vm vm;
			if (cloudlet.getVmId() == -1) { // if user didn't bind this cloudlet
											// and it has not been
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

			// request arrival rate sample according to a specific distribution type
			tempArrivalTime = distri.sample();

			// store arrival time
			History.add(tempArrivalTime);
			WriteToResultFile.AddValueToSheetTab(tempArrivalTime, myCloudlet.getCloudletId(), 1);

			// each Cloudlet are scheduled according to the request arrival rate sample
			send(getVmsToDatacentersMap().get(vm.getId()), tempArrivalTime - CloudSim.clock(), CloudSimTags.CLOUDLET_SUBMIT,
					myCloudlet);
			Log.formatLine("%.1f: %s: Cloudlet #%3d is scheduled to be sent to VM #%3d at %7.3f second(s)",
					CloudSim.clock(), getName(), cloudlet.getCloudletId(), vm.getId(), tempArrivalTime);

			// increment variable
			cloudletsSubmitted++;
			vmIndex = (vmIndex + 1) % getVmsCreatedList().size();

			// update Cloudlet Submitted List
			getCloudletSubmittedList().add(cloudlet);
		}

		// remove submitted cloudlets from waiting list
		for (Cloudlet cloudlet : getCloudletSubmittedList()) {
			getCloudletList().remove(cloudlet);
		}
	}

	/**
	 * Gets the arrival time history
	 * 
	 * @return the history
	 */
	public List<Double> getArrivalTimeHistory() {
		return History;
	}

	/**
	 * Sets the request arrival distribution
	 * 
	 * @param type
	 *            the type of distribution
	 * @param source
	 *            the source file containing time distribution
	 */
	public void setDistri(String type, String source) {
		switch (type) {
			case "basic":
				distri = new MyBasicDistr(source);
				break;
			case "expo":
				distri = new ExponentialDistr(60);
				break;

			case "pois":
				distri = new MyPoissonDistr(60);
				break;

			case "unif":
				distri = new UniformDistr(0, 1);
				break;

			case "wiki":
				distri = new MyWikiDistr(source);
				break;

			default:
				distri = new UniformDistr(1, 2);
				break;
		}
	}
}
