package com.MessengerClient.ChatHandler;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.MessengerClient.DataTransferUnit.ClientProfile;
import com.MessengerClient.DataTransferUnit.DataTransferPacket;
import com.MessengerClient.ServerInfo.MainChatServerInfo;
import com.MessengerClient.ServerInfo.UserSelectServerInfo;
import com.MessengerClient.Utility.BlinkingMessage;

public class ChatHandlerMain
{
  private Socket connection_local_server;
  private Socket connection_main_server;
  private BlinkingMessage blinking_message;
  private String title;
  private UserSelectServerInfo local_server;
  private MainChatServerInfo main_server;
  private String host;
  private int port;
  private ObjectOutputStream main_oos, local_oos;
  private ObjectInputStream main_ois, local_ois;
  private ClientProfile client;
  private ClientProfile reciever;
  private ChatReader chat_reader;
  private RecieverCheck reciever_check; 
  private ExecutorService e;
  private Scanner sc;
  private DataTransferPacket dp; 
  private ChatWriter chat_writer;

  public ChatHandlerMain()
  {
    this.title = "Chat-Page";
    this.local_server = new UserSelectServerInfo();
    this.host = local_server.getHost();
    this.port = local_server.getPort();
    this.blinking_message = new BlinkingMessage(host, port, title);
    this.e = Executors.newCachedThreadPool();
    this.sc = new Scanner(System.in);
  }
  
  private void initializeLocalIOStreams()
  {
    try
    {
      local_ois = new ObjectInputStream(connection_local_server.getInputStream());
      local_oos = new ObjectOutputStream(connection_local_server.getOutputStream());
    }
    catch(Exception e)
    {
      e.printStackTrace();
      System.out.println("Error Initializing Input Stream");
    }
  }

  private void initializeMainIOStreams()
  {
    try
    {
      main_oos = new ObjectOutputStream(connection_main_server.getOutputStream());
      main_ois = new ObjectInputStream(connection_main_server.getInputStream());
    }
    catch(Exception e)
    {
      e.printStackTrace();
      System.out.println("Error Initializing Input Stream");
    }
  }


  /*private void initializeInputStream(Socket s)
  {
    try
    {
      ois = new ObjectInputStream(s.getInputStream());
    }
    catch(Exception e)
    {
      System.out.println("Error Initializing Input Stream");
    }
  }

  private void initializeOutputStream(Socket s)
  {
    try
    {
      oos = new ObjectOutputStream(s.getOutputStream());
    }
    catch(Exception e)
    {
      System.out.println("Error Initializing Output Stream");
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
      e.printStackTrace();
      System.out.println("Error Sending Data");
    }
  }

  public boolean connectMainServer()
  {
    try
    {
      if(connection_main_server == null)
      {
        main_server = new MainChatServerInfo();
        connection_main_server = new Socket(main_server.getHost(), main_server.getPort());
    //    System.out.println("Successfully connected to main chat server");
//        System.out.println("Initializing Streams");
        initializeMainIOStreams();
  //      System.out.println("Streams Initialized");
        }
    }
    catch(Exception e)
    {
      e.printStackTrace();
      System.out.println("Error Connecting to Main Server");
      return false;
    }
    return true;
  }

  public ClientProfile recieveData()
  {
      try
      {
        System.out.println("waiting to recieve data");
      //initializeInputStream(connection_local_server);
        return (ClientProfile)local_ois.readObject();
      }
      catch(Exception e)
      {
        e.printStackTrace();
        System.out.println("Error Reading Input");
        return null;
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
    System.out.print("\033[H\033[2J"); //CODE TO CLEAR TERMINAL  
    while(true)
    {
      if(connection_local_server == null)
      {
        connection_local_server = blinking_message.start("CHATHANDLER");
        client = blinking_message.getClient();
        client.printProfile();
        local_ois = blinking_message.getInputStream();
        local_oos = blinking_message.getOutputStream();
       // System.out.println(" " + (local_oos == null) + " " + (local_ois == null ));
        //initializeLocalIOStreams();
      } 

      if(connectMainServer() == false)
      {
        continue;
      }
      
      //System.out.print("\033[H\033[2J"); //CODE TO CLEAR TERMINAL  
     // System.out.println("------------------Chat-Page--------------------");  
      
      reciever = recieveData();
      if(reciever == null)
      {
        continue;
      }
      //System.out.println("Data Recieved");
      //System.out.println("Recieved reciever-id : "+ reciever.getID());
      sleep_for_n_seconds(2); 
      
      chat_writer = new ChatWriter(connection_main_server, client, main_oos, main_ois, sc);
      e.execute(chat_writer);
     // System.out.println("Past ChatWriter");
 //     sleep_for_n_seconds(1);
      chat_reader = new ChatReader(connection_main_server, client, main_oos, main_ois);
      e.execute(chat_reader);
      //System.out.println("Past ChatReader");
   //   sleep_for_n_seconds(1);
      reciever_check = new RecieverCheck(connection_local_server, connection_main_server,
                                         client, reciever, chat_reader, chat_writer, main_oos,
                                         local_ois);
      e.execute(reciever_check);
     // System.out.println("Past Reciever_Check");
   }
  }

  public static void main(String[] args)
  {
    ChatHandlerMain obj = new ChatHandlerMain();
    obj.start();
  }
}
