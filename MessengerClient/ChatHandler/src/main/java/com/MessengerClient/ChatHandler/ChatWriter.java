package com.MessengerClient.ChatHandler;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import com.MessengerClient.DataTransferUnit.ClientProfile;
import com.MessengerClient.DataTransferUnit.DataTransferPacket;

public class ChatWriter implements Runnable
{
  private Socket connection_main_server;
  private DataTransferPacket dp;
  private ObjectOutputStream oos;
  private ObjectInputStream ois;
  private ClientProfile reciever;
  private ClientProfile client;
  private String reciever_id;
  private Scanner sc;
  private boolean connection_stable_main_server;

  public ChatWriter(Socket connection_main_server, ClientProfile client,
                    ObjectOutputStream oos, ObjectInputStream ois, Scanner sc)
  {
    this.connection_main_server = connection_main_server;
    this.oos = oos;
    this.ois = ois;
    this.client = client;
    this.sc = sc;
    this.connection_stable_main_server = true;
  }
  
  public void setReciever(ClientProfile reciever)
  {
    this.reciever = reciever;
    this.reciever_id = reciever.getID();
    System.out.println("reciever changed with id : " + reciever_id);
  }

  public void sendData()
  {
    try
    {
      oos.writeObject(dp);
    }
    catch(Exception e)
    {
      connection_stable_main_server = false;
      e.printStackTrace();
      System.out.println("Error Sending Data");
    }
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
  public void start()
  {
//    System.out.println("Inside Chat-Writer");
  //  System.out.println("ChatWriter sleep 2 sec");
    sleep_for_n_seconds(2);
    //System.out.println("Waiting for reciever_id");
    while(reciever_id == null);
   // System.out.println("recived reciever with id: " + reciever_id);

   // System.out.println("Above Enter-Chat");
    while(connection_stable_main_server)
    {
     // System.out.println("Inside Enter Chat");
      System.out.print("Enter Chat : ");
      String message = sc.nextLine();
      dp = new DataTransferPacket("MSG", client, reciever.getID() , message, null); 
      sendData();
    }
  }

  public void run()
  {
    start();
  }
}
