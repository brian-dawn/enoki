(defproject enoki-lang-clj "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 ;[instaparse "1.4.2"]
                 [com.lucasbradstreet/instaparse-cljs "1.4.1.2"]
                 [org.clojure/clojurescript "1.9.89"]]
  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-npm "0.6.2"]]
  :npm {:dependencies [[immutable "3.8.1"]]}
  :main "out/main.js"
  :cljsbuild {
    :builds [{
        :source-paths ["src"]
        ; The standard ClojureScript compiler options:
        ; (See the ClojureScript compiler documentation for details.)
        :compiler {
                   :output-to "out/main.js"  ; default: target/cljsbuild-main.js
                   :output-dir "out"
                   
                   :optimizations :simple
                   ;; advanced compilation won't work due to nodejs name munging
                   ;; caused by the closure compiler. D:
                   
                   :target :nodejs
                   :pretty-print true
                   :main enoki-lang-clj.core
                   



                   :cache-analysis true
                   ;:source-map true
                   }}]}

;  :main ^:skip-aot enoki-lang-clj.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
