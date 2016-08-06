# enoki-lang-clj

Yet another compiles to JS language. I felt the world needed more of these.

Right now the compiler is Clojure, but it should be trivial to leverage CLJS so that
it runs on node.

## Usage

Run the example with leiningen and node.

    cat example.🍄  | lein run | node

Yeah that's right, the file ext is an emoji. You're welcome.

## TODO

* Strings
* Datastructures with immutablejs or mori
* Code comments
* Lazy infinite seqs? Yes plz. Maybe syntax for ranges 1.. or 1..10

## License

Copyright © 2016 Brian Dawn

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
