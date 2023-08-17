package net.mineles.library.docker.binding.env;

import net.mineles.library.docker.binding.AbstractBinding;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnvironmentVariableBinding extends AbstractBinding<String, String> {

    public EnvironmentVariableBinding(@NotNull String key, @Nullable String value) {
        super(key, value, '=');
    }

    public static @NotNull EnvironmentVariableBinding fromBinding(String binding) {
        String[] parts = binding.split("=", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid environment variable: " + binding);
        }

        return new EnvironmentVariableBinding(parts[0], parts[1]);
    }
}
