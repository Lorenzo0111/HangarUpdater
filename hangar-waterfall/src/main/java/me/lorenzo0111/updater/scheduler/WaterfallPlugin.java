/*
 * This file is part of HangarUpdater, licensed under the MIT License.
 *
 * Copyright (c) Lorenzo0111
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

package me.lorenzo0111.updater.scheduler;

import me.lorenzo0111.updater.hangar.scheduler.IScheduler;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import java.util.concurrent.CompletableFuture;

public class WaterfallPlugin implements IScheduler {
    private final TaskScheduler scheduler;
    private final Plugin plugin;

    private WaterfallPlugin(TaskScheduler scheduler, Plugin plugin) {
        this.scheduler = scheduler;
        this.plugin = plugin;
    }

    public static WaterfallPlugin create(Plugin plugin) {
        return new WaterfallPlugin(plugin.getProxy().getScheduler(), plugin);
    }

    @Override
    public CompletableFuture<Void> sync(Runnable runnable) {
        throw new UnsupportedOperationException("BungeeCord does not support sync tasks.");
    }

    @Override
    public CompletableFuture<Void> async(Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        scheduler.runAsync(plugin,() -> {
            runnable.run();
            future.complete(null);
        });

        return future;
    }
}
