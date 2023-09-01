package com.MessengerServer.DataTransferUnit;

import java.io.Serializable;
import java.util.Stack;
import com.MessengerServer.DataTransferUnit.ClientProfile;
import com.MessengerServer.DataTransferUnit.Message;

public class ChangeChatReturn implements Serializable
{
  private Stack<Message> messages;
  
  public ChangeChatReturn(Stack<Message> messages)
  {
    this.messages = messages;
  }

  public void printMessages()
  {
    while(!messages.empty())
    {
      Message m = messages.peek();
      System.out.println(m.fetchMessage());
      messages.pop();
    }
  }
}
