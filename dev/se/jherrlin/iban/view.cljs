(ns se.jherrlin.iban.view
  (:require
   [clojure.spec.alpha :as s]
   [clojure.test.check.generators :as gen]
   [se.jherrlin.iban :as iban]))

(defn init []
  (println "Hello World"))

(gen/generate (s/gen ::iban/iban))
(gen/generate (s/gen ::iban/SE))
(s/valid? ::iban/iban "LC55HEMM000100010012001200023015")
(iban/regex :SE)
(iban/regexs)
(iban/registry)
(re-find
 (iban/regex :SE)
 "Hej och välkommen till din bank! Här är ditt IBAN: SE4550000000058398257466")
(re-seq
 (iban/regexs)
 "Hej och välkommen till din bank! Här är ditt IBAN: SE4550000000058398257466, LC55HEMM000100010012001200023015")
