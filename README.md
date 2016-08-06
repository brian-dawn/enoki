# enoki-lang 🍄

Yet another compiles to JS language.

All I really want from programming languages is immutability by default,
I also wanted to have fun with automatic currying.

The compiler is written in CLJS but should compile to clean JS that could
be ran in a web browser.

## Features

* None of the extra baggage of JS.
* Immutability.
* No operators, only functions.
* Automatic currying.
* Simple JS interop.
* No semicolons.
* Hard to type file extensions.

## Usage

First compile the compiler:

    lein cljsbuild once

Now run the example:

    cat example.🍄  | node out/main.js | node

Yeah that's right, the file ext is an emoji. You're welcome.

## TODO

* Strings
* Datastructures with immutablejs or mori
* Code comments
* Lazy infinite seqs? Yes plz. Maybe syntax for ranges 1.. or 1..10
* EDN support instead of JSON - maybe.

## License

Copyright © 2016 Brian Dawn

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
