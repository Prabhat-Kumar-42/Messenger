package com.MessengerClient.Login;

import java.io.Console;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import com.MessengerClient.DataTransferUnit.ClientProfile;
import com.MessengerClient.DataTransferUnit.DataTransferPacket;
import com.MessengerClient.LocalServer.LocalServer;

public class Login
{
  private Socket serverConnection = null;
  private String email;
  private String password;
  private DataTransferPacket dp;
  private ClientProfile client;
  private ObjectOutputStream oos;
  private ObjectInputStream ois;
  private boolean notSuppress = true;
  private Scanner sc; 
  private LocalServer local_server;  

  public void setUpStreams()
  {
    try
    {
      oos = new ObjectOutputStream(serverConnection.getOutputStream());
    }
    catch(Exception e)
    {
      System.out.println("Error Creating Output Stream");
    }
    try
    {
      ois = new ObjectInputStream(serverConnection.getInputStream());
    }
    catch(Exception e)
    {
      System.out.println("Error Creating Input Stream");
    }
  }
  
  public Login(Socket serverConnection, ClientProfile client, boolean notSuppress, Scanner sc)
  {
    this.sc = sc;
    this.serverConnection = serverConnection;
    this.client = client;
    this.notSuppress = notSuppress;
    setUpStreams();
  }
  public Login(Socket serverConnection, ObjectOutputStream oos, 
               ObjectInputStream ois, Scanner sc)
  {
    this.serverConnection = serverConnection;
    this.sc = sc;
    this.oos = oos;
    this.ois = ois;
    //setUpStreams();
  }
 
  public Login(Socket serverConnection, Scanner sc)
  {
    this.serverConnection = serverConnection;
    this.sc = sc;
    setUpStreams();
  }
  
  public String readPassword()
  {        
    Console console = System.console();
    if (console == null) 
    {
      System.out.println("Couldn't get Console instance");
      return null;
    }

    char[] passwordArray = console.readPassword();
    return new String(passwordArray);

  }
  
  public void tryLogin()
  {
    client = null;
    try
    {
      oos.writeObject(dp);      
    }
    catch(Exception e)
    {
      System.out.println("Error Sending Login Data");
    }
    try
    {
      client = (ClientProfile)ois.readObject();
    }
    catch(Exception e)
    {
      System.out.println("Error Recieveing Data");
    } 
  }

  public ClientProfile start()
  {
    while(true)
    { 
      if(notSuppress == true)
      {
        System.out.print("\033[H\033[2J"); //CODE TO CLEAR TERMINAL 
        System.out.println("-----------------------LOGIN----------------------");
        System.out.println();
        System.out.println("Enter Email : ");
        email = sc.next();
        client = new ClientProfile();
        client.setEmail(email); 
        System.out.println("Enter Password");
      }
      else
      {
        System.out.println("Enter Old Password");
      }
      
      password = readPassword();
      client.setPass(password);

      dp = new DataTransferPacket("LOGIN", client,null, null, null);
      tryLogin();
      if(client != null)
      {
        if(notSuppress == true)
        {
          System.out.println("Login Successfull");
          local_server = new LocalServer(2, client);
          local_server.start();
          try
          {
            Thread.sleep(1000);
          }
          catch(Exception e)
          {
            System.out.println("Thread Sleep Error");
          }
        }
        break;
      }
      else
      {
        if(notSuppress)
        {
          System.out.println("Login Failed : Wrong User-name or Password");
        }
        else
        {
          System.out.println("Wrong Password");
        }
        System.out.println("Enter y to continue");
        System.out.println("Enter any other key to exit");
        char sub_option = sc.next().charAt(0);

        if(sub_option == 'y')
        {
          continue;
        }
        else
        {
          break;
        }
      }
    }
    return client;
  }
}
