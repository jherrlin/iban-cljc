(ns se.jherrlin.iban.lib.specs
  "Specs and generators in this namespace is auto generated.

  Spec keywords are pointing at the se.jherrlin.iban namespace."
  (:require
   [clojure.spec.alpha :as s]
   [clojure.string :as str]
   [clojure.test.check.generators :as gen]
   [se.jherrlin.iban.lib.registry :as registry]))


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

(def parse-int #?(:clj  #(Character/digit % 10)
                  :cljs #(js/parseInt %)))

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
       (map (fn [[_ len fixed c]] [(parse-int len) (seq fixed) c]))))

(comment
  ;; Used to generate the s-exps below.
  (->> registry/data
       :registry
       (vals)
       (map (fn [{:keys [iban-regex-strict id iban-structure]}]
              `(s/def ~(keyword (str "se.jherrlin.iban/" (name id)))
                 (s/with-gen
                   (s/and string?
                          (fn [~'s] (re-find (re-pattern ~iban-regex-strict) ~'s)))
                   (fn [] (->> ~iban-structure
                               structure-regexps
                               (map (fn [[~'len ~'_ ~'c]]
                                      ((get conversions-map ~'c) ~'len)))
                               (cons (gen/return (subs ~iban-structure 0 2)))
                               (apply gen/tuple)
                               (gen/fmap str/join)))))))
       doall)

  `(clojure.spec.alpha/def :se.jherrlin.iban/iban
     ~(->> registry/data
           :registry
           (vals)
           (map (fn [{:keys [id]}]
                  `(~id ~(keyword (str "se.jherrlin.iban/" (name id))))))
           (apply concat)
           (concat `(s/or))))
  )

;; This s-exps are auto generated. Dont manually change them!
;; They are here so that cljs can pick them up at compile time.
(clojure.spec.alpha/def
  :se.jherrlin.iban/TL
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^TL\\d{2}\\d{3}\\d{14}\\d{2}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "TL2!n3!n14!n2!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "TL2!n3!n14!n2!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/SA
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^SA\\d{2}\\d{2}[a-zA-Z0-9]{18}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "SA2!n2!n18!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "SA2!n2!n18!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/LU
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^LU\\d{2}\\d{3}[a-zA-Z0-9]{13}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "LU2!n3!n13!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "LU2!n3!n13!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/PT
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^PT\\d{2}\\d{4}\\d{4}\\d{11}\\d{2}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "PT2!n4!n4!n11!n2!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "PT2!n4!n4!n11!n2!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/KW
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^KW\\d{2}[A-Z]{4}[a-zA-Z0-9]{22}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "KW2!n4!a22!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "KW2!n4!a22!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/IL
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^IL\\d{2}\\d{3}\\d{3}\\d{13}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "IL2!n3!n3!n13!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "IL2!n3!n3!n13!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/QA
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^QA\\d{2}[A-Z]{4}[a-zA-Z0-9]{21}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "QA2!n4!a21!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "QA2!n4!a21!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/SC
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^SC\\d{2}[A-Z]{4}\\d{2}\\d{2}\\d{16}[A-Z]{3}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "SC2!n4!a2!n2!n16!n3!a"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "SC2!n4!a2!n2!n16!n3!a" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/BE
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^BE\\d{2}\\d{3}\\d{7}\\d{2}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "BE2!n3!n7!n2!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "BE2!n3!n7!n2!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/VA
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^VA\\d{2}\\d{3}\\d{15}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "VA2!n3!n15!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "VA2!n3!n15!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/GT
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^GT\\d{2}[a-zA-Z0-9]{4}[a-zA-Z0-9]{20}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "GT2!n4!c20!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "GT2!n4!c20!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/MC
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^MC\\d{2}\\d{5}\\d{5}[a-zA-Z0-9]{11}\\d{2}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "MC2!n5!n5!n11!c2!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "MC2!n5!n5!n11!c2!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/SK
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^SK\\d{2}\\d{4}\\d{6}\\d{10}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "SK2!n4!n6!n10!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "SK2!n4!n6!n10!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/IT
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^IT\\d{2}[A-Z]{1}\\d{5}\\d{5}[a-zA-Z0-9]{12}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "IT2!n1!a5!n5!n12!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "IT2!n1!a5!n5!n12!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/CY
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^CY\\d{2}\\d{3}\\d{5}[a-zA-Z0-9]{16}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "CY2!n3!n5!n16!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "CY2!n3!n5!n16!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/GB
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^GB\\d{2}[A-Z]{4}\\d{6}\\d{8}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "GB2!n4!a6!n8!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "GB2!n4!a6!n8!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/PS
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^PS\\d{2}[A-Z]{4}[a-zA-Z0-9]{21}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "PS2!n4!a21!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "PS2!n4!a21!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/HR
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^HR\\d{2}\\d{7}\\d{10}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "HR2!n7!n10!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "HR2!n7!n10!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/DK
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^DK\\d{2}\\d{4}\\d{9}\\d{1}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "DK2!n4!n9!n1!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "DK2!n4!n9!n1!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/IQ
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^IQ\\d{2}[A-Z]{4}\\d{3}\\d{12}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "IQ2!n4!a3!n12!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "IQ2!n4!a3!n12!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/SE
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^SE\\d{2}\\d{3}\\d{16}\\d{1}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "SE2!n3!n16!n1!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "SE2!n3!n16!n1!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/DO
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^DO\\d{2}[a-zA-Z0-9]{4}\\d{20}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "DO2!n4!c20!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "DO2!n4!c20!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/LC
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^LC\\d{2}[A-Z]{4}[a-zA-Z0-9]{24}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "LC2!n4!a24!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "LC2!n4!a24!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/GL
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^GL\\d{2}\\d{4}\\d{9}\\d{1}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "GL2!n4!n9!n1!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "GL2!n4!n9!n1!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/ST
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^ST\\d{2}\\d{4}\\d{4}\\d{11}\\d{2}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "ST2!n4!n4!n11!n2!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "ST2!n4!n4!n11!n2!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/AE
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^AE\\d{2}\\d{3}\\d{16}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "AE2!n3!n16!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "AE2!n3!n16!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/JO
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^JO\\d{2}[A-Z]{4}\\d{4}[a-zA-Z0-9]{18}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "JO2!n4!a4!n18!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "JO2!n4!a4!n18!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/SM
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^SM\\d{2}[A-Z]{1}\\d{5}\\d{5}[a-zA-Z0-9]{12}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "SM2!n1!a5!n5!n12!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "SM2!n1!a5!n5!n12!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/FO
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^FO\\d{2}\\d{4}\\d{9}\\d{1}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "FO2!n4!n9!n1!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "FO2!n4!n9!n1!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/SV
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^SV\\d{2}[A-Z]{4}\\d{20}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "SV2!n4!a20!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "SV2!n4!a20!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/GI
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^GI\\d{2}[A-Z]{4}[a-zA-Z0-9]{15}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "GI2!n4!a15!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "GI2!n4!a15!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/MU
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern
         "^MU\\d{2}[A-Z]{4}\\d{2}\\d{2}\\d{12}\\d{3}[A-Z]{3}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "MU2!n4!a2!n2!n12!n3!n3!a"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "MU2!n4!a2!n2!n12!n3!n3!a" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/AL
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^AL\\d{2}\\d{8}[a-zA-Z0-9]{16}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "AL2!n8!n16!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "AL2!n8!n16!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/LY
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^LY\\d{2}\\d{3}\\d{3}\\d{15}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "LY2!n3!n3!n15!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "LY2!n3!n3!n15!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/IS
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^IS\\d{2}\\d{4}\\d{2}\\d{6}\\d{10}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "IS2!n4!n2!n6!n10!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "IS2!n4!n2!n6!n10!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/LB
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^LB\\d{2}\\d{4}[a-zA-Z0-9]{20}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "LB2!n4!n20!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "LB2!n4!n20!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/ME
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^ME\\d{2}\\d{3}\\d{13}\\d{2}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "ME2!n3!n13!n2!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "ME2!n3!n13!n2!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/LV
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^LV\\d{2}[A-Z]{4}[a-zA-Z0-9]{13}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "LV2!n4!a13!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "LV2!n4!a13!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/SI
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^SI\\d{2}\\d{5}\\d{8}\\d{2}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "SI2!n5!n8!n2!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "SI2!n5!n8!n2!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/MD
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^MD\\d{2}[a-zA-Z0-9]{2}[a-zA-Z0-9]{18}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "MD2!n2!c18!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "MD2!n2!c18!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/RO
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^RO\\d{2}[A-Z]{4}[a-zA-Z0-9]{16}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "RO2!n4!a16!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "RO2!n4!a16!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/AD
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^AD\\d{2}\\d{4}\\d{4}[a-zA-Z0-9]{12}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "AD2!n4!n4!n12!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "AD2!n4!n4!n12!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/BA
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^BA\\d{2}\\d{3}\\d{3}\\d{8}\\d{2}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "BA2!n3!n3!n8!n2!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "BA2!n3!n3!n8!n2!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/VG
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^VG\\d{2}[A-Z]{4}\\d{16}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "VG2!n4!a16!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "VG2!n4!a16!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/RS
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^RS\\d{2}\\d{3}\\d{13}\\d{2}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "RS2!n3!n13!n2!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "RS2!n3!n13!n2!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/XK
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^XK\\d{2}\\d{4}\\d{10}\\d{2}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "XK2!n4!n10!n2!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "XK2!n4!n10!n2!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/MK
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^MK\\d{2}\\d{3}[a-zA-Z0-9]{10}\\d{2}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "MK2!n3!n10!c2!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "MK2!n3!n10!c2!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/NL
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^NL\\d{2}[A-Z]{4}\\d{10}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "NL2!n4!a10!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "NL2!n4!a10!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/BR
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern
         "^BR\\d{2}\\d{8}\\d{5}\\d{10}[A-Z]{1}[a-zA-Z0-9]{1}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "BR2!n8!n5!n10!n1!a1!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "BR2!n8!n5!n10!n1!a1!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/EE
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^EE\\d{2}\\d{2}\\d{2}\\d{11}\\d{1}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "EE2!n2!n2!n11!n1!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "EE2!n2!n2!n11!n1!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/CR
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^CR\\d{2}\\d{4}\\d{14}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "CR2!n4!n14!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "CR2!n4!n14!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/TR
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^TR\\d{2}\\d{5}\\d{1}[a-zA-Z0-9]{16}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "TR2!n5!n1!n16!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "TR2!n5!n1!n16!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/LT
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^LT\\d{2}\\d{5}\\d{11}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "LT2!n5!n11!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "LT2!n5!n11!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/IE
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^IE\\d{2}[A-Z]{4}\\d{6}\\d{8}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "IE2!n4!a6!n8!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "IE2!n4!a6!n8!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/ES
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^ES\\d{2}\\d{4}\\d{4}\\d{1}\\d{1}\\d{10}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "ES2!n4!n4!n1!n1!n10!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "ES2!n4!n4!n1!n1!n10!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/AT
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^AT\\d{2}\\d{5}\\d{11}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "AT2!n5!n11!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "AT2!n5!n11!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/BH
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^BH\\d{2}[A-Z]{4}[a-zA-Z0-9]{14}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "BH2!n4!a14!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "BH2!n4!a14!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/GE
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^GE\\d{2}[A-Z]{2}\\d{16}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "GE2!n2!a16!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "GE2!n2!a16!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/TN
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^TN\\d{2}\\d{2}\\d{3}\\d{13}\\d{2}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "TN2!n2!n3!n13!n2!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "TN2!n2!n3!n13!n2!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/BG
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^BG\\d{2}[A-Z]{4}\\d{4}\\d{2}[a-zA-Z0-9]{8}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "BG2!n4!a4!n2!n8!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "BG2!n4!a4!n2!n8!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/NO
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^NO\\d{2}\\d{4}\\d{6}\\d{1}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "NO2!n4!n6!n1!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "NO2!n4!n6!n1!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/PK
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^PK\\d{2}[A-Z]{4}[a-zA-Z0-9]{16}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "PK2!n4!a16!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "PK2!n4!a16!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/GR
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^GR\\d{2}\\d{3}\\d{4}[a-zA-Z0-9]{16}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "GR2!n3!n4!n16!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "GR2!n3!n4!n16!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/AZ
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^AZ\\d{2}[A-Z]{4}[a-zA-Z0-9]{20}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "AZ2!n4!a20!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "AZ2!n4!a20!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/MR
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^MR\\d{2}\\d{5}\\d{5}\\d{11}\\d{2}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "MR2!n5!n5!n11!n2!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "MR2!n5!n5!n11!n2!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/FR
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^FR\\d{2}\\d{5}\\d{5}[a-zA-Z0-9]{11}\\d{2}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "FR2!n5!n5!n11!c2!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "FR2!n5!n5!n11!c2!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/PL
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^PL\\d{2}\\d{8}\\d{16}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "PL2!n8!n16!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "PL2!n8!n16!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/FI
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^FI\\d{2}\\d{3}\\d{11}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "FI2!n3!n11!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "FI2!n3!n11!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/MT
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^MT\\d{2}[A-Z]{4}\\d{5}[a-zA-Z0-9]{18}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "MT2!n4!a5!n18!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "MT2!n4!a5!n18!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/BY
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^BY\\d{2}[a-zA-Z0-9]{4}\\d{4}[a-zA-Z0-9]{16}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "BY2!n4!c4!n16!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "BY2!n4!c4!n16!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/CH
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^CH\\d{2}\\d{5}[a-zA-Z0-9]{12}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "CH2!n5!n12!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "CH2!n5!n12!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/EG
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^EG\\d{2}\\d{4}\\d{4}\\d{17}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "EG2!n4!n4!n17!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "EG2!n4!n4!n17!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/CZ
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^CZ\\d{2}\\d{4}\\d{6}\\d{10}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "CZ2!n4!n6!n10!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "CZ2!n4!n6!n10!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/KZ
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^KZ\\d{2}\\d{3}[a-zA-Z0-9]{13}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "KZ2!n3!n13!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "KZ2!n3!n13!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/HU
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^HU\\d{2}\\d{3}\\d{4}\\d{1}\\d{15}\\d{1}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "HU2!n3!n4!n1!n15!n1!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "HU2!n3!n4!n1!n15!n1!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/UA
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^UA\\d{2}\\d{6}[a-zA-Z0-9]{19}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "UA2!n6!n19!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "UA2!n6!n19!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/DE
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^DE\\d{2}\\d{8}\\d{10}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "DE2!n8!n10!n"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "DE2!n8!n10!n" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
  :se.jherrlin.iban/LI
  (clojure.spec.alpha/with-gen
    (clojure.spec.alpha/and
     clojure.core/string?
     (clojure.core/fn
       [s]
       (clojure.core/re-find
        (clojure.core/re-pattern "^LI\\d{2}\\d{5}[a-zA-Z0-9]{12}$")
        s)))
    (clojure.core/fn
      []
      (clojure.core/->>
       "LI2!n5!n12!c"
       se.jherrlin.iban.lib.specs/structure-regexps
       (clojure.core/map
        (clojure.core/fn
          [[len _ c]]
          ((clojure.core/get se.jherrlin.iban.lib.specs/conversions-map c) len)))
       (clojure.core/cons
        (clojure.test.check.generators/return
         (clojure.core/subs "LI2!n5!n12!c" 0 2)))
       (clojure.core/apply clojure.test.check.generators/tuple)
       (clojure.test.check.generators/fmap clojure.string/join)))))
(clojure.spec.alpha/def
 :se.jherrlin.iban/iban
 (clojure.spec.alpha/or
  :TL
  :se.jherrlin.iban/TL
  :SA
  :se.jherrlin.iban/SA
  :LU
  :se.jherrlin.iban/LU
  :PT
  :se.jherrlin.iban/PT
  :KW
  :se.jherrlin.iban/KW
  :IL
  :se.jherrlin.iban/IL
  :QA
  :se.jherrlin.iban/QA
  :SC
  :se.jherrlin.iban/SC
  :BE
  :se.jherrlin.iban/BE
  :VA
  :se.jherrlin.iban/VA
  :GT
  :se.jherrlin.iban/GT
  :MC
  :se.jherrlin.iban/MC
  :SK
  :se.jherrlin.iban/SK
  :IT
  :se.jherrlin.iban/IT
  :CY
  :se.jherrlin.iban/CY
  :GB
  :se.jherrlin.iban/GB
  :PS
  :se.jherrlin.iban/PS
  :HR
  :se.jherrlin.iban/HR
  :DK
  :se.jherrlin.iban/DK
  :IQ
  :se.jherrlin.iban/IQ
  :SE
  :se.jherrlin.iban/SE
  :DO
  :se.jherrlin.iban/DO
  :LC
  :se.jherrlin.iban/LC
  :GL
  :se.jherrlin.iban/GL
  :ST
  :se.jherrlin.iban/ST
  :AE
  :se.jherrlin.iban/AE
  :JO
  :se.jherrlin.iban/JO
  :SM
  :se.jherrlin.iban/SM
  :FO
  :se.jherrlin.iban/FO
  :SV
  :se.jherrlin.iban/SV
  :GI
  :se.jherrlin.iban/GI
  :MU
  :se.jherrlin.iban/MU
  :AL
  :se.jherrlin.iban/AL
  :LY
  :se.jherrlin.iban/LY
  :IS
  :se.jherrlin.iban/IS
  :LB
  :se.jherrlin.iban/LB
  :ME
  :se.jherrlin.iban/ME
  :LV
  :se.jherrlin.iban/LV
  :SI
  :se.jherrlin.iban/SI
  :MD
  :se.jherrlin.iban/MD
  :RO
  :se.jherrlin.iban/RO
  :AD
  :se.jherrlin.iban/AD
  :BA
  :se.jherrlin.iban/BA
  :VG
  :se.jherrlin.iban/VG
  :RS
  :se.jherrlin.iban/RS
  :XK
  :se.jherrlin.iban/XK
  :MK
  :se.jherrlin.iban/MK
  :NL
  :se.jherrlin.iban/NL
  :BR
  :se.jherrlin.iban/BR
  :EE
  :se.jherrlin.iban/EE
  :CR
  :se.jherrlin.iban/CR
  :TR
  :se.jherrlin.iban/TR
  :LT
  :se.jherrlin.iban/LT
  :IE
  :se.jherrlin.iban/IE
  :ES
  :se.jherrlin.iban/ES
  :AT
  :se.jherrlin.iban/AT
  :BH
  :se.jherrlin.iban/BH
  :GE
  :se.jherrlin.iban/GE
  :TN
  :se.jherrlin.iban/TN
  :BG
  :se.jherrlin.iban/BG
  :NO
  :se.jherrlin.iban/NO
  :PK
  :se.jherrlin.iban/PK
  :GR
  :se.jherrlin.iban/GR
  :AZ
  :se.jherrlin.iban/AZ
  :MR
  :se.jherrlin.iban/MR
  :FR
  :se.jherrlin.iban/FR
  :PL
  :se.jherrlin.iban/PL
  :FI
  :se.jherrlin.iban/FI
  :MT
  :se.jherrlin.iban/MT
  :BY
  :se.jherrlin.iban/BY
  :CH
  :se.jherrlin.iban/CH
  :EG
  :se.jherrlin.iban/EG
  :CZ
  :se.jherrlin.iban/CZ
  :KZ
  :se.jherrlin.iban/KZ
  :HU
  :se.jherrlin.iban/HU
  :UA
  :se.jherrlin.iban/UA
  :DE
  :se.jherrlin.iban/DE
  :LI
  :se.jherrlin.iban/LI))
