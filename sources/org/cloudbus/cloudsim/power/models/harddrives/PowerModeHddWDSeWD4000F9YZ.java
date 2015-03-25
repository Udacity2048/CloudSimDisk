/*
 * Title:        CloudSim EES Extention
 * Description:  CloudSim extention for Energy Efficient Storage
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2015, Luleå University of Techonology
 */
package org.cloudbus.cloudsim.power.models.harddrives;

/**
 * The power model of a WD Se WD4000F9YZ.
 *
 * Info source: http://www.storagereview.com/wd_se_hdd_review
 * 
 * @author Baptiste Louis
 */
public class PowerModeHddWDSeWD4000F9YZ extends PowerModelHdd{

    /**
     * The power consumption of the HDD in idle mode in W.
     */
    private final double powerIdle = 8.1;
    
    /**
     * The power consumption of the HDD in operation mode in W.
     */
    private final double powerOpe = 9.5;
    
    /*
     * Gets the power data. See PowerModelSpecPower.java fore more understanding.
     * 
     * @param key 0 for Idle mode, 1 for operation mode.
     * @return the power data
     */
    @Override
    protected double getPowerData(int key) {
        if (key == 0) {
            return powerIdle;
        } else {
            return powerOpe;
        }
    }

}
