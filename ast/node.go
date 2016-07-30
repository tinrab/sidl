package ast

type Node interface {
	Accept(visitor Visitor)
}
