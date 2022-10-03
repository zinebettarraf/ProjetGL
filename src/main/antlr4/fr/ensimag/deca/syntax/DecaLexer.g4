lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
}

// Deca lexer rules.

//caratere de formatage
EOL: '\n' { skip(); };
ESPACE: ' ' { skip(); };
TAB: '\t' {skip();};
RETOUR: '\r' {skip();};

//Mots reserves
ASM: 'asm';
CLASS: 'class';
EXTENDS: 'extends';
ELSE: 'else';
FALSE: 'false';
IF: 'if';
INSTANCEOF: 'instanceof';
NEW: 'new';
NULL: 'null';
READINT: 'readInt';
READFLOAT: 'readFloat';
PRINT: 'print';
PRINTLN: 'println';
PRINTLNX: 'printlnx';
PRINTX: 'printx';
PROTECTED: 'protected';
RETURN: 'return';
THIS: 'this';
TRUE: 'true';
WHILE: 'while';


//Identificateurs
fragment LETTER: 'a' .. 'z' | 'A' .. 'Z'; // A FAIRE : Règle bidon qui reconnait tous les caractères.
                // A FAIRE : Il faut la supprimer et la remplacer par les vraies règles.
fragment DIGIT: '0' .. '9';
IDENT: (LETTER | '$' | '_')(LETTER | DIGIT | '$' | '_')*;

// Litteraux entiers
fragment POSITIVE_DIGIT: '1' .. '9';
INT: '0' | POSITIVE_DIGIT  DIGIT*;

//Symbole speciaux
EQUALS: '=';
PLUS: '+';
MINUS: '-';
TIMES: '*';
SLASH: '/';
EXCLAM: '!';
OPARENT: '(';
CPARENT: ')';
OBRACE: '{';
CBRACE: '}';
SEMI: ';';
DOT: '.';
COMMA: ',';
AND: '&&';
OR: '||';
EQEQ: '==';
NEQ: '!=';
LEQ: '<=';
GEQ: '>=';
LT: '<';
GT: '>';
PERCENT: '%';



//Litteraux flottant
fragment NUM: DIGIT+;
fragment SIGN: '+' | '-';
fragment EXP: ('E' | 'e') SIGN NUM;
fragment DEC: NUM '.' NUM;
fragment FLOATDEC: (DEC | DEC EXP) ('F' | 'f' |  );
fragment DIGITHEX: '0' .. '9' | 'A' .. 'F' | 'a' .. 'f';
fragment NUMHEX: DIGITHEX+;
fragment FLOATHEX: ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f' |  );
FLOAT: FLOATDEC | FLOATHEX;

//Chaine de caracteres
fragment STRING_CAR: ~('"' | '\\' | '\n');
STRING: '"' (STRING_CAR | '\\"' | '\\\\')* '"';
MULTI_LINE_STRING: '"' (STRING_CAR | EOL | '\\"' | '\\\\')* '"';

//Commentaires
fragment COMM : '//' (~('\n'))*;
COMMENT: ( COMM | '/*' .*? '*/' ){ skip(); };

//inclusion fichier
fragment FILENAME: (LETTER | DIGIT | '.' | '-' | '_')+;
INCLUDE: '#include' (' ')* '"' FILENAME '"' {doInclude(getText());};

//DEFAULT: .;