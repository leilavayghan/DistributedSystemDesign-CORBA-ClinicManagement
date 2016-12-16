package tests;



import data.StaffRecordRepository;
import models.DoctorRecord;
import models.NurseRecord;
import models.StaffRecord;
/**
 * Test class 
 */
public class BeforeTest
{
	
	public String[] doctorRecordFields;
	public StaffRecordRepository StaffRecords = new StaffRecordRepository();

	public BeforeTest(){
		
	}
	
	/**
	 * Function to create doctor record 
	 */
	public void createDoctorRecord(String FirstName, String LastName, String Address, String PhoneNumber,
			String Specialization, String Location, String managerID)	
	{
		
		String cliniccode = Location.substring(0,3);
		DoctorRecord record = new DoctorRecord(cliniccode);
		
		record.FirstName = FirstName;	
		record.LastName = LastName;
		record.Address = Address;
		record.PhoneNumber = PhoneNumber;
		record.Specialization = Specialization;
		
		System.out.println("Record ID get is  " + record.getRecordID());
		StaffRecords.Add(record);
		
	}
	/**
	 * Function to create nurse record
	 */
	public void createNurseRecord(String FirstName, String LastName,String designation, String status,
			String string,String managerID) 
	{
		NurseRecord record = new NurseRecord();
		record.FirstName = FirstName;
		record.LastName = LastName;
		record.designation = designation;
		record.status = status;
		record.statusDate =  string;
		record.getRecordID();
		
		StaffRecords.Add(record);
	}
	
	/**
	 * To edit the record
	 */
	public void editRecord(String RecordID, String FieldName, String FieldValue) 
	{
		try
		{
			StaffRecords.Edit(RecordID, FieldName, FieldValue);
		}
		catch(Exception ex)
		{
			
		}
	}

	/**
	 *  Getting the record
	 */
		public StaffRecord getRecord(String recordID)
		{
			return StaffRecords.GetRecord(recordID);
		}
}