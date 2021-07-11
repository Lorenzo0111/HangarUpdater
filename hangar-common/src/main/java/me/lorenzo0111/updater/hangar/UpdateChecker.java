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

package me.lorenzo0111.updater.hangar;

import me.lorenzo0111.updater.hangar.plugin.UpdatablePlugin;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

/**
 * Hangar Update Checker
 */
@SuppressWarnings("unused")
public class UpdateChecker {
    private boolean fetched = false;
    private boolean updateAvailable;
    private String newVersion;

    private final UpdatablePlugin plugin;
    private final int resourceId;
    private final String message;

    /**
     * @param plugin Plugin
     * @param message Message to send to the user. Placeholders: %version% %new-version%
     * @param resourceId Hangar resource id
     */
    public UpdateChecker(UpdatablePlugin plugin, String message, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
        this.message = message;

        this.fetch();
    }

    /**
     * Fetch updates from hangar api
     */
    private CompletableFuture<Void> fetch() {
        return plugin.scheduler().async(() -> {
            try (InputStream inputStream = new URL("" + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    String version = scanner.next();

                    this.updateAvailable = !this.plugin.version().equalsIgnoreCase(version);
                    this.fetched = true;
                    this.newVersion = version;
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * @param entity Entity to send the update message
     */
    public void sendUpdateCheck(Audience entity) {
        if (!fetched) {
            this.fetch().thenAccept((unused) -> sendUpdateCheck(entity));
            return;
        }

        if (updateAvailable) {
            entity.sendMessage(Component.text(message.replace("%version%",plugin.version()).replace("%new-version%", newVersion)));
        }
    }

}