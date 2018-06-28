/**
* Generated By NPCScript :: A scripting engine created for openrsc by Zilent
*/
package org.openrsc.server.npchandler.Prince_Ali_Rescue;
import org.openrsc.server.Config;
import org.openrsc.server.event.SingleEvent;
import org.openrsc.server.logging.Logger;
import org.openrsc.server.logging.model.eventLog;
import org.openrsc.server.model.Npc;
import org.openrsc.server.model.ChatMessage;
import org.openrsc.server.model.InvItem;
import org.openrsc.server.model.MenuHandler;
import org.openrsc.server.model.World;
import org.openrsc.server.event.DelayedQuestChat;
import org.openrsc.server.model.Player;
import org.openrsc.server.model.Quest;
import org.openrsc.server.npchandler.NpcHandler;
import org.openrsc.server.util.DataConversions;
public class Hassan implements NpcHandler {
	public void handleNpc(final Npc npc, final Player owner) throws Exception {
		npc.blockedBy(owner);
		owner.setBusy(true);
		Quest q = owner.getQuest(Config.Quests.PRINCE_ALI_RESCUE);
		if(q != null) {
			if(q.finished()) {
				questFinished(npc, owner);
			} else {
				switch(q.getStage()) {
					case 0:	//Just Started
						beforeHassan(npc, owner);
						break;
					case 1:
						afterHassan(npc, owner);
						break;
					case 2:
					case 3:
					case 4:
						if(!owner.hasRecievedKeyPayment()) {
							payForBronzeKey(npc, owner);
						} else {
							bronzeKeyPaid(npc, owner);
						}
						break;
					case 5:
						finishQuest(npc, owner, owner.hasRecievedKeyPayment());
				}
			}
		} else {
			questNotStarted(npc, owner);
		}
	}

	private final void finishQuest(final Npc npc, final Player owner, final boolean alreadyPaid) {
		final String[] messages0 = {"You have the eternal gratitude of the Emir for rescuing his son", "I am authorized to pay you 700 coins"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages0, true) {
			public void finished() {
				if(alreadyPaid) {
					World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"80 was put aside for the key. that leaves 620"}) {
						public void finished() {
							owner.finishQuest(Config.Quests.PRINCE_ALI_RESCUE);
							owner.sendMessage("The chancellor pays you 620 coins");
							owner.getInventory().add(new InvItem(10, 620));
							owner.sendMessage("You have completed the quest of the Prince of Al Kharid");
							owner.sendMessage("@gre@You have gained 3 quest points!");
							owner.sendInventory();
							owner.setBusy(false);
							npc.unblock();
							Logger.log(new eventLog(owner.getUsernameHash(), owner.getAccount(), owner.getIP(), DataConversions.getTimeStamp(), "<strong>" + owner.getUsername() + "</strong>" + " has completed the <span class=\"recent_quest\">Prince Of Al Kharid</span> quest!"));
						}
					});
				} else {
					owner.finishQuest(Config.Quests.PRINCE_ALI_RESCUE);
					owner.sendMessage("The chancellor pays you 700 coins");
					owner.getInventory().add(new InvItem(10, 700));
					owner.sendMessage("You have completed the quest of the Prince of Al Kharid");
					owner.sendMessage("@gre@You have gained 3 quest points!");
					owner.sendInventory();
					owner.setBusy(false);
					npc.unblock();
					Logger.log(new eventLog(owner.getUsernameHash(), owner.getAccount(), owner.getIP(), DataConversions.getTimeStamp(), "<strong>" + owner.getUsername() + "</strong>" + " has completed the <span class=\"recent_quest\">Prince Of Al Kharid</span> quest!"));
				}
			}
		});
	}

	private final void payForBronzeKey(final Npc npc, final Player owner) {
		final String[] messages1 = {"You have proved your services useful to us", "Here is 80 coins for the work you have already done"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages1, true) {
			public void finished() {
				owner.sendMessage("The chancellor hands you 80 coins");
				owner.getInventory().add(new InvItem(10, 80));
				owner.sendInventory();
				owner.setRecievedKeyPayment(true);
				owner.setBusy(false);
				npc.unblock();
			}
		});
	}

	private final void bronzeKeyPaid(final Npc npc, final Player owner) {
		final String[] messages1 = {"Hello again adventurer", "You have recieved payment for your tasks so far", "No more will be paid until the Prince is rescued"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages1, true) {
			public void finished() {
				owner.setBusy(false);
				npc.unblock();
			}
		});
	}

	private final void afterHassan(final Npc npc, final Player owner) {
		final String[] messages1 = {"I understand the Spymaster has hired you", "I will pay the reward only when the Prince is rescued", "I can pay some expenses once the spymaster approves it"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages1, true) {
			public void finished() {
				owner.setBusy(false);
				npc.unblock();
			}
		});
	}

	private final void beforeHassan(final Npc npc, final Player owner) {
		final String[] messages0 = {"Have you found the spymaster, Osman, yet?", "You cannot proceed in your task without reporting to him"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages0, true) {
			public void finished() {
				owner.setBusy(false);
				npc.unblock();
			}
		});
	}

	private final void questFinished(final Npc npc, final Player owner) {
		final String[] messages0 = {"You are a friend of the town of Al Kharid", "If we have more tasks to complete, we will ask you", "Please, keep in contact. Good employees are not easy to find"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages0, true) {
			public void finished() {
				owner.setBusy(false);
				npc.unblock();
			}
		});
	}

	private final void questNotStarted(final Npc npc, final Player owner) {
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Greetings. I am Hassan. Chancellor to the Emir of Al Kharid"}, true) {
			public void finished() {
				World.getDelayedEventHandler().add(new SingleEvent(owner, 1500) {
					public void action() {
						final String[] options0 = {"Can I help you? You must need some help here in the desert.", "Its too hot here. How can you stand it?", "Do you mind if I just kill your Warriors?"};
						owner.setBusy(false);
						owner.sendMenu(options0);
						owner.setMenuHandler(new MenuHandler(options0) {
							public void handleReply(final int option, final String reply) {
								owner.setBusy(true);
								for(Player informee : owner.getViewArea().getPlayersInView()) {
									informee.informOfChatMessage(new ChatMessage(owner, reply, npc));
								}
								switch(option) {
									case 0:
										World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"I need the services of someone, yes.", "If you are interested, see the spymaster, Osman", "I manage the finances here, come to me when you need payment"}) {
											public void finished() {
												owner.setBusy(false);
												npc.unblock();
												owner.addQuest(Config.Quests.PRINCE_ALI_RESCUE, 3);
											}
										});
										break;
									case 1:
										World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"We manage, in our humble way. We are a wealthy town", "And we have water. It cures many thirsts"}) {
											public void finished() {
												owner.sendMessage("The chancellor hands you some water");
												World.getDelayedEventHandler().add(new SingleEvent(owner, 1000) {
													public void action() {
														owner.getInventory().add(new InvItem(50, 1));
														owner.sendInventory();
														owner.setBusy(false);
														npc.unblock();
													}
												});
											}
										});
										break;
									case 2:
										World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"You are welcome. They are not expensive.", "We have them here to stop the elite guard from being bothered", "They are a little harder to kill."}) {
											public void finished() {
												owner.setBusy(false);
												npc.unblock();
											}
										});
										break;
								}
							}
						});
					}
				});
			}
		});
	}
}