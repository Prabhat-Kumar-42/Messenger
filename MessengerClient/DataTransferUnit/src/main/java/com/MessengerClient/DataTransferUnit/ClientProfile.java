package com.MessengerClient.DataTransferUnit;

import java.io.Serializable;

public class ClientProfile implements Serializable
{
  private String name;
  private String email;
  private String id;
  private String security_key; 
  private String account_type;
  private String password;
  //setters
 
  public ClientProfile(String name, String email, String id, String security_key)
  {
    this.name = name;
    this.email = email;
    this.id = id;
    this.security_key = security_key;
  }


  public ClientProfile(String name, String email, String id, String security_key, String password)
  {
    this.name = name;
    this.email = email;
    this.id = id;
    this.security_key = security_key;
    this.password = password;
  }

  public ClientProfile()
  {
    this.name = null;
    this.email = null;
    this.id = null;
    this.security_key = null;
    this.password = null;
  }
  
  public void setPass(String password)
  {
    this.password = password;
  }

  public void setAccountType(String account_type)
  {
    this.account_type = account_type;
  }

  public void setName(String name)
  {
      this.name = name;
  }
  
  public void setEmail(String email)
  {
      this.email = email;
  }

  public void setID(String id) 
  {
    this.id = id;
  }

  public void setSecurityKey(String security_key)
  {
    this.security_key = security_key;
  }

  //getters

  public String getPass()
  {
    return this.password;
  }

  public String getAccountType()
  {
    return this.account_type;
  }

  public String getName()
  {
      return this.name;  
  }
  
  public String getEmail()
  {
      return this.email;
  }

  public String getID() 
  {
      return this.id;
  }
  
  public String getSecurityKey()
  {
      return security_key;
  }

  public void printProfile()
  {
      System.out.println("Name is : " + name);
      System.out.println("email is : " + email);
      System.out.println("ID is : " + id);
  }
}
