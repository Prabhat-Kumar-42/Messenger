package com.MessengerClient.Login;

import java.io.Console;

public class PasswordReader
{
  public String readPassword()
  {        
    Console console = System.console();
    if (console == null) 
    {
      System.out.println("Couldn't get Console instance");
      return null;
    }

    char[] passwordArray = console.readPassword();
    return new String(passwordArray);
  }
}
