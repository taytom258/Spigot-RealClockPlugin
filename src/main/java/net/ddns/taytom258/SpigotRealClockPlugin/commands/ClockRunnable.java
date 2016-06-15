/**
 * 
 */
package net.ddns.taytom258.SpigotRealClockPlugin.commands;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.lang.StringUtils;

import com.maxmind.geoip2.exception.GeoIp2Exception;

import net.ddns.taytom258.SpigotRealClockPlugin.chat.ChatHandler;
import net.ddns.taytom258.SpigotRealClockPlugin.config.Configuration;
import net.ddns.taytom258.SpigotRealClockPlugin.geoIP.GeoIP;
import net.ddns.taytom258.SpigotRealClockPlugin.http.TimeZoneDB;
import net.ddns.taytom258.SpigotRealClockPlugin.logger.LogHandler;

/**
 * @author taytom258
 *
 */
public class ClockRunnable{
		
	public static void lookup() {
	
		String latlng = null, lat = null, lng = null, time = "Error";
		
		//Run GeoIP Lookup logic
		try {
			if (Configuration.develop) {
				latlng = GeoIP.getLocation("70.170.27.129");
			} else {
				latlng = GeoIP.getLocation(ClockCommand.player.getAddress().getHostString());
			}
		} catch (IOException e) {
			LogHandler.warning("", e);
		} catch (GeoIp2Exception e) {
			LogHandler.warning("", e);
		}
		
		//Process GeoIP Output
		String[] newlatlng = StringUtils.split(latlng, ';');
		for (int i = 0; i < newlatlng.length; i++) {
			if (i == 0) {
				lat = newlatlng[i];
			} else {
				lng = newlatlng[i];
			}
		}
		
		//Get time from lat & lng
		try {
			time = TimeZoneDB.sendRequest(lat, lng);
		} catch (URISyntaxException e) {
			LogHandler.warning("", e);
		} catch (Exception e) {
			LogHandler.warning("", e);
		}
		ChatHandler.sendPlayer(ClockCommand.player, Configuration.chatcolor, time);
	}
	
	/**
	 * Command cooldown
	 * 
	 */
	public static void cooldown(){
		
		//Command cooldown
		if (!ClockCommand.bypass){
			int cooldownTime = 60; // Time in seconds
			if (ClockCommand.cooldowns.containsKey(ClockCommand.player.getName())) {
				long secondsLeft = ((ClockCommand.cooldowns.get(ClockCommand.player.getName())
						/ 1000) + cooldownTime)
						- (System.currentTimeMillis() / 1000);
				if (secondsLeft > 0) {
					// Still cooling down
					ChatHandler.sendPlayer(ClockCommand.player, Configuration.chatcolor, "You can not use that command for another "
											+ secondsLeft
											+ " seconds!");
					ClockCommand.cooldown = true;
				}else{
					// Cooldown has expired, save new cooldown
					ClockCommand.cooldowns.put(ClockCommand.player.getName(), System.currentTimeMillis());
				}
			}else{
				// Cooldown not found, save new cooldown
				ClockCommand.cooldowns.put(ClockCommand.player.getName(), System.currentTimeMillis());
			}
		}
		ClockCommand.cooldown = false;
	}
}