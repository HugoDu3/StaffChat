package fr.jayrex.staffchat.commands;

import fr.jayrex.staffchat.StaffChat;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ToggleStaffChatCommand extends Command {

	public ToggleStaffChatCommand() {
		super("togglestaffchat", "staffchat.use", "togglesc", "sctoggle", "staffchattoggle");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)) {
			sender.sendMessage(new TextComponent(
					ChatColor.translateAlternateColorCodes('&', "&cVous devez être un joueur.")));
			return;
		}
		ProxiedPlayer p = (ProxiedPlayer) sender;
		boolean newState = !StaffChat.getInstance().hasStaffChatToggled(p);
		StaffChat.getInstance().setStaffChatToggled(p, newState);
		if(newState) {
			p.sendMessage(StaffChat.getInstance().getMessage("enabled"));
		}else {
			p.sendMessage(StaffChat.getInstance().getMessage("disabled"));
		}
	}

}
