package fr.jayrex.staffchat.commands;

import fr.jayrex.staffchat.StaffChat;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;


public class StaffChatCommand extends Command {

	public StaffChatCommand() {
		super("staffchat", "staffchat.use", "sc");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)) {
			sender.sendMessage(new TextComponent(
					ChatColor.translateAlternateColorCodes('&', "&cVous devez être un joueur.")));
			return;
		}
		ProxiedPlayer p = (ProxiedPlayer) sender;
		String format = StaffChat.getInstance().getMessage("staffmessage")
				.replace("{server}", p.getServer().getInfo().getName()).replace("{player}", p.getName())
				.replace("{message}", String.join(" ", args));
		StaffChat.getInstance().tellStaff(format, "staffchat.use");
	}

}
