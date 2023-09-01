package com.MessengerClient.DataTransferUnit;

import java.util.List;
import java.io.Serializable;
import java.util.HashMap;
import com.MessengerClient.DataTransferUnit.ClientProfile;

public class GetNotiReturn implements Serializable
{
  private List<ClientProfile> friend_req;
  private HashMap<ClientProfile, Integer> sender_unread_list;

  public GetNotiReturn(List<ClientProfile> list, HashMap<ClientProfile, Integer> sender)
  {
    this.friend_req = list; 
    this.sender_unread_list = sender; 
  }
  
  public void printUnreadList()
  {
    System.out.println("---------------------UNREAD-MESSAGES--------------------");
    System.out.println();
    System.out.println();
    System.out.println("From User\t\t\t\tUser-ID\t\t\t\tUnreadMessage");
    for(HashMap.Entry<ClientProfile, Integer> entry : sender_unread_list.entrySet())
    {
      System.out.println(entry.getKey().getName() + "\t\t\t\t" + entry.getKey().getID()
                        + "\t\t\t\t" + entry.getValue());
    }
    System.out.println("---------------------------------------------------------"); 
  }

  public void printFriendReqList()
  {
     System.out.println("---------------------FRIEND-REQUESTS--------------------");
     System.out.println();
     System.out.println();
     System.out.println("From User\t\t\t\tUser-ID");

    for(ClientProfile i : friend_req)
    {
      System.out.println(i.getName() + "\t\t\t\t" + i.getID());
    }
    System.out.println("---------------------------------------------------------");
  }
}
