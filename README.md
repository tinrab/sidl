# Simple Interface Description Language
[![Build Status](https://travis-ci.org/paidgeek/sidl.svg?branch=master)](https://travis-ci.org/paidgeek/sidl)

## Example
An example using all features.
```
namespace RPG

type Character {
	@Unique
	Name s
	Speed f32
	Bag .Inventory.Inventory
	MainHand RPG.Inventory.Item
	Buffs [8]f64
}


namespace RPG.Inventory

enum Quality u8 { Common = 0, Rare, Epic }

type Inventory {
	Capacity u
	Items []*Item
}

@Cached(timeout = 60)
interface Item {
	Name s
	Quality Quality
	Cost u64
}

type Weapon : Item {
	Damage u64
}

type Armor : Item {
	Defense u64
}
```
