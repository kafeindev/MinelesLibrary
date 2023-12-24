package net.mineles.library.command.context;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

final class DefaultCommandContexts {
    private DefaultCommandContexts() {
    }

    static final CommandContext<Player> PLAYER = (sender, s) -> Bukkit.getPlayer(s);
    static final CommandContext<Integer> INTEGER = (sender, s) -> {
        try {
            return Integer.parseInt(s);
        }catch (Exception e) {
            return null;
        }
    };
    static final CommandContext<Double> DOUBLE = (sender, s) -> {
        try {
            return Double.parseDouble(s);
        }catch (Exception e) {
            return null;
        }
    };
    static final CommandContext<Float> FLOAT = (sender, s) -> {
        try {
            return Float.parseFloat(s);
        }catch (Exception e) {
            return null;
        }
    };
    static final CommandContext<Long> LONG = (sender, s) -> {
        try {
            return Long.parseLong(s);
        }catch (Exception e) {
            return null;
        }
    };
}
