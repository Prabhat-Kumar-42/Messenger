package com.MessengerClient.DataTransferUnit;

import java.io.Serializable;
import java.util.ArrayList;
import com.MessengerClient.DataTransferUnit.ClientProfile;

public class GrpMngReturnList implements Serializable
{
  private ArrayList<ClientProfile> added_client;
  private ArrayList<ClientProfile> removed_client;
  private ArrayList<ClientProfile> client_not_exists;
  private GroupProfile group;

  public GrpMngReturnList()
  {
    this.added_client = new ArrayList<>();
    this.removed_client = new ArrayList<>();
    this.client_not_exists = new ArrayList<>();
  }

  public GrpMngReturnList(ArrayList<ClientProfile> added_client, ArrayList<ClientProfile> removed_client)
  {
      this.added_client =  added_client;
      this.removed_client = removed_client;
  }
  
  public void setGroupProfile(GroupProfile group)
  {
    this.group = group;
  }
  
  public GroupProfile getGroupProfile()
  {
    return this.group;
  }

  public ArrayList<ClientProfile> getAddedClientList()
  {
    return this.added_client;
  }

  public void insertToAddedClient(ClientProfile client)
  {
    added_client.add(client);
  }
 
  public void insertToRemovedClient(ClientProfile client)
  {
    removed_client.add(client);
  }
  public void insertToNonExistingClient(ClientProfile client)
  {
    client_not_exists.add(client);
  } 

  public String addedMembers()
  {
    if(added_client == null)
    {
      return null;
    }
   
    String console_message_start = "------------Added Users : \t\t\t\tUser Id's--------------";
    System.out.println(console_message_start);
    String str = "---------------------System Message---------------------\n\t\t\t\t\tAdded Members";
    String s = "| ";
    for(ClientProfile users : added_client)
    {
      String console_message_contiue = users.getName() + "\t\t\t\t" + users.getID();
      System.out.println(console_message_contiue);
      s += users.getName() + " | "; 
    }
    String console_message_end = "---------------------------------------------------\n";
    System.out.println(console_message_end);
    return (str + s + "\n" + console_message_end);
  }

  public String removedMembers()
  {
    if(removed_client == null)
    {
      return null;
    }
   
    String console_message_start = "------------Removed Users : \t\t\t\tUser Id's--------------";
    System.out.println(console_message_start);
    String str = "---------------------System Message---------------------\n\t\t\t\t\tRemoved Members";
    String s = "| ";
    for(ClientProfile users : removed_client)
    {
      String console_message_contiue = users.getName() + "\t\t\t\t" + users.getID();
      System.out.println(console_message_contiue);
      s += users.getName() + " | "; 
    }
    String console_message_end = "---------------------------------------------------\n";
    System.out.println(console_message_end);
    return (str + s + "\n" + console_message_end);
  }
 
  public String notAddedMembers()
  {
    if(client_not_exists == null)
    {
      return null;
    }
   
    String console_message_start = "------------Non Exisiting Clients: \t\t\t\tUser Id's--------------";
    System.out.println(console_message_start);
    String str = "---------------------System Message---------------------\n\t\t\t\t\tRemoved Members";
    String s = "| ";
    String console_message_contiue;
    String printable = null;

    for(ClientProfile users : client_not_exists)
    {
      if(users.getID() != null)
      {
        printable = users.getID();
      }
      else if(users.getEmail() != null)
      {
        printable = users.getEmail(); 
      }

      console_message_contiue = printable; 
      System.out.println(console_message_contiue);
      s += printable + " | "; 
    }
    String console_message_end = "---------------------------------------------------\n";
    System.out.println(console_message_end);
    return (str + s + "\n" + console_message_end);
  }
  /* 
  public String getMessage()
  {
    String added_members = addedMembers();
    String removed_members = removedMembers();

    if(removed_members == null)
    {
        return added_members;
    }
    if(added_members == null)
    {
        return removed_members;
    }
    return added_members + removed_members; 
  }*/
}
