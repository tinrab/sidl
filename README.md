# Simple Interface Description Language
[![Build Status](https://travis-ci.org/paidgeek/sidl.svg?branch=master)](https://travis-ci.org/paidgeek/sidl)

## Grammar
```xml
<Schema>:
	<EOF>
	use <Namespace>
	namespace <Namespace>
	<Definition> <Schema>
<Namespace>:
	<Identifier> : <Namespace>
	<Identifier>
<Definition>:
	type [Identifier] [Identifier]
	type [Identifier] <Type>
	type [Identifier] { <FieldList> }
	enum [Identifier] { <EnumList> }
	enum [Identifier] : <IntType> { <EnumList> }
<FieldList>:
	<Field>
	<Field> <ListDelimiter> <FieldList>
<Field>:
	[Identifier] <Type>
	{ <FieldList> }
<Type>:
	[]<Type>
	[[LITERAL_INTEGER]]<Type>
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
```python
use otherns:inner

namespace rpg

enum Quality : uint8 {
	Common = 0,
	Rate,
	Epic
}

type Item {
	Name s
	Quality Quality
	Cost u64
}

type Inventory {
	Capacity i
	Items []*Item #references
}

type Character {
	Name s
	Bag []Item #embedded
	Attributes [8]f32 #array
}

```
