(ns se.jherrlin.iban.specs
  "Specs in this namespace is auto generated."
  (:require
   [clojure.spec.alpha :as s]
   [se.jherrlin.iban.registry :as registry]))


(doseq [s-exp (->> registry/registry
                   :registry
                   (vals)
                   (sort-by :id)
                   (map (fn [{:keys [iban-regex-strict id]}]
                          `(s/def ~(keyword (str "se.jherrlin.iban/" (name id)))
                             (s/and string? (fn [~'s] (re-find ~iban-regex-strict ~'s)))))))]
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
