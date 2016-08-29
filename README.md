# Simple Schema Definition Language
[![Build Status](https://travis-ci.org/paidgeek/ssdl.svg?branch=master)](https://travis-ci.org/paidgeek/ssdl)

## Grammar
```xml
<Document>:
    <EOF>
    <Definition> <Document>
<Definition>:
    type [Identifier] [Identifier]
    type [Identifier] [Type]
    type [Identifier] { <FieldList> }
    enum [Identifier] { <IdentifierList> }
<FieldList>:
    <Field>
    <Field> <ListDelimiter> <FieldList>
<Field>:
    [Identifier] [Type]
    { <FieldList> }
<IdentifierList>:
    [Identifier]
    [Identifier] <ListDelimiter> <IdentifierList>
<ListDelimiter>:
    <space>
    ,
    \n
```

## Example
```python
enum Quality { Common, Rate, Epic }

type Item {
	Name string
	Quality Quality
    Cost u64
	# inner type
	Buff {
	    Attribute i
	    Amount f64
	}
}

type Inventory {
	Capacity i
	Items []*Item # references
}

type Character {
	Name s
	Bag []Item # embedded
}
```
