(ns enoki-lang-clj.core-test
  (:require [clojure.test :refer :all]
            [clojure.pprint :refer [pprint]]
            [enoki-lang-clj.core :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
;    (pprint (parser "foo bar baz"))
;    (pprint (parser "(foo (bar baz))"))
;    (pprint (parser "a b => (add a b)"))
;    (pprint (parser "((a b => 3) a call)"))
;    (pprint (parser "a => b => 3"))
;    (pprint (parser "a b => (add a b {a => 3})"))

                                        ;    (println (code-gen (parser "add a $ sub 4")))

    (println (code-gen (parser "|a => |u => 5||")))

    (println "foo bar $ grawl") ;; call foo with bar
    (println (code-gen (parser "(map |a => 4| mylist)")))
    (println (code-gen (parser "$ foo"))) ;; this has to go into prev

    (pprint (code-gen (parser "$ foo bar $ baz wat $ yay")))
    (pprint (code-gen (parser "$ |a => 1| bar $ baz wat $ yay")))
    (pprint (code-gen (parser "|=> 1|")))
    (pprint (code-gen (parser "$ foo")))


    (pprint (code-gen (parser "let x = |x => $ add 1 inc|")))

    (pprint  (parser "{
let x = 3
let y = 5
let inc = |a => $ add a one|
let x = add
}"))
    (println (code-gen (parser "{
let x = 3
let y = 5
let inc = |a => $ add a one|
let x = add
let f = |=> add|



let x = |a b c => foo|

}")))


    
    ;; I don't like these call semantics - we shouldn't need the dollar sign
    ;; because we know the call is the first thing.
    ;; we can make all things be a call then let x = 3 is a function that returns 3? idk
    
    ;; let inc = add ;;a function call with zero arguments due to currying semantics should just return add as a fn
    ;; let inc = add 1 ;; a function call with 1 argument
    ;; let inc = PI 2 ;; a function call against a variable storing 3.14 will fail at runtime.

;; call semantics need to change because currying.
    

    ;; implicit parenthesis?
    ;; inc = add ;; is this a fn call or a value get?
    ;; we could just assume value lookup
    
    ;; then if we want to call inc = $ add
    ;; or inc = (add)

    
;    (pprint (parser "add a b"))
;    (pprint (code-gen (parser "add a b")))



    ;; lets get dollar operator working... maybe?
    (is true)
    ))
