package com.MessengerClient.Login;

import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.MessengerClient.DataTransferUnit.ClientProfile;
import com.MessengerClient.DataTransferUnit.DataTransferPacket;

public class Logout
{
  private Socket serverConnection;
  private ClientProfile client;
  private DataTransferPacket dp;
  private ObjectOutputStream oos;
  private ObjectInputStream ois;
  private DataInputStream in;
  public Logout(Socket serverConnection, ClientProfile client)
  {
    this.serverConnection = serverConnection;
    this.client = client;
     try
    {
      in = new DataInputStream(serverConnection.getInputStream());
      oos = new ObjectOutputStream(serverConnection.getOutputStream());
      ois = new ObjectInputStream(serverConnection.getInputStream());
    }
    catch (Exception e)
    {
      System.out.println("Error Creating Stream");
    }
  }

  public boolean logout()
  {
    System.out.print("\033[H\033[2J"); //CODE TO CLEAR TERMINAL   
    System.out.println("---------------------------Logging-Out--------------------\n");
    dp = new DataTransferPacket("LOGOUT", client, null, null, null);
    
    try
    {
      oos.writeObject(dp);
    }
    catch(Exception e)
    {
      System.out.println("Data Write Error");
      return false;
    }
   try
    {
      return in.readUTF().equals("true");
    }
    catch(Exception e)
    {
      System.out.println("Data Read Error");
    }
    return false;
  }
}
