package com.MessengerServer.MongoConnect;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class DataBaseMain
{
  protected String uri;
  protected MongoClient mongo_client = null;
  protected String database_name; 
  protected String collection_name;
  protected MongoDatabase db;
  protected MongoCollection<Document> collection;
  
  public DataBaseMain(String collection_name)
  {
    uri = "mongodb://localhost:27017";
    database_name = "messenger";
    this.collection_name = collection_name;
  }

  public DataBaseMain(String host, String database_name, String collection_name)
  {
    this.uri = host;
    this.database_name = database_name;
    this.collection_name = collection_name;
  }

  public void connectToDataBaseServer()
  {
    mongo_client = MongoClients.create(uri);
    db = mongo_client.getDatabase(database_name);
    System.out.println("Connected to DataBase");
  }

  public void getCollection()
  {
    collection = db.getCollection(collection_name);  
  }
}
