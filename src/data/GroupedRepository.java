package data;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import java.io.*;
import interfaces.IGroupable;
import interfaces.IRecord;
import models.DoctorRecord;
import models.NurseRecord;
import models.StaffRecord;
import shared.Config;
import shared.ExceptionStrings;
import shared.exceptions.NoRecordTypeException;
import shared.exceptions.RecordIndexOverflowException;
import view.Login;
/**
 * Groupedepository deals with all the functionalities required by Client by invoking method on Server. It takes generic record as type and implement functionality on it. 
 * @param <GroupableType>
 */
public class GroupedRepository<GroupableType extends IGroupable & IRecord> 
{
	
	
	private HashMap<String, ArrayList<GroupableType>> _records = new HashMap<String, ArrayList<GroupableType>>();
	static Logger loggerSystem = Logger.getLogger("dsmssystem");
	/**
	 *  Add creates a record depending upon the type of staffrecord it is, a doctor record or nurse record
	 * @param record type of record either nurse or doctor
	 */
	public synchronized void Add(GroupableType record)
	{
		
		String key = record.getGroupKey();
		
		if(_records.containsKey(key)){
			ArrayList<GroupableType> existingGroup = _records.get(key);
			existingGroup.add(record);
		}
		
		else{
			ArrayList<GroupableType> newGroup = new ArrayList<GroupableType>();
			newGroup.add(record);
			_records.put(key, newGroup);
		}
		
		
	}
	/**
	 *  returns the complete record of staff member
	 * @param recordID unique id of record
	 * @return Instance of staffrecord
	 */
	public synchronized GroupableType GetRecord(String recordID)
	{
		for(List<GroupableType> group : _records.values())
		{
			for(GroupableType record : group)
			{
				if(record.getRecordID().equals(recordID))
					return record;
			}
		}
		
		return null;
	}
	/**
	 * Function implemented to get an array of all the record ids
	 * @return array of record-ids.
	 */
	public String[] getRecordIDs()
	{
		ArrayList<String> recordIDs = new ArrayList<String>();
		
		for(List<GroupableType> group : _records.values())
		{
			for(GroupableType record : group)
			{
				recordIDs.add(record.getRecordID());
			}
		}
		
		return recordIDs.toArray(new String[0]);
	}
	/**
	 *  Function used to edit the field of record 
	 * @param recordID unique id of record
	 * @param FieldName attribute to edit
	 * @param FieldValue he value by which field is to edit
	 * @throws Exception 
	 */
	public void Edit(String recordID, String FieldName, String FieldValue) throws Exception
	{
		GroupableType record = GetRecord(recordID);
		
		Field[] recordFields = record.getClass().getFields();

		for(Field field : recordFields)
		{	
			if(field.getName().equals(FieldName))
			{
					field.set(record, FieldValue);
					break;
				}
				else
				{
					throw new Exception(ExceptionStrings.InvalidaFieldValue); 
				}
			}
		}
	
	
	/**
	 * Function to return recordIDs of all the records entered
	 * @return string of recordIDs
	 */
	public synchronized String getRecord()
	{
		ArrayList<String> recordIDs = new ArrayList<String>();
		
		for(List<GroupableType> group : _records.values())
		{
			for(GroupableType record : group)
			{
				recordIDs.add(record.getRecordID());
			}
		}
		
		return recordIDs.toString();
	}
	/**
	 * Function used to delete the record from the hashmap
	 * @param recordID
	 */
	public synchronized void deleteRecord( String recordID)
	{
		String key = GetRecord(recordID).getGroupKey();
		_records.get(key).remove(GetRecord(recordID));
		
	}
	/**
	 * Function used to find whether the records is present or not 
	 * @param recordID
	 * @return
	 */
	public synchronized boolean findRecord(String recordID)
	{
		boolean isFound = false;
		for(List<GroupableType> group : _records.values())
	
			{
				for(GroupableType record : group)
				{
					if(record.getRecordID().equals(recordID))
					{
						isFound = true;
					}
				}
			}
		return isFound;
	}
}
