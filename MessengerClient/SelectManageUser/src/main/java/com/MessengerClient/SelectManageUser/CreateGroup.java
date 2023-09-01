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

public class CreateGroup
{
  private Socket connection_to_main_server;
  private GroupProfile group;
  private ClientProfile client;
  private String name;  
  private Scanner sc;
  private SelectGroup select_group;
  private GrpMngList members_list;
  private String email;
  private DataTransferPacket dp;
  private ObjectOutputStream oos;
  private ObjectInputStream ois;
  private GrpMngReturnList server_response;

  public CreateGroup(Socket connection_to_main_server, ClientProfile client, Scanner sc)
  {
    this.connection_to_main_server = connection_to_main_server;
    this.client = client;
    this.sc = sc;
    this.select_group = new SelectGroup(sc);
    this.members_list = new GrpMngList();
  }

  public CreateGroup(Socket connection_to_main_server, ClientProfile client,
                     ObjectOutputStream oos, ObjectInputStream ois, Scanner sc)
  {
    this.connection_to_main_server = connection_to_main_server;
    this.oos = oos;
    this.ois = ois;
    this.client = client;
    this.sc = sc;
    this.select_group = new SelectGroup(sc);
    this.members_list = new GrpMngList();
  }


  public boolean sendData()
  {
    try
    {
      oos.writeObject(dp);
      return true;
    } 
    catch (Exception e)
    {
      e.printStackTrace();
      return false;
    }
  }

  public void recieveData()
  {
    try
    {
      server_response = (GrpMngReturnList)ois.readObject();    
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.out.println("Data Read Error");
    }
  }

  public boolean cancleGroupCreation(String str)
  {
    if(str.equals("-1"))
    {
      System.out.println("Group Creation Terminated");
      return true;
    }
    return false;
  }

  public boolean start()
  {
    System.out.println("Enter -1 at any point to exit");
    System.out.println("Enter Name: ");
    name = sc.next();
    if(cancleGroupCreation(name) == true)
    {
      return false;
    }
    System.out.println("Enter Email (Optional)");
    email = sc.next();
    if(cancleGroupCreation(email) == true)
    {
      return false;
    }
    members_list = select_group.selectMembers();
    group = new GroupProfile(name, email, null, null, null);
    group.addMembersToAddList(members_list);
    dp = new DataTransferPacket("GRPMNGCREATE", client, null, null, group);

    if(sendData() == false)
    {
      System.out.println("Data Send Error");
      return false;
    }
    
    recieveData();
    if(server_response != null)
    {
      System.out.println("Group Created Successfully");
      group = server_response.getGroupProfile();
      group.printProfile();
      return true;
    }
    return false;
  }

}
