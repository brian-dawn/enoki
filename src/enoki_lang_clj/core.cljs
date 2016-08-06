(ns enoki-lang-clj.core
  (:require
   [instaparse.core :as insta]
   [clojure.string :as string]
   [goog.string :refer [format]]
   )
  )

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
               | PROGRAM


    EXPRESSIONS = {EXPRESSION <WS>?}
    BLOCK = <OSQUIGGLY> <WS>? EXPRESSIONS <WS>? <CSQUIGGLY> <WS>? (* squigglys not working*)

    PROGRAM = <WS>? EXPRESSIONS <WS>? <EOL>
    COMMAEXPRS = {(EXPRESSION <COMMA> <WS>?)* EXPRESSION}
    CALL = EXPRESSION <OPAREN> COMMAEXPRS <CPAREN>

    LAMBDA = <OPAREN> COMMAEXPRS <CPAREN> <WS>? <ROCKET> <WS>? EXPRESSION
           | <ROCKET> <WS>? EXPRESSION
    PARAMLIST = {NAME <WS>?}
    JSBLOCK = <BACKTICK> ANYNOTBACKTICK <BACKTICK>

    ASSIGNMENT = NAME <WS>? <EQUAL> <WS>? EXPRESSION
    NAME = #'[a-zA-Z]+|\\+|\\*|\\-|\\/'
    WS = #'\\s+'
    
    NL = #'\\n+'
    EOL = #'$'
    EOF = #'\\A'


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
    COMMA = ','
    ANYNOTBACKTICK = #'[^`]+'
    "))


(defmulti nxt
  first)

(defmethod nxt :PROGRAM [ast]
  (string/join "\n" (map nxt (rest ast))))

(defmethod nxt :JSBLOCK [ast]
  (let [js-code (second (second ast))]
    js-code))

(defmethod nxt :BLOCK [ast]
  (str "{\n"
       (string/join "\n" (map nxt (rest ast)))
       "\n}"))

(defmethod nxt :EXPRESSION [ast]
  (string/join "\n" (map nxt (rest ast))))

(defmethod nxt :COMMAEXPRS [ast]
  ;; Returns a list of them.
  (map nxt (rest ast)))

(defmethod nxt :EXPRESSIONS [ast]
  (string/join "\n" (map nxt (rest ast))))

(defmethod nxt :CALL [ast]
  (let [exprs (second ast)
        fn (second exprs)
        args (nxt (nth ast 2))]

    (if (empty? args)
      (str (nxt fn) "()")
      (apply str (nxt fn) (map #(format "(%s)" %) args)))))

(defmethod nxt :NAME [ast]
  (let [result
        (second ast)]
    ;; lets be awful and translate symbols into corresponding valid JS names here.
    (case result
      "*" "mul"
      "+" "add"
      "-" "sub"
      "/" "div"
      result
      )
    ))

(defmethod nxt :NUMBER_LITERAL [ast]
  (second ast))

(defmethod nxt :LAMBDA [ast]

  (if (= 2 (count ast))
    (format "(() => %s)" (nxt (nth ast 1)))
    (let [param-list (nxt (nth ast 1))
          fn-body (nxt (nth ast 2))]

      (println "ha" (apply str (map #(format "(%s) => " %) param-list)))
      ;;curried
      (if (empty? param-list)
        (format "(() => %s)" fn-body)
        (str
         "("
         (apply str (map #(format "(%s) => " %) param-list))
         fn-body
         ")"
         )))))

(defmethod nxt :PARAMLIST [ast]
  (apply str (map #(format "(%s) => " %) (map second (rest ast)))))

(defmethod nxt :ASSIGNMENT [ast]
  (let [assignment-name (nth ast 1)
        righthand-side (nth ast 2)]
    (format "const %s = %s;" (nxt assignment-name) (nxt righthand-side))))

(defn code-gen [parser]
  (nxt parser))

(def fs (js/require "fs"))
(defn read-stdin []
  (.toString (.readFileSync fs "/dev/stdin")))


(enable-console-print!)

(defn -main
  "🍄 goes in, 💩 comes out."
  [& args]
  (println (code-gen (parser (read-stdin)))))

(set! *main-cli-fn* -main)