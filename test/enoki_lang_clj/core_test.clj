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
    (pprint (parser "$ foo bar $ baz wat"))
    (pprint (code-gen (parser "$ foo bar $ baz wat $ yay"))) ;; this has to go into prev 

    ;; lets get dollar operator working... maybe?
    (is true)
    ))
