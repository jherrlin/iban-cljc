(ns se.jherrlin.iban.view
  (:require
   [clojure.spec.alpha :as s]
   [se.jherrlin.iban :as iban]))

(defn init []
  (println "Hello World"))


(s/valid? ::iban/iban "LC55HEMM000100010012001200023015")

(comment
  (init)
  )
