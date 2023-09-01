package com.MessengerClient.Login;

import java.io.Console;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import com.MessengerClient.DataTransferUnit.ClientProfile;
import com.MessengerClient.DataTransferUnit.DataTransferPacket;

public class UpdateProfile
{
  private ClientProfile client;
  private Socket serverSocket;
  private DataTransferPacket dp;
  private ObjectOutputStream oos;
  private ObjectInputStream ois;
  private Scanner sc;
  public UpdateProfile(Socket serverSocket, ClientProfile client, Scanner sc)
  {
    this.serverSocket = serverSocket;
    this.sc = sc;
    try
    {
      oos = new ObjectOutputStream(serverSocket.getOutputStream());
      ois = new ObjectInputStream(serverSocket.getInputStream());
    }
    catch (Exception e)
    {
      System.out.println("Error Creating Stream");
    }
    this.client = client;
  }
  
  public ClientProfile updateProfile()
  {
    System.out.print("\033[H\033[2J"); //CODE TO CLEAR TERMINAL   
    System.out.println("-------------------Update-Profile---------------\n");
    System.out.println("Leave Fields Empty For No Change");
    System.out.println("Enter new name ( " + client.getName() + ") : ");
    String name = sc.next();
    if(name.equals(""))
    {
      name = client.getName();
    }
    client.setName(name);
    System.out.println("Enter new email (" + client.getEmail()+ ") : ");
    String email = sc.next();
    if(email.equals(""))
    {
      email = client.getEmail();
    }
    client.setName(name);
    client.setEmail(email);
    System.out.println("Updating Profile");
    dp = new DataTransferPacket("UPDATEPROFILE", client, null, null, null);
    
    try
    {
      oos.writeObject(dp);
    }
    catch(Exception e)
    {
      System.out.println("Data Write Error");
    }
    try
    {
      client = (ClientProfile)ois.readObject();
    }
    catch(Exception e)
    {
      System.out.println("Data Read Error");
    }
    return client;
  } 
}
