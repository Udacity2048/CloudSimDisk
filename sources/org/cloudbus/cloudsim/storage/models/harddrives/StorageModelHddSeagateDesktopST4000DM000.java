/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cloudbus.cloudsim.storage.models.harddrives;

/**
 * Storage Model based on Seagate Desktop HDD.15 Review (ST4000DM000).
 * 
 * Link: http://www.storagereview.com/seagate_desktop_hdd15_review_st4000dm000
 * 
 * @author baplou
 */
public class StorageModelHddSeagateDesktopST4000DM000 extends StorageModelHdd {

    /**
     * The characteristics of the hard drive: capacity, latency, seekTime,
     * maxTransferRate.
     */
    private final double[] parameters = {4000, 0.00516, 0.0085, 146};

    /**
     * The characteristics of the hard drive: capacity, latency, seekTime,
     * maxTransferRate.
     */
    private final String[] nominative = {"Seagate Desktop", "ST4000DM000"};
    
    /*
     * (non-Javadoc)
     * @see org.cloudbus.cloudsim.power.models.PowerModelSpecPower#getPowerData(int)
     */
    @Override
    protected double getParameter(int index) {
        return parameters[index];
    }

    /*
     * (non-Javadoc)
     * @see org.cloudbus.cloudsim.power.models.PowerModelSpecPower#getPowerData(int)
     */
    @Override
    protected String getnominative(int index) {
        return nominative[index];
    }
}
