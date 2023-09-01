package com.MessengerServer.DataTransferUnit;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable
{
  private ClientProfile client;
  private String message;
  private Date date;
  private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    
  public Message(ClientProfile client, String message, Date date)
  {
    this.client = client;
    this.message = message;
    this.date = date;
  }

  public String fetchMessage()
  {
   return "From " + client.getName() + " at " + sdf.format(date) + "\n\t\t\t" + message + "\n";
  }

  public String getRawMessage()
  {
    return message;
  }

  public ClientProfile getClient()
  {
    return client;
  }

  public Date getDate()
  {
    return date;
  }
}
