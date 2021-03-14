(ns se.jherrlin.iban-test
  (:require
   [clojure.spec.alpha :as s]
   #?(:clj [clojure.test :as t])
   #?(:cljs [cljs.test :as t :include-macros true])
   [clojure.test.check.generators :as gen]
   [se.jherrlin.iban :as iban]
   [se.jherrlin.iban.lib.registry :as registry]
   [se.jherrlin.iban.lib.specs]))


(comment
  ;; Generate and run
  (doseq [sexp (->> registry/data
                    :registry
                    vals
                    (map (fn [{:keys [electronic-format-example id]}]
                           `(t/deftest ~(symbol electronic-format-example)
                              (t/is (s/valid? ::iban/iban ~electronic-format-example))
                              (t/is (= (s/conform ::iban/iban ~electronic-format-example)
                                       ~(s/conform ::iban/iban electronic-format-example)))
                              (t/is (= (re-find (iban/regex ~id) ~electronic-format-example)
                                       ~(re-find (iban/regex id) electronic-format-example)))
                              (t/is (= (re-find (iban/regex ~id) ~electronic-format-example)
                                       ~(re-find (iban/regex id) electronic-format-example)))))))]
    (eval sexp))

  (t/run-all-tests)
  )


(t/deftest generators
  (t/is (string? (gen/generate (s/gen ::iban/SE))))
  (t/is (string? (gen/generate (s/gen ::iban/iban)))))

(t/deftest find-in-str
  (t/is (= (re-seq (iban/regexs) "CR05015202001026284066,LC55HEMM000100010012001200023015
,HEJSAN,HOPPSAN,MK07250120000058984")
           '("CR05015202001026284066"
             "LC55HEMM000100010012001200023015"
             "MK07250120000058984"))))

(t/deftest regex
  (t/is (nil? (re-find (iban/regex :LC) "LC55HEMM0001000100")))
  (t/is (= (re-find (iban/regex :LC) "LC55HEMM000100010012001200023015")
           "LC55HEMM000100010012001200023015"))
  (t/is (= (re-find (iban/regex :LC) "SOME TEXT LC55HEMM000100010012001200023015 SOMETHIN MORE")
           "LC55HEMM000100010012001200023015")))

(t/deftest CR05015202001026284066
  (t/is (not (s/valid? :se.jherrlin.iban/iban "CR05015202001")))
  (t/is (s/valid? :se.jherrlin.iban/iban "CR05015202001026284066"))
  (t/is (= (s/conform :se.jherrlin.iban/iban "CR05015202001026284066")
           [:CR "CR05015202001026284066"]))
  (t/is (= (re-find (se.jherrlin.iban/regex :CR) "CR05015202001026284066")
           "CR05015202001026284066")))

(t/deftest MT84MALT011000012345MTLCAST001S
  (t/is (s/valid? :se.jherrlin.iban/iban "MT84MALT011000012345MTLCAST001S"))
  (t/is (= (s/conform :se.jherrlin.iban/iban "MT84MALT011000012345MTLCAST001S")
           [:MT "MT84MALT011000012345MTLCAST001S"]))
  (t/is (= (re-find (se.jherrlin.iban/regex :MT) "MT84MALT011000012345MTLCAST001S")
           "MT84MALT011000012345MTLCAST001S")))

(t/deftest PL61109010140000071219812874
  (t/is (s/valid? :se.jherrlin.iban/iban "PL61109010140000071219812874"))
  (t/is (= (s/conform :se.jherrlin.iban/iban "PL61109010140000071219812874")
           [:PL "PL61109010140000071219812874"]))
  (t/is (= (re-find (se.jherrlin.iban/regex :PL)  "PL61109010140000071219812874")
           "PL61109010140000071219812874")))
