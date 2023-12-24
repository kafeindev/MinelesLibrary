package net.mineles.library.command;

import net.mineles.library.command.context.CommandContext;
import net.mineles.library.command.context.CommandContextMap;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

public final class Arguments {
    private final CommandSender sender;
    private final String[] args;

    public Arguments(CommandSender sender, String[] args) {
        this.sender = sender;
        this.args = args;
    }

    public String[] getArgs() {
        return this.args;
    }

    public @Nullable String get(int index) {
        return this.args.length <= index ? null : this.args[index];
    }

    public <T> T get(int index, Class<T> tClass) {
        String arg = get(index);
        if (arg == null) {
            return null;
        }

        CommandContext<T> commandContext = CommandContextMap.getDefaults().getCommandContext(tClass);
        if (commandContext == null) {
            throw new RuntimeException();
        }

        return commandContext.apply(this.sender, arg);
    }

    public int getSize() {
        return this.args.length;
    }
}
