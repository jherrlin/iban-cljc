(ns se.jherrlin.iban.specs
  "Specs and generators in this namespace is auto generated."
  (:require
   [clojure.spec.alpha :as s]
   [clojure.string :as str]
   [clojure.test.check.generators :as gen]
   [se.jherrlin.iban.registry :as registry]))

(defn- alfa-range [start stop length]
  (gen/fmap str/join (gen/vector (gen/fmap char (gen/choose start stop)) length)))

(def upper-case (partial alfa-range 65 90))
(def numbers    (partial alfa-range 48 57))

(defn blank [length]
  (gen/fmap str/join (gen/vector (gen/return " ") length)))

(defn alfa-numeric [length]
  (gen/fmap str/join
            (gen/vector
             (gen/fmap char
                       (gen/one-of [(gen/choose 48 57)
                                    (gen/choose 65 90)
                                    (gen/choose 97 122)]))
             length)))

(def convetions-map
  "Conversion map from structure char to regexp."
  {"n" numbers
   "a" upper-case
   "c" alfa-numeric
   "e" blank})

(defn structure-regexps [structure]
  (->> structure
       (re-seq #"(\d+)(!?)([nace])")
       (map (fn [[_ len fixed c]] [(Integer. len) (seq fixed) c]))))

(doseq [s-exp (->> registry/registry
                   :registry
                   (vals)
                   (sort-by :id)
                   (map (fn [{:keys [iban-regex-strict id iban-structure]}]
                         `(s/def ~(keyword (str "se.jherrlin.iban/" (name id)))
                            (s/with-gen
                              (s/and string?
                                     ~#(re-find (re-pattern ~iban-regex-strict) ~'%))
                              ~#(->> iban-structure
                                      structure-regexps
                                      (map (fn [[len _ c]]
                                             ((get convetions-map c) len)))
                                      (cons (gen/return (subs iban-structure 0 2)))
                                      (apply gen/tuple)
                                      (gen/fmap str/join))
                              )))))]
  (eval s-exp))

(eval
 `(clojure.spec.alpha/def :se.jherrlin.iban/iban
    ~(->> registry/registry
          :registry
          (vals)
          (sort-by :id)
          (map (fn [{:keys [id]}]
                 `(~id ~(keyword (str "se.jherrlin.iban/" (name id))))))
          (apply concat)
          (concat `(s/or)))))
