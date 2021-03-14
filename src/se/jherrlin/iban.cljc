(ns se.jherrlin.iban
  "jherrlin.iban is a lib for validating and generating IBANs.
  It contains regexps and specs with generators.

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
   [se.jherrlin.iban.registry :as iban.registry]
   [se.jherrlin.iban.specs]))


(defn union-re-patterns
  "Combine regexps."
  [& patterns]
  (re-pattern (apply str (interpose "|" (map str patterns)))))

#_(defn registry
  "The IBAN registry data structure."
  []
  iban.registry/data)

(defn regexs
  "Takes an optional `registry` and returns a huge regex for all IBANs."
  ([]
   (regexs iban.registry/data))
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
   (regex iban.registry/data id))
  ([registry id]
   (re-pattern (get-in registry [:registry id :iban-regex]))))

(defn regex-strict
  "Strict regex for a single registry entry."
  ([id]
   (regex-strict iban.registry/data id))
  ([registry id]
   (re-pattern (get-in registry [:registry id :iban-regex-strict]))))

(defn info
  "Info about a registry entry."
  ([id]
   (info iban.registry/data id))
  ([registry id]
   (get-in registry [:registry id])))
