package net.mineles.library.menu.action;

import net.mineles.library.menu.misc.contexts.ClickContext;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ClickAction extends BiConsumer<ClickContext, RegisteredClickAction> {
}
