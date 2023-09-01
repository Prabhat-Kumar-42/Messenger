package  com.MessengerServer.SelectManageUserServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import com.MessengerClient.DataTransferUnit.ClientProfile;
import com.MessengerClient.DataTransferUnit.DataTransferPacket;
import com.MessengerClient.DataTransferUnit.GroupProfile;
import com.MessengerClient.DataTransferUnit.GrpMngList;
import com.MessengerClient.DataTransferUnit.GrpMngReturnList;
import com.MessengerServer.MongoConnect.ChatDataBase;
import com.MessengerServer.MongoConnect.LoginDataBase;

public class SelectServerManager implements Runnable
{
  private Socket requesting_client;
  private ObjectOutputStream oos;
  private ObjectInputStream ois;
  private DataTransferPacket dp;
  private GrpMngReturnList status;
  private ClientProfile client;
  private ChatDataBase chat_database;
  private LoginDataBase login_database;
  private GrpMngList user_list;
  private Set<String> email_list;
  private Set<String> user_id_list;
  private Set<String> members_to_add;
  private GroupProfile incoming_group_data;

  public SelectServerManager(Socket requesting_client)
  {
    this.requesting_client = requesting_client;
    this.chat_database = new ChatDataBase();
    this.login_database = new LoginDataBase();
    this.status = new GrpMngReturnList();
    this.members_to_add = new HashSet<>();
    initializeIOStream();
  }

  public void initializeIOStream()
  {
    try
    {
      this.ois = new ObjectInputStream(requesting_client.getInputStream());
      this.oos = new ObjectOutputStream(requesting_client.getOutputStream());
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public void recieveData()
  {
    try
    {
      dp = (DataTransferPacket)ois.readObject();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public void sendData()
  {
    try
    {
      oos.writeObject(status);  
    } 
    catch (Exception e) 
    {
      e.printStackTrace();
    }
  }

  public void run()
  {
    while(true)
    {
      dp = null;
      while(dp == null)
      {
        recieveData();
      }

      String req_type = dp.getReqType();
      client = dp.getProfile();
      incoming_group_data = (GroupProfile)dp.getObj();
      user_list = incoming_group_data.getMembersToAddList(); 
      email_list = user_list.getEmailList();
      user_id_list = user_list.getUserIdList();
      
      if(email_list != null)
      {
        for(String email : email_list)
        {
          ClientProfile user = login_database.getUserByEmail(email);
          if(user != null)
          {
            members_to_add.add(user.getID());
            status.insertToAddedClient(user);
          }
          else
          {
            status.insertToNonExistingClient(new ClientProfile( null, email, null, null));
          }
        }
      }
      if(user_id_list != null)
      {
        for(String user_id: user_id_list)
        {
          ClientProfile user = login_database.getUser(user_id);
          if(user != null)
          {
            members_to_add.add(user.getID());
            status.insertToAddedClient(user);
          }
          else
          {
            status.insertToNonExistingClient(new ClientProfile( null, null, user_id, null));
          }
        }
      }

      if(req_type.equals("GRPMNGCREATE"))
      {
        String chatroom_id = chat_database.createGroupChatRoom(client, "GROUP", members_to_add, incoming_group_data);
        GroupProfile created_group = new GroupProfile(incoming_group_data.getName(), incoming_group_data.getEmail(), chatroom_id, client, null, status.getAddedClientList());
        status.setGroupProfile(created_group);
      }
      else if(req_type.equals("GRPMNGADDUSERS"))
      {
        
      }
      else if(req_type.equals("GRPMNGDELUSERS"))
      {

      }
      else if(req_type.equals("GRPMNGDELGRP"))
      {
        
      }
      sendData();
    }
  }
}
