## Grammar
```xml
<Document>:
	<EOF>
	namespace <Namespace> <Document>
	<AttributeList> <Definition> <Document>
<Namespace>:
	[IDENTIFIER] . <Namespace>
	[IDENTIFIER]
<Definition>:
	interface [IDENTIFIER] { <FieldList> }
	interface [IDENTIFIER] : [IDENTIFIER] { <FieldList> }
	type [IDENTIFIER] [IDENTIFIER]
	type [IDENTIFIER] <Type>
	type [IDENTIFIER] : [IDENTIFIER] { <FieldList> }
	type [IDENTIFIER] { <FieldList> }
	enum [IDENTIFIER] { <EnumList> }
	enum [IDENTIFIER] <IntType> { <EnumList> }
	service [IDENTIFIER] { <FuncList> }
	service [IDENTIFIER] : [IDENTIFIER] { <FuncList> }
<FieldList>:
	empty
	<Field>
	<Field> <ListDelimiter> <FieldList>
<Field>:
	<AttributeList> [IDENTIFIER] <Type>
	<AttributeList> [IDENTIFIER] { <FieldList> }
<Type>:
	[]<PrimaryType>
	[[LITERAL_INTEGER]]<PrimaryType>
	<PrimaryType>
<PrimaryType>:
	[IDENTIFIER]
	*[IDENTIFIER]
	s | bool | <IntType>
<EnumList>:
	<EnumValue>
	<EnumValue> <ListDelimiter> <EnumList>
<EnumValue>:
	[IDENTIFIER]
	[IDENTIFIER] = [LITERAL_INTEGER]
<AttributeList>:
	empty
	<Attribute>
	<Attribute> <AttributeList>
<Attribute>:
	@ [IDENTIFIER] ( <AttributeEntries> )
	@ [IDENTIFIER]
<AttributeEntries>:
	empty
	<AttributeEntry> <ListDelimiter> <AttributeEntries>
<AttributeEntry>:
	<Literal>
	[IDENTIFIER] = <Literal>
<FuncList>:
	empty
	[IDENTIFIER] ( <ParameterList> ) <Type> <ListDelimiter> <FuncList>
<ParameterList>:
  empty
  [IDENTIFIER] <Type>
  [IDENTIFIER] <Type> , <ParameterList>
<Literal>:
	[LITERAL_INTEGER]
	[LITERAL_STRING]
	[LITERAL_FLOAT]
<ListDelimiter>:
	<space>
	,
	\n
<IntType>:
	i | i8 | i16 | i32 | i64 | u | u8 | u16 | u32 | u64
```
