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
	struct [IDENTIFIER] { <StructFieldList> }
	class [IDENTIFIER] : [IDENTIFIER] { <FieldList> }
	class [IDENTIFIER] { <ClassFieldList> }
	enum [IDENTIFIER] { <EnumList> }
	enum [IDENTIFIER] <IntType> { <EnumList> }
	service [IDENTIFIER] { <FuncList> }
	service [IDENTIFIER] : [IDENTIFIER] { <FuncList> }
<StructFieldList>:
	empty
	<StructField>
	<StructField> <ListDelimiter> <StructFieldList>
<StructField>:
  <AttributeList> [IDENTIFIER] <Type> # <Type>: only scalar types and other structs
<ClassFieldList>:
	empty
	<ClassField>
	<ClassField> <ListDelimiter> <ClassFieldList>
<ClassField>:
	<AttributeList> [IDENTIFIER] <Type>
<Type>:
	[] <PrimaryType> # vector
	[ [LITERAL_INTEGER] ] <PrimaryType> # array
	< <PrimaryType> , <PrimaryType> > # map
	<PrimaryType>
<PrimaryType>:
	[IDENTIFIER]
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
