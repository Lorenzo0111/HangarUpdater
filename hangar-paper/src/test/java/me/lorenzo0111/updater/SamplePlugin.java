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

package me.lorenzo0111.updater;

import me.lorenzo0111.updater.hangar.UpdateChecker;
import me.lorenzo0111.updater.hangar.plugin.UpdatablePlugin;
import me.lorenzo0111.updater.hangar.scheduler.IScheduler;
import me.lorenzo0111.updater.scheduler.BukkitScheduler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class SamplePlugin extends JavaPlugin implements UpdatablePlugin {
    private BukkitScheduler scheduler;

    @Override
    public void onEnable() {
        this.scheduler = BukkitScheduler.create(this);

        String updateText = ChatColor.translateAlternateColorCodes('&', "&8[&eUpdater&8] &7An update for " + name() + " has been found. New Version: &e&n%new-version%");
        UpdateChecker updater = new UpdateChecker(this,updateText,"paper","Resource", "paper", null);
        updater.sendUpdateCheck(Bukkit.getConsoleSender());
    }

    @Override
    public String version() {
        return this.getDescription().getVersion();
    }

    @Override
    public String name() {
        return this.getName();
    }

    @Override
    public String serverVersion() {
        return this.getServer().getMinecraftVersion();
    }

    @Override
    public IScheduler scheduler() {
        return scheduler;
    }
}
