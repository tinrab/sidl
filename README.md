# Simple Schema Definition Language
[![Build Status](https://travis-ci.org/paidgeek/ssdl.svg?branch=master)](https://travis-ci.org/paidgeek/ssdl)

## Grammar
```xml
<Document>:
    <EOF>
    <Definition> <Document>
<Definition>:
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
    ,
    \n
```

## Example
```python
enum Quality { Common, Rate, Epic }

type Item {
	Name string
	Quality Quality
    Cost uint64
	# inner type
	Buff {
	    Attribute int
	    Amount float64
	}
}

type Inventory {
	Capacity int
	Items []*Item # references
}

type Character {
	Name string
	Bag []Item # embedded
}
```
