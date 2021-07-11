# HangarUpdater
An update checker library for [HangarMC](https://github.com/HangarMC/Hangar)

## Example Plugin
There are two methods to use this update checker.

### 1: Creating a Plugin instance:
```java
public class MyPlugin extends JavaPlugin {
    public void onEnable() {
        BukkitPlugin plugin = BukkitPlugin.from(this);
        UpdateChecker checker = new UpdateChecker(plugin,String.format("[%s] Update found: %new-version%", this.getName()), 1000)
        updater.sendUpdateCheck(Bukkit.getConsoleSender());
    }
}
```
### 2: Implementing UpdatablePlugin
You can view the example [here](hangar-paper/src/test/java/me/lorenzo0111/updater/SamplePlugin.java).

## Build Script Setup
Add the Lorenzo0111 repository and the HangarUpdater dependency, then shade and relocate it to your own package.
Relocation helps avoid version conflicts with other plugins using HangarUpdater. 

Remember to replace PLATFORM with your platform ( paper, waterfall, velocity ) and VERSION with the version.

### Gradle

Repo:
```groovy
repositories {
    maven {
        name "lorenzo0111"
        url "https://repo.repsy.io/mvn/lorenzo0111/public"
    }
}
```

Dependency:
```groovy
dependencies {
    implementation "me.lorenzo0111.updater:hangar-PLATFORM:VERSION"
}
```

Shadow Jar and Relocate:
```groovy
plugins {
  id "com.github.johnrengelman.shadow" version "7.0.0"
}
shadowJar {
   relocate "me.lorenzo0111.updater", "[YOUR PLUGIN PACKAGE].updater"
}
```

### Maven
Repo:
```xml
<repositories>
    <repository>
        <id>lorenzo0111</id>
        <url>https://repo.repsy.io/mvn/lorenzo0111/public</url>
    </repository>
</repositories>
```
Dependency:
```xml
<dependencies>
    <dependency>
        <groupId>me.lorenzo0111.updater</groupId>
        <artifactId>hangar-PLATFORM</artifactId>
        <version>VERSION</version>
        <scope>compile</scope>
     </dependency>
 </dependencies>
 ```
 
Shade & Relocate:
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.2.4</version> <!-- Make sure to always use the latest version (https://maven.apache.org/plugins/maven-shade-plugin/) -->
            <configuration>
                <dependencyReducedPomLocation>${project.build.directory}/dependency-reduced-pom.xml</dependencyReducedPomLocation>
                <relocations>
                    <relocation>
                        <pattern>me.lorenzo0111.updater</pattern>
                        <shadedPattern>[YOUR PLUGIN PACKAGE].updater</shadedPattern> <!-- Replace this -->
                    </relocation>
                </relocations>
            </configuration>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

## License
HangarUpdater is licensed under the [MIT license](LICENSE)

