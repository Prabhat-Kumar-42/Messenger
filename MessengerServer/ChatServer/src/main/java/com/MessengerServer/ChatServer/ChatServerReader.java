package com.MessengerServer.ChatServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import com.MessengerClient.DataTransferUnit.Message;
import com.MessengerServer.MongoConnect.ChatDataBase;

public class ChatServerReader implements Runnable
{
  private ChatDataBase database;
  private String chatroom_id;
  private String client_id;
  private ArrayList<Message> message_list;
  private ObjectOutputStream oos;
  private ObjectInputStream ois;

  public ChatServerReader(ChatDataBase database, String chatroom_id, String client_id,
                           ObjectOutputStream oos, ObjectInputStream ois)
  {
    this.database = database;
    this.chatroom_id = chatroom_id;
    this.client_id = client_id;
    this.oos = oos;
    this.ois = ois;
  }
 
  public ChatServerReader(ChatDataBase database, ObjectOutputStream oos, ObjectInputStream ois)
  {
    this.database = database;
    this.oos = oos;
    this.ois = ois;
  }
   
  public void setChatRoomId(String chatroom_id)
  {
    this.chatroom_id = chatroom_id;
    System.out.println("ChatReadServer : new chatroom_id is " + chatroom_id);
  }

  public void setClientId(String client_id)
  {
    this.client_id = client_id;
    System.out.println("ChatReadServer : new client_id is " + client_id);
  }

  public void sendData()
  {
    try 
    {
      oos.writeObject(message_list);   
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public void sleep_for_n_seconds(int n)
  {
    try
    {
      Thread.sleep(n*1000);
    }
    catch (Exception e) 
    {
      e.printStackTrace();
    }
  }

  public void start()
  {
//      Scanner sc = new Scanner(System.in);
  //    int temp_to_stop_thread_to_read_message_list;
//    System.out.println("Waiting for chatroom_id to set ");
  //  while(true)
    //{
      if(chatroom_id != null)
      {
        System.out.println("chatroom_id set");
        message_list = database.read(client_id, chatroom_id);
        System.out.println("Message List is : ");
        System.out.println(message_list);
        sendData();
    /*    if(message_list != null)
        {
          temp_to_stop_thread_to_read_message_list = sc.nextInt();
        }*/
        System.out.println("Message List Sent");
        /*if(message_list != null)
        {
          try
          {
            Thread.sleep(10*1000);
          } catch (Exception e) 
          {
            e.printStackTrace();
          }
      }*/
      }
    //}
  }

  public void run()
  {
    start();
  }

}
