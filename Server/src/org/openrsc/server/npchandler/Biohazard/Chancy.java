/**
* Generated By NPCScript :: A scripting engine created for openrsc by Zilent
*/
//npc ID 505
package org.openrsc.server.npchandler.Biohazard;
import org.openrsc.server.Config;
import org.openrsc.server.event.DelayedQuestChat;
import org.openrsc.server.event.SingleEvent;
import org.openrsc.server.model.MenuHandler;
import org.openrsc.server.model.Npc;
import org.openrsc.server.model.Player;
import org.openrsc.server.model.Quest;
import org.openrsc.server.model.World;
import org.openrsc.server.npchandler.NpcHandler;


public class Chancy implements NpcHandler
 {
	int wrongPot = 0;
	int correctPot = 0;
	
	public void handleNpc(final Npc npc, final Player owner) throws Exception
	{
		npc.blockedBy(owner);
		owner.setBusy(true);
		
		Quest q = owner.getQuest(Config.Quests.BIOHAZARD);
		
		if(q != null) 
		{	
			if(owner.getQuest(Config.Quests.BIOHAZARD) != null && owner.getQuest(Config.Quests.BIOHAZARD).getStage() == 7)
			{
				questStage7(npc, owner);
			}
			else
			{
				owner.sendMessage("Chancy seems to be too busy to talk");
				owner.setBusy(false);
				npc.unblock();
			}	
		} 
		else
		{
			owner.sendMessage("Chancy seems to be too busy to talk");
			owner.setBusy(false);
			npc.unblock();
		}
	}
	
	private void questStage7(final Npc npc, final Player owner) 
	{
		
		if (owner.getX() <= 94) //change coords to varrock location
		{
			World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Hi, thanks for doing that"}, true)
			{
				public void finished()
				{
					World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"No problem"})
					{
						public void finished()
						{
							if (correctPot == 1)
							{
								World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Next time give me something more valuable", "I couldn't get anything for this on the blackmarket"})
								{
									public void finished()
									{
										owner.sendMessage("He gives you the vial of liquid honey");
										owner.getInventory().add(809, 1);
										owner.sendInventory();
										World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"That was the idea"})
										{
											public void finished()
											{
												correctPot--;
												owner.setBusy(false);
												npc.unblock();
											}
										});
									}
								});
							}
							else if (wrongPot == 1)
							{
								World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"erm... actually I sold it to the blackmarket", "I am sorry"})
								{
									public void finished()
									{
										wrongPot--;
										owner.setBusy(false);
										npc.unblock();
									}
								});
							}
							else
							{
								owner.setBusy(false);
								npc.unblock();
							}
						}
					});
				}
			});
		}
		else
		{
			if (correctPot == 1|| wrongPot == 1)
			{
				World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"I'll meet you at the dancing donkey in varrock"}, true)
				{
					public void finished()
					{
						owner.setBusy(false);
						npc.unblock();
					}
				});
			}
			else
			{
				World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Hello, I've got a vial for you to take to Varrock"}, true)
				{
					public void finished()
					{
						World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Tssch... That chemist asks for a lot for thewages he pays"})
						{
							public void finished()
							{
								World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Maybe you should ask him for more money"})
								{
									public void finished()
									{
										World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Nah...I just use my initiative here and there"})
										{
											public void finished()
											{
												World.getDelayedEventHandler().add(new SingleEvent(owner, 2000)
												{
													public void action()
													{
														final String[] options107 = {"Give him the vial of ethenea", "Give him the vial of liquid honey", "Give him the vial of sulphuric broline"};
														owner.setBusy(false);
														owner.sendMenu(options107);
														owner.setMenuHandler(new MenuHandler(options107) 
														{
															public void handleReply(final int option, final String reply)
															{
																owner.setBusy(true);
																switch(option) 
																{
																	case 0:
																		if(owner.getInventory().countId(810) == 0)
																		{
																			owner.sendMessage("You dont have a vial of ethernea to give");
																			owner.setBusy(false);
																			npc.unblock();
																		}
																		else
																		{
																			wrongPot++;
																			owner.getInventory().remove(810, 1);
																			owner.sendInventory();
																			owner.sendMessage("You give him the vial of ethernea");
																			World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Right, I'll see you later in the dancing donkey inn"})
																			{
																				public void finished()
																				{
																					World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Be lucky"})
																					{
																						public void finished()
																						{
																							owner.setBusy(false);
																							npc.unblock();
																						}
																					});
																				}
																			});
																		}
																	break;
																	case 1:
																		if(owner.getInventory().countId(809) == 0)
																		{
																			owner.sendMessage("You dont have a vial of liquid honey to give");
																			owner.setBusy(false);
																			npc.unblock();
																		}
																		else
																		{
																			correctPot++;
																			owner.getInventory().remove(809, 1);
																			owner.sendInventory();
																			owner.sendMessage("You give him the vial of liquid honey");
																			World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Ok, We're meeting at the dancing donkey in Varrock right?"})
																			{
																				public void finished()
																				{
																					World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"That's right"})
																					{
																						public void finished()
																						{
																							owner.setBusy(false);
																							npc.unblock();
																						}
																					});
																				}
																			});
																		}
																	break;
																	case 2:
																		if(owner.getInventory().countId(811) == 0)
																		{
																			owner.sendMessage("You dont have a vial of sulphuric broline to give");
																			owner.setBusy(false);
																			npc.unblock();
																		}
																		else
																		{
																			wrongPot++;
																			owner.getInventory().remove(811, 1);
																			owner.sendInventory();
																			owner.sendMessage("You give him the vial of sulphuric broline");
																			World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Ok, We're meeting at the dancing donkey in Varrock right?"})
																			{
																				public void finished()
																				{
																					World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"That's right"})
																					{
																						public void finished()
																						{
																							owner.setBusy(false);
																							npc.unblock();
																						}
																					});
																				}
																			});
																		}
																	break;
																}
															}
														});
													}
												});
											}
										});
									}
								});
							}
						});
					}
				});
			}	
		}
	}
	
}