# Simple Schema Definition Language
[![Build Status](https://travis-ci.org/paidgeek/ssdl.svg?branch=master)](https://travis-ci.org/paidgeek/ssdl)
[![Go Report Card](https://goreportcard.com/badge/github.com/paidgeek/ssdl)](https://goreportcard.com/report/github.com/paidgeek/ssdl)

## Grammar
```
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
<IdentifierList>:
    [Identifier]
    [Identifier] <ListDelimiter> <IdentifierList>
<ListDelimiter>:
    ,
    \n
```

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
