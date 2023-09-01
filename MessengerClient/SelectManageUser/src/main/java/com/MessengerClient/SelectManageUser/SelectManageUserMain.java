package com.MessengerClient.SelectManageUser;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import com.MessengerClient.DataTransferUnit.ClientProfile;
import com.MessengerClient.ServerInfo.MainServerInfo;
import com.MessengerClient.ServerInfo.LocalClientServerInfo;
import com.MessengerClient.ServerInfo.MainSelectServerInfo;
import com.MessengerClient.Utility.BlinkingMessage;

public class SelectManageUserMain 
{
  private Socket connection_local_server;
  private Socket connection_main_server;
  private String host;
  private int port;
  private BlinkingMessage blinking_message;
  private String title;
  private ClientProfile client;
  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private LocalClientServerInfo local_server_info;
  private SelectUser select_user;
  private ManageGroup manage_group;
  private MainSelectServerInfo main_server;
  private CreateGroup create_group;
  private Scanner sc;
  
  public SelectManageUserMain()
  {
    this.sc = new Scanner(System.in);
    this.title = "Chat-Manage-Page";
    this.local_server_info = new LocalClientServerInfo();
    this.host = local_server_info.getHost();
    this.port = local_server_info.getPort();
    this.main_server = new MainSelectServerInfo();
    this.blinking_message = new BlinkingMessage(host, port, title);
  }

  
  private void initializeIOStream()
  {
    try
    {
      oos = new ObjectOutputStream(connection_main_server.getOutputStream());
      ois = new ObjectInputStream(connection_main_server.getInputStream());
    }
    catch(Exception e)
    {
      System.out.println("Error Initializing Input Stream");
    }
  }

 
  public boolean connectMainServer()
  {
    try
    {
      if(connection_main_server == null)
      {
        connection_main_server = new Socket(main_server.getHost(), main_server.getPort());
        initializeIOStream();
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return false;
    }
    return true;
  }
  
  public void start()
  {
    System.out.print("\033[H\033[2J"); //CODE TO CLEAR TERMINAL    
    while(true)
    {
      if(connection_local_server == null)
      {
        connection_local_server = blinking_message.start("SELECTMANAGEUSER");
        client = blinking_message.getClient();
        select_user = new SelectUser(client, sc);
        System.out.print("\033[H\033[2J"); //CODE TO CLEAR TERMINAL  
        System.out.println("------------------Chat-Selection-Manage-Page--------------------");
        client.printProfile();
      }      
      if(connectMainServer() == false)
      {
        System.out.println("Can't Connect To Server");
        continue;
      }
   
      System.out.println("Select Respective Option");
      System.out.println("1. Select User/Group");
      System.out.println("2. Create Group");
      System.out.println("3. Manage Group");

      int option = sc.nextInt();
      sc.nextLine();

      if(option == 1)
      {
        select_user.setClient(client);
        select_user.start();
      }
      else if(option == 3)
      {
        if(connectMainServer() == true)
        {
          if(manage_group == null)
          {
            manage_group = new ManageGroup(connection_main_server, client, select_user, sc);
          }
          manage_group.start();
        }
      }
      else if(option == 2)
      {
        if(create_group == null)
        {
          create_group = new CreateGroup(connection_main_server, client, oos, ois, sc);
        }
        if(create_group.start() == false)
        {
          continue;
        }
        System.out.println("Enter any key to exit");
        System.out.println("Switch To Created Group ? (Enter : y)");
        char sub_options = sc.next().charAt(0);
        if(sub_options == 'y')
        {
          if(select_user == null)
          {
             select_user = new SelectUser(client, sc);
          }
          select_user.start();
        }
      }
      else
      {
        System.out.println("Invalid Option");
        System.out.println("Retry");
        continue;
      }
    }
  }

  public static void main(String[] args)
  {
    SelectManageUserMain obj = new SelectManageUserMain();
    obj.start();
  }

}
