package view;

import java.rmi.RemoteException;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import Manager.ManagerSession;
import models.NurseRecord;
import models.StaffRecord;
import shared.enumerations.Locations;
import shared.enumerations.NurseDesignation;
import shared.enumerations.NurseStatus;


public class TransferRecord extends JPanel{
	
	private JPanel jpanel;
	private static JTextField txtRecordID,txtDestination;
	private static String strRecordID,strDestination;
	private static JComboBox cmbDestination;
	static Logger loggerSystem = Logger.getLogger("dsmssystem");
	static Logger loggerServer = Logger.getLogger("server");
	
	
	
	public JPanel panel(boolean restriction){			// false for creating , true for editing

	JPanel jpanel = new JPanel();
	
	JLabel lblRecordID = new JLabel("Record ID: ");
	txtRecordID = new JTextField();
		
	JLabel lblDesignation = new JLabel("Destination Server: ");
	
	if(Login.managerCode.equals("MTL")){
		String[] myStrings = { "DDO", "LVL"};
		cmbDestination = new JComboBox(myStrings);
	}
	else if(Login.managerCode.equals("DDO")){
		String[] myStrings = { "MTL", "LVL"};
		cmbDestination = new JComboBox(myStrings);
	}
	else if(Login.managerCode.equals("LVL")){
		String[] myStrings = { "MTL", "DDO"};
		cmbDestination = new JComboBox(myStrings);
	}
	
	
	jpanel.setLayout(new BoxLayout(jpanel, BoxLayout.Y_AXIS));
	jpanel.add(lblRecordID);
	jpanel.add(txtRecordID);
	
	jpanel.add(lblDesignation);
	jpanel.add(cmbDestination);
	
	if(restriction)
	{
		txtRecordID.setEditable(false);
	}
	return jpanel;
	
	}
	
	public void TransferValidCreate(ManagerSession session) throws Exception
	{
		
		strRecordID = txtRecordID.getText().toString();
		strDestination = cmbDestination.getSelectedItem().toString();
		if(strRecordID.equals(""))
			JOptionPane.showMessageDialog(jpanel,"Enter Record ID");	
			else if(strDestination.equals(""))
			JOptionPane.showMessageDialog(jpanel,"Choose Destination Server");
			else
			{
				session.getService().transferRecord(Login.managerID, strRecordID, strDestination);
				loggerSystem.info("The manager with managerID of "
						 + Login.managerID + " in clinic location " + Login.managerCode + " wants to transfer the record with the ID of " + strRecordID + " to the server in " + strDestination);
				loggerServer.info("Transfer Record Operation is Invoked at server of " + Login.managerCode + "by manager with manager-id" + Login.managerID);
			}
	}
}
