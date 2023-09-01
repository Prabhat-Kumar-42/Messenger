package com.MessengerClient.ChatHandler;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.MessengerClient.DataTransferUnit.ClientProfile;
import com.MessengerClient.DataTransferUnit.DataTransferPacket;

public class RecieverCheck implements Runnable
{

  private Socket connection_local_server;
  private Socket connection_main_server;
  private ObjectOutputStream main_oos;
  private ObjectInputStream local_ois;
  private ClientProfile reciever;
  private DataTransferPacket dp;
  private ClientProfile client;
  private ChatReader chat_reader;
  private ChatWriter chat_writer;
  
  public RecieverCheck(Socket connection_local_server,Socket connection_main_server,
                      ClientProfile client,ClientProfile reciever, ChatReader chat_reader)
  {
    this.connection_local_server = connection_local_server;
    this.connection_main_server = connection_main_server;
    this.reciever = reciever;
    this.client = client;
    this.chat_reader = chat_reader;
  }
   public RecieverCheck(Socket connection_local_server,Socket connection_main_server,
                      ClientProfile client,ClientProfile reciever, ChatReader chat_reader,
                      ChatWriter chat_writer, ObjectOutputStream main_oos, ObjectInputStream local_ois)
  {
    this.connection_local_server = connection_local_server;
    this.connection_main_server = connection_main_server;
    this.reciever = reciever;
    this.client = client;
    this.chat_reader = chat_reader;
    this.chat_writer = chat_writer;
    this.local_ois = local_ois;
    this.main_oos = main_oos;
  }
  /*
  public void initializeInputStream(Socket s)
  {
    try
    {
      ois = new ObjectInputStream(s.getInputStream());
    }
    catch (Exception e)
    {
      System.out.println("Error Initializing Input Stream");
    }
  }
  public void initializeOutputStream(Socket s)
  {
    try
    {
      oos = new ObjectOutputStream(s.getOutputStream());
    }
    catch (Exception e)
    {
      System.out.println("Error Initializing Input Stream");
    }
  }
  */
  public void sendData()
  {
    try
    {
//      initializeOutputStream(connection_main_server);
      main_oos.writeObject(dp);
    }
    catch(Exception e)
    {
      System.out.println("Error Sending Data");
    }
  }

  public ClientProfile getData()
  {
    try
    {
  //    initializeInputStream(connection_local_server);
      return (ClientProfile)local_ois.readObject();
    }
    catch (Exception e)
    {
      System.out.println("Error Reading Input Stream");
      return null;
    }
  }

  public void checkRecieverSwitch()
  {
//    System.out.println("Inside RecieverCheck");

    if(connection_main_server == null)
    {
      System.out.println("Cant Connect to Main Server");
      return;
    }
    
  //  System.out.println("Connected to main server");
    //System.out.println("Current RecieverID : " + reciever.getID());
    
    chat_reader.setReciever(reciever);
    
   // System.out.println("reciever sent to chat_reader");

   // System.out.println("Connected to main server");
   // System.out.println("Current RecieverID : " + reciever.getID());

    chat_writer.setReciever(reciever);
    
   // System.out.println("reciever sent to chat_writer");


    ClientProfile old_reciever = reciever;
    dp = new DataTransferPacket("CHGCHAT", client, reciever.getID(), null, null);
    sendData();
  //  System.out.println("reciever-id and change-chat sent to main chat server");
    while(true)
    {
    //  System.out.println("now waiting for reciever change");
      while(reciever == null || old_reciever == null ||
              old_reciever.getID().equals(reciever.getID()))
      {
        if(reciever != null)
        {
          old_reciever.setID(reciever.getID());
        }
        reciever = getData();
      }
    //  System.out.println("Reciever Changed");
    //  System.out.println("New RecieverID : " + reciever.getID());
      dp = new DataTransferPacket("CHGCHAT", client, reciever.getID(), null, null);
      sendData();
    //  System.out.println("new reciever-id and change-chat sent to main chat server");
      chat_reader.setReciever(reciever);
     // System.out.println("New RecieverID sent to chat_reader");
      chat_writer.setReciever(reciever);
     // System.out.println("New RecieverID sent to chat_writer");
      }
  }

  public void run()
  {
    checkRecieverSwitch(); 
  }
}
