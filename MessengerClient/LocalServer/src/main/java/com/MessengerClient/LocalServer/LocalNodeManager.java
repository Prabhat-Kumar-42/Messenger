package com.MessengerClient.LocalServer;

import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import com.MessengerClient.DataTransferUnit.ClientProfile;
import com.MessengerClient.DataTransferUnit.DataTransferPacket;

public class LocalNodeManager implements Runnable 
{
  private Socket s;
  private ClientProfile client;
  private ObjectOutputStream oos;
  private ObjectInputStream ois;
  private LocalNodeManager node_manager;
  private HashMap<String, LocalNodeManager> connectedclients; 
  //private DataInputStream in;
  private String client_type;

  public LocalNodeManager(Socket s, ClientProfile client,
         HashMap<String, LocalNodeManager> connectedclients)
  {
    this.s = s;
    this.client = client;
    this.connectedclients = connectedclients;
    initializeIOStream();
  }
  public LocalNodeManager(Socket s, ClientProfile client)
  {
    this.s = s;
    this.client = client;
    initializeIOStream();
  }

  public void setNodeManager(LocalNodeManager node_manager)
  {
    this.node_manager = node_manager;
  }
 
  public void initializeIOStream()
  {
    try
    {
      oos = new ObjectOutputStream(s.getOutputStream());
      ois = new ObjectInputStream(s.getInputStream());
      //in = new DataInputStream(s.getInputStream());
    }
    catch( Exception e)
    {
      System.out.println("Error in IO Stream Creation");
    }
  }

  public void sendObject(ClientProfile obj)
  {
    try
    {
      oos.writeObject(obj);
    }
    catch(Exception e)
    {
      e.printStackTrace();
      System.out.println("Error In Sending Data");
    }
  }
  
  public void sendReciever(ClientProfile reciever)
  {
    sendObject(reciever);
  }

  public void manageNode()
  {
    try
    {
      DataTransferPacket dp = (DataTransferPacket)ois.readObject();
      client_type = dp.getMessage();
      //client_type = in.readUTF(); 
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    connectedclients.put(client_type, node_manager);   
    sendObject(client);
  }

  public void run()
  {
    manageNode();
  }
}
