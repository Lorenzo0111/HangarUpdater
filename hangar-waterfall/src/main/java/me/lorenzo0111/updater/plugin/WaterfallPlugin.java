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

package me.lorenzo0111.updater.plugin;

import me.lorenzo0111.updater.hangar.plugin.UpdatablePlugin;
import me.lorenzo0111.updater.hangar.scheduler.IScheduler;
import net.md_5.bungee.api.plugin.Plugin;

public class WaterfallPlugin implements UpdatablePlugin {
    private final String version;
    private final String name;
    private final String serverVersion;
    private final IScheduler scheduler;

    private WaterfallPlugin(String version, String name, String serverVersion, IScheduler scheduler) {
        this.version = version;
        this.name = name;
        this.scheduler = scheduler;
        this.serverVersion = serverVersion;
    }

    public static WaterfallPlugin from(Plugin plugin) {
        return new WaterfallPlugin(plugin.getDescription().getVersion(), plugin.getDescription().getName(), plugin.getProxy().getVersion(), me.lorenzo0111.updater.scheduler.WaterfallPlugin.create(plugin));
    }

    @Override
    public String version() {
        return version;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String serverVersion() {
        return serverVersion;
    }

    @Override
    public IScheduler scheduler() {
        return scheduler;
    }
}
