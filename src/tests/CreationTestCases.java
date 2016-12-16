package tests;

import static org.junit.Assert.assertEquals;

import java.rmi.RemoteException;
import java.sql.Date;

import org.junit.BeforeClass;
import org.junit.Assert.*;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import data.GroupedRepository;

import org.junit.Assert;
import org.junit.Before;
import shared.enumerations.NurseDesignation;
import shared.enumerations.NurseStatus;
import view.Login;

import org.junit.Test;

import IClinicServiceCorba.DSMSCorba;
import IClinicServiceCorba.DSMSCorbaHelper;
import Manager.ManagerSession;
import corba.DSMSServer;

import java.util.Calendar;
import java.util.Properties;

import javax.naming.Context;

import models.DoctorRecord;
import models.NurseRecord;
import models.StaffRecord;
public class CreationTestCases {
	
	ManagerSession clinicservice= new ManagerSession("MTL");
	DSMSCorba reference;
	BeforeTest beforetest = new BeforeTest();
	@Test
	public void createDoctor() throws RemoteException{
	
		beforetest.createDoctorRecord("Ranjan", "Batra", "2360 Rue Sigouin", "22222", "SURGEON", Login.managerLocation, Login.managerID);
		beforetest.createDoctorRecord("Louisa", "Fletcher", "744 Home", "82622", "ENT", Login.managerLocation, Login.managerID);
		beforetest.createDoctorRecord("Gatma", "Eriv", "85 HL Est-Montreal", "234411", "ORTHOPADEICS", Login.managerLocation, Login.managerID);
		beforetest.createDoctorRecord("Francais", "Mitchel", "550 Rue Filatri", "874422", "surgeon", Login.managerLocation, Login.managerID);
		beforetest.createDoctorRecord("Nimar", "Wain", "745 West Ile", "821622", "CARDIOLOGY", Login.managerLocation, Login.managerID);
		beforetest.createDoctorRecord("Chin", "Yun Le", "33 RIverie Sud", "234461", "GYNECOLOGY", Login.managerLocation, Login.managerID);
		beforetest.createDoctorRecord("Katie", "Rosie", "550 Rue Filatri", "824422", "surgeon", Login.managerLocation, Login.managerID);
		
	    System.out.println("Doctord.dCount");
		assertEquals(DoctorRecord.dCount,7);
	   
	}
	@Test
	public void createNurseRecords() throws Exception{
		
		beforetest.createNurseRecord("Ranjan", "Batra","JUNIOR", "ACTIVE","23/10/2010","MTL");
		beforetest.createNurseRecord("Javed", "Jahar","JUNIOR", "ACTIVE","02/16/2015","DDO");
		beforetest.createNurseRecord("Siffredi", "Aniston","JUNIOR", "ACTIVE","15/23/2013","LVL");
		assertEquals(NurseRecord.nCount, 3);
	}
}
