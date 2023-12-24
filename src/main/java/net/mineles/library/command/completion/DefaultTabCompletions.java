package net.mineles.library.command.completion;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public final class DefaultTabCompletions {
    private DefaultTabCompletions() {}

    public static final TabCompletion PLAYERS = sender -> Bukkit.getOnlinePlayers().stream()
            .map(Player::getName)
            .collect(Collectors.toList());
}
