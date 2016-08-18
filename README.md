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

## Examples

There are no operators, only functions:

    +(1, 3) = 4

Currying is automatic, which is a fancy way of saying that if
you supply less arguments than the total number of arguments a function
takes then it will return a function with the partially applied arguments
already applied. For example:

    +(1) = [a function that takes one argument and adds 1 to it]

Because `+` takes two arguments, and we only supplied 1 we get back a function that 
takes one argument and adds `1` to it.

Lambdas are defined just like they are in JavaScript, except you can omit the parenthesis
for lambdas that take zero arguments.

    my-cool-add-fn = (a, b) => +(a, b)
    some-other-fn = => 3

JavaScript interop is simple. Surround source code in backticks ``` and it will
get spat out into the target source.

    my-fancy-println = (a) => `console.log(a)`

The compiler won't mangle names in JS output, except in the case where it does. For example if you use `+`,
`-`, `/`, `*`, or anytime you use kebab case like in the above examples it will get converted
to a JS friendly name.

    foo-bar-baz becomes foo_bar_baz
    +, -, *, / become add, sub, mul, and div respectively

## Usage

First compile the compiler:

    lein cljsbuild once

Right now you unfortunately need ImmutableJS installed globally:

    npm install -g immutable

Now run the example:

    cat example.🍄 | node out/main.js stdlib.🍄 | node

Yeah that's right, the file ext is an emoji. You're welcome.
New enoki source files can be created with the provided helper:

    bin/mush mynewfile

## TODO

* Strings
* Branching - currently use JS interop for if statements :P woops.
* Maps
* Sets
* Keywords
* EDN support instead of JSON - maybe.

## License

Copyright © 2016 Brian Dawn

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
