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

import java.util.List;

import org.l2jmobius.commons.network.PacketWriter;
import org.l2jmobius.gameserver.data.sql.CharNameTable;
import org.l2jmobius.gameserver.network.OutgoingPackets;

/**
 * @author Sdw
 */
public class BlockListPacket implements IClientOutgoingPacket
{
	private final List<Integer> _playersId;
	
	public BlockListPacket(List<Integer> playersId)
	{
		_playersId = playersId;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.BLOCK_LIST.writeId(packet);
		packet.writeD(_playersId.size());
		for (int playerId : _playersId)
		{
			packet.writeS(CharNameTable.getInstance().getNameById(playerId));
			packet.writeS(""); // memo ?
		}
		return true;
	}
}
