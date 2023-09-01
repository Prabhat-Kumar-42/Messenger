package com.MessengerClient.SelectManageUser;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import com.MessengerClient.DataTransferUnit.ClientProfile;
import com.MessengerClient.DataTransferUnit.DataTransferPacket;
import com.MessengerClient.DataTransferUnit.GroupProfile;
import com.MessengerClient.DataTransferUnit.GrpMngList;
import com.MessengerClient.DataTransferUnit.GrpMngReturnList;

public class ManageGroup
{
  
  private Socket connection_main_server;
  private GrpMngReturnList updated_members_list;
  private GrpMngList members_list;
  private Scanner sc;
  private SelectUser current_selected_user;
  private GroupProfile reciever;
  private ClientProfile client;
  private SelectGroup select_group;
  private DataTransferPacket dp;
  private ObjectOutputStream oos;
  private ObjectInputStream ois;

  public ManageGroup(Socket connection_main_server, ClientProfile client, SelectUser current_selected_user, Scanner sc)
  {
    this.connection_main_server = connection_main_server;
    this.client = client;
    this.sc = sc;
    this.current_selected_user = current_selected_user;
    this.select_group = new SelectGroup(client, current_selected_user, sc);
  }

  private void initializeOutputStream(Socket s)
  {
    try
    {
       oos = new ObjectOutputStream(s.getOutputStream());
    }
    catch(Exception e)
    {
      System.out.println("Error Initializing Output Stream");
    }
  }
  
  private void initializeInputStream(Socket s)
  {
    try
    {
      ois = new ObjectInputStream(s.getInputStream());
    }
    catch(Exception e)
    {
      System.out.println("Error Initializing Input Stream");
    }
  }

  private boolean sendData()
  {
    try
    {
      initializeOutputStream(connection_main_server);
      oos.writeObject(dp);
      return true;
    }
    catch(Exception e)
    {
      return false;
    }
  }

  private void recieveData()
  {
    try
    {
      initializeInputStream(connection_main_server);
      updated_members_list = (GrpMngReturnList)ois.readObject();
    }
    catch(Exception e)
    {
      return;
    }
  }

  public void start()
  {
    select_group.setConnectionTo(connection_main_server);
    System.out.println("Enter Respective Option");
    System.out.println();
    System.out.println("\t1. Add User To Group");
    System.out.println("\t2. Remove User From Group");
    System.out.println("\t3. Delete Group");
    System.out.println("\t4. Any Other Key to Exit");

    System.out.print("Enter Option: ");
    int option = sc.nextInt();
    sc.nextLine();

    reciever = select_group.selectGroup();
    if(reciever == null)
    {
      return;
    }
    System.out.println("Selected Group: ");
    System.out.println();
    reciever.printProfile();
    members_list = select_group.selectMembers();
    if(reciever.getAdmin().getID() != client.getID())
    {
      System.out.println("Only Admins Can Add/Remove from group");
      return;
    }
    
    if(option == 1)
    {
      dp = new DataTransferPacket("GRPMNGADDUSERS", client, null, null, members_list);
    }
    else if(option == 2)
    {
      dp = new DataTransferPacket("GRPMNGDELUSERS", client, null, null, members_list);
    }
    else if(option == 3)
    {
      dp = new DataTransferPacket("GRPMNGDELGRP", client, null, null, null);
    }
    else
    {
      return;
    }
    if(sendData() == false)
    {
      System.out.println("Error Sending Data");
    }
    
    recieveData();
    if(updated_members_list != null)
    {
      updated_members_list.addedMembers();
      updated_members_list.removedMembers();
    }
    else
    {
      System.out.println("Can't Update Data: UNEXPECTED ERROR");  
    }
  }
}
