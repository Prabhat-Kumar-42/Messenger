package com.MessengerServer.DataTransferUnit;

import java.io.Serializable;
import java.util.Date;

public class DataTransferPacket implements Serializable
{
  private String req_type;
  private String reciever_id;
  private Message message_packet;
  private Object obj;

  public DataTransferPacket(String req_type, ClientProfile client,String reciever_id,
                            String message, Object obj)
  {
      this.req_type = req_type;
      this.reciever_id = reciever_id;
      this.obj = obj;
      message_packet = new Message(client, message, new Date());
  }
  
  public String getPass()
  {
    return message_packet.getClient().getPass();
  }

  public String getEmail()
  {
    return this.message_packet.getClient().getEmail();
  }
  public String getReqType()
  {
    return req_type;
  }

  public String getRecieverID()
  {
    return reciever_id;
  }

  public String getMessage()
  {
    return message_packet.getRawMessage();
  }
  
  public String getClientId()
  {
    return message_packet.getClient().getID();
  }

  public Object getObj()
  {
    return obj;
  }

  public Date getDate()
  {
    return message_packet.getDate();
  }

  public Message getMessagePacket()
  {
    return message_packet;
  }

  public String getName()
  {
    return message_packet.getClient().getName();
  }

  public ClientProfile getProfile()
  {
    return message_packet.getClient();
  }
}
