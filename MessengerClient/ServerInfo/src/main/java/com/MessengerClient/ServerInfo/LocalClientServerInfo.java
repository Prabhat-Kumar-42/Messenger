package com.MessengerClient.ServerInfo;

public class LocalClientServerInfo 
{
  private String host;
  private int port;

  public LocalClientServerInfo()
  {
    this.host = "127.0.0.1";
    this.port = 51951;
  }

  public String getHost()
  {
    return host;
  }

  public int getPort()
  {
    return port;
  }
}
