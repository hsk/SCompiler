package org.scompiler.syntactic

import org.scompiler.lexer.TokenType._

class PascalGrammarGraph extends GrammarGraph {

  'program ~> { 'program_heading ~ SemiColon ~ 'block ~ Dot }

  'program_heading ~> {
    "PROGRAM" ~ Identifier ~ !(ParenthesisOpen ~ 'identifier_list ~ ParenthesisClose)
  }

  'block ~> {
    'label_declaration_part ~
    'constant_definition_part ~
    'type_definition_part ~
    'var_declaration_part ~
    'procedure_and_function_declaration_part ~
    'statement_part
  }

  'number ~> { NaturalNumber | RealNumber | ScientificNotationNumber }

  'label_declaration_part ~> { !("LABEL" ~ 'label_list ~ SemiColon) }

  'label_list ~> { 'number ~ !(Comma ~ 'label_list) }

  'constant_definition_part ~> { !("CONST" ~ 'constant_list) }

  'constant_list ~> { (Identifier ~ EqualsOperator ~ 'cexpression ~ SemiColon).+ }

  'cexpression ~> { String | ( !('sign) ~ (Identifier | 'number) ) }

  'type_definition_part ~> { !("TYPE" ~ 'type_declaration) }

  'type_declaration ~> { (Identifier ~ EqualsOperator ~ 'type ~ SemiColon).+ }

  'var_declaration_part ~> { !("VAR" ~ 'var_declaration) }

  'var_declaration ~> { ('identifier_list ~ Colon ~ 'type ~ SemiColon).+ }

  'identifier_list ~> { Identifier ~ !(Comma ~ 'identifier_list) }

  'procedure_and_function_declaration_part ~> { ('procedure_and_function_declaration).* }

  'procedure_and_function_declaration ~> {
    (
      ("PROCEDURE" ~ Identifier ~ 'palist) |
      ("FUNCTION" ~ Identifier ~ 'palist ~ Colon ~ Identifier)
    ) ~ SemiColon ~ 'block ~ SemiColon
  }

  'statement_part ~> { "BEGIN" ~ 'statement_list ~ "END" }

  'statement_list ~> { 'statement ~ !(SemiColon ~ 'statement_list) }

  'term ~> {
    'factor ~
    !((MultiplicationOperator | DivisionOperator | "DIV" | "MOD" | "AND") ~ 'term )
  }

  'siexpr ~> { !(AddOperator | MinusOperator) ~ 'siexpr_term }

  'siexpr_term ~> {
    'term  ~ !( (AddOperator | MinusOperator | "OR") ~ 'siexpr_term)
  }

  'expr ~> {
    'siexpr ~
    !(
      ( EqualsOperator |
        SmallerThenOperator |
        SmallerEqualsThenOperator |
        GreaterEqualsThenOperator |
        GreaterThenOperator |
        NotEqualsOperator |
        "IN") ~ 'siexpr )
  }

  'expr_list ~> { 'expr ~ !(Comma ~ 'expr_list) }

  'sitype ~> {
    Identifier |
    (ParenthesisOpen ~ 'identifier_list  ~ ParenthesisClose) |
    ('const_value ~ Range ~ 'const_value)
  }

  'type ~> {
    (Pointer ~ Identifier) |
    (!("PACKED") ~ 'type_branches)
  }

  'type_branches ~> {
    'type_array_branch |
    'type_file_branch |
    'type_set_branch |
    'type_record_branch |
    'sitype
  }

  'type_array_branch ~> { "ARRAY" ~ BracketOpen ~ 'type_array_sitypes ~ BracketClose ~ "OF" ~ 'type }

  'type_array_sitypes ~> { 'sitype ~ !(Comma ~ 'type_array_sitypes) }

  'type_file_branch ~> {"FILE" ~ "OF" ~ 'type }

  'type_set_branch ~> { "SET" ~ "OF" ~ 'sitype }

  'type_record_branch ~> { "RECORD" ~ 'filist ~ "END" }

  'infipo ~> {
    (
      (BracketOpen ~ 'expr_list ~ BracketClose) |
        (Dot ~ Identifier) |
        Pointer
    ).*
  }

  'palist ~> {
    !(ParenthesisOpen ~ 'palist_args ~ ParenthesisClose)
  }

  'palist_args ~> {
    'palist_branches ~ !(SemiColon ~ 'palist_args)
  }

  'palist_branches ~> {
    ("PROCEDURE" ~  'identifier_list) |
    (("FUNCTION" | "VAR") ~ 'identifier_list ~ Colon ~ Identifier)
  }

  'factor ~> {
    (Identifier ~ !((ParenthesisOpen ~ 'expr_list ~ ParenthesisClose) | 'infipo))  | //FUIDEN
//    (Identifier ~ 'infipo) | //VAIDEN
//     Identifier | //COIDEN
     "NIL" |
     'number |
     String |
     (ParenthesisOpen ~ 'expr ~ ParenthesisClose) |
     'factor_expr |
     ("NOT" ~ 'factor)
  }

  'factor_expr ~> {
    BracketOpen ~ !('factor_expr_list) ~ BracketClose
  }

  'factor_expr_list ~> {
    ('expr ~ !(Range ~ 'expr)) ~ !(Comma ~ 'factor_expr_list)
  }

  'filist ~> {
    ('filist_iden_list) ~
    "CASE" ~ !(Identifier ~ Dot) ~ //FIXME: VERIFICAR
    Pointer ~ Identifier ~ "OF" ~ 'filist_list
  }

  'filist_list ~> {
    'filist_const_list ~ !(SemiColon ~ 'filist_list)
  }

  'filist_const_list ~> { ('const_value_list ~ Colon ~ ParenthesisOpen ~ 'filist ~ ParenthesisClose) }

  'filist_iden_list ~> {
    ('identifier_list ~ Colon ~ 'type) ~ !(SemiColon ~ 'filist_iden_list )
  }

  'statement ~> {
    ('number ~ Colon) |
    ("BEGIN" ~ 'statement_list ~ "END") |
    ("GOTO" ~ 'number) |
    ('statement_setvalue_branch) |
    ('statement_priden_branch) |
    ('statement_if_branch) |
    ('statement_case_branch) |
    ('statement_while_branch) |
    ('statement_repeat_branch) |
    ('statement_for_branch) |
    ('statement_with_branch)
  }

  'statement_while_branch ~> { "WHILE" ~ 'expr ~ "DO" ~ 'statement }

  'statement_repeat_branch ~> {
    "REPEAT" ~ 'statement_list ~ "UNTIL" ~ 'expr
  }

  'statement_for_branch ~> {
    "FOR" ~ Identifier ~ (!'infipo) ~ AttributionOperator ~ 'expr ~ ("TO" | "DOWNTO") ~ 'expr ~ "DO" ~ 'statement
  }

  'statement_with_branch ~> { "WITH" ~ 'statement_infipo_list ~ "DO" ~ 'statement }

  'statement_infipo_list ~> {
    ( Identifier ~ !('infipo)) ~ !(Comma ~ 'statement_infipo_list)
  }

  'statement_setvalue_branch ~> {
    ((Identifier ~ 'infipo) | Identifier) ~ AttributionOperator ~ 'expr
  }

  'statement_priden_branch ~> {
    Identifier ~ ParenthesisOpen ~ !('priden_list) ~ ParenthesisClose
  }

  'priden_list ~> {
    ('expr | Identifier) ~ !(Comma ~ 'priden_list)
  }

  'statement_if_branch ~> {
    "IF" ~ 'expr ~ "THEN" ~ 'statement ~ !("ELSE" ~ 'statement)
  }

  'statement_case_branch ~> {
    "CASE" ~ 'expr ~ "OF" ~ 'statement_case_list
  }

  'statement_case_list ~> {
    'statement_case ~
    ( (SemiColon ~ 'statement_case_list) |
      "END" )
  }

  'statement_case ~> { ('const_value_list ~ Colon ~ 'statement) }

  'const_value_list ~> {
    'const_value ~ !(Comma ~ 'const_value_list)
  }

  'const_value ~> {
    String |
    ( !('sign) ~ (Identifier | 'number ) )
  }

  'sign ~> {
    AddOperator | MinusOperator
  }
}
