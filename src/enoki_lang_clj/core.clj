(ns enoki-lang-clj.core
  (:require
   [instaparse.core :as insta]
   [clojure.string :as string])
  
  (:gen-class))
"B = 'b'+
// a grammar in JSON
var grammar = {
               \"lex\": {
                       \"rules\": [
                                 [\"\\\\s+\", \"/* skip whitespace */\"],
                                 // [\"\\\\s+\", \"return 'WS';\"],
                                 [\"[a-zA-Z]+\", \"return 'NAME';\"],
                                 [\"[0-9]+\", \"return 'NUMBER_LITERAL';\"],
                                 [\"\\\\[\", \"return 'OBRACKET';\"],
                                 [\"\\\\]\", \"return 'CBRACKET';\"],
                                 [\"\\\\{\", \"return 'OSQUIGGLY';\"],
                                 [\"\\\\}\", \"return 'CSQUIGGLY';\"],
                                 [\"\\\\(\", \"return 'OPAREN';\"],
                                 [\"\\\\)\", \"return 'CPAREN';\"],
                                 [\"\\\\|\", \"return 'BAR';\"],
                                 [\"\\=\\>\", \"return 'ROCKET';\"],
                                 [\"\\=\", \"return 'EQUAL';\"],
                                 [\"let\", \"return 'LET';\"],
                                 [\"[a-f0-9]+\", \"return 'HEX';\"]]},
               \"bnf\": {
                       \"expressions\": [\"expression expressions\", \"expression\"],
                       \"expression\": [\"OPAREN expressions CPAREN\",
                                      \"lambda\",
                                      \"NAME\",
                                      \"literal\",
                                      ]}}"
"
    PARAMLIST = NAME
              | NAME <WS> PARAMLIST
"

(def parser
  (insta/parser
   "


    EXPRESSION = CALL
               | NAME 
               | NUMBER_LITERAL
               | LAMBDA
               | ASSIGNMENT
               | BLOCK

    EXPRESSIONS = {EXPRESSION <WS>?}
    BLOCK = <OSQUIGGLY> <WS>? EXPRESSIONS <WS>? <CSQUIGGLY> (* squigglys not working*)

    CALL = <OPAREN> EXPRESSIONS <CPAREN>
         | <DOLLAR> <WS> EXPRESSIONS !<DOLLAR> (* we want to avoid being greedy for nesting to work*)
    LAMBDA = <PIPE> PARAMLIST <WS>? <ROCKET> <WS> EXPRESSION <PIPE>
    PARAMLIST = {NAME <WS>?}

    ASSIGNMENT = <LET> <WS> NAME <WS>? <EQUAL> <WS>? EXPRESSION
    NAME = #'[a-zA-Z]+'
    WS = #'\\s+'

    NUMBER_LITERAL = #'[0-9]+'
    OPAREN = '('
    CPAREN = ')'
    ROCKET = '=>'
    DOLLAR = '$'
    PIPE = '|'
    EQUAL = '='
    LET = 'let'
    OSQUIGGLY = '{'
    CSQUIGGLY = '}'
    "))
;;    EXPRESSION = OPAREN EXPRESSION CPAREN | NAME EXPRESSION | NAME
(parser "foo bar")




(defmulti next
  first
  )

(defmethod next :BLOCK [ast]
  (string/join "\n" (map next (rest ast)))
  )
(defmethod next :EXPRESSION [ast]
  (string/join "\n" (map next (rest ast))) ;; todo also join?
  ;(map next rest)
  )

(defmethod next :EXPRESSIONS [ast]
  (string/join "\n" (map next (rest ast))))

(defmethod next :CALL [ast]
  (let [exprs (second ast)
        fn (second exprs)
        args (drop 2 exprs)]
    ;; currying
    (apply str (next fn) (map #(format "(%s)" %) (map next args)))
    ;; non currying
;    (format "%s(%s)" (next fn) (string/join ", " (map next args)))
    ))

(defmethod next :NAME [ast]
  (second ast))

(defmethod next :NUMBER_LITERAL [ast]

  (second ast) ;; emit number literal
;  (println "gen" (second ast))
;  (map next (drop 2 ast))
  )
(defmethod next :LAMBDA [ast]

  (let [param-list (nth ast 1)
        fn-body (nth ast 2)]

    ;; non curried
    ;(format "function(%s){\n  return %s;\n}" (next param-list) (next fn-body))

    ;;curried
    (str (next param-list) (next fn-body))))

(defmethod next :PARAMLIST [ast]
  (apply str (map #(format "(%s) => " %) (map second (rest ast)))))

(defmethod next :ASSIGNMENT [ast]
  (println "assign" ast)
  (let [assignment-name (nth ast 1)
        righthand-side (nth ast 2)
        ]
    (format "var %s = %s;" (next assignment-name) (next righthand-side))
    )

  )
(defn code-gen [parser]
  (next parser)
  )


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
