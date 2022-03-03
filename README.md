# JabberwockyInterpreter
A java implementation of a Jabberwocky language interpreter. This program accepts a command-line argument denoting the name of a .txt file containing a JABBERWOCK program.
_Author: Dakota Kallas
_
**The rules of the Jabberwocky language are detailed below:**

The JABBERWOCK language supports a single data type: a JABBERWOCK-string. There are no numbers, booleans or objects. JABBERWOCK supports only one type of statement: an assignment. 
There are no conditionals, loops, methods or method calls. As with all languages, an assignment statement assigns a value to a variable. The syntax of the language is described 
informally and rigorously below. In this definition, symbols given in red font are literal while those elements given in black font denote abstract language elements.

1. A JABBERWOCK-string literal is a sequence of one-or-more consecutive -, *, or ? characters. A JABBERWOCK-string literal is a JABBERWOCK-expression.
2. The word undefined is a JABBERWOCK-expression.
3. A sequence of alphabetic characters is a variable name. Variable names are case sensitive. A variable name is a JABBERWOCK-expression.
4. If E1 and E2 are JABBERWOCK-expressions then each of the following is a JABBERWOCK-expression involving a binary operator.
   a. E1 & E2
   b. E1 || E2
   c. E1 # E2
5. A variable name followed by a JABBERWOCK-expression is a JABBERWORK-assignment-statement.
6. A JABBERWOCK-program is a sequence of JABBERWOCK-assignment-statements having exactly one statement per line.
Note that there is at least one space (but possible many) between any two consecutive language elements. For example, -*?*#* is not a valid JABBERWOCK-expression because there 
is no space between the operator [#] and the operands. However, -*?* # * is a valid JABBERWOCK-expression.

**The following ad-hoc rules describe the semantics of a JABBERWOCK-expression.**

1. There are only two kinds of values in this language: JABBERWOCK-strings and the literal undefined.
2. JABBERWOCK-strings have a maximum length of 2022 symbols. If any literal or operation would produce a JABBERWOCK-string of length 2023 or greater, that operation or literal 
   produces the value undefined instead.
3. Any operation that operates on the value undefined produces the value undefined.
4. The value of a JABBERWOCK-string literal is itself (except see 2 above).
5. The value of the literal undefined is itself.
6. The value of a variable is the value that has been most recently associated with that variable in previous statements. If a variable has not been previously assigned a value,
   the value is undefined.
7. The value of the expressions given in the syntactic definitions of 4 above are obtained by application of an operator to the values of the operands as defined below. The 
   binary operators are right-associative and all have the same precedence.
8. & denotes the JABBERWOCK-string concatenation-without-duplicate-sequencing operator. This operator will a) peform the concatenation of the two operands and b) reduce any 
   sequence of multiple occurrences of a letter into a single occurrence. For example, ** & *?--- yields the JABBERWOCK-string *?-.
9. || denotes the JABBERWOCK-string interleaving operator. This operator interleaves the symbols of the two JABBERWOCK-string operators to produce a JABBERWOCK-string. If the 
   length of the two operands are unequal, the "unmatched" symbols of the longest JABBERWOCK-string are merely copied to the result. Consider the following examples for clarity: 
   -- || ** => -*-* and **-- || ?? => *?*?--
10. # denotes the JABBERWOCK-string splice operator where the first operand is spliced into the center of the second operand. The idea is to split the second operand in two 
   equal or nearly-equal halves and insert the first operand into the center. If the second operands length is not even, the first segment is the shorter of the two halves. By 
   way of example, ** # ------ yields ---**--- and ** # ----- yields --**---.
11. A JABBERWOCK program is executed one statement at a time in the order they occur in the program file. The output of a JABBERWORK program is the set of all variables that 
   have been assigned a value in the program along with their values. These variables are printed in alphabetically ascending order.
