package com.MessengerClient.ServerInfo;

public class UserSelectServerInfo 
{
  private String host;
  private int port;

  public UserSelectServerInfo()
  {
    this.host = "127.0.0.1";
    this.port = 53195;
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
