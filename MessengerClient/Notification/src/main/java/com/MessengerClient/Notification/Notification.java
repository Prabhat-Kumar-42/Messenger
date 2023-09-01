package com.MessengerClient.Notification;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.MessengerClient.DataTransferUnit.ClientProfile;
import com.MessengerClient.DataTransferUnit.DataTransferPacket;
import com.MessengerClient.DataTransferUnit.GetNotiReturn;
import com.MessengerClient.ServerInfo.MainServerInfo;
import com.MessengerClient.ServerInfo.LocalClientServerInfo;
import com.MessengerClient.Utility.BlinkingMessage;

public class Notification 
{
  private Socket connection_main_server;
  private Socket connection_local_server;
  private String host;
  private int port;
  private BlinkingMessage blinking_message;
  private String title;
  private ClientProfile client;
  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private DataTransferPacket dp;
  private GetNotiReturn notifications;
  private LocalClientServerInfo local_server;
  private MainServerInfo main_server;
    
  public Notification()
  {
    this.title = "Notification-Page";
    this.local_server = new LocalClientServerInfo();
    this.host = local_server.getHost();
    this.port = local_server.getPort();
    this.blinking_message = new BlinkingMessage(host, port, title);
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

  public boolean connectMainServer()
  {
    try
    {
      main_server = new MainServerInfo();
      connection_main_server = new Socket(main_server.getHost(), main_server.getPort());
      try
      {
        initializeInputStream(connection_main_server);
        initializeOutputStream(connection_main_server);
        notifications = (GetNotiReturn)ois.readObject();
        return true;
      }
      catch(Exception e)
      {
        System.out.println("Error Recieveing Data From Main Server");
      }
    }
    catch(Exception e)
    {
      System.out.println("Error Connecting to Main Server");
    }
    return false;
  }

  public void start()
  {
    System.out.print("\033[H\033[2J"); //CODE TO CLEAR TERMINAL  
    while(true)
    {
      connection_local_server = blinking_message.start("NOTIFICATION");
      System.out.print("\033[H\033[2J"); //CODE TO CLEAR TERMINAL  
      System.out.println("------------------Notification-Page--------------------");  

      client = blinking_message.getClient();

      if(connectMainServer() == false)
      {
        System.out.println("Can't Connect To Server");
        return;
      }

      while(notifications != null)
      {
        notifications.printFriendReqList();
        notifications.printUnreadList();
        dp = new DataTransferPacket("NOTIFY", client, null, null, null);
        try
        {
          oos.writeObject(dp);
        }
        catch(Exception e)
        {
          System.out.println("Error Sending Data to Main Server");
        }
        try
        {
          notifications = (GetNotiReturn)ois.readObject();
        }
        catch(Exception e)
        {
          System.out.println("Main Server Data Read Error");
        }
      }
      System.out.println("Logged Out"); 
    }
  }

  public static void main(String[] args)
  {
    Notification notification_screen = new Notification();
    notification_screen.start();
  }

}
