package net.mineles.library.menu;

import net.mineles.library.components.PlayerComponent;
import net.mineles.library.manager.AbstractManager;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public final class MenuManager extends AbstractManager<String, Menu> {
    public void stopViewing() {
        getValues().forEach(Menu::stopViewing);
    }

    public void stopViewing(@NotNull String name) {
        find(name).ifPresent(Menu::stopViewing);
    }

    public void stopViewing(@NotNull Menu menu) {
        menu.stopViewing();
    }

    public void stopViewing(@NotNull UUID uniqueId) {
        findByViewer(uniqueId).ifPresent(menu -> menu.close(uniqueId));
    }

    public void stopViewing(@NotNull PlayerComponent player) {
        findByViewer(player.getUniqueId()).ifPresent(menu -> menu.close(player));
    }

    public @NotNull Optional<Menu> findByViewer(@NotNull UUID uniqueId) {
        return this.getValues().stream()
                .filter(menu -> menu.isViewing(uniqueId))
                .findFirst();
    }
}
