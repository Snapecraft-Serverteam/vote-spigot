![pre-release](https://github.com/Snapecraft-Serverteam/vote-spigot/workflows/pre-release/badge.svg)  
![GitHub release (latest by date)](https://img.shields.io/github/v/release/Snapecraft-Serverteam/vote-spigot)
![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/Snapecraft-Serverteam/vote-spigot?include_prereleases&label=pre-release)
# vote-spigot

A server-side handler for our custom votifier system. Allows you to redeem your rewards on the subservers with an easy command.
[Bungeecord handler here](https://github.com/Snapecraft-Serverteam/vote-bungee)

### Dependencies
- [SmartInvs](https://github.com/MinusKube/SmartInvs)

### Configuration

```YAML
SQL: # Login informations for SQL sync
  host: localhost
  user: root
  pw: ''
  db: vote
  port: 3306
Shopconfig:
  size: 4 # How much rows should have the shop Inventory.
Shop: # Each shop Entry here
  '0': # Counting... if you add more than one just count one up.
    name: Diamand # Displayname of the Item
    material: DIAMOND # Material of the Item.
    price: 500 # The price (Coins)
```

*Note:* 
- If the childs of Shop are somethin another than '0', '1', '3'... will happen unkwon things.
- The Plugin creates needed tables into mysql

Materials List: [https://helpch.at/docs/1.8.8/org/bukkit/Material.html](https://helpch.at/docs/1.8.8/org/bukkit/Material.html)
