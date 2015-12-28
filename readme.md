# MCTurtles
Scriptable Mining Turtles for Bukkit/Spigot Minecraft Server


The goal of this project is to build a tool that educators can use to teach introductory programming using Turtles (similar to logo) in Minecraft. This plugin requires no client mods as it uses only the Bukkit/Spigot api.

## Commands

MCTurtles understand many commands but require a player to direct them.

Players can get an instruction manual in-game by typing "/t book" to get the book (without quotes). In the book each command has its own page.

### Note

A parameter in angle brackets <dir> is required.

A parameter in square brackets [num] is optional.

More info:
techplex.io/mcturtles

## Move <dir> [num]§r

Move the turtle in <dir> direction [num] blocks.

Where <dir> is one of:
       NORTH SOUTH EAST
   WEST LEFT RIGHT UP
   DOWN FORWARD BACK

And [num] is optional, when not provided 1 is used.

## Rotate <dir>

Rotate the turtle 90° in <dir> direction.

Where <dir> is one of:
   NORTH SOUTH EAST
   WEST LEFT RIGHT UP
   DOWN FORWARD BACK

## Mine <dir>

Mine 1 block in <dir> direction.

Where <dir> is one of:
   NORTH SOUTH EAST
   WEST LEFT RIGHT UP
   DOWN FORWARD BACK

## Place <dir> <mat>

Place block of type <mat> in <dir> direction.

Where <mat> is a valid material. Page x lists some common materials.

And <dir> is one of:
   NORTH SOUTH EAST
   WEST LEFT RIGHT UP
   DOWN FORWARD BACK

  @note obey creative...

## PenDown <mat>

Place a block each time the turtle moves leaving a trail of <mat> blocks behind.

Where <mat> is a valid material. Page x lists some common materials.

## PenUp

Stop leaving a trail of blocks.

## Bookmark <name>

Save the current location with the name <name>.

## GoBookmark <name>

Move the turtle to the saved bookmark named <name>.

## Common Materials 

Dirt
Grass
Cobblestone
Wood
Sand


