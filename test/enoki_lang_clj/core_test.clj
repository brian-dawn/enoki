(ns enoki-lang-clj.core-test
  (:require [clojure.test :refer :all]
            [clojure.pprint :refer [pprint]]
            [enoki-lang-clj.core :refer :all]))

(deftest a-test
  (testing "Currying"
    (is (= "foo(bar)(baz)"
           (code-gen (parser "foo(bar, baz)"))))

    #_(println (code-gen (parser "{

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




    ))
