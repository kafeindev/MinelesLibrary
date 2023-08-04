package net.mineles.library.menu;

import net.mineles.library.components.PlayerComponent;
import net.mineles.library.menu.button.Button;
import net.mineles.library.menu.misc.ClickResult;
import net.mineles.library.menu.misc.contexts.ClickContext;
import net.mineles.library.property.Attribute;
import net.mineles.library.property.AttributeMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface Menu {
    default boolean open(@NotNull PlayerComponent player) {
        return open(player, 0);
    }

    boolean open(@NotNull PlayerComponent player, int page);

    boolean close(@NotNull PlayerComponent player);

    boolean refresh(@NotNull PlayerComponent player);

    boolean refreshButton(@NotNull PlayerComponent player, int slot);

    boolean refreshButton(@NotNull PlayerComponent player, @NotNull String buttonName);

    @NotNull ClickResult click(@NotNull ClickContext context);

    @NotNull AttributeMap getAttributes();

    @Nullable <T> Attribute<T> getAttribute(@NotNull MenuAttributes attribute);

    @Nullable <T> Attribute<T> getAttribute(@NotNull MenuAttributes attribute, @NotNull Class<T> type);

    @Nullable <T> Attribute<T> getAttribute(@NotNull String name);

    @Nullable <T> Attribute<T> getAttribute(@NotNull String name, @NotNull Class<T> type);

    @NotNull Optional<Button> findButton(@NotNull String name);

    @NotNull Optional<Button> findButton(int slot);

    void putButtons(@NotNull Button... buttons);

    void putButtons(@NotNull Iterable<Button> buttons);

    void putButton(@NotNull Button button);

    void removeButtons(@NotNull Button... buttons);

    void removeButtons(@NotNull Iterable<Button> buttons);

    void removeButton(@NotNull Button button);
}
