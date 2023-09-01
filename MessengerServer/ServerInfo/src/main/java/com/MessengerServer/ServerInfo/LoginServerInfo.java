package com.MessengerServer.ServerInfo;

public class LoginServerInfo 
{
  private int port;

  public LoginServerInfo()
  {
    this.port = 53753;
  }

  public LoginServerInfo(int port)
  {
    this.port = port;
  }

  public int getPort()
  {
    return this.port;
  }
}
