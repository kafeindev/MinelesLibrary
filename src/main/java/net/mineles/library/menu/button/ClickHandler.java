package net.mineles.library.menu.button;

import net.mineles.library.menu.misc.ClickResult;
import net.mineles.library.menu.misc.contexts.ClickContext;

import java.util.function.Function;

@FunctionalInterface
interface ClickHandler extends Function<ClickContext, ClickResult> {
}
