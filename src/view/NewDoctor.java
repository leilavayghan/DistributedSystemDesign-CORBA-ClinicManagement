package view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.apache.log4j.chainsaw.Main;

import Manager.ManagerSession;
import data.GroupedRepository;
import models.DoctorRecord;
import models.StaffRecord;
import shared.enumerations.ClinicCode;


public class NewDoctor extends JPanel{
	
	private ManagerSession session;
	
	private JPanel jpanel;
	private static JTextField txtFirstName,txtLastName,txtAddress,txtPhoneNumber,txtSpecialization;
	private static String strFirstName,strLastName,strAddress,strPhoneNumber,strSpecialization;
	static Logger loggerSystem = Logger.getLogger("dsmssystem");
	static Logger loggerServer = Logger.getLogger("server");
	
	public JPanel panel(boolean restriction)
	{
	JPanel jpanel = new JPanel();
	
	JLabel lblFirstName = new JLabel("First Name:");
	txtFirstName =  new JTextField();
	
	JLabel lblLastName = new JLabel("Last Name: ");
	txtLastName = new JTextField();
	
	JLabel lblAddress = new JLabel("Address: ");
	txtAddress = new JTextField();
	
	JLabel lblPhoneNumber = new JLabel("Phone Number: ");
	txtPhoneNumber = new JTextField();
	
	JLabel lblSpecialization = new JLabel("Specialization: ");
	txtSpecialization = new JTextField();
	
	JLabel lblLocation = new JLabel("Location");
	JComboBox cmbLocation = new JComboBox(ClinicCode.values());

	jpanel.setLayout(new BoxLayout(jpanel, BoxLayout.Y_AXIS));
	jpanel.add(lblFirstName);
	jpanel.add(txtFirstName);
	
	jpanel.add(lblLastName);
	jpanel.add(txtLastName);
	
	jpanel.add(lblAddress);
	jpanel.add(txtAddress);
	
	jpanel.add(lblPhoneNumber);
	jpanel.add(txtPhoneNumber);
	
	jpanel.add(lblSpecialization);
	jpanel.add(txtSpecialization);
	
	if(restriction)
	{
		txtFirstName.setEnabled(false);
		txtLastName.setEnabled(false);
		txtSpecialization.setEnabled(false);
		jpanel.add(lblLocation);
		jpanel.add(cmbLocation);
	}
	
	return jpanel;
	
	}

	public void DoctorCreateValid(ManagerSession session) throws Exception{
		strFirstName = txtFirstName.getText().toString();
		strLastName = txtLastName.getText().toString();
		strAddress = txtAddress.getText().toString();
		strPhoneNumber = txtPhoneNumber.getText().toString(); 
		strSpecialization = txtSpecialization.getText().toString();
		if(strFirstName.equals(""))
		JOptionPane.showMessageDialog(jpanel,"Enter First Name");
		else if(strLastName.equals(""))
		JOptionPane.showMessageDialog(jpanel,"Enter Last Name");
		else if(strAddress.equals(""))
		JOptionPane.showMessageDialog(jpanel,"Enter Address");
		else if(strPhoneNumber.equals(""))
		JOptionPane.showMessageDialog(jpanel,"Enter Phone Number");
		else if(strSpecialization.equals(""))
		JOptionPane.showMessageDialog(jpanel,"Enter Specialization");
		else
		{
			session.getService().createDoctorRecord(strFirstName, strLastName, strAddress, strPhoneNumber, strSpecialization,Login.managerCode, Login.managerID);
			
			loggerServer.info("Create Doctor Operation is Invoked at server of " + Login.managerCode + " by manager with manager-id" + Login.managerID);
			loggerSystem.info("New Doctor Record is created by manager with managerID "
					 + Login.managerID + " in clinic location " + Login.managerCode);
			
		}
	}
	
	public void setDoctorEdit(String strFirstName,String strLastName,String strAddress,String strPhoneNumber,String strSpecialization)
	{
		
	     txtFirstName.setText(strFirstName);
		 txtLastName.setText(strLastName);
		 txtAddress.setText(strAddress);
		 txtPhoneNumber.setText(strPhoneNumber);
		 txtSpecialization.setText(strSpecialization);
		 
	}
}