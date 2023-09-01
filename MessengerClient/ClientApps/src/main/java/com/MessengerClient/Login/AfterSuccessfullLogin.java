package com.MessengerClient.Login;

//import java.io.Console;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import com.MessengerClient.DataTransferUnit.ClientProfile;
//import com.MessengerClient.DataTransferUnit.DataTransferPacket;
//import com.MessengerClient.DataTransferUnit.Message;
//import com.MessengerClient.Login;

public class AfterSuccessfullLogin
{
  private ClientProfile client;
  private Socket serverSocket;
  private Scanner sc;
//  private DataTransferPacket dp;
//  private ObjectOutputStream oos;
//  private ObjectInputStream ois;

  public AfterSuccessfullLogin(Socket serverSocket, ClientProfile client, Scanner sc)
  {
    this.sc = sc;
    this.serverSocket = serverSocket;
 /*   try
    {
      oos = new ObjectOutputStream(serverSocket.getOutputStream());
      ois = new ObjectInputStream(serverSocket.getInputStream());
    }
    catch (Exception e)
    {
      System.out.println("Error Creating Stream");
    }
   */ this.client = client;
  }
/*  
  public void updateProfile()
  {
    Scanner sc = new Scanner(System.in);
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
    sc.close();
  }
  */
/*
 * public String readPassword()
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
 */
/*
  public boolean changePassword()
  {
    Scanner sc = new Scanner(System.in);
    System.out.print("\033[H\033[2J"); //CODE TO CLEAR TERMINAL   
    System.out.println("-------------------Update-Password---------------\n");
   
    Login login_obj = new Login(serverSocket, client, false);
    ClientProfile check = login_obj.start();
    
    if(check != null)
    {
      System.out.println("Enter New Password"); 
      String password= readPassword(); 
      if(password.equals(""))
      {
        sc.close();
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
      sc.close();
      if(client != null)
      {
        return true;
      }
    }  
    sc.close();
    return false;
  }
*/
  public void start()
  {
    
    while(true)
    {
      System.out.print("\033[H\033[2J"); //CODE TO CLEAR TERMINAL   
      System.out.println("------------------Profile-Data----------------\n");
    
      client.printProfile();
    
      System.out.println();
      System.out.println();
    
      System.out.println("Enter:");
      System.out.println("\t\t\t1. Update-Profile");
      System.out.println("\t\t\t2.Change-Password");
      System.out.println("\t\t\t3. Logout");

      int option = sc.nextInt();
      sc.nextLine();

      if(option == 1)
      {
        UpdateProfile update = new UpdateProfile(serverSocket, client, sc);
        ClientProfile updatedProfile = update.updateProfile();
        if(updatedProfile == null)
        {
          System.out.println("Profile Updation Failed");
          continue;
        }
        client = updatedProfile;
        System.out.println("Profile Updated Successfully");
 //       updateProfile();
      }
      else if(option == 2)
      {
         ChangePassword change_password = new ChangePassword(serverSocket, client, sc);
         boolean isChanged = change_password.changePassword();
         if(isChanged == true)
         {
            System.out.println("Password Successfully Changed");
         }
         else
         {
            System.out.println("Password Change Failed");
         }
 //      changePassword();  
      }
      else if(option == 3)
      {
        Logout logout = new Logout(serverSocket, client);       
        if(logout.logout() == true)
        {
          System.out.println("Successfully Logged Out");
        }
      }
    }
  }

}

