package com.MessengerServer.LoginServer;

import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.MessengerClient.DataTransferUnit.ClientProfile;
import com.MessengerClient.DataTransferUnit.DataTransferPacket;
import com.MessengerServer.MongoConnect.LoginDataBase;

public class LoginServerDataBaseCheck implements Runnable
{
  private Socket client_requesting_connection;
  private DataTransferPacket dp;
  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private ClientProfile client;
  private DataOutputStream out;
  private String status;
  private LoginDataBase login_database;

  public LoginServerDataBaseCheck(Socket client_requesting_connection)
  {
    this.client_requesting_connection = client_requesting_connection;
    this.login_database = new LoginDataBase();
    intitializeInputStream(client_requesting_connection);
    intitializeOutputStream(client_requesting_connection);
    intitializeStatusStream(client_requesting_connection);
  }

  public void intitializeInputStream(Socket s)
  {
    try
    {
      ois = new ObjectInputStream(s.getInputStream());
      System.out.println("Input Stream Initialized");
    } 
    catch (Exception e) 
    {
      System.out.println("Error intitializing Input Stream");
    }
  }

  public void intitializeOutputStream(Socket s)
  {
    try
    {
      oos = new ObjectOutputStream(s.getOutputStream());
    } 
    catch (Exception e) 
    {
      System.out.println("Error intitializing Output Stream");
    }
  }
 
  public void intitializeStatusStream(Socket s)
  {
    try
    {
      out = new DataOutputStream(s.getOutputStream());
    } 
    catch (Exception e) 
    {
      System.out.println("Error intitializing Output Stream");
    }
  }
   
  public void readData()
  {
    //intitializeInputStream(client_requesting_connection); 
    try
    {
       System.out.println("At Data Read Section");
       dp = (DataTransferPacket)ois.readObject();
       System.out.println("Data Read Successfully");
    }
    catch(Exception e)
    {
      System.out.println(e.toString());
      System.out.println("Error Reading Input");
      System.out.println(e);
      e.printStackTrace();
    }
  }

  public void sendData(Object obj)
  {
    //intitializeOutputStream(client_requesting_connection); 
    try
    {
      oos.writeObject(obj);
    }
    catch(Exception e)
    {
      System.out.println("Error Sendind Output");
    }
  }
  
  public void sendStatus()
  {
    //intitializeStatusStream(client_requesting_connection);
    try
    {
      out.writeUTF(status);
    }
    catch(Exception e)
    {
      System.out.println("Error Sendind Output");
    }
  }
  
  public void start()
  {
    readData();

    String req_type = dp.getReqType();
    String email = dp.getEmail();
    String password = dp.getPass();
    System.out.println(req_type + " " + email + " " + password);    
    if(req_type.equals("LOGIN") == false  && req_type.equals("SIGNUP") == false)
    {
      sendData(null); 
    }
    
    /*if(checkValidEmail(email) == false)
    {
      status = "INVALIDEMAIL";
      sendStatus();
      return;
    }
*/
    if(req_type.equals("SIGNUP") == true)
    {
      if(password.length() < 8)
      {
        status = "SHORTPASSWORD";
      }
      else
      {
        dp.getProfile().setAccountType("PERSONAL");
        status = login_database.addAccount(dp.getProfile()); //collection_name, email, pass;
      }
      sendStatus();
      return;
    }
    
    client = login_database.checkLogin(email);
       
    if(client != null && client.getPass().equals(password))
    {
      System.out.println("Inside Client Not Null and password == password");
      client.setPass(null);
      sendData(client);
      login_database.updateStatus("online");
      System.out.println("User-ID :" + client.getID() + " Name: " + client.getName()
                        + " logged in ");
    }
    else
    {
      sendData(null);
    }
  }

  public void run()
  {
    start();
  }
}
