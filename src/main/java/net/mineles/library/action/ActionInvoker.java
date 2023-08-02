/*
 * MIT License
 *
 * Copyright (c) 2023 Kafein
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.mineles.library.action;

import net.mineles.library.action.context.Context;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class ActionInvoker {
    private ActionInvoker() {}

    @NotNull
    public static CompletableFuture<ActionResult> invoke(@NotNull Action action,
                                                         @NotNull Object... objects) {
        return invoke(action, result -> {}, objects);
    }

    @NotNull
    public static CompletableFuture<ActionResult> invoke(@NotNull Action action,
                                                         @NotNull Consumer<ActionResult> result,
                                                         @NotNull Object... objects) {
        CompletableFuture<ActionResult> future = new CompletableFuture<>();
        future.thenAccept(result);

        return invoke(action, future, objects);
    }

    @NotNull
    public static CompletableFuture<ActionResult> invoke(@NotNull Action action,
                                                         @NotNull CompletableFuture<ActionResult> future,
                                                         @NotNull Object... objects) {
        Context context = Context.create(objects);

        action.invoke(context, future);
        return future;
    }
}
