package com.MessengerServer.DataTransferUnit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.MessengerServer.DataTransferUnit.ClientProfile;

public class GrpMngReturnList implements Serializable
{
  private List<ClientProfile> added_client;
  private List<ClientProfile> removed_client;


  public GrpMngReturnList(ArrayList<ClientProfile> added_client, ArrayList<ClientProfile> removed_client)
  {
      this.added_client =  added_client;
      this.removed_client = removed_client;
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
  }
}
