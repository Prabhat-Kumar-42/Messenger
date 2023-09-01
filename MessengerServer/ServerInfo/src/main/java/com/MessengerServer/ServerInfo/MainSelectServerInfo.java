package com.MessengerServer.ServerInfo;

public class MainSelectServerInfo 
{
  private String host;
  private int port;

  public MainSelectServerInfo()
  {
     this.host = "127.0.0.1";
     this.port = 54417;
  }
  
  public String getHost()
  {
    return this.host;
  }

  public int getPort()
  {
    return this.port;
  }
} 
