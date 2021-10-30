
# Pehkui
Library mod for the Fabric and Forge mod loaders that allows mod developers to modify the size of entities.  

# Information for Players
<details open>
<summary>Show/Hide Information for Players</summary><table width=100%><td>

## Required Mods to Run
<details open>
<summary>Show/Hide Required Mods</summary><table width=100%><td>

### Playing on Fabric

- Newest version of the [Fabric mod loader](https://fabricmc.net/use/)  
- Newest version of the [Fabric A](https://www.curseforge.com/minecraft/mc-mods/fabric-api/files/all)[PI mod](https://modrinth.com/mod/fabric-api/versions) for whichever Minecraft version you're playing on

### Playing on Forge

- Newest version of the [Forge mod loader](https://files.minecraftforge.net/net/minecraftforge/forge/) for whichever Minecraft version you're playing on
</td></table></details>

## Supported Minecraft Versions
<details>
<summary>Show/Hide Supported Minecraft Versions</summary><table width=100%><td>

### Fabric Versions
Supported Versions of `Pehkui-x.y.z+1.14.4-1.18`:  
`1.14.4`, `1.15.2`, `1.16.5`, `1.17.1`, `21w43a`

### Forge Versions

Supported Versions of `Pehkui-x.y.z+1.16.5-forge`:  
`1.16.5`

Supported Versions of `Pehkui-x.y.z+1.17.1-forge`:  
`1.17.1`
</td></table></details>

## Mod Features
<details>
<summary>Show/Hide Mod Features</summary><table width=100%><td></br>

Pehkui allows mod developers to:

- Change the size of entities through modifying scale data
- Affect other properties of an entity that are considered as dependant on the size</br>(e.g. movement speed, explosion size, reach distance)
- Have the scalable properties of an entity be affected by other scale data types or by external data</br>through scale modifiers
</td></table></details>
</td></table></details>

# Information for Developers
<details>
<summary>Show/Hide Information for Developers</summary><table width=100%><td>

## Adding a Dependency
<details open>
<summary>Show/Hide Dependency Information</summary><table width=100%><td>

### Maven

<details open>
<summary>Show/Hide Maven Information</summary><table width=100%><td>

To make use of Pehkui in your own mod, you'll first need to go to your `repositories` block near the</br>top of your `build.gradle` and add JitPack to the bottom of the block like below:

```groovy
repositories {
	// ... your other maven repositories above ...
	maven {
		url = "https://jitpack.io"
	}
}
```
</td></table></details>

### Mod Version and Dependency Configuration

<details open>
<summary>Show/Hide Dependency Configuration Information</summary><table width=100%><td>

Now that a Maven repository is specified, add `pehkui_version=x.y.z-w` to your `gradle.properties`,</br>replacing `x.y.z-w` with one of the available version strings from the [list of release tags](../../tags).

Lastly, in your `build.gradle`'s `dependencies` block, add the corresponding line from below</br>depending on your mod loader:

#### Developing for Fabric with Loom

```groovy
modApi("com.github.Virtuoel:Pehkui:${pehkui_version}")
```

#### Developing for Forge with ForgeGradle

```groovy
api fg.deobf("com.github.Virtuoel:Pehkui:${pehkui_version}")
```

#### Developing for Forge with Architectury Loom

```groovy
modApi("com.github.Virtuoel:Pehkui:${pehkui_version}")
```
</td></table></details>
</td></table></details>
<!--
## API Information
<details>
<summary>Show/Hide API Information</summary><table width=100%><td>

### WIP

</td></table></details>
-->
</td></table></details>
