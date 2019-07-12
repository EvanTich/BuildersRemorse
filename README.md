## Builder's Remorse
Builder's Remorse is a plugin for [Spigot 1.14.3](https://getbukkit.org/download/spigot).
This plugin acts as a challenge of sorts, do not play with this plugin unless you like a challenge (or do, and suffer). 
It effectively removes the right for the player to fix any building errors that were made; like a typewriter but with Minecraft.
The aim of this plugin is to fight and kill the ender dragon while dealing with the (many) building mistakes that the players have created.

The honor system is a big thing for everyone on the server. It's very possible to box someone in forever. Server rules are a thing.

Originally created for my friends and I as a fun challenge.

### How to Install
1. Go to the [releases](https://github.com/EvanTich/BuildersRemorse/releases) tab in the repository.
2. Download the [latest release](https://github.com/EvanTich/BuildersRemorse/releases/latest).
3. Throw the .jar into the server plugins folder.
4. Done!

### How to Use
#### config.yml
After you run the server for the first time with the plugin a config file gets created.
The config file looks like this:
```yaml
# Whether the plugin is internally enabled or not
enabled: [boolean] 
# The list of every block placed by players, it's not wise to edit this list manually
blocks: [list] 
- [world] [x] [y] [z]...
```

#### Commands
There's technically one command that can be used, but there are multiple functions of the command.

`/br [remove | enable | disable | version | help]` or longer `/buildersremorse ...`

`remove` removes the remorse from the block you're looking at.

`enable` re-enables the plugin internally.

`disable` disables the plugin internally.

`version` shows the version of the plugin.

`help` shows the usage information because I can't be bothered to make it better.

### End
That's about all you need to know, have fun!
