package net.mineles.library.menu.button;

import net.mineles.library.property.AttributeMap;
import org.jetbrains.annotations.NotNull;

public interface Button {
    @NotNull AttributeMap getAttributes();

    default @NotNull <T> T getAttribute(@NotNull ButtonAttributes attribute) {
        return getAttributes().get(attribute);
    }
}
