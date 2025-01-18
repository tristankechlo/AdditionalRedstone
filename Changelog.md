# Changelog

### Version 1.18.2 - 1.2.0

- port to fabric
- remove recipes for timer and oscillator
- overhaul of the crafting system for all logic gates
    - add new item `circuit_base` which is used to craft all gates/circuits
    - add custom recipe type `additionalredstone:circuit_maker` which is used for all recipes in the circuit maker
    - timer and oscillator are now crafted in the circuit maker
- add support for JustEnoughItems to show the recipes of the circuit maker
    - when JEI is installed, the circuit maker will have a button to show all the possible recipes
- reworked menu for following blocks
    - oscillator, time and sequencer
    - now closable like all menus, with the inventory key or escape key
- new menu for following blocks
    - and_gate, or_gate, xor_gate, nand_gate, nor_gate, xnor_gate
    - opened by right-clicking the block
- added new block `supergate`
    - special logic gate which can be configured
    - output depends on the configuration for all input combinations
- added new block `dimmable_redstone_lamp`
    - emitted light level depends on the strength on the input redstone signal
    - strongest signal will be used
- added new block `light_detector`
    - redstone strength is the light level at its position

### Version 1.18.2 - 1.1.0

- added the `/additionalredstone` command
    - `/additionalredstone github` shows the url to the github page
    - `/additionalredstone discord` shows the url to the discord server
    - `/additionalredstone wiki` shows the url to the wiki
    - `/additionalredstone issues` shows the url to the issues page
    - `/additionalredstone curseforge` shows the url to the curseforge page
    - `/additionalredstone modrinth` shows the url to the modrinth page

### Version 1.18.2 - 1.0.4

- port to 1.18.2
- change file name to `additionalredstone-forge-{version}.jar`

### Version 1.18.1 - 1.0.3

- Fix NoSuchMethodError

### Version 1.18.1 - 1.0.2

- port to 1.18.1

### Version 1.17.1 - 1.0.2

- add sequencer recipe to the circuit-maker

### Version 1.17.1 - 1.0.1

- fix redstone-dust connecting wrong to diodes (fixes #1)
- switch harvest-level and tool-type for circuit_maker to json format instead of hardcoded

### Version 1.17.1 - 1.0.0

- first beta release
