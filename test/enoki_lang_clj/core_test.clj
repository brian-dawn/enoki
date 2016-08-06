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

    (println (code-gen (parser "{

foo(baz, boo)

what()

what(huh)

a(b(c, d, e))

(foo) => 3

=> 3

x = 3

y = => 3

+ = `(a) => (b) => a + b`
inc = add(1)

myfunction = (a, b, c) => a

add(2, 3)

var = {
  hello()

}

}
")))

    #_(println (code-gen (parser "(foo, bar) => foo")))
    (println (code-gen (parser "=>=>=> foo")))

    (println (code-gen (parser "
hello()
world(a,b,c)

")))
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
