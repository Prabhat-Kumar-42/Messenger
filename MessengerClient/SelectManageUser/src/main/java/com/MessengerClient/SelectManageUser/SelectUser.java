package com.MessengerClient.SelectManageUser;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import com.MessengerClient.DataTransferUnit.ClientProfile;
import com.MessengerClient.DataTransferUnit.DataTransferPacket;
import com.MessengerClient.LocalServer.LocalServer;
import com.MessengerClient.ServerInfo.UserSelectServerInfo;

public class SelectUser
{
  private LocalServer local_server;
  private UserSelectServerInfo local_server_info;
  private int port;
  private String host;
  private ClientProfile client;
  private ClientProfile reciever;
  private Scanner sc;
  private DataTransferPacket dp;

  public SelectUser(Scanner sc)
  {
    local_server_info = new UserSelectServerInfo();
    this.host = local_server_info.getHost();
    this.port = local_server_info.getPort();
    this.sc = sc;
    local_server = new LocalServer( 1, client, port);
    local_server.start();
  }


  public SelectUser(ClientProfile client, Scanner sc)
  {
    local_server_info = new UserSelectServerInfo();
    this.client = client;
    this.host = local_server_info.getHost();
    this.port = local_server_info.getPort();
    this.sc = sc;
    local_server = new LocalServer( 1, client, port);
    local_server.start();
  }
  
  public void setClient(ClientProfile client)
  {
    this.client  = client;
  }

  public ClientProfile getReciever()
  {
    return reciever;  
  }

/*  public void fetchRecieverData()
  {
    dp = new DataTransferPacket("FETCHRECIEVERDAT", client, reciever.getID(), null, null);
  }
*/
  public void switchUser(String reciever_id)
  {
    reciever = new ClientProfile();
    reciever.setID(reciever_id);
    //fetchRecieverData();
//    if(reciever == null)
  //  {
    //  System.out.println("NO USER WITH USER-ID " + reciever_id + " EXISTS");
      //return;
    //}
    local_server.setReciever(reciever);
    local_server.sendReciever("CHATHANDLER");
  }

  public void start()
  {
    System.out.print("Enter Reciever ID : ");
    String reciever_id = sc.next();
    switchUser(reciever_id);
 }
}
