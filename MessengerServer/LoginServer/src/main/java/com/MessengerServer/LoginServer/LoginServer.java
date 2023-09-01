package com.MessengerServer.LoginServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.MessengerServer.ServerInfo.LoginServerInfo;

public class LoginServer 
{
  private ServerSocket login_server_socket;
  private Socket client_requesting_connection;
  private LoginServerInfo login_server_info;
  private int port;
  private ExecutorService e;
  private LoginServerDataBaseCheck login_database_check;

  public LoginServer()
  {
    login_server_info = new LoginServerInfo();
    this.port = login_server_info.getPort();
    e = Executors.newCachedThreadPool();
  }

  public LoginServer(int port)
  {
    login_server_info = new LoginServerInfo(port);
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

    System.out.println("Login Server Started at port " + port);
    try
    {
      while(true)
      {
         client_requesting_connection = login_server_socket.accept();
         login_database_check = new LoginServerDataBaseCheck(client_requesting_connection);
         e.execute(login_database_check);
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
    LoginServer login_server = new LoginServer();
    login_server.start();
  }

}
