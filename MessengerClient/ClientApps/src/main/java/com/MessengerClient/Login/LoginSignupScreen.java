package com.MessengerClient.Login;

import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import com.MessengerClient.DataTransferUnit.ClientProfile;

public class LoginSignupScreen
{
  private ServerSocket ss;
  private Socket notification_page;
  private Socket userSelection_page;
  private Socket user_list;
  private Socket connection_to_server;
  
  private ClientProfile client;
  private ObjectOutputStream oos;
  private ObjectInputStream ois;
  private DataInputStream in;
  
  private void buildIOStream()
  {
    try
    {
      oos = new ObjectOutputStream(connection_to_server.getOutputStream());
      ois = new ObjectInputStream(connection_to_server.getInputStream());
      in = new DataInputStream(connection_to_server.getInputStream());
    }
    catch(Exception e)
    {
      System.out.println("Error Creating Input-Output Streams");
    }
  }

  public void sleep_for_seconds(int n)
  {
    try
    {
      Thread.sleep(n*1000);
    }
    catch (Exception e)
    {
      System.out.println("Thread Sleep Error");
    }
  }

  public void CloseConnection()
  {
     
    try 
    {
      System.out.println("Closing Connection To Server");
      connection_to_server.close();
      System.out.println("Server Connection Successfully Closed");
    }
    catch(Exception e)
    {
      System.out.println("Error with Closing connection to server");
    }

    System.out.println("Shutting Down");
    try 
    {
       notification_page.close();
    }
    catch(Exception e)
    {
      System.out.println("Internal Shutdown Error Code : NEXP");
    }
    try 
    {
      userSelection_page.close();
    }
    catch(Exception e)
    {
      System.out.println("Internal Shutdown Error Code : USEXP");
    }
    try 
    {
      user_list.close();
    }
    catch(Exception e)
    {
      System.out.println("Internal Shutdown Error Code : ULEXP");
    }
    try
    {
      ss.close();
    }
    catch(Exception e)
    {
      System.out.println("Internal Shutdown Error Code : SSHDEXP");
    }
    System.out.println("Successfully Closed All Connections");
    System.out.println("Exiting Program");
    System.out.println("Exit");
    System.exit(0);
  }

  public void serverConnect(String host, int port)
  {
    try
    {
      System.out.print("\033[H\033[2J"); //CODE TO CLEAR TERMINAL 
      System.out.println("---------------------Connecting To Server----------------");
      connection_to_server= new Socket(host, port);
      buildIOStream();
      System.out.println();
      System.out.println("---------------------------------------------------------");
      System.out.println("\t\tServer Connection Successfull");
      System.out.println("---------------------------------------------------------");  
    }
    catch(Exception e)
    {
      System.out.println("Server Connection Error");
      System.exit(0);
    }
    sleep_for_seconds(1);

    Scanner sc = new Scanner(System.in); 
    while(true)
    {
      System.out.print("\033[H\033[2J"); //CODE TO CLEAR TERMINAL 
      System.out.println("-------------------------LoginScreen-------------------------");
      System.out.println();
      System.out.println("1. Login");
      System.out.println("2. Sign-up");
      System.out.println("3. Exit");
      System.out.println();
      System.out.println("Enter option : 1, 2, 3 for respective option");
    

      int option = sc.nextInt();
      sc.nextLine();
      
      if(option == 1)
      {
        Login login_obj = new Login(connection_to_server, oos, ois, sc);
        client = login_obj.start();
        
        if(client != null)
        {
          AfterSuccessfullLogin show= new AfterSuccessfullLogin(connection_to_server, client, sc);
          show.start();
        }

      }
      else if(option == 2)
      {
        SignUp signup = new SignUp(connection_to_server, oos, ois, in, sc);
        boolean status = signup.signUp();
        if(status == true)
        {
          sleep_for_seconds(1);
        }
      }
      else if(option == 3)
      {
        break;
      }
      else
      {
        System.out.println("Invalid Option");
        System.out.println("Enter y to continue");
        System.out.println("Enter any other key to exit");
        char sub_option = sc.next().charAt(0);

        if(sub_option == 'y')
        {
          continue;
        }
        else
        {
          break;
        }
      }
    }
    
    sc.close(); 
    CloseConnection();
    System.exit(0);
  }

  public static void main(String[] args)
  {
    LoginSignupScreen main_screen = new LoginSignupScreen();
    main_screen.serverConnect("127.0.0.1", 53753);
  }
}
