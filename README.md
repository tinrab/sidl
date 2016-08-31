# Simple Interface Description Language
[![Build Status](https://travis-ci.org/paidgeek/sidl.svg?branch=master)](https://travis-ci.org/paidgeek/sidl)

## Grammar
```xml
<Document>:
	<EOF>
	namespace <Namespace>
	<Definition> <Document>
<Namespace>:
	<Identifier> : <Namespace>
	<Identifier>
<Definition>:
	type [Identifier] [Identifier]
	type [Identifier] <Type>
	type [Identifier] { <FieldList> }
	enum [Identifier] { <EnumList> }
	enum [Identifier] <IntType> { <EnumList> }
<FieldList>:
	<Field>
	<Field> <ListDelimiter> <FieldList>
<Field>:
	[Identifier] <Type>
	{ <FieldList> }
<Type>:
	[]<PrimaryType>
	[[LITERAL_INTEGER]]<PrimaryType>
	<PrimaryType>
<PrimaryType>:
	[Identifier]
	*[Identifier]
	s | bool | <IntType>
<EnumList>:
	[EnumValue]
	[EnumValue] <ListDelimiter> <EnumList>
<EnumValue>:
	[Identifier]
	[Identifier] = [LITERAL_INTEGER]
<ListDelimiter>:
	<space>
	,
	\n
<IntType>:
	i | i8 | i16 | i32 | i64 | u | u8 | u16 | u32 | u64
```

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
