package Manager;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import IClinicServiceCorba.DSMSCorba;
import shared.Config;

/**
 * Class containing the necessary variables for creating a session by manager or client
 */
public class ManagerSession implements Serializable{

	private String managerID;
	public DSMSCorba service;
	/**
	 * Getting client service based on manager id and clinic code
	 * @param managerID
	 * @throws Exception
	 */
	public ManagerSession(String managerID, DSMSCorba ref) throws Exception
	{
		this.managerID = managerID;
		this.service = ref;
	}
	
	public ManagerSession(String managerID) 
	{
		this.managerID = managerID;
		
	}
	/**
	 *getting manager id
	 * @return
	 */
	public String getManagerID()
	{
		return managerID;
	}
	/**
	 * Gettin instance of DSMSCorba
	 * @return
	 */
	public DSMSCorba getService()
	{
		return service;
	}
			
	public String getCountByPacket(int serverport) {
		DatagramSocket clientSocket = null;
		String loc = null;
		try
		{
			
			if (serverport==Config.PORT_NUMBER_MTL){
				loc = "MTL";
			}
			else if (serverport==Config.PORT_NUMBER_DDO){
				loc = "DDO";
			}
			else if (serverport==Config.PORT_NUMBER_LVL){
				loc = "LVL";
			}
			
			clientSocket = new DatagramSocket();
			
			byte[] udpCount = ("1"+loc).getBytes();
			InetAddress address = InetAddress.getByName("localhost");
			
			DatagramPacket request = new DatagramPacket(udpCount, ("1"+loc).length(),address,serverport);
			
			clientSocket.send(request);
			
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			clientSocket.receive(reply);
			
			return new String(reply.getData());
		
			
		}
		catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {System.out.println("IO: " + e.getMessage());}
		finally
		{
			if(clientSocket!=null)
			clientSocket.close();
		}
		return null;
	
	}
}
