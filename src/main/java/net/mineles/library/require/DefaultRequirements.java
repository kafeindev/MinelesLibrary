package net.mineles.library.require;

import net.mineles.library.components.PlayerComponent;

final class DefaultRequirements {
    /*static Requirement<PlayerComponent> HAS_MONEY = Requirement.of(
        (player, registeredRequirement) -> player.getMoney() >= registeredRequirement.getMoney(),
        (player, registeredRequirement) -> player.setMoney(player.getMoney() - registeredRequirement.getMoney())
    );*/

    private DefaultRequirements() {}
}
