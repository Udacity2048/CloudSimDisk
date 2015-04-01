/*
 * Title:        CloudSim EES Extention
 * Description:  CloudSim extention for Energy Efficient Storage
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2015, Luleå University of Techonology
 */
package org.cloudbus.cloudsim.power.models.harddrives;

import org.cloudbus.cloudsim.power.models.PowerModel;

/**
 * The abstract class of power models for hard disk drives.
 *
 * @author Baptiste Louis
 */
public abstract class PowerModelHdd implements PowerModel {

    /*
     * This methode return the power consumption of an Hard drive according to 
     * its mode: 0 for Idle mode, 1 for operation mode.
     *
     * Note: further, new mode can be implemented.
     *
     * @see org.cloudbus.cloudsim.power.models.PowerModel#getPower(double)
     */
    @SuppressWarnings("javadoc")
	@Override
    public double getPower(double mode) throws IllegalArgumentException {
        double power = getPowerData((int) mode);
        return power;
    }

    /**
     * Gets the power data.
     *
     * @param key 0 for Idle mode, 1 for operation mode.
     * @return the power data
     */
    protected abstract double getPowerData(int key);

}
