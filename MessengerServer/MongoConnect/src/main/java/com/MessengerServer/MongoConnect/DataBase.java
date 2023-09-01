package com.MessengerServer.MongoConnect;

import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.MessengerServer.DataTransferUnit.ClientProfile;

public class DataBase 
{
    private String uri ;//= "mongodb://localhost:27017";
    protected MongoClient mongoClient = null; 
    protected ClientProfile client;
    protected ClientProfile friend;
    protected String databaseName;
    protected String fromCollection;
    protected MongoDatabase db = null;
    protected MongoCollection<Document> collection = null; 
    //protected NotificationPage np = null;  

    public DataBase(String host, ClientProfile client, ClientProfile friend, String databaseName
                    /*, NotificationPage np*/)
    {
        this.uri = host;
        this.client = client;
        this.databaseName = databaseName;
    //  this.np = np;
        try 
        {
            mongoClient = MongoClients.create(uri);
            System.out.println("Connected to Database");
            db = mongoClient.getDatabase(databaseName);
            List<String> collectionNames = db.listCollectionNames().into(new ArrayList<String>()); 
        
            collection = db.getCollection(fromCollection);
            String key = client.getEmail()+friend.getEmail();
            if(collectionNames.contains(key))
            {
                fromCollection = key;
            }
            else
            {
                fromCollection = friend.getEmail()+client.getEmail();
            }
        }
       catch (Exception e)
        {
            System.out.println("Database Connecion Error");
        }
    }

    public void closeConnection()
    {
        try
        {
          if(mongoClient != null)
          {
            mongoClient.close();
          }
        }
        catch (Exception e)
        {
            System.out.println("Database connection Close Error");
        }
    }
 
    public List<String> readFromDB(int number_of_message_to_read)
    {
        if(mongoClient == null)
        {
            return null;
        }
        
        if(collection == null)
        {
            return null;
        }
        List<Document> result = collection.find().sort(new Document("date", 1)).projection(Projections.include("from", "message"))
                      .limit(number_of_message_to_read).into(new ArrayList<Document>());
        if(result == null || result.size() == 0)
        {
            return null;
        }
        
        Collections.reverse(result);
        
        List<String> msgList = new ArrayList<String>();
      
        for(Document doc : result)
        {
            msgList.add(doc.getString("from") + doc.getString("message"));
        }
        return msgList;
   }
   
    public void writeToDB(String user, String message)
    {
        try
        {
            Document doc = new Document().append("from", user).append("message",message)
                                    .append("date", new Date());
    
            collection.insertOne(doc);
            System.out.println("data entered successufylly");
        }
        catch (Exception e)
        {
            System.out.println("Data Write Error");
        }
    }
}
