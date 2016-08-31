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
