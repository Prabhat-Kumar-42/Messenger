package com.MessengerClient.Utility;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import com.MessengerClient.DataTransferUnit.ClientProfile;
import com.MessengerClient.DataTransferUnit.DataTransferPacket;

public class BlinkingMessage
{
  private Socket connection_local_server;
  private String host;
  private int port;
  private String title;
  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private DataOutputStream os;
  private ClientProfile client;
  private DataTransferPacket dp;

  public BlinkingMessage(String host, int port, String title)
  {
    this.host = host;
    this.port = port;
    this.title = title;  
  }
  
  public ObjectInputStream getInputStream()
  {
    return this.ois;
  }

  public ObjectOutputStream getOutputStream()
  {
    return this.oos;
  }

  private void sleep_for_seconds(int n)
  {
    try
    {
      Thread.sleep(n*1000);
    }
    catch(Exception e)
    {
      System.out.println("Thread Sleep Error");
    }
  }
  
  private void initializeOutputStream()
  {
    try
    {
      os = new DataOutputStream(connection_local_server.getOutputStream());
    }
    catch(Exception e)
    {
      System.out.println("Error Initializing Output Stream");
    }
  }
  
  private void initializeIOStream()
  {
    try
    {
      ois = new ObjectInputStream(connection_local_server.getInputStream());
      oos = new ObjectOutputStream(connection_local_server.getOutputStream());
    }
    catch(Exception e)
    {
      System.out.println("Error Initializing Input Stream");
    }
  }
  
  public ClientProfile getClient()
  {
    return this.client;
  }

  public Socket start(String client_type)
  {
    while(true)
    {
      System.out.println("------------------"+ title +"--------------------");
      System.out.println();
      System.out.println();
      try
      {
        connection_local_server = new Socket(host, port);
        initializeIOStream();
     //   initializeOutputStream();
        
        dp = new DataTransferPacket(null, null, null, client_type, null);
        oos.writeObject(dp);

        //os.writeUTF(client_type);
        try
        {
          client = (ClientProfile)ois.readObject();
          System.out.println("InBlinking message");
          client.printProfile();
          System.out.println("\t\tLogin Successfull");
          break;
        }
        catch(Exception e)
        {
          System.out.println("Error Reading Input");
          e.printStackTrace();
        }
      }
      catch( Exception e)
      {
        sleep_for_seconds(1);
        System.out.println("\t\tAwaiting Login");
        sleep_for_seconds(1);
        System.out.print("\033[H\033[2J"); //CODE TO CLEAR TERMINAL 
      }
    }
   return connection_local_server;
  }
}
