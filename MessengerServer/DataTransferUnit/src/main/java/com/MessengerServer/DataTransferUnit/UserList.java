package com.MessengerServer.DataTransferUnit;

import java.io.Serializable;
import java.util.List;

import com.MessengerServer.DataTransferUnit.ClientProfile;

public class UserList implements Serializable
{
  private List<ClientProfile> clients;

  public UserList()
  {
    this.clients = null;
  }
  public UserList(List<ClientProfile> clients)
  {
    this.clients = clients;
  }
  
  public void printList()
  {
    if(clients == null)
    {
      System.out.println("Empty List");
      return;
    }
    System.out.println("----------------------User-Name\t\tUser-Id----------------");
    for(ClientProfile i : clients)
    {
      System.out.println(i.getName() + "\t\t" +i. getID());
    }
    System.out.println("-----------------------------------------------------------");
  }

}
