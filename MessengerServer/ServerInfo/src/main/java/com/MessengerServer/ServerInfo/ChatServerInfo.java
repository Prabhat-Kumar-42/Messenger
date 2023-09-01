package com.MessengerServer.ServerInfo;

public class ChatServerInfo 
{
  private int port;

  public ChatServerInfo()
  {
    this.port = 53800;
  }

  public ChatServerInfo(int port)
  {
    this.port = port;
  }

  public int getPort()
  {
    return this.port;
  }
}
