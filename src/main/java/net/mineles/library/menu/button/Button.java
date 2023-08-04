package net.mineles.library.menu.button;

import net.mineles.library.menu.misc.ClickResult;
import net.mineles.library.menu.misc.contexts.ClickContext;
import net.mineles.library.property.Attribute;
import net.mineles.library.property.AttributeMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Button {
    @NotNull ClickResult click(@NotNull ClickContext context);

    @NotNull AttributeMap getAttributes();

    @Nullable <T> T getAttribute(@NotNull ButtonAttributes attribute);

    @Nullable <T> Attribute<T> getAttribute(@NotNull ButtonAttributes attribute, @NotNull Class<T> type);

    @Nullable <T> Attribute<T> getAttribute(@NotNull String name);

    @Nullable <T> Attribute<T> getAttribute(@NotNull String name, @NotNull Class<T> type);
}
