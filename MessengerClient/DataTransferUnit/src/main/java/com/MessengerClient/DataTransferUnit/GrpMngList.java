package com.MessengerClient.DataTransferUnit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;

public class GrpMngList implements Serializable
{
  private Set<String> email;
  private Set<String> id;

  public GrpMngList()
  {
      email = new HashSet<String>(); 
      id = new HashSet<String>();
  }
  
  public Set<String> getEmailList()
  {
    return email;
  }

  public Set<String> getUserIdList()
  {
    return id;
  }

  public void addUser(String str)
  {
    if(isEmail(str))
    {
      addEmail(str);
    }
    else
    {
      addID(str);
    }
  }

  public boolean isEmail(String str)
  {
    if(str == null)
    {
      return false;
    }

    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                        "[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                        "A-Z]{2,7}$";

    Pattern pat = Pattern.compile(emailRegex);
    return pat.matcher(str).matches();
  }

  public void addEmail(String email)
  {
    if(this.email.contains(email))
    {
      System.out.println("Already Added");
      return;
    }
    this.email.add(email);
    System.out.println("Added Email : " + email);
  }
  
  public void addID(String id)
  {
    if(this.id.contains("id"))
    {
      System.out.println("Already Addded");
      return;
    }
    this.id.add(id);
    System.out.println("Added ID : " + id);
  }

  public void removeEmail(String email)
  {
    boolean flag = this.email.remove(email);
    if(flag)
    {
      System.out.println("Succefully Removed");
    }
    else
    {
      System.out.println("Not Present in Added Email List");   
    }
  }

  public void removeID(String id)
  {
    boolean flag = this.id.remove(id);
    if(flag)
    {
      System.out.println("Succefully Removed");
    }
    else
    {
      System.out.println("Not Present in Added ID List");   
    }
  }

  public void printAddedEmails()
  {
    System.out.println("--------------------Added-Emails--------------------");
    
    for(String e : email)
    {
     System.out.println(e); 
    }

    System.out.println("-----------------------------------------------------");
  }

  public void printAddedID()
  {
    System.out.println("--------------------Added-ID's-----------------------");
    
    for(String i : id)
    {
     System.out.println(i); 
    }

    System.out.println("-----------------------------------------------------");
  }
}
