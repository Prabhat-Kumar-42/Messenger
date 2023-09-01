package com.MessengerClient.ChatHandler;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.MessengerClient.DataTransferUnit.ClientProfile;
import com.MessengerClient.DataTransferUnit.DataTransferPacket;
import com.MessengerClient.DataTransferUnit.Message;

public class ChatReader implements Runnable
{
  private Socket connection_main_server;
  private ArrayList<Message> unread_message;
  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private ClientProfile reciever;
  private String reciever_id;
  private ClientProfile client;
  private DataTransferPacket dp;

  public ChatReader(Socket connection_main_server, ClientProfile client )
  {
    this.connection_main_server = connection_main_server;
    this.client = client;
  }

  public ChatReader(Socket connection_main_server, ClientProfile client,
                    ObjectOutputStream oos, ObjectInputStream ois)
  {
    this.connection_main_server = connection_main_server;
    this.client = client;
    this.oos = oos;
    this.ois = ois;
  }
  public void sendData()
  {
    try
    {
//      initializeOutputStream(connection_main_server);
      oos.writeObject(dp);
    }
    catch(Exception e)
    {
      e.printStackTrace();
      System.out.println("Error Sending Data");
    }
  }

  public void readIncomingData()
  {
    try
    {
     unread_message = (ArrayList<Message>)ois.readObject();
    }  
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public void setReciever(ClientProfile reciever)
  {
    this.reciever = reciever;
    this.reciever_id = reciever.getID();
    System.out.println("Reciever Changed");
  }
  
  public void setTitle()
  {
    System.out.print("\033[H\033[2J"); //CODE TO CLEAR TERMINAL  
    System.out.println("------------------+"+ reciever.getID() +"--------------------"); 
  }

  public void getUnread()
  {
//    System.out.println("In Unread");
//    sendData();
    try
    {
  //    System.out.println("Sending fetchMessage to main server");

      dp = new DataTransferPacket("FETCHUNREAD", client, reciever_id, null, null);
      sendData();
    //  System.out.println("Request sent to main server");
      //System.out.println("Waiting to Recieve Data");
      readIncomingData();
      //System.out.println("Data Recieved");
    //  initializeInputStream(connection_main_server);
    //  unread_message = (ArrayList<Message>)ois.readObject();
      
      if(unread_message == null)
      {
        //System.out.println("Recieved null");
        //System.out.println("Logged Out");
      }
      else
      {
  //      System.out.println("Writing Data"); 
        for(Message m : unread_message)
        {
          System.out.println(m.fetchMessage());
        }
    //    dp = new DataTransferPacket("FETCHUNREAD", client, reciever_id, null, null);
    //    sendData();
    //    readIncomingData();
//        System.out.println("Data Writing Done");
        /*
        if(unread_message != null)
        {
          System.out.println(unread_message);
        }
        */
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.out.println("Error Recieveing Unread Msgs");
    }
    //System.out.println("Returning form Unread");
  }
  
 
  public void sleep_for_n_seconds(int n)
  {
    try
    {
      Thread.sleep(n*1000);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public void readMessage()
  {
   // System.out.println("Inside ChatReader");

    if(connection_main_server == null)
    {
      System.out.println("Cant Connect to Main Server");
      return;
    }
   // System.out.println("Connected to main server");
   // System.out.println("ChatReader sleep for 2 sec");
    sleep_for_n_seconds(2);
   // System.out.println("Waiting For Reciever");
    while(reciever == null);
   // System.out.println("Reciever Recieved with id : " + reciever.getID());
    setTitle();
    //getUnread();
    
    //initializeInputStream(connection_main_server);
    
    ClientProfile old_reciever = reciever;

    while(true)
    {
      try
      {
     //   System.out.println("Checking for Reciever Change");
        if(old_reciever != null && reciever != null &&
           old_reciever.getID().equals(reciever.getID()) == false)
        {
          //clear_screen
          try
          {
       //     System.out.println("Reciever Changed");
            Thread.sleep(500);
            setTitle();
          }
          catch(Exception e)
          {
            e.printStackTrace();
         //   System.out.println("Thread Sleep Error");
          }
        }
        setTitle();
        getUnread();
        Thread.sleep(500);
//        System.out.println(((Message)ois.readObject()).fetchMessage()); 
      }
      catch(Exception e)
      {
        e.printStackTrace();
      //  System.out.println("Can't Read Messages : Input Stream Error");
      }
    }
    
  }

  public void run()
  {
    readMessage(); 
  }
}
