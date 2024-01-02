package net.mineles.library.cluster.binding.folder;

import net.mineles.library.cluster.binding.AbstractBinding;
import org.jetbrains.annotations.NotNull;

public class FolderBinding extends AbstractBinding<String, String> {

    public FolderBinding(@NotNull String key, @NotNull String value) {
        super(key, value);
    }

    public static @NotNull FolderBinding fromBinding(String binding) {
        String[] parts = binding.split(":", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid folder: " + binding);
        }

        return new FolderBinding(parts[0], parts[1]);
    }
}
