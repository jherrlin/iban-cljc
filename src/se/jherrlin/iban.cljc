(ns se.jherrlin.iban
  "jherrlin.iban is a lib for validating and generating IBANs.
  Generating is only available on JVM Clojure. It contains regexps and specs
  with generators.

  Each country included in the registry have a different IBAN structure.
  Validation and generating can be made on all of them or on specific entries.

  Specs are namespaced here.

  Example:
  :se.jherrlin.iban/iban
  :se.jherrlin.iban/SE
  :se.jherrlin.iban/PL

  Example usage:
  (s/valid? ::iban/iban \"LC55HEMM000100010012001200023015\")  ;; Validate a IBAN.
  (s/valid? ::iban/SE \"LC55HEMM000100010012001200023015\")    ;; Validate Swe IBAN.
  (s/conform ::iban/iban \"LC55HEMM000100010012001200023015\") ;; Conform string.
  (gen/generate (s/gen ::iban/iban))                           ;; Generate random IBAN.
  (gen/generate (s/gen ::iban/SE))                             ;; Generate Swe IBAN.
  (iban/regexs)                                                ;; Regex that matches all IBANs.
  (iban/regex :SE)                                             ;; Regex to match Swe IBANs."
  (:require
   [se.jherrlin.iban.lib.registry :as registry]
   se.jherrlin.iban.lib.specs))

(defn union-re-patterns
  "Combine regexps."
  [& patterns]
  (re-pattern (apply str (interpose "|" (map str patterns)))))

(defn registry
  "The IBAN registry data structure."
  []
  registry/data)

(defn regexs
  "Takes an optional `registry` and returns a huge regex for all IBANs."
  ([]
   (regexs (registry)))
  ([registry]
   (->> registry
        :registry
        (vals)
        (sort-by :id)
        (map :iban-regex)
        (apply union-re-patterns))))

(defn regex
  "Regex for a single registry entry."
  ([id]
   (regex (registry) id))
  ([registry id]
   (re-pattern (get-in registry [:registry id :iban-regex]))))

(defn regex-strict
  "Strict regex for a single registry entry."
  ([id]
   (regex-strict (registry) id))
  ([registry id]
   (re-pattern (get-in registry [:registry id :iban-regex-strict]))))

(defn info
  "Info about a registry entry."
  ([id]
   (info (registry) id))
  ([registry id]
   (get-in registry [:registry id])))
