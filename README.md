# Simple Interface Definition Language
[![Build Status](https://travis-ci.org/paidgeek/sidl.svg?branch=master)](https://travis-ci.org/paidgeek/sidl)

## Example
An example using all features.
```
namespace Rpg

struct Position {
  float x
  float y
}

class Character {
	@Unique
	Name s
	Position Position
	Speed f32
	Bag .Inventory.Inventory
	Equipment <s, Rpg.Inventory.Item>
	Buffs [8]f64
}

service RpgService {
  GetItemByName(Name s) Item
}

namespace Rpg.Inventory

enum Quality u8 { Common = 0, Rare, Epic }

class Inventory {
	Capacity u
	Items []Item
}

@Cached(timeout = 60)
interface Item {
	Name s
	Quality Quality
	Cost u64
}

class Weapon : Item {
	Damage u64
}

```
