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
               | JSBLOCK


    EXPRESSIONS = {EXPRESSION <WS>?}
    BLOCK = <WS>? <OSQUIGGLY> <WS>? EXPRESSIONS <WS>? <CSQUIGGLY> <WS>? (* squigglys not working*)


    CALL = <OPAREN> EXPRESSIONS <CPAREN>
         | <DOLLAR> <WS> EXPRESSIONS (<EOL> | <NL> | !<DOLLAR>)  (* we want to avoid being greedy for nesting to work*)
    LAMBDA = <PIPE> PARAMLIST <WS>? <ROCKET> <WS> EXPRESSION <PIPE>
    PARAMLIST = {NAME <WS>?}
    JSBLOCK = <BACKTICK> ANYNOTBACKTICK <BACKTICK>

    ASSIGNMENT = <LET> <WS> NAME <WS>? <EQUAL> <WS>? EXPRESSION
    NAME = #'[a-zA-Z]+|\\+|\\*|\\-|\\/'
    WS = #'\\s+'
    
    NL = #'\\n+'
    EOL = #'$'

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
    BACKTICK = '`'
    ANYNOTBACKTICK = #'[^`]+'
    "))
;;    EXPRESSION = OPAREN EXPRESSION CPAREN | NAME EXPRESSION | NAME
(parser "foo bar")




(defmulti next
  first
  )

(defmethod next :PROGRAM [ast]
  (string/join "\n" (map next (rest ast)))
  )
(defmethod next :JSBLOCK [ast]
  (let [js-code (second (second ast))]
    js-code))

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
    (if (empty? args)
      (str (next fn) "()")
      (apply str (next fn) (map #(format "(%s)" %) (map next args))))
    ;; non currying
;    (format "%s(%s)" (next fn) (string/join ", " (map next args)))
    ))

(defmethod next :NAME [ast]
  (let [result
        (second ast)]
    (case result
      "*" "mul"
      "+" "add"
      "-" "sub"
      "/" "div"
      result
      )
    ))

(defmethod next :NUMBER_LITERAL [ast]

  (second ast) ;; emit number literal
  )
(defmethod next :LAMBDA [ast]

  (let [param-list (next (nth ast 1))
        fn-body (next (nth ast 2))]

    ;;curried
    (if (= "" param-list)
      (str "(() => " fn-body ")")
      (str "(" param-list fn-body ")"))))

(defmethod next :PARAMLIST [ast]
  (apply str (map #(format "(%s) => " %) (map second (rest ast)))))

(defmethod next :ASSIGNMENT [ast]
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
