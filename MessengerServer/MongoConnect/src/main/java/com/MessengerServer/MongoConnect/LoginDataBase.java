package com.MessengerServer.MongoConnect;

import org.bson.Document;

import com.MessengerClient.DataTransferUnit.ClientProfile;
import com.mongodb.client.model.Filters;

public class LoginDataBase extends DataBaseMain 
{
  private ClientProfile client;
  private Document document;
  private LoginId login_id;
  
  public LoginDataBase()
  {
    super("login_info");
    login_id = new LoginId();
    connectToDataBaseServer();
    getCollection();
  }

  public LoginDataBase(String host, String database_name, String collection_name)
  {
    super(host, database_name, collection_name);
    login_id = new LoginId();
    connectToDataBaseServer();
    getCollection();
  }

  public Document findDocument(String field, String field_value)
  {
    return collection.find(Filters.eq(field, field_value)).first();
  }

  public void updateStatus(String status)
  {
    Document update = new Document("$set", new Document("status", status));
    collection.updateOne(document, update);
  }

  public ClientProfile getUserByEmail(String email)
  {
    Document user = findDocument("email", email);
    if(user == null)
    {
      return null;
    }
    return new ClientProfile(user.getString("name"), user.getString("email"), user.getString("_id"), null);
  }


  public ClientProfile getUser(String id)
  {
    Document user = findDocument("_id", id);
    if(user == null)
    {
      return null;
    }
    return new ClientProfile(user.getString("name"), user.getString("email"), id, null);
  }

  public boolean userExist(ClientProfile user)
  {
    if(user.getID() != null)
    {
      return findDocument("_id", user.getID()) != null;
    }
    return findDocument("email", user.getEmail()) != null;
  }

  public ClientProfile checkLogin(String email)
  {
    document = findDocument("email", email);
    if(document == null)
    {
      return null;
    }
    client = new ClientProfile(document.getString("name"), document.getString("email"),
                               document.getString("_id"), document.getString("security_key"),
                               document.getString("pass"));

    return client;
  }

  public String addAccount(ClientProfile requesting_client)
    /*String email, String name, String pass, String account_type, String security_key)*/
  {
    String email = requesting_client.getEmail();
    String name = requesting_client.getName();
    String pass = requesting_client.getPass();
    String account_type = requesting_client.getAccountType();
    String security_key = requesting_client.getSecurityKey();
    
    document = findDocument("email", email);
    if(document != null)
    {
      return "EMAILEXIST";
    }
    String id = login_id.getNewID(account_type);
    document = new Document("_id", id).append("email", email).append("name", name).append("account_type", account_type).
                   append("pass", pass).append("security_key", security_key).append("status", "offline");
    collection.insertOne(document);
    return "SUCCESS";
  }
}
