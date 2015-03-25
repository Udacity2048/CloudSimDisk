/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cloudbus.cloudsim.storage.models.harddrives;

/**
 * The abstract class of a specific hard drive model.
 *
 * @author baplou
 */
public abstract class StorageModelHdd {

    /**
     * Get the name of the harddrive.
     *
     * @return name of the harddrive
     */
    public String getFullName() {
        return getnominative(0);
    }
    
    /**
     * Get the reference of the harddrive.
     *
     * @return reference of the harddrive
     */
    public String getReference() {
        return getnominative(1);
    }

    /**
     * Get the total capacity of the harddrive in MB.
     *
     * @return total capacity of the harddrive in MB
     */
    public double getCapacity() {
        return getParameter(0);
    }

    /**
     * Get the latency of the harddrive in seconds.
     *
     * @return latency of the harddrive in seconds
     */
    public double getLatency() {
        return getParameter(1);
    }

    /**
     * Get the average seek time in seconds.
     *
     * @return average seek time in seconds
     */
    public double getAvgSeekTime() {
        return getParameter(2);
    }

    /**
     * Get the maximum transfer rate in MB/sec.
     *
     * @return maximum transfer rate in MB/sec
     */
    public double getMaxTransferRate() {
        return getParameter(3);
    }

    /**
     * Get Parameters of a specific StorageModelHdd : 0 the capacity, 1 the
     * Latency, 2 the avgSeekTime, 3 the maxTransferRate.
     *
     * @param key key value of the parameter
     * @return the requested parameter
     */
    protected abstract double getParameter(int key);

    /**
     * Get the nominative of the hard drive: 0 the name, 1 the reference.
     * @param key key value of the nominative
     * @return reference of the hard drive
     */
    protected abstract String getnominative(int key);
}
