package fr.jayrex.staffchat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.UUID;

import fr.jayrex.staffchat.commands.ChatCommand;
import fr.jayrex.staffchat.commands.StaffChatCommand;
import fr.jayrex.staffchat.commands.ToggleStaffChatCommand;
import fr.jayrex.staffchat.events.Events;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class StaffChat extends Plugin {

	private Configuration config;
	private static StaffChat instance;
	private ArrayList<UUID> toggled = new ArrayList<UUID>();

	@Override
	public void onEnable() {
		instance = this;

		if (!getDataFolder().exists())
			getDataFolder().mkdir();

		File file = new File(getDataFolder(), "config.yml");

		if (!file.exists()) {
			try (InputStream in = getResourceAsStream("config.yml")) {
				Files.copy(in, file.toPath());
			} catch (IOException e) {
				getLogger().warning(
						"Une erreur s'est produite lors du rechargement de la configuration. =");
				e.printStackTrace();
				return;
			}
		}

		try {
			reloadConfig();
		} catch (IOException e) {
			getLogger().warning(
					"Une erreur s'est produite lors du rechargement de la configuration.");
			e.printStackTrace();
			return;
		}


		getProxy().getPluginManager().registerListener(this, new Events());
		
		getProxy().getPluginManager().registerCommand(this, new ChatCommand());
		getProxy().getPluginManager().registerCommand(this, new StaffChatCommand());
		getProxy().getPluginManager().registerCommand(this, new ToggleStaffChatCommand());
	}

	public static StaffChat getInstance() {
		if (instance == null) {
			instance = new StaffChat();
		}
		return instance;
	}

	public boolean hasStaffChatToggled(ProxiedPlayer p) {
		return toggled.contains(p.getUniqueId());
	}

	public void setStaffChatToggled(ProxiedPlayer p, boolean newVal) {
		if (hasStaffChatToggled(p) != newVal) {
			if (newVal) {
				toggled.add(p.getUniqueId());
			} else {
				toggled.remove(p.getUniqueId());
			}
		}
	}
	
	public void reloadConfig() throws IOException {
		config = ConfigurationProvider.getProvider(YamlConfiguration.class)
				.load(new File(getDataFolder(), "config.yml"));
	}

	public boolean hasStaffJoinEnabled() {
		return !getMessage("staffjoin").isEmpty();
	}

	public boolean hasStaffMoveEnabled() {
		return !getMessage("staffmove").isEmpty();
	}

	public boolean hasStaffLeaveEnabled() {
		return !getMessage("staffleave").isEmpty();
	}

	public boolean hasStaffChatPrefixEnabled() {
		return !getMessage("prefix").isEmpty();
	}

	public String getMessage(String message) {
		return ChatColor.translateAlternateColorCodes('&',
				config.contains(message) ? config.getString(message) : "&cMessage introuvable!");
	}

	public String getMessage(String message, boolean translateColors) {
		if (!translateColors) {
			return config.contains(message) ? config.getString(message) : "&cMessage introuvable!";
		}
		return getMessage(message);
	}

	@SuppressWarnings("deprecation")
	public void tellStaff(String message, String permissionRequired) {
		for (ProxiedPlayer p : getProxy().getPlayers()) {
			if (p.hasPermission(permissionRequired)) {
				p.sendMessage(message);
			}
		}
	}

}
