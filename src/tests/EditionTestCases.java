package tests;

import static org.junit.Assert.assertEquals;

import java.rmi.RemoteException;

import org.junit.Test;

import Manager.ManagerSession;
import models.DoctorRecord;
import shared.Config;
import view.Login;

public class EditionTestCases {
	
	ManagerSession clinicservice;
	BeforeTest beforetest = new BeforeTest();
	@Test
	public void editDoctor1() throws RemoteException{

		beforetest.createDoctorRecord("Ranjan", "Batra", "2360 Rue Sigouin", "909", "SURGEON", Config.CLINIC_CODE_LVL, Login.managerID);
		beforetest.editRecord("DR10000", "Address", "Rivere");
		
		DoctorRecord record = (DoctorRecord)beforetest.getRecord("DR10000");
		assertEquals(record.Address,"Rivere");
	}
}
