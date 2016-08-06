(ns enoki-lang-clj.core
  (:require
   [instaparse.core :as insta]
   [clojure.string :as string]
;   [cljs.pprint :refer [pp]]
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
               | VECTOR
               | ASSIGNMENT
               | BLOCK
               | JSBLOCK
               | COMMENT


    EXPRESSIONS = {EXPRESSION <WS>?}
    BLOCK = <OSQUIGGLY> <WS>? EXPRESSIONS <WS>? <CSQUIGGLY> <WS>? (* squigglys not working*)
          (*| <WS?> EXPRESSIONS <WS>? <EOF>  TODO this doesn't work *)

    COMMENT = #'#.*'

    COMMAEXPRS = {(EXPRESSION <COMMA> <WS>?)* EXPRESSION}
    CALL = EXPRESSION <OPAREN> COMMAEXPRS <CPAREN>

    LAMBDA = <OPAREN> COMMAEXPRS <CPAREN> <WS>? <ROCKET> <WS>? EXPRESSION
           | <ROCKET> <WS>? EXPRESSION
    PARAMLIST = {NAME <WS>?}
    JSBLOCK = <BACKTICK> ANYNOTBACKTICK <BACKTICK>

    VECTOR = <OSQUARE> COMMAEXPRS <CSQUARE>

    ASSIGNMENT = NAME <WS>? <EQUAL> <WS>? EXPRESSION
    NAME = #'[a-zA-Z]+|\\+|\\*|\\-|\\/'
    WS = #'\\s+'
    
    NL = #'\\n+'
    EOL = #'\\$'

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



    OSQUARE = '['
    CSQUARE = ']'
    "))


(defmulti nxt
  first)

(defmethod nxt :COMMENT [ast])

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

(defmethod nxt :VECTOR [ast]
  (format "Immutable.List.of(%s);" (string/join ", " (first (map nxt (rest ast))))))

(defmethod nxt :NUMBER_LITERAL [ast]
  (second ast))

(defmethod nxt :LAMBDA [ast]

  (if (= 2 (count ast))
    (format "() => %s" (nxt (nth ast 1)))
    (let [param-list (nxt (nth ast 1))
          fn-body (nxt (nth ast 2))]

      ;;curried
      (if (empty? param-list)
        (format "() => %s)" fn-body)
        (str
         (apply str (map #(format "(%s) => " %) param-list))
         fn-body
         )))))

(defmethod nxt :PARAMLIST [ast]
  (apply str (map #(format "(%s) => " %) (map second (rest ast)))))

(defmethod nxt :ASSIGNMENT [ast]
  (let [assignment-name (nth ast 1)
        righthand-side (nth ast 2)]
    (format "const %s = %s;" (nxt assignment-name) (nxt righthand-side))))

(defn code-gen [parser]
  (nxt parser)
  )

(def fs (js/require "fs"))
(defn read-stdin []
  (.toString (.readFileSync fs "/dev/stdin")))


(enable-console-print!)

(defn dev-main
  "🍄 goes in, 💩 comes out."
  [& args]

  (let [src (read-stdin)
        ast (parser src)
        _ (println ast)
        out (code-gen ast)
        ]
    (println "# Input")
    (println src)

    (println)
    (println "# AST")
    (println ast)

    (println)
    (println "# out")
    (println out)))

(defn -main
  "🍄 goes in, 💩 comes out."
  [& args]

  (let [src (read-stdin)
        other-srcs (for [src args]
                     (.readFileSync fs src "utf8"))

        big-src (str "{" (apply str other-srcs) src "}")
        ast (parser big-src)
        ]
    (if (map? ast)
      (println ast)
      (do
        (println "const Immutable = require('immutable');")
        (println
         (code-gen ast))))))

(set! *main-cli-fn* -main)
