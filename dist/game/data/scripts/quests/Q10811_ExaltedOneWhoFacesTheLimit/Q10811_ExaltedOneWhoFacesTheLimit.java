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
package quests.Q10811_ExaltedOneWhoFacesTheLimit;

import org.l2jmobius.gameserver.enums.Movie;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.events.EventType;
import org.l2jmobius.gameserver.model.events.ListenerRegisterType;
import org.l2jmobius.gameserver.model.events.annotations.RegisterEvent;
import org.l2jmobius.gameserver.model.events.annotations.RegisterType;
import org.l2jmobius.gameserver.model.events.impl.creature.player.OnPlayerAbilityPointsChanged;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;

/**
 * Exalted, One Who Faces the Limit (10811)
 * @author Gladicek
 */
public class Q10811_ExaltedOneWhoFacesTheLimit extends Quest
{
	// Npc
	private static final int LIONEL = 33907;
	// Items
	private static final int LIONEL_HUNTER_MISSING_LIST = 45627;
	private static final int ELIKIA_CERTIFICATE = 45623;
	private static final int MYSTERIOUS_BUTLER_CERTIFICATE = 45624;
	private static final int SIR_ERIC_RODEMAI_CERTIFICATE = 45626;
	private static final int GALLADUCI_RODEMAI_CERTIFICATE = 45625;
	private static final int SPELLBOOK_DIGNITY_OF_THE_EXALTED = 45922;
	// Misc
	private static final int MIN_LEVEL = 99;
	
	public Q10811_ExaltedOneWhoFacesTheLimit()
	{
		super(10811);
		addStartNpc(LIONEL);
		addTalkId(LIONEL);
		addCondMinLevel(MIN_LEVEL, "33907-07.html");
		registerQuestItems(LIONEL_HUNTER_MISSING_LIST, ELIKIA_CERTIFICATE, MYSTERIOUS_BUTLER_CERTIFICATE, SIR_ERIC_RODEMAI_CERTIFICATE, GALLADUCI_RODEMAI_CERTIFICATE);
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
			case "33907-03.html":
			case "33907-04.html":
			{
				htmltext = event;
				break;
			}
			case "movieStart":
			{
				qs.startQuest();
				playMovie(player, Movie.SC_HONORS);
				break;
			}
			case "33907-05.html":
			{
				qs.setCond(2);
				giveItems(player, LIONEL_HUNTER_MISSING_LIST, 1);
				htmltext = event;
				break;
			}
			case "33907-09.html":
			{
				if (qs.isCond(3))
				{
					giveItems(player, SPELLBOOK_DIGNITY_OF_THE_EXALTED, 1);
					qs.exitQuest(false, true);
					htmltext = event;
				}
				break;
			}
			case "SUBQUEST_FINISHED_NOTIFY":
			{
				if (hasQuestItems(player, ELIKIA_CERTIFICATE, MYSTERIOUS_BUTLER_CERTIFICATE, SIR_ERIC_RODEMAI_CERTIFICATE, GALLADUCI_RODEMAI_CERTIFICATE) && (player.getAbilityPointsUsed() >= 16))
				{
					qs.setCond(3, true);
				}
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
				htmltext = (player.getLevel() >= MIN_LEVEL) && (player.getNobleLevel() > 0) ? "33907-01.htm" : "33907-07.htm";
				break;
			}
			case State.STARTED:
			{
				if (qs.isCond(1))
				{
					htmltext = "33907-02.html";
				}
				else if (qs.isCond(2))
				{
					htmltext = "33907-06.html";
				}
				else if (qs.isCond(3))
				{
					htmltext = "33907-08.html";
				}
				break;
			}
			case State.COMPLETED:
			{
				htmltext = getAlreadyCompletedMsg(player);
				break;
			}
		}
		return htmltext;
	}
	
	@RegisterEvent(EventType.ON_PLAYER_ABILITY_POINTS_CHANGED)
	@RegisterType(ListenerRegisterType.GLOBAL_PLAYERS)
	private void OnPlayerAbilityPointsChanged(OnPlayerAbilityPointsChanged event)
	{
		notifyEvent("SUBQUEST_FINISHED_NOTIFY", null, event.getPlayer());
	}
}