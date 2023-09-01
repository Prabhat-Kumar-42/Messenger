package com.MessengerServer.SelectManageUserServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.MessengerServer.ServerInfo.MainSelectServerInfo;

public class SelectManageUserMainServer 
{

  private ServerSocket ss;
  private Socket requesting_client;
  private MainSelectServerInfo server_info;
  private SelectServerManager node_manager;
  private int port;
  private ExecutorService e;

  public SelectManageUserMainServer()
  {
    this.server_info = new MainSelectServerInfo();    
    this.port = server_info.getPort();
    this.e = Executors.newCachedThreadPool();
  }

  public void start()
  {
    try
    {
      ss = new ServerSocket(port);
    }
    catch (Exception e) 
    {
      System.out.println("Cannot Start Server");
      e.printStackTrace();
    }

    System.out.println("SelectServer Started at port : " + port);
    try
    {
      while(true)
      {
        requesting_client = ss.accept();
        node_manager = new SelectServerManager(requesting_client);
        e.execute(node_manager);
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public static void main(String[] args)
  {
    SelectManageUserMainServer server = new SelectManageUserMainServer(); 
    server.start();
  }
}
