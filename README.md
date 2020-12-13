_It's everyone's favourite_
# MondayWhitelist
The whitelist plugin for the Computing Society Minecraft Server.

## Usage
Drag and drop the jar into the `plugins/` folder of your Bukkit compatible installation of your Minecraft server.
You can then modify the configuration file found at `plugins/MinecraftMonday/config.yml`.

## Server
A Minecraft Monday compliant HTTP server will respect the following.

1. Send `ok` and code `200` when a UUID is in the whitelist
2. Send `notfound` and code `200` when a UUID is not in the whitelist
3. Send `banned` and code `200` when a UUID is banned
4. Do not send `200` for anything else.
5. Send code `401` when the Monday `Authorization` header is incorrect. `:(`
