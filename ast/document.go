package ast

type Document struct {
	Definitions []Definition
}

func (n *Document) Accept(visitor Visitor) {
	visitor.VisitDocument(n)
}
