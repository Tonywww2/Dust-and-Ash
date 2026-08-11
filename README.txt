Dust and Ash for 1.20.1
https://www.curseforge.com/minecraft/mc-mods/dust-and-ash

Dust settles and ash remains. Gather elemental Dust, refine alloys, build
processing machines, and control a modular fission reactor.

Build layout
------------

Stonecutter owns the version tree. The shared Java and resource sources remain under
src/main, while version-specific dependency coordinates live under versions.

Active node: 1.20.1-forge
Java toolchain: 17

Common commands
---------------

List version nodes:
	gradlew.bat projects

Compile after adding or moving source files:
	gradlew.bat :1.20.1-forge:compileJava --rerun-tasks

Build the remapped mod jar:
	gradlew.bat :1.20.1-forge:build

Run tests:
	gradlew.bat :1.20.1-forge:test

The distributable jar is written to:
	versions/1.20.1-forge/build/libs/
