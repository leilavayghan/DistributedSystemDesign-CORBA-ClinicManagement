package models;

import java.io.Serializable;

import jdk.nashorn.internal.runtime.regexp.joni.Config;
import shared.exceptions.RecordIndexOverflowException;
/**
 * Class representing the doctor records and the attributes
 *
 */
public class DoctorRecord extends StaffRecord implements Serializable
{
	public String Address;
	public String PhoneNumber;
	public String Specialization;
	public String Location;
	public static int dCount=0;
	public DoctorRecord(){
		dCount++;
	}
	public DoctorRecord(String clinicCode){
		if(clinicCode.equals("MTL")){
			int index = 10000 + MTLcount;
			RecordID = "DR" + Integer.toString(index);
			MTLcount++;
		}
		if(clinicCode.equals("LVL")){
			int index = 10000 +  LVLcount;
			RecordID = "DR" + Integer.toString(index);
			LVLcount++;
		}
		if(clinicCode.equals("DDO")){
			int index = 10000 + DDOcount;
			RecordID = "DR" + Integer.toString(index);
			DDOcount++;
		}
	}	
}
