package org.scompiler.syntactic

import org.scompiler.lexer.TokenType._

class PascalGrammarGraph extends GrammarGraph {

  'file ~> { 'program | 'module }

  'program ~> { 'program_heading ~ SemiColon ~ 'block ~ Dot }

  'program_heading ~> {
    'Program ~ 'identifier ~ !(ParenthesisOpen ~ 'identifier_list ~ ParenthesisClose)
  }

  'identifier_list ~> { ('identifier_list ~ Comma ~ 'identifier) | 'identifier }

  'block ~> {
    'label_declaration_part ~
    'constant_definition_part ~
    'type_definition_part ~
    'variable_declaration_part ~
    'procedure_and_function_declaration_part ~
    'statement_part
  }

  'module ~> {
    'constant_definition_part ~
    'type_definition_part ~
    'variable_declaration_part ~
    'procedure_and_function_declaration_part
  }

  'label_declaration_part ~> { !('LABEL ~ 'label_list ~ SemiColon) }

  'label_list ~> { ('label_list ~ Comma ~ 'label) | 'label }

  'label ~> { NaturalNumber }

  'constant_definition_part ~> { !('CONST ~ 'constant_list) }

  'constant_list ~> { ('constant_list ~ 'constant_definition) | 'constant_definition }

  'constant_definition ~> { 'identifier ~ EqualsOperator ~ 'cexpression ~ SemiColon }

  'cexpression ~> { 'csimple_expression | ( 'csimple_expression ~ 'relop ~ 'csimple_expression) }

  'csimple_expression ~> { 'cterm | ( 'csimple_expression ~ 'addop ~ 'cterm ) }

  'cterm ~> { 'cfactor | ( 'cterm ~ 'mulop ~ 'cfactor ) }

  'cfactor ~> { ('sign ~ 'cfactor) | 'cexponentiation }

}
