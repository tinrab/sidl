# Simple Interface Description Language
[![Build Status](https://travis-ci.org/paidgeek/sidl.svg?branch=master)](https://travis-ci.org/paidgeek/sidl)

## Example
```
namespace RPG

type Character {
	Name s
	Speed f32
	Bag :Inventory:Inventory
	MainHand RPG:Inventory:Item
	Buffs [8]f64
}


namespace RPG:Inventory

enum Quality u8 { Common = 0, Rare, Epic }

type Item {
	Name s
	Quality Quality
	Cost u64
}

type Inventory {
	Capacity u
	Items []*Item
}
```
