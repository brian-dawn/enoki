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

    EXPRESSIONS = {EXPRESSION <WS>?}

    CALL = <OPAREN> EXPRESSIONS <CPAREN>
         | <DOLLAR> <WS> EXPRESSIONS !<DOLLAR>
    LAMBDA = <PIPE> PARAMLIST <WS> <ROCKET> <WS> EXPRESSION <PIPE>
    PARAMLIST = {NAME <WS>?}

    NAME = #'[a-zA-Z]+'
    WS = #'\\s+'

    NUMBER_LITERAL = #'[0-9]+'
    OPAREN = '('
    CPAREN = ')'
    ROCKET = '=>'
    DOLLAR = '$'
    PIPE = '|'

    "))
;;    EXPRESSION = OPAREN EXPRESSION CPAREN | NAME EXPRESSION | NAME
(parser "foo bar")




(defmulti next
  first
  )

(defmethod next :EXPRESSION [ast]
  (string/join "\n" (map next (rest ast))) ;; todo also join?
  ;(map next rest)
  )

(defmethod next :EXPRESSIONS [ast]
  (map next (rest ast)))

(defmethod next :CALL [ast]
  (let [exprs (second ast)
        fn (second exprs)
        args (drop 2 exprs)
        ]
    (format "%s(%s)" (next fn) (string/join ", " (map next args)))))

(defmethod next :NAME [ast]
  (second ast))

(defmethod next :NUMBER_LITERAL [ast]

  (second ast) ;; emit number literal
;  (println "gen" (second ast))
;  (map next (drop 2 ast))
  )
(defmethod next :LAMBDA [ast]

  (let [param-list (nth ast 1)
        fn-body (nth ast 2)
        ]
    (format "function(%s){\n  return %s;\n}" (next param-list) (next fn-body))))

(defmethod next :PARAMLIST [ast]
  (string/join ", " (map second (rest ast))))

(defn code-gen [parser]
  (next parser)
  )


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
