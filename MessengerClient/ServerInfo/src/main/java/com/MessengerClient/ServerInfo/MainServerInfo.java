package com.MessengerClient.ServerInfo;

public class MainServerInfo
{
  private String host;
  private int port;

  public MainServerInfo()
  {
    this.host = "127.0.0.1";
    this.port = 53753;
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
