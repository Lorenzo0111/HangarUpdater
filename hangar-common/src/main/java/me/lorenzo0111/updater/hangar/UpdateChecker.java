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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.lorenzo0111.updater.hangar.plugin.UpdatablePlugin;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private final String author;
    private final String slug;
    private final String message;
    private final String platform;
    private final String api;

    /**
     * @param plugin Plugin
     * @param message Message to send to the user. Placeholders: %version% %new-version%
     * @param author Hangar username of the resource's author
     * @param slug Hangar slug of the resource
     * @param api API Host or null to default. Do not include any link. Only the host. Ex: localhost:3000
     */
    public UpdateChecker(UpdatablePlugin plugin, String message, String author, String slug, @Nullable String platform, @Nullable String api) {
        this.plugin = plugin;
        this.author = author;
        this.slug = slug;
        this.message = message;
        this.platform = platform;
        // ToDo: Edit default url
        this.api = api == null ? "localhost:3000" : api;

        this.fetch();
    }

    /**
     * Fetch updates from hangar api
     */
    private CompletableFuture<Void> fetch() {
        return plugin.scheduler().async(() -> {
            try {
                URL url = new URL(this.formatUrl());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                JsonObject json = JsonParser.parseString(content.toString()).getAsJsonObject();
                con.disconnect();

                JsonArray result = json.get("result").getAsJsonArray();
                JsonObject newVersion = result.get(0).getAsJsonObject();

                this.updateAvailable = false;

                if (newVersion.get("name").getAsString().equals(this.plugin.version())) {
                    this.fetched = true;
                    return;
                }

                JsonObject platform = newVersion.get("platformDependencies").getAsJsonObject();
                JsonArray platformVersion = platform.get(this.platform.toUpperCase()).getAsJsonArray();

                platformVersion.forEach((version) -> {
                    if (version.getAsString().contains(plugin.serverVersion())) {
                        this.updateAvailable = true;
                    }
                });

                this.fetched = true;

            } catch (IOException e) {
                e.printStackTrace();
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

    private String formatUrl() {
        // http://API/api/v1/projects/AUTHOR/SLUG/versions?channel=Release&limit=1&offset=0&platform=PLATFORM
        StringBuilder format = new StringBuilder("%s/api/v1/projects/%s/%s/versions?channel=Release&limit=1&offset=0");
        if (this.platform != null) {
            format.append("&platform=").append(platform);
        }

        return String.format(format.toString(), api, author, slug);
    }
}