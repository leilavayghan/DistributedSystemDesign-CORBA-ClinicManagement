package corba;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import java.util.Properties;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import IClinicServiceCorba.*;
import data.StaffRecordRepository;
import models.DoctorRecord;
import models.NurseRecord;
import shared.Config;
import shared.exceptions.RecordIndexOverflowException;
import view.Login;

public class DSMSServer extends DSMSCorbaPOA implements Runnable{

	public  StaffRecordRepository StaffRecords = new StaffRecordRepository();
	private ORB orb;
	public static int MTLcount = 0;
	public static int DDOcount = 0;
	public static int LVLcount = 0;
	public static int udpserverport = Config.PORT_NUMBER_MTL - 100;
	public static String receivedmsg,sendmsg;
	public static boolean isDone=false;
		
	public void setORB(ORB orb_val)
	{
		orb = orb_val;
	}
	public void createDoctorRecord(String firstName, String lastName, String address, String phone,
			String specialization, String location, String managerID) {
		
		DoctorRecord record = new DoctorRecord(location);
		record.FirstName = firstName;	
		record.LastName = lastName;
		record.Address = address;
		record.PhoneNumber = phone;
		record.Specialization = specialization;
		record.getRecordID();
		record.Location = location;
		System.out.println("Location " + location);
		StaffRecords.Add(record);
		if(location.equals("MTL")){
			MTLcount++;
		}
		if(location.equals("DDO")){
			DDOcount++;
		}
		if(location.equals("LVL")){
			LVLcount++;
		}
	}

	public void createNurseRecord(String firstName, String lastName, String designation, String status, String date,
			String location, String managerID) 
	{
		NurseRecord record = new NurseRecord(location);
		record.FirstName = firstName;
		record.LastName = lastName;
		record.designation = designation;
		record.status = status;
		record.statusDate =  date;
		record.getRecordID();
		record.cliniclocation = location;
		System.out.println("Location " + location);
		StaffRecords.Add(record);
		
		if(location.equals("MTL")){
			MTLcount++;
		}
		if(location.equals("DDO")){
			DDOcount++;
		}
		if(location.equals("LVL")){
			LVLcount++;
		}
		
	}

	public void editRecord(String recordID, String fieldName, String fieldValue) 
	{
		try
		{
			StaffRecords.Edit(recordID, fieldName, fieldValue);
		}
		catch(Exception ex)
		{
			
		}
		
	}

	public void transferRecord(String managerID, String recordID, String remoteClinicServerName) 
	{
		int remoteport;
		if(remoteClinicServerName.equals("MTL")){
			remoteport = Config.PORT_NUMBER_MTL;
		}
		else if(remoteClinicServerName.equals("DDO")){
			remoteport = Config.PORT_NUMBER_DDO;
		}
		else{
			remoteport = Config.PORT_NUMBER_LVL;
		}
		if(StaffRecords.findRecord(recordID)){
			String mymsg = null;
			DoctorRecord doctor;
			NurseRecord nurse;
			
			if (recordID.substring(0, 1).equals("D")){
				doctor = (DoctorRecord) StaffRecords.GetRecord(recordID);
				mymsg = "D" + ",," + doctor.FirstName + ",," + doctor.LastName + ",," + doctor.Address + ",," + 
						doctor.PhoneNumber + ",," + doctor.Specialization + ",," + remoteClinicServerName + 
							",," + managerID;
			}
			
			else if (recordID.substring(0, 1).equals("N")){
				nurse = (NurseRecord) StaffRecords.GetRecord(recordID);
				mymsg = "N" + ",," + nurse.FirstName + ",," + nurse.LastName + ",," + nurse.designation + ",," + 
						nurse.status + ",," + nurse.statusDate + ",," + remoteClinicServerName + 
							",," + managerID;
			}
			
			DatagramSocket clientSocket = null;
			try
			{
				
				clientSocket = new DatagramSocket();
				
				byte[] udpCount = mymsg.getBytes();
				InetAddress address = InetAddress.getByName("localhost");
				
				DatagramPacket request = new DatagramPacket(udpCount, mymsg.length(),address,remoteport);
				
				clientSocket.send(request);
				
				byte[] buffer = new byte[1000];
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
				clientSocket.receive(reply);
				if(new String(reply.getData()).substring(0, 4).equals("done")){
					isDone=true;
				}
				
			}
			catch (SocketException e){System.out.println("Socket: " + e.getMessage());
			} catch (IOException e) {System.out.println("IO: " + e.getMessage());}
			finally{
				if(clientSocket!=null)
				clientSocket.close();
			}
			
			if (isDone){
				StaffRecords.deleteRecord(recordID);
				if(managerID.substring(0,3).equals("MTL"))
					MTLcount--;
				else if(managerID.substring(0,3).equals("DDO"))
					DDOcount--;
				else if(managerID.substring(0,3).equals("LVL"))
					LVLcount--;
				isDone = false;
			}
		}
		else{
			JOptionPane.showMessageDialog(null, "Invalid Record ID for transfer");
		}
	}
	@Override
	public void deleteRecord(String recordID) {
		
		
	}
	@Override
	public String getRecord() {
		return StaffRecords.getRecord();
	}

	public static String interpret(String received){
		
		// if received.substring(0, 1) : it means we need to send the count
		// if received.substring(0, 1) is something else, we need to transfer
		
		if (received.substring(0, 1).equals("1")){
			
			if (received.substring(1, 4).equals("MTL")){
				return Integer.toString(MTLcount);
			}
			else if (received.substring(1, 4).equals("DDO")){
				return Integer.toString(DDOcount);
			}
			else {
				return Integer.toString(LVLcount);
			}
		}
		
		else{
			
			return received;
		}
	}
	
	public static void main(String args[])
	{
		try 
		{
			
				
		Properties props = new Properties();
        props.put("org.omg.CORBA.ORBInitialPort", "1050");
        ORB orb = ORB.init(args, props);
        
		POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
		rootpoa.the_POAManager().activate();
		
		//MTL
		DSMSServer dsmsServantMTL = new DSMSServer();
		dsmsServantMTL.setORB(orb);
		org.omg.CORBA.Object refMTL = rootpoa.servant_to_reference(dsmsServantMTL);
		DSMSCorba dsmsCorbaMTL = DSMSCorbaHelper.narrow(refMTL);
		
		org.omg.CORBA.Object objRefMTL = orb.resolve_initial_references("NameService");
		NamingContextExt ncRefMTL = NamingContextExtHelper.narrow(objRefMTL);
		NameComponent pathMTL[] = ncRefMTL.to_name("MTL");
		ncRefMTL.rebind(pathMTL,dsmsCorbaMTL);
		Thread mtl = new Thread(dsmsServantMTL);
		mtl.start();
		System.out.println("Montreal Server Running");
		
		//DDO
		DSMSServer dsmsServantDDO = new DSMSServer();
		dsmsServantDDO.setORB(orb);
		org.omg.CORBA.Object refDDO = rootpoa.servant_to_reference(dsmsServantDDO);
		DSMSCorba dsmsCorbaDDO = DSMSCorbaHelper.narrow(refDDO);
		
		org.omg.CORBA.Object objRefDDO = orb.resolve_initial_references("NameService");
		NamingContextExt ncRefDDO = NamingContextExtHelper.narrow(objRefDDO);
		NameComponent pathDDO[] = ncRefDDO.to_name("DDO");
		ncRefDDO.rebind(pathDDO,dsmsCorbaDDO);
		
		Thread ddo = new Thread(dsmsServantDDO);
		ddo.start();

		System.out.println("DDO Server Running");
		
		//LVL
		DSMSServer dsmsServantLVL = new DSMSServer();
		dsmsServantLVL.setORB(orb);
		org.omg.CORBA.Object refLVL = rootpoa.servant_to_reference(dsmsServantLVL);
		DSMSCorba dsmsCorbaLVL = DSMSCorbaHelper.narrow(refLVL);
		
		org.omg.CORBA.Object objRefLVL = orb.resolve_initial_references("NameService");
		NamingContextExt ncRefLVL = NamingContextExtHelper.narrow(objRefLVL);
		NameComponent pathLVL[] = ncRefLVL.to_name("LVL");
		ncRefLVL.rebind(pathLVL,dsmsCorbaLVL);
		Thread lvl = new Thread(dsmsServantLVL);
		lvl.start();
		System.out.println("LVL Server Running");
		
		for(;;)
		{
		orb.run();
		}
		}
		catch (NotFound e) 
		{
		e.printStackTrace();
		} 
		catch (CannotProceed e) 
		{
		e.printStackTrace();
		}
		catch (InvalidName e) 
		{
		e.printStackTrace();
		}
		catch (AdapterInactive e) 
		{
		e.printStackTrace();
		}
		catch (ServantNotActive e) 
		{
		e.printStackTrace();
		}
		catch (WrongPolicy e) 
		{
		e.printStackTrace();
		}
		catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) 
		{
		e.printStackTrace();
		}
		
	}
	@Override
	public void run() {
		DatagramSocket mySocket = null;
		
		try
		{
			udpserverport = udpserverport + 100;
			mySocket = new DatagramSocket(udpserverport);
			
			byte[] mybuffer = new byte[1000];
			
			while(true)
			{
				
				DatagramPacket myrequest = new DatagramPacket(mybuffer, mybuffer.length);
				
				mySocket.receive(myrequest);
				receivedmsg=(new String(myrequest.getData()));
				sendmsg=interpret(receivedmsg);
				String[] array = sendmsg.split(",,");
				if (array[0].equals("D")){
					createDoctorRecord(array[1], array[2],array[3],array[4],
							array[5],array[6],array[7]);
					sendmsg="done";
					}
				if (array[0].equals("N")){
					createNurseRecord(array[1], array[2],array[3],array[4],
							array[5],array[6],array[7]);
					sendmsg="done";
					}
				DatagramPacket myreply = new DatagramPacket(sendmsg.getBytes(),
						sendmsg.length(),myrequest.getAddress(),myrequest.getPort());
				mySocket.send(myreply);
				
			}
		}
		catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {System.out.println("IO: " + e.getMessage());}
		finally
		{
			if(mySocket!=null){
				mySocket.close();
			}
		}
		
	}
	
}
