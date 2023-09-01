package com.MessengerClient.Login;

import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import com.MessengerClient.DataTransferUnit.ClientProfile;
import com.MessengerClient.DataTransferUnit.DataTransferPacket;

public class SignUp
{
  private Socket serverConnection;
  private DataTransferPacket dp;
  private ObjectOutputStream oos;
  private ObjectInputStream ois;
  private DataInputStream in;
  private PasswordReader password_reader;
  private ClientProfile client;
  private String email;
  private String password;
  private String name;
  private String status;
  private Scanner sc;

  public SignUp(Socket serverConnection, ObjectOutputStream oos,
                ObjectInputStream ois, DataInputStream in, Scanner sc)
  {
    this.sc = sc;
    this.serverConnection = serverConnection;
    this.oos = oos;
    this.ois = ois;
    this.in = in;
    this.password_reader = new PasswordReader();
    this.status = "";
    this.client = new ClientProfile();
 //   buildIOStream();
  }

  public SignUp(Socket serverConnection, Scanner sc)
  {
    this.sc = sc;
    this.serverConnection = serverConnection;
    this.password_reader = new PasswordReader();
    this.status = "";
    this.client = new ClientProfile();
 //   buildIOStream();
  }

  public boolean signUp()
  {
    while(true)
    {
      System.out.print("\033[H\033[2J"); //CODE TO CLEAR TERMINAL 
      System.out.println("----------------------Sign-Up------------------------");
      System.out.println("Enter email: ");
      email = sc.next();
      System.out.println("Enter Name: ");
      name = sc.next();
      System.out.println("Enter Password");
      password = password_reader.readPassword();
      client.setName(name);
      client.setEmail(email);
      client.setPass(password);

      dp = new DataTransferPacket("SIGNUP", client, null, null, null);
      
      try
      {
        oos.writeObject(dp);
        System.out.println("data sent");
      }
      catch(Exception e)
      {
        System.out.println("Data Write Error");
      }
      
      try
      {
        status = in.readUTF();
        System.out.println("Data read");
      }
      catch(Exception e)
      {
        System.out.println("Data Read Error");
      }
     
      if(status.equals("SUCCESS"))
      {
        System.out.println("Sign-up Success");
        break;
      }
      else 
      {
        System.out.println("Sign-up Failed");
        if(status.equals("EMAILEXIST"))
        {
          System.out.println("Email Already Exists");
        }
        else if(status.equals("INVALIDEMAIL"))
        {
          System.out.println("Please Provide a vaild email");
        }
        else if(status.equals("SHORTPASSWORD"))
        {
          System.out.println("Password Too Short");
          
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

    if(status.equals("SUCCESS"))
    {
      return true;
    }
    return false;
  }
}
