package com.MessengerClient.LocalServer;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.MessengerClient.ServerInfo.LocalClientServerInfo;
import com.MessengerClient.DataTransferUnit.ClientProfile;

public class LocalServer 
{
  private ServerSocket ss;
  private Socket s;
  private ClientProfile client;
  private int no_of_sockets;
  private ExecutorService cached_thread_pool;
  private int port;
  private ClientProfile reciever;
  private HashMap<String, LocalNodeManager> connectedclients;
  private LocalClientServerInfo local_server;

  public LocalServer(int no_of_sockets, ClientProfile client)
  {
    local_server = new LocalClientServerInfo();
    this.port = local_server.getPort();
    this.client = client;
    this.no_of_sockets = no_of_sockets;
    this.connectedclients = new HashMap<>();
    cached_thread_pool = Executors.newCachedThreadPool();
  }

  public LocalServer(int no_of_sockets, ClientProfile client, int port)
  {
    this.port = port;
    this.client = client;
    this.no_of_sockets = no_of_sockets;
    this.connectedclients = new HashMap<>();
    cached_thread_pool = Executors.newCachedThreadPool();
  }

  public void setReciever(ClientProfile reciever)
  {
    this.reciever = reciever;
  }
  
  public void sendReciever(String client_type) //send userid to chatroom
  {
    System.out.println(connectedclients.keySet());

    if(connectedclients.containsKey(client_type) == false)
    {
      System.out.println("Undefined Client Type");
      return;
    }
    connectedclients.get(client_type).sendReciever(reciever);
  }

  public void start()
  {
    try
    {
      ss = new ServerSocket(port);
    }
    catch( Exception e )
    {
      System.out.println("Error Starting LocalServer");
      System.out.println();
      e.printStackTrace();
      System.out.println("--------------------------------------");
    }

    for(int i = 0; i < no_of_sockets; i++)
    {
      try
      {
          synchronized(this)
          {
            s = ss.accept();
            LocalNodeManager node_manager = new LocalNodeManager(s, client, connectedclients);
            node_manager.setNodeManager(node_manager); 
            cached_thread_pool.execute(node_manager);
          }
      }
      catch( Exception e)
      {
        e.printStackTrace();
        System.out.println("Error Connecting to Client");
      }
    }
  }
}
