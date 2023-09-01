package com.MessengerServer.MongoConnect;

import org.bson.Document;

import com.mongodb.client.model.Filters;

public class LoginId extends DataBaseMain 
{
  private Document doc;
 
  public LoginId()
  {
    super("LoginId");
    connectToDataBaseServer();
    getCollection();
  }

  public LoginId(String host, String database_name)
  {
    super(host, database_name, "LoginId");
    connectToDataBaseServer();
    getCollection();
  }
  
  public LoginId(String host, String database_name, String collection_name)
  {
    super(host, database_name, collection_name);
    connectToDataBaseServer();
    getCollection();
  }

  private void updateIdNumber(int id_number)
  {
    Document update = new Document("$set", new Document("id_number", id_number));
    collection.updateOne(doc, update);
  }

  public String getNewID(String account_type)
  {
    doc = collection.find(Filters.eq("account_type", account_type)).first(); 
    String id_prefix = doc.getString("id_prefix");
    int id_number = doc.getInteger("id_number");
    String new_id = id_prefix + id_number;
    updateIdNumber(id_number+1);
    return new_id;
  }
}
