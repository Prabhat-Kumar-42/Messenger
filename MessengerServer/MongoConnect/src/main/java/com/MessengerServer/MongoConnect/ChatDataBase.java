package com.MessengerServer.MongoConnect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.bson.Document;

import com.MessengerClient.DataTransferUnit.ClientProfile;
import com.MessengerClient.DataTransferUnit.GroupProfile;
import com.MessengerClient.DataTransferUnit.Message;
import com.mongodb.client.model.Filters;

public class ChatDataBase extends DataBaseMain
{
  private Document chatroom;
  private Document chat;
  private LoginId id;
  private LoginDataBase login_db;

  public ChatDataBase()
  {
    super("chatroom");
    connectToDataBaseServer();
    getCollection();
    id = new LoginId();
    login_db = new LoginDataBase();
  }
  
  public ChatDataBase(String host, String database_name, String collection_name)
  {
    super(host, database_name, collection_name);
    connectToDataBaseServer();
    getCollection();
    id = new LoginId();
    login_db = new LoginDataBase();
  }
  
  public boolean checkUserExist(String user_id)
  {
    ClientProfile user = new ClientProfile();
    user.setID(user_id);
    return login_db.userExist(user);
  }

  public Document findChatroom(String chatroom_id)
  {
    return collection.find(Filters.eq("_id", chatroom_id)).first();
  }

  public boolean chatRoomExists(String chatroom_id)
  {
    return findChatroom(chatroom_id) != null;
  }

  public boolean chatRoomExists(Document chatroom)
  {
    return chatroom != null;
  }
 
  public void createPersonalChatroom(String chatroom_id,  Set<String> members_to_add)
  {
    Document new_chatroom = new Document().append("_id", chatroom_id);
    collection.insertOne(new_chatroom);
    addMembers(chatroom_id, members_to_add);
  }

  public String createGroupChatRoom(ClientProfile client, String account_type, Set<String> members_to_add, GroupProfile group)
  {
    String chatroom_id = id.getNewID(account_type);
    Document new_chatroom = new Document().append("_id", chatroom_id).append("name", group.getName()).append("email", group.getEmail())
                                          .append("admin", client.getID());
    collection.insertOne(new_chatroom);
    addMembers(chatroom_id, members_to_add);
    return chatroom_id;
  }

  public void addMembers(String chatroom_id, Set<String> members_to_add)
  {
    Document group = findChatroom(chatroom_id);
    
    List<Document> members_list = (List<Document>)group.getList("members_list", Document.class);
    if(members_list == null)
    {
      members_list = new ArrayList<>();
    }
    
    Document member;
    for(String user_id : members_to_add)
    {
      member = new Document().append("_id", user_id).append("unread", 0);
      members_list.add(member);
    }
    Document update = new Document("$set", new Document("members_list", members_list));
    collection.updateOne(group, update);
  }

  public void removeMembers(String chatroom_id, Set<String> members_to_remove)
  {
    Document group = findChatroom(chatroom_id);
    List<Document> members_list = (List<Document>)group.getList("members_list", Document.class);
    if(members_list == null)
    {
      return;
    }

    for(Document member : members_list)
    {
      if(members_to_remove.contains(member.getString("_id")))
      {
        members_list.remove(member);
      }
    }

    Document update = new Document("$set", new Document("members_list", members_list));
    collection.updateOne(group, update);
  }
  
  public ArrayList<Message> read(String client_id, String chatroom_id)
  { 
    Document chatroom = findChatroom(chatroom_id);
    List<Document> chat_log = (List<Document>) chatroom.getList("chat_log", Document.class);

    if(chat_log == null)
    {
      return null;
    }

    ArrayList<Message> messages = new ArrayList<>();
    List<Document> members_list = (List<Document>) chatroom.getList("members_list", Document.class);
    int unreadCount = 0;
    
    for(Document member : members_list)
    {
      String memberId = member.getString("_id");
      if (memberId.equals(client_id))
      {
        unreadCount = member.getInteger("unread", 20);
        break;
      }
    }
    
    if(unreadCount < 20)
    {
      unreadCount = 20;
    }
  
    int messagesToRetrieve = Math.min(unreadCount, chat_log.size());
    ClientProfile sender;

    for(int i = chat_log.size() - 1; i >= chat_log.size() - messagesToRetrieve; i--) 
    {
      Document messageDoc = chat_log.get(i);
      String user_id = messageDoc.getString("user_id");
      Date date = messageDoc.getDate("date-time");
      String message = messageDoc.getString("message");
      sender = login_db.getUser(user_id); 
      Message msg = new Message(sender, message, date);
      messages.add(msg);
/*
      System.out.println("sender ID is : " + msg.getClient().getID());
      System.out.println("Date is : " + msg.getDate());
      System.out.println("Msg is : " + msg.getRawMessage());
  */
    }

    for(Document member : members_list)
    {
      String memberId = member.getString("_id");
      if(memberId.equals(client_id)) 
      {
        member.put("unread", 0);
        break;
      }
    }
    Document update = new Document("$set", new Document("members_list", members_list));
    collection.updateOne(Filters.eq("_id", chatroom_id), update);
    Collections.reverse(messages);
    return messages;
  }

  public void write(String chatroom_id, Message message)
  {
    System.out.println("In ChatroomDataBase.write, trying to write");
    System.out.println("Writing in chatroom " + chatroom_id + "Message : " + message.getRawMessage());
    chatroom = findChatroom(chatroom_id);
    ClientProfile client = message.getClient();
    
    chat = new Document().append("user_id", client.getID())
                         .append("date-time", message.getDate())
                         .append("message", message.getRawMessage());
    
    List<Document> chat_log = null;
    if(chatroom != null)
    {
      chat_log = (List<Document>)chatroom.getList("chat_log", Document.class);
    }

    if(chat_log == null)
    {
      System.out.println("chat_log null");
      System.out.println("creating new chat_log");
      chat_log = new ArrayList<>();
      System.out.println("chat_log created");
    }
    else
    {
      System.out.println("chat_log not null");
      System.out.println("Old chat_log is");
      System.out.println(chat_log);
    }

    chat_log.add(chat);
    Document update = new Document("$set", new Document("chat_log", chat_log));

    System.out.println("new chat_log is ");
    System.out.println(chat_log);
    System.out.println("Updating chat_log");

    collection.updateOne(Filters.eq("_id", chatroom_id), update);

    System.out.println("chat_log updated");
    
    System.out.println("After Update chat_log is");
    chat_log = (List<Document>)chatroom.getList("chat_log", Document.class);
    System.out.println(chat_log);

    List<Document> members_list = (List<Document>)chatroom.getList("members_list", Document.class);
    for(Document member : members_list)
    {
      if(member.getString("_id").equals(message.getClient().getID()))
      {
        continue;
      }
      int unread = member.getInteger("unread", 0);
      member.put("unread", unread+1);    
    }

    update = new Document("$set", new Document("members_list", members_list));
    collection.updateOne(Filters.eq("_id", chatroom_id), update);
  }
}
