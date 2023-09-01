package com.MessengerClient.SelectManageUser;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import com.MessengerClient.DataTransferUnit.ClientProfile;
import com.MessengerClient.DataTransferUnit.DataTransferPacket;
import com.MessengerClient.DataTransferUnit.GroupProfile;
import com.MessengerClient.DataTransferUnit.GrpMngList;

public class SelectGroup 
{
  private Socket connection_main_server;
  private GrpMngList members_list;
  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private DataTransferPacket dp;
  private Scanner sc;
  private SelectUser current_selected_user;
  private GroupProfile reciever;
  private String group_id; 
  private ClientProfile client;
  
  public SelectGroup(Scanner sc)
  {
    this.sc = sc;
    this.members_list = new GrpMngList();
  }

  public SelectGroup(ClientProfile client, SelectUser current_selected_user, Scanner sc)
  {
    this.client = client;
    this.members_list = new GrpMngList();
    this.sc = sc;
    this.current_selected_user = current_selected_user;
    this.group_id = current_selected_user.getReciever().getID();
  }

  public void setConnectionTo(Socket s)
  {
    this.connection_main_server = s;
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
      oos.writeObject(dp);
      return true;
    }
    catch(Exception e)
    {
      return false;
    }
  }

  private ClientProfile recieveData()
  {
    try
    {
      ClientProfile data = (ClientProfile)ois.readObject();
      return data;
    }
    catch(Exception e)
    {
      return null;
    }
  }

  public int printManageListMessage(String type)
  {
    System.out.println("Select Respective Options");
    System.out.println("1. Finalize " + type + " List");
    System.out.println("2. Add To " + type + " List");
    System.out.println("3. Remove from " + type + " List");
    int option;
    while(true)
    {
      option = sc.nextInt();
      sc.nextLine();
      if(option < 1 || option > 3)
      {
        System.out.println("Invalid Option");
        System.out.println("Try Again");
      }
      else
      {
        break;
      }
    }
    return option;
  }
  
  private void removeMembersID()
  {
    System.out.println("Removing Users");
    System.out.println("Enter -1 to exit");
    String member = "";

    while(member.equals("-1") == false)
    {
      System.out.println("Enter User-ID");
      member = sc.next();
      if(member.equals("-1") == false)
      {
        members_list.removeID(member);
      }
    }
  }

  private void removeMembersEmail()
  {
    System.out.println("Removing Users");
    System.out.println("Enter -1 to exit");
    String member = "";

    while(member.equals("-1") == false)
    {
      System.out.println("Enter Email");
      member = sc.next();
      if(member.equals("-1") == false)
      {
        members_list.removeEmail(member);
      }
    }
  }
  private void manageList()
  {
    System.out.println("User-ID To Be Added: ");
    members_list.printAddedID();
    int option = printManageListMessage("User-ID");
    if(option == 2)
    {
      selectMembers();
    }
    else if(option == 3)
    {
      removeMembersID(); 
    }
    System.out.println("User With Email To Be Added ");
    option = printManageListMessage("Email");
    if(option == 2)
    {
      selectMembers();
    }
    else if(option == 3)
    {
      removeMembersEmail(); 
    }
  }

  public GrpMngList selectMembers()
  {
    System.out.println("Enter -1 to exit");
    String member = "";

    while(member.equals("-1") == false)
    {
      System.out.println("Enter User Email or User-ID");
      member = sc.next();
      if(member.equals("-1") == false)
      {
        members_list.addID(member);
      }
    }
    manageList();
    return members_list;
  }

  private void fetchRecieverData()
  {
    dp = new DataTransferPacket("FETCHRECIEVERDATA", client, reciever.getID(), null, null); 
    initializeOutputStream(connection_main_server);
    if(sendData() == false)
    {
      System.out.println("Error Sending Data"); 
    }
    initializeInputStream(connection_main_server);
    reciever = (GroupProfile)recieveData();
  }

  public GroupProfile selectGroup()
  { 
    while(true)
    {
      System.out.println("Select Group Id");
      System.out.println("Enter 1 To Select Current Group Opened In Chat Window");
      System.out.println("Enter a Group ID");
      String local_group_id = sc.next();
      if(local_group_id.equals("-1") == false)
      {
        group_id = local_group_id;
      }
      reciever = new GroupProfile();
      reciever.setID(group_id);
      fetchRecieverData();
      
      if(reciever == null)
      {
        System.out.println("NO GROUP WITH ID " + group_id + "EXISTS");
        System.out.println("Enter 'y' To Enter a New Group ID");
        System.out.println("Enter anyother key to exit");

        char sub_option = sc.next().charAt(0);
        if(sub_option != 'y')
        {
          return null;
        }
      }
      else
      {
        break;
      }
    }
   return reciever;
    //reciever.printProfile();  --update group print profile
  }
   
}
 
