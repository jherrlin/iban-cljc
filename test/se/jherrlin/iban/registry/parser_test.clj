(ns se.jherrlin.iban.registry.parser-test
  (:require [clojure.java.io :as io]
            [clojure.test :as t]
            [pdfboxing.text :as text]
            [se.jherrlin.iban.lib.registry :as registry]
            [se.jherrlin.iban.registry.parser :as parser]))


(t/deftest current-registry-against-just-parsed
  (t/is (= (get registry/data :registry)
           (parser/registry
            (text/extract
             (.getFile (io/resource "SWIFT_IBAN_Registry-downloaded-2021-03-06.pdf")))))))
