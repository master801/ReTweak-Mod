# ReTweak-Mod

Info
------------
A 1.7.10 Minecraft mod that (attempts) to load older Minecraft mods ('1.2.5', '1.4.7', '1.5.2', '1.6.2', '1.6.4').<br/>
This uses a ***ton*** of magic and hacks to make it possible.<br/>
**Currently does *not* work.**<br/>
**Do not expect this project to go anywhere soon, if at all.**

For the technical people, ReTweak-Mod does all of the following (in chronological order) during runtime:
- Find and load mappings for deobfuscation
- Find and load ReTweak mods
- Construct ReTweak mods
- Deobfuscate and tweak ReTweak mods being constructed, before the class is loaded
- PreInitialization Event - Send event to all ReTweak mods
- Initialization Event - Send event to all ReTweak mods
- PostInitialization Event - Send event to all ReTweak mods

Goals
-------------
ReTweak-Mod ***will*** support multiple versions of Minecraft (in the future) instead of only one version.<br/><br/>
Currently, the focus is to work on only one version of Minecraft to make sure everything works properly, then work on the next version,
which will be voted upon. If no votes are casted, then a version shall be picked instead.

- [X] Custom mod loading
- [X] Deobfuscation
- [ ] Get mods properly loaded and working
- [ ] Get other versions of Minecraft properly working

Mods being tested
-------------
- 1.2.5 - **NOT BEING WORKED ON**
    - ? - **NOT WORKING**
- 1.4.7 - **NOT BEING WORKED ON**
    - [AtmosMobs - Daveyx0](https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/1282471) - **NOT WORKING**
    - [FancyGlass - Darkmainiac](https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/1287608) - **NOT WORKING**
    - [Iron Chests - cpw](https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/1280827) - **NOT WORKING**
    - [Coloured Beds Mod - Gorion91](https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/1286370) - **NOT WORKING**

- 1.5.2 - **Currently Being Worked On**
    - Campfire Mod - JSOne Studios - **NOT WORKING**
    - [Growing Flowers - gelber_kaktus](https://www.curseforge.com/minecraft/mc-mods/growing-flowers) - **NOT WORKING**
    - [Aquaculture - Team Metallurgy](https://www.curseforge.com/minecraft/mc-mods/aquaculture) - **NOT WORKING**

- 1.6.2 - **NOT BEING WORKED ON**

- 1.6.4 - **NOT BEING WORKED ON**

Bugs
-------------
**Everything.**<br/>
Currently ***not*** working.

Misconception
-------------
No, this project is not the same as [Intermediary by FyberOptic](https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/wip-mods/2360685), although the goals are similarly aligned.<br/>
It seems we had the same idea around the same time and they were the one to actually make it possible, albeit for '1.2.5'.

Credit
-------------
FyberOptic - For publicly releasing Intermediary first before this project was even loading.
