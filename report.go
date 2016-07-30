package main

import "fmt"

func reportf(position Position, format string, a ...interface{}) string {
	return fmt.Sprintf("%d:%d ", position.Line, position.Column) + fmt.Sprintf(format + "\n", a...)
}
