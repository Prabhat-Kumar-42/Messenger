package com.MessengerServer.DataTransferUnit;

import java.util.ArrayList;


public class GroupProfile extends ClientProfile 
{
  private ClientProfile admin;
  private ArrayList<ClientProfile> all_members_list;
  private GrpMngList member_to_add_list;

  public GroupProfile()
  {
    super();
    this.admin = null;
    this.all_members_list = null;
  }
  
  public void addMembersToAddList(GrpMngList member_to_add_list)
  {
    this.member_to_add_list = member_to_add_list;
  }

  public GroupProfile(String name, String email, String id, ClientProfile admin,
                      String security_key)
  {
    super(name, email, id, security_key);
    this.admin = admin;
    this.all_members_list = new ArrayList<>();
  }

  public GroupProfile(String name, String email, String id, ClientProfile admin,
                      String security_key, ArrayList<ClientProfile> members)
  {
    super(name, email, id, security_key);
    this.admin = admin;
    this.all_members_list = members;
  }

  public void setAdmin(ClientProfile admin)
  {
    this.admin = admin;
  }

  public ClientProfile getAdmin()
  {
    return this.admin;
  }
  
  public void assignList(ArrayList<ClientProfile> members)
  {
    this.all_members_list = members;
  }

  public void addList(ClientProfile user)
  {
    all_members_list.add(user);
  }
  
  public ArrayList<ClientProfile> getMembersList()
  {
    return all_members_list;
  }

  public void printListMembers()
  {
    System.out.println("Name ");

    for(ClientProfile member : all_members_list)
    {
      System.out.println(member.getName() + "\t" + member.getID());
    }
  }
  
  public void printProfile()
  {
    System.out.println("Group ID      : " + this.getID());
    System.out.println("Group Name    : " + this.getName());
    System.out.println("Group Email   : " + this.getEmail());
    System.out.println("Group Admin   : " + this.getAdmin().getName());
    System.out.println("No. of Members: " + this.all_members_list.size());
  }
}
