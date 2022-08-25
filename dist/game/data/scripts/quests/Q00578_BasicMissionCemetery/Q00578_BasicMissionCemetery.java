/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package quests.Q00578_BasicMissionCemetery;

import org.l2jmobius.gameserver.enums.Faction;
import org.l2jmobius.gameserver.enums.QuestType;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;

/**
 * Basic Mission: Cemetery (578)
 * @URL https://l2wiki.com/Basic_Mission:_Cemetery
 * @author Dmitri
 */
public class Q00578_BasicMissionCemetery extends Quest
{
	// NPCs
	private static final int PENNY = 34413;
	private static final int QUTAERMASTER = 33407;
	// Rewards
	private static final int SCROLL_OF_ESCAPE_CEMETERY = 47062;
	private static final int SCROLL_OF_ESCAPE_TOWN_OF_ADEN = 48413;
	// Misc
	private static final int MIN_LEVEL = 97;
	private static final int MAX_LEVEL = 99;
	
	public Q00578_BasicMissionCemetery()
	{
		super(578);
		addStartNpc(PENNY);
		addTalkId(PENNY, QUTAERMASTER);
		addCondLevel(MIN_LEVEL, MAX_LEVEL, "nolevel.html");
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		final QuestState qs = getQuestState(player, false);
		if (qs == null)
		{
			return null;
		}
		
		String htmltext = null;
		switch (event)
		{
			case "34413-02.htm":
			case "34413-03.htm":
			{
				htmltext = event;
				break;
			}
			case "34413-04.htm":
			{
				qs.startQuest();
				htmltext = event;
				break;
			}
			case "33407-02.html":
			{
				qs.setCond(2, true);
				htmltext = event;
				break;
			}
			case "34413-07.html":
			{
				// Rewards
				giveItems(player, SCROLL_OF_ESCAPE_CEMETERY, 1);
				addExpAndSp(player, 1346064975L, 1346055);
				addFactionPoints(player, Faction.ADVENTURE_GUILD, 140); // add FP points to ADVENTURE_GUILD Faction
				qs.exitQuest(QuestType.DAILY, true);
				htmltext = event;
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		final QuestState qs = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		switch (qs.getState())
		{
			case State.CREATED:
			{
				if (npc.getId() == PENNY)
				{
					htmltext = "34413-01.htm";
				}
				break;
			}
			case State.STARTED:
			{
				switch (npc.getId())
				{
					case PENNY:
					{
						if (qs.getCond() == 1)
						{
							htmltext = "34413-04.htm";
						}
						else if (qs.getCond() == 2)
						{
							htmltext = "33509-05.html";
						}
						else if (qs.getCond() == 3)
						{
							htmltext = "34413-06.html";
						}
						break;
					}
					case QUTAERMASTER:
					{
						if (qs.getCond() == 1)
						{
							htmltext = "33407-01.html";
						}
						else if (qs.getCond() == 2)
						{
							final QuestState st = player.getQuestState("Q00758_TheFallenKingsMen");
							if ((st != null) && st.isCompleted())
							{
								qs.setCond(3, true);
								giveItems(player, SCROLL_OF_ESCAPE_TOWN_OF_ADEN, 1);
								htmltext = null;
							}
							else
							{
								htmltext = "33407-03.html";
							}
						}
						break;
					}
				}
				break;
			}
			case State.COMPLETED:
			
			{
				if (qs.isNowAvailable())
				{
					qs.setState(State.CREATED);
					htmltext = "34413-01.htm";
				}
				else
				{
					htmltext = getAlreadyCompletedMsg(player, QuestType.DAILY);
				}
				break;
			}
		}
		return htmltext;
	}
}
