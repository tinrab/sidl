package util

func IsWhitespace(ch rune) bool {
	return ch == ' ' || ch == '\t' || ch == '\n'
}

func IsLetter(ch rune) bool {
	return IsLowerCaseLetter(ch) || IsUpperCaseLetter(ch)
}

func IsLowerCaseLetter(ch rune) bool {
	return ch >= 'a' && ch <= 'z'
}

func IsUpperCaseLetter(ch rune) bool {
	return ch >= 'A' && ch <= 'Z'
}

func IsDigit(ch rune) bool {
	return ch >= '0' && ch <= '9'
}
