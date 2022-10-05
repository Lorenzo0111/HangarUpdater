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

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `maven-publish`
    java
    id("net.kyori.indra.publishing") version "3.0.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "me.lorenzo0111.updater"
version = "1.0-SNAPSHOT"

subprojects {
    apply(plugin = "net.kyori.indra.publishing")
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "java")

    group = project.group
    version = project.version

    repositories {
        mavenCentral()
        maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
        maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/releases/") }
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/") {
            name = "sonatype-oss-snapshots"
        }
    }

    dependencies {
        implementation("net.kyori:adventure-api:4.9.3")
    }

    indra {
        mitLicense()
        github("Lorenzo0111", "HangarUpdater")
        publishAllTo("repsy", "https://repo.repsy.io/mvn/lorenzo0111/public")
    }

    tasks {
        named<ShadowJar>("shadowJar") {
            relocate("io.papermc.lib", "me.lorenzo0111.updater.paperlib")
        }
    }
}
