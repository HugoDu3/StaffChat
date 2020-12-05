package fr.jayrex.staffchat.events;

import fr.jayrex.staffchat.StaffChat;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Events implements Listener {

	@EventHandler
	public void playerJoin(ServerSwitchEvent e) {
		if (e.getFrom() != null)
			return;
		if (!e.getPlayer().hasPermission("staffchat.use"))
			return;
		if (!StaffChat.getInstance().hasStaffJoinEnabled())
			return;

		String message = StaffChat.getInstance().getMessage("staffjoin");
		message = message.replace("{player}", e.getPlayer().getName()).replace("{server}",
				e.getPlayer().getServer().getInfo().getName());
		StaffChat.getInstance().tellStaff(message, "staffchat.use");
	}

	@EventHandler
	public void serverSwitch(ServerSwitchEvent e) {
		if (e.getFrom() == null)
			return;
		if (!e.getPlayer().hasPermission("staffchat.use"))
			return;
		if (!StaffChat.getInstance().hasStaffMoveEnabled())
			return;

		// Tell the rest of the staff.
		String message = StaffChat.getInstance().getMessage("staffmove");
		message = message.replace("{player}", e.getPlayer().getName())
				.replace("{to}", e.getPlayer().getServer().getInfo().getName())
				.replace("{from}", e.getFrom().getName());
		StaffChat.getInstance().tellStaff(message, "staffchat.use");
	}

	@EventHandler
	public void playerDisconnect(PlayerDisconnectEvent e) {
		StaffChat.getInstance().setStaffChatToggled(e.getPlayer(), false);

		if (!e.getPlayer().hasPermission("staffchat.use"))
			return;
		if (!StaffChat.getInstance().hasStaffLeaveEnabled())
			return;

		// Tell the rest of the staff.
		String message = StaffChat.getInstance().getMessage("staffleave");
		message = message.replace("{player}", e.getPlayer().getName()).replace("{server}",
				e.getPlayer().getServer().getInfo().getName());
		StaffChat.getInstance().tellStaff(message, "staffchat.use");
	}

	@EventHandler
	public void playerChat(ChatEvent e) {
		if (e.getMessage().startsWith("/"))
			return;
		ProxiedPlayer p = (ProxiedPlayer) e.getSender();
		if (!p.hasPermission("staffchat.use"))
			return;
		if (!(StaffChat.getInstance().hasStaffChatPrefixEnabled()
				&& e.getMessage().startsWith(StaffChat.getInstance().getMessage("prefix", false)))
				&& !StaffChat.getInstance().hasStaffChatToggled(p))
			return;

		String message;
		if (!StaffChat.getInstance().hasStaffChatToggled(p)) {
			message = e.getMessage().substring(StaffChat.getInstance().getMessage("prefix", false).length());
		} else {
			message = e.getMessage();
		}

		String format = StaffChat.getInstance().getMessage("staffmessage")
				.replace("{server}", p.getServer().getInfo().getName()).replace("{player}", p.getName())
				.replace("{message}", message);
		StaffChat.getInstance().tellStaff(format, "staffchat.use");
		e.setCancelled(true);
	}

}
