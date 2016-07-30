# Simple Schema Definition Language
[![Build Status](https://travis-ci.org/paidgeek/ssdl.svg?branch=master)](https://travis-ci.org/paidgeek/ssdl)

## Example
```go
enum Quality { Common, Rate, Epic }

type Item {
	Name string
	Cost uint64
	Quality Quality
}

type Inventory {
	Capacity int
	Items []*Item # these are references
}

type Character {
	Name string
	Holding []Item # embedded items
}
```
