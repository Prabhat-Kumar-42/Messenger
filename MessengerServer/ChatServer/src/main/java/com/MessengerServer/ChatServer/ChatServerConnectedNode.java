package com.MessengerServer.ChatServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bson.Document;

import com.MessengerClient.DataTransferUnit.ClientProfile;
import com.MessengerClient.DataTransferUnit.DataTransferPacket;
import com.MessengerClient.DataTransferUnit.Message;
import com.MessengerServer.MongoConnect.ChatDataBase;

public class ChatServerConnectedNode implements Runnable
{
  private Socket client_connection;
  private ClientProfile client;
  private DataTransferPacket dp;
  private Message message_packet;
  private ChatDataBase database;
  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private String client_id;
  private String reciever_id;
  private String chatroom_id;
  private ChatServerReader chat_reader;
  private ExecutorService e;

  public ChatServerConnectedNode(Socket client_connection)
  {
    this.client_connection = client_connection;
    initializeIOStream();
    database = new ChatDataBase();
    chat_reader = new ChatServerReader(database, oos, ois);
    //e = Executors.newCachedThreadPool();
    //e.execute(chat_reader);
  }

  public void initializeIOStream()
  {
    try
    {
      ois = new ObjectInputStream(client_connection.getInputStream());
      oos = new ObjectOutputStream(client_connection.getOutputStream());
    }
    catch(Exception e)
    {
      e.printStackTrace(); 
    }
  }

  public void recieveData()
  {
    try
    {
      dp = (DataTransferPacket)ois.readObject();  
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
 
  public void sendData()
  {
    try
    {
      oos.writeObject(dp);  
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
   
  public void insideClass()
  {
    System.out.println("------------ChatServerConnectedNode-----------");
  }

  public void endline()
  {
    System.out.println("------------------------------------------------");
  }

  public void start()
  {
  
    while(true)
    {
      dp = null;
      while(dp == null)
      {
        recieveData();
      }
      
      client = dp.getProfile();
      reciever_id = dp.getRecieverID();
      System.out.println("ReqType " + dp.getReqType() + " Msg " + dp.getMessage()
                         + " reciever_id " + reciever_id); 
      if(reciever_id.contains("GRP") == true)
      {
        if(!database.chatRoomExists(reciever_id))
        {
          dp = new DataTransferPacket(null, client, reciever_id, "group doesn't exists", null);
          sendData();
          System.out.println("GROUP doesn't exists");
          continue;
        }
        chatroom_id = reciever_id;
      }
      else 
      {
        if(!database.checkUserExist(reciever_id))
        {
          dp = new DataTransferPacket(null, client, reciever_id, "User doesn't exists", null);    
          sendData();
          System.out.println("USR doesn't exists");
          continue;
        }

        client_id = client.getID();
        if(reciever_id.compareTo(client_id) > 0)
        {
          chatroom_id = client_id + reciever_id;
        }
        else
        {
          chatroom_id = reciever_id + client_id;
        }
        if(!database.chatRoomExists(chatroom_id))
        {
          System.out.println("Database doesn't exists");
          Set<String> members_to_add = new HashSet<>();
          members_to_add.add(client_id);
          members_to_add.add(reciever_id);
          database.createPersonalChatroom(chatroom_id, members_to_add);
          System.out.println("DataBase Created");
        }
      }
      System.out.println("chatroom_id is " + chatroom_id);
      if(dp.getReqType().equals("FETCHUNREAD"))
      {
        System.out.println("setting chat_reader values");
        chat_reader.setClientId(client_id);
        chat_reader.setChatRoomId(chatroom_id);
        chat_reader.start();
        continue;
      }

      if(dp.getReqType().equals("MSG") == false)
      {
        continue;
      }

      database.write(chatroom_id, dp.getMessagePacket());
    }
    
  }

  public void run()
  {
    start();
  }
}
