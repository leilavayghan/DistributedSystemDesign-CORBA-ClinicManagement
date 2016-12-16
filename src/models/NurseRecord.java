package models;

import java.io.Serializable;
import java.sql.Date;

import shared.enumerations.NurseDesignation;
import shared.enumerations.NurseStatus;
import shared.exceptions.RecordIndexOverflowException;
/**
 * Class representing the nurse records
 */
public class NurseRecord extends StaffRecord implements Serializable
{
	
	public String designation; 
	public String status;
	public String cliniclocation;
	public String statusDate;
	public static int nCount =0;
	public NurseRecord(){
		nCount++;
	}
	
	public NurseRecord(String clinicCode){
		
		if(clinicCode.equals("MTL")){
			int index = 10000  + MTLcount;
			RecordID = "NR" + Integer.toString(index);
			MTLcount++;
		}
		
		if(clinicCode.equals("LVL")){
			int index = 10000  + LVLcount;
			RecordID = "NR" + Integer.toString(index);
			LVLcount++;
		}
		
		if(clinicCode.equals("DDO")){
			int index = 10000  + DDOcount;
			RecordID = "NR" + Integer.toString(index);
			DDOcount++;
		}
	}
}
