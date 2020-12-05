package fr.jayrex.staffchat.commands;

import java.io.IOException;

import fr.jayrex.staffchat.StaffChat;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class ChatCommand extends Command {

	public ChatCommand() {
		super("chat", "", "sc");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length != 0 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("staffchat.reload")) {
			sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&6Reloading...")));
			try {
				StaffChat.getInstance().reloadConfig();
			} catch (IOException e) {
				sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&',
						"&cUne erreur s'est produite lors du rechargement de la configuration!")));
				e.printStackTrace();
				return;
			}
			sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&aReload avec succès!")));
		} else {
			sender.sendMessage(new TextComponent());
		}
	}

}
