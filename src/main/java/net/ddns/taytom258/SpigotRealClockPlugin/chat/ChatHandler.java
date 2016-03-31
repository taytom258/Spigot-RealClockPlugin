/**
 * 
 */
package net.ddns.taytom258.SpigotRealClockPlugin.chat;

import org.bukkit.entity.Player;

/**
 * Handles various chat related methods
 * 
 * @author taytom258
 *
 */
public class ChatHandler {

	/**
	 * Sends message to player with color
	 * 
	 * @param player
	 * @param color number in minecraft color code
	 * @param message to send
	 * @see Player
	 */
	public static void sendPlayer(Player player, String color, String message){
		player.sendMessage("§".concat(color).concat(message));
	}
}
