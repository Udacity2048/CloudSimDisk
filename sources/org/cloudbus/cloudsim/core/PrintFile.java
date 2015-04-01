/**
 * 
 */
package org.cloudbus.cloudsim.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class create a trace of all Events passing and other OBSERVATION during a simulation. To log a new message, use
 * the code <PrintFile.AddtoFile("OBSERVATION")>. The file is available in the "logs" folder. More information at:
 * https://www.youtube.com/watch?v=tj2GKa39yTk
 * 
 * @author Anupinder Singh
 * @author Baptiste Louis
 */
public class PrintFile {
	
	/**
	 * The relative path of the future log files.
	 */
	public static String	file_name	= "";
	
	/**
	 * Add a new message to the Log file.
	 * 
	 * @param msg
	 *            the message to add on the Log file.
	 */
	public static void AddtoFile(
			String msg) {
		try {
			// Variable to change each log files names.
			java.util.Date d = new java.util.Date();
			
			// If the path of the future log files has not be initialized
			if (file_name == "") {
				file_name = "logs/cloudSim_Log" + d.getTime() + ".txt";
			}
			
			// Instantiate a File object
			File file = new File(
					file_name);
			
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			
			// write the message on the Log file
			FileWriter fw = new FileWriter(
					file.getAbsoluteFile(),
					true);
			String text = System.lineSeparator() + msg.replace("\n",
					System.lineSeparator());
			fw.write(text);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
