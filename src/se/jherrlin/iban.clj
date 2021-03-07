(ns se.jherrlin.iban
  (:require
   [se.jherrlin.iban.registry :as registry]
   [se.jherrlin.iban.specs]))


(defn union-re-patterns
  "Combine regexps."
  [& patterns]
  (re-pattern (apply str (interpose "|" (map str patterns)))))

(defn regexs
  "Takes an optional `registry` and returns a huge regex for all IBANs."
  ([]
   (regexs registry/registry))
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
   (regex registry/registry id))
  ([registry id]
   (re-pattern (get-in registry [:registry id :iban-regex]))))

(defn regex-strict
  "Strict regex for a single registry entry."
  ([id]
   (regex-strict registry/registry id))
  ([registry id]
   (re-pattern (get-in registry [:registry id :iban-regex-strict]))))

(defn info
  "Info about a registry entry."
  ([id]
   (info registry/registry id))
  ([registry id]
   (get-in registry [:registry id])))



(comment
  (regexs)
  (regex :SV)
  )
