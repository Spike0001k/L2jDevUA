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
package org.l2jmobius.gameserver.network.serverpackets;

import org.l2jmobius.commons.network.PacketWriter;
import org.l2jmobius.gameserver.data.xml.RecipeData;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.RecipeHolder;
import org.l2jmobius.gameserver.network.OutgoingPackets;
import org.l2jmobius.gameserver.network.PacketLogger;

public class RecipeItemMakeInfo implements IClientOutgoingPacket
{
	private final int _id;
	private final Player _player;
	private final Boolean _success;
	private final long _offeringMaximumAdena;
	
	public RecipeItemMakeInfo(int id, Player player, boolean success, long offeringMaximumAdena)
	{
		_id = id;
		_player = player;
		_success = success;
		_offeringMaximumAdena = offeringMaximumAdena;
	}
	
	public RecipeItemMakeInfo(int id, Player player, boolean success)
	{
		_id = id;
		_player = player;
		_success = success;
		_offeringMaximumAdena = 0;
	}
	
	public RecipeItemMakeInfo(int id, Player player, long offeringMaximumAdena)
	{
		_id = id;
		_player = player;
		_success = null;
		_offeringMaximumAdena = offeringMaximumAdena;
	}
	
	public RecipeItemMakeInfo(int id, Player player)
	{
		_id = id;
		_player = player;
		_success = null;
		_offeringMaximumAdena = 0;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		final RecipeHolder recipe = RecipeData.getInstance().getRecipe(_id);
		if (recipe != null)
		{
			OutgoingPackets.RECIPE_ITEM_MAKE_INFO.writeId(packet);
			packet.writeD(_id);
			packet.writeD(recipe.isDwarvenRecipe() ? 0 : 1); // 0 = Dwarven - 1 = Common
			packet.writeD((int) _player.getCurrentMp());
			packet.writeD(_player.getMaxMp());
			packet.writeD(_success == null ? -1 : (_success ? 1 : 0)); // item creation none/success/failed
			packet.writeC(_offeringMaximumAdena > 0 ? 1 : 0); // Show offering window.
			packet.writeQ(_offeringMaximumAdena); // Adena worth of items for maximum offering.
			return true;
		}
		PacketLogger.info("Character: " + _player + ": Requested unexisting recipe with id = " + _id);
		return false;
	}
}
