package com.MessengerServer.ChatServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.MessengerServer.ServerInfo.ChatServerInfo;

public class ChatServer 
{
  private ServerSocket login_server_socket;
  private Socket client_requesting_connection;
  private ChatServerInfo login_server_info;
  private int port;
  private ExecutorService e;
  private ChatServerConnectedNode node_manager;
  //  private LoginServerDataBaseCheck login_database_check;

  public ChatServer()
  {
    login_server_info = new ChatServerInfo();
    this.port = login_server_info.getPort();
    e = Executors.newCachedThreadPool();
  }

  public ChatServer(int port)
  {
    login_server_info = new ChatServerInfo(port);
    this.port = port;
  }
  
  public void start()
  {
    try
    {
      login_server_socket = new ServerSocket(port);
    }
    catch(Exception e)
    {
      System.out.println("Error Starting server");
    }

    System.out.println("Chat Server Started at port " + port);
    try
    {
      while(true)
      {
        client_requesting_connection = login_server_socket.accept();
        node_manager = new ChatServerConnectedNode(client_requesting_connection);
        /*        login_database_check = new LoginServerDataBaseCheck(client_requesting_connection);*/
         e.execute(node_manager);
      }
    }
    catch (Exception e)
    {
      System.out.println(e.toString());
      System.out.println("Error Establishing Connection With Client");
    }
  }
  
  public static void main(String[] args)
  {
    ChatServer login_server = new ChatServer();
    login_server.start();
  }

}
