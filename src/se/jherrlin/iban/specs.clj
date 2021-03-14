(ns se.jherrlin.iban.specs
  "Specs and generators in this namespace is auto generated.

  Spec keywords are pointing at the se.jherrlin.iban namespace."
  (:require
   [clojure.spec.alpha :as s]
   [clojure.string :as str]
   [clojure.test.check.generators :as gen]
   [se.jherrlin.iban.registry :as registry]))


(defn upper-case
  "Generator returns a `length` long uppercase alpha [A-Z] string."
  [length]
  (gen/fmap str/join (gen/vector (gen/fmap char (gen/choose 65 90)) length)))

(defn numbers
  "Generator returns a `length` long number [0-9] string."
  [length]
  (gen/fmap str/join (gen/vector (gen/fmap char (gen/choose 48 57)) length)))

(defn blank
  "Generator returns a `length` long empty [ ] string."
  [length]
  (gen/fmap str/join (gen/vector (gen/return " ") length)))

(defn alfa-numeric
  "Generator returns a `length` long alphanumeric [a-zA-Z0-9] string."
  [length]
  (gen/fmap str/join
            (gen/vector
             (gen/fmap char
                       (gen/one-of [(gen/choose 48 57)
                                    (gen/choose 65 90)
                                    (gen/choose 97 122)]))
             length)))

(def conversions-map
  "Conversion map from structure char to regexp."
  {"n" numbers
   "a" upper-case
   "c" alfa-numeric
   "e" blank})

(defn structure-regexps
  "Parse the IBAN structure string into a data structure."
  [structure]
  (->> structure
       (re-seq #"(\d+)(!?)([nace])")
       (map (fn [[_ len fixed c]] [(Integer. len) (seq fixed) c]))))

(doseq [s-exp (->> registry/data
                   :registry
                   (vals)
                   (map (fn [{:keys [iban-regex-strict id iban-structure]}]
                          `(s/def ~(keyword (str "se.jherrlin.iban/" (name id)))
                             (s/and string?
                                      (fn [~'s] (re-find (re-pattern ~iban-regex-strict) ~'s))))))
                   doall)]
  (eval s-exp))

(eval
 `(clojure.spec.alpha/def :se.jherrlin.iban/iban
    ~(->> registry/data
          :registry
          (vals)
          (map (fn [{:keys [id]}]
                 `(~id ~(keyword (str "se.jherrlin.iban/" (name id))))))
          (apply concat)
          (concat `(s/or)))))
