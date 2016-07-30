package ast

type Document struct {
	Definitions []Node
}

func (n *Document) Accept(visitor Visitor) {
	visitor.VisitDocument(n)
}
