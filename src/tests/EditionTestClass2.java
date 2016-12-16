package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.rmi.RemoteException;

import org.junit.Test;

import IClinicServiceCorba.DSMSCorba;
import Manager.ManagerSession;
import corba.DSMSServer;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import models.DoctorRecord;
import view.Login;
import view.NewDoctor;

public class EditionTestClass2 {
	BeforeTest beforetest = new BeforeTest();
	@Test
	public void editDoctor2(){
	
	beforetest.createDoctorRecord("Ranjan", "Batra", "2360 Rue Sigouin", "909", "SURGEON", shared.Config.CLINIC_CODE_MTL, Login.managerID);
	beforetest.editRecord("DR10000", "Address", "Rivere");

	DoctorRecord record = (DoctorRecord)beforetest.getRecord("DR10000");
	assertNotSame(record.Address,"Noere");
	}
}