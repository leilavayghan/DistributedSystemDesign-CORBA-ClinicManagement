package models;

import shared.enumerations.ClinicCode;
/**
 * Class representing the manager records and functionalities on the records requied for their implementation
 */
public class ManagerRecord {
	public String managerID;
	public static int countID = 1000;
	
	public ManagerRecord(String Location){
		countID++;
		if(Location.equals(ClinicCode.MTL))
		{
			managerID = "MTL" + countID;	
		}
		if(Location.equals(ClinicCode.LVL))
		{
			managerID = "LVL" + countID;	
		}
		if(Location.equals(ClinicCode.DDO))
		{
			managerID = "DDO" + countID;	
		}
	}
}