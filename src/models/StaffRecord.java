package models;

import java.io.Serializable;

import javax.naming.Context;

import interfaces.IGroupable;
import interfaces.IRecord;
/**
 * Base class of constituting the attributes of the records
 *
 */
public class StaffRecord implements IRecord, IGroupable ,Serializable
{
	public String RecordID;
	public static int MTLcount = 0;
	public static int DDOcount = 0;
	public static int LVLcount = 0;
	public static String countString;
	public String FirstName; 
	public String LastName;
	/**
	 * To get recordId of the record
	 */
	@Override
	public String getRecordID() 
	{
		return RecordID;
	}
	/**
	 *  To get the group key
	 */
	@Override
	public String getGroupKey() 
	{
		return LastName.substring(0, 1);
	}

	
}
