package com.MessengerClient.Login;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import com.MessengerClient.DataTransferUnit.ClientProfile;
import com.MessengerClient.Login.PasswordReader;
import com.MessengerClient.DataTransferUnit.DataTransferPacket;

public class ChangePassword
{
  private ClientProfile client;
  private Socket serverSocket;
  private DataTransferPacket dp;
  private ObjectOutputStream oos;
  private ObjectInputStream ois;
  private PasswordReader pass_reader;
  private Scanner sc;
  public ChangePassword(Socket serverSocket, ClientProfile client, Scanner sc)
  {
    this.serverSocket = serverSocket;
    this.pass_reader = new PasswordReader();
    this.client = client;
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
  }
  
  public boolean changePassword()
  {
    System.out.print("\033[H\033[2J"); //CODE TO CLEAR TERMINAL   
    System.out.println("-------------------Update-Password---------------\n");
   
    Login login_obj = new Login(serverSocket, client, false, sc);
    ClientProfile check = login_obj.start();
    
    if(check != null)
    {
      System.out.println("Enter New Password"); 
      String password= pass_reader.readPassword(); 
      if(password.equals(""))
      {
        System.out.println("Empty Field");
        return false;
      }
      System.out.println("Updating Password");
      dp = new DataTransferPacket("UPDATEPASS", client, null, password, null);
    
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
      if(client != null)
      {
        return true;
      }
    }  
    return false;
  }

}
