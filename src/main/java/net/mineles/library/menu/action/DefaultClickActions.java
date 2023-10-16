package net.mineles.library.menu.action;

import net.mineles.library.plugin.BukkitPlugin;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static net.mineles.library.utils.text.PlaceholderParser.applyPlaceholders;

final class DefaultClickActions {
    static ClickAction CLOSE = (clickContext, registeredClickAction) -> clickContext.getPlayer().closeInventory();

    static ClickAction OPEN = (clickContext, registeredClickAction) -> {
        String menuName = registeredClickAction.getValue();
        checkNotNull(menuName, "Menu name cannot be null");

        BukkitPlugin plugin = clickContext.getPlugin();
        plugin.getMenuManager().find(menuName).ifPresent(menu -> menu.open(clickContext.getPlayer()));
    };

    static ClickAction COMMAND_CONSOLE = (clickContext, registeredClickAction) -> {
        String command = registeredClickAction.getValue();
        checkNotNull(command, "Command cannot be null");

        Server server = clickContext.getPlugin().getServer();
        server.dispatchCommand(server.getConsoleSender(), applyPlaceholders(command, Map.of(
                "%player%", clickContext.getPlayer().getName(),
                "%menu%", clickContext.getMenu().getName()
        )));
    };

    static ClickAction COMMAND_PLAYER = (clickContext, registeredClickAction) -> {
        String command = registeredClickAction.getValue();
        checkNotNull(command, "Command cannot be null");

        Player player = clickContext.getPlayer().getHandle();
        player.performCommand(applyPlaceholders(command, Map.of(
                "%player%", player.getName(),
                "%menu%", clickContext.getMenu().getName()
        )));
    };

    static ClickAction SERVER = (clickContext, registeredClickAction) -> {
        String serverName = registeredClickAction.getValue();
        checkNotNull(serverName, "Server name cannot be null");

        clickContext.getPlayer().switchServer(serverName);
    };

    private DefaultClickActions() {}
}
