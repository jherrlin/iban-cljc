(ns se.jherrlin.iban.registry.parser
  "This file is for development purposes and contains dependencies that are only
  needed when developing.

  This file takes care of download and parsing of the IBAN PDF and returns a
  registry data structure. The registry data structure is serialazable."
  (:require
   [clj-http.client :as client]
   [clojure.java.io :as io]
   [clojure.string :as str]
   [pdfboxing.text :as text]
   [se.jherrlin.iban.registry]))


(def CountryName    #"(?<=Name of country)(?<CountryName>\s+\b[A-Z].*\v)")
(def CountryCode    #"(?<=IBAN prefix country code \(ISO 3166\))(?<CountryCode>\s+\b[A-Z]{2}\b)")
(def IBANstructure  #"(?<=IBAN structure)(?<IBANstructure>\s+\b[A-Z]{2}.*\b)")
(def IBANlength     #"(?<=IBAN length)(?<IBANlength>\s+\b\d*\b)")
(def EffectiveDate  #"(?<=Effective date)(?<EffectiveDate>\s+\b[A-Z][a-z]+-\d{2}\b)")
(def IBANelectronic #"(?<=IBAN electronic format example)(?<IBANelectronic>\s+\b[A-Z]{2}.*\b)")
(def IBANprint      #"(?<=IBAN print format example)(?<IBANprint>\s+\b[A-Z]{2}.*\v)")

(defn write-file!
  "Write file to local filesystem."
  [file bytes*]
  (with-open [w (java.io.BufferedOutputStream. (java.io.FileOutputStream. file))]
    (.write w bytes*)))

(defn save-pdf-file!
  "If response is a PDF file, save it.

  Returns full file path as a string."
  [response]
  (let [filename (re-find #"(?<=filename=\").*(?=\")"
                          (get-in response
                                  [:headers "Content-Disposition"]
                                  "filename=\"SWIFT_IBAN_Registry.pdf\""))
        file-with-path (str "/tmp/" filename)]
    (write-file! file-with-path (:body response))
    file-with-path))

(defn request-pdf!
  "Request a PDF via GET request from `url`."
  [url]
  (let [response (client/get url {:as :byte-array})]
    (if (and (str/includes? (get-in response [:headers "Content-Type"]) "pdf")
             (bytes? (:body response)))
      response
      (throw (Exception. "Response if not a PDF or it's not a bytes body")))))

(defn pdf->text
  "Read PDF file from `file-path` and extract text."
  [file-path]
  (text/extract file-path))

(defn union-re-patterns
  "Combine regexps."
  [& patterns]
  (re-pattern (apply str (interpose "|" (map str patterns)))))

(defn re-matches->m
  "Parse registry foundings into map."
  [xs]
  {:country                   (nth xs 0)
   :id                        (-> xs (nth 1) (keyword))
   :iban-structure            (nth xs 2)
   :iban-regex                nil
   :iban-regex-strict         nil
   :iban-length               (-> xs (nth 3) (Integer.))
   :effective-date            (nth xs 4)
   :electronic-format-example (nth xs 5)
   :print-format-example      (nth xs 6)})

(def conversion-map
  "Conversion map from structure char to regexp."
  {"n" "\\d"            ;; Digits (numeric characters 0 to 9 only)
   "a" "[A-Z]"          ;; Upper case letters (alphabetic characters A-Z only)
   "c" "[a-zA-Z0-9]"    ;; upper and lower case alphanumeric characters (A-Z, a-z and 0-9)
   "e" "\\s"})          ;; blank space

(defn build-regex-atom
  "Build the smallest piece of the regex."
  [[_ lenght fixed-length reg]]
  (let [fixed-length (seq fixed-length)]
    [(get conversion-map reg) "{" (when-not fixed-length "0,") lenght "}"]))

(defn parse-structure
  "Find the structure patterns in structure string."
  [structure]
  (re-seq #"(\d+)(!?)([nace])" structure))

(defn build-iban-regex
  "Build the full registry regex from structure."
  [structure]
  (->> (parse-structure structure)
       (map build-regex-atom)
       (cons (subs structure 0 2))
       (map (partial apply str))
       (reduce str)))

(defn add-regex
  "Add registry regex to iban map."
  [{:keys [iban-structure] :as m}]
  (let [regex (build-iban-regex iban-structure)]
    (assoc m
           :iban-regex        regex
           :iban-regex-strict (str "^" regex "$"))))

(defn self-validate
  "The registry contains examples, so validate the built regex against that."
  [{:keys [electronic-format-example iban-regex iban-length] :as m}]
  (assoc m :self-validate (->> (re-find (re-pattern iban-regex) electronic-format-example)
                               (count)
                               (= iban-length))))

(defn release-and-year
  "Find release and date of publication in `pdf-text`."
  [pdf-text]
  (re-find #"Release\s*\d*.*[A-Z][a-z]*\s*\d{4}" pdf-text))

(defn registry
  "Parse the PDF text to a registry."
  [pdf-text]
  (->> pdf-text
       (re-seq (union-re-patterns
                CountryName
                CountryCode
                IBANstructure
                IBANlength
                EffectiveDate
                IBANelectronic
                IBANprint))
       (map (comp str/trim first))
       (partition-all 7)
       (map re-matches->m)
       (map add-regex)
       (map self-validate)
       (map (juxt :id identity))
       (into {})))

(defn iban-registry!
  "Download iban registry PDF, parse it and return a iban registry map."
  [url]
  (let [pdf-text (->> url
                      (request-pdf!)
                      (save-pdf-file!)
                      (pdf->text))]
    {:url      url
     :info     (release-and-year pdf-text)
     :registry (registry pdf-text)}))

(comment
  (def iban-registry-from-online
    (get (iban-registry! "https://www.swift.com/resource/iban-registry-pdf") :registry))

  (def iban-registry-from-resource
    (registry
     (text/extract
      (.getFile (io/resource "SWIFT_IBAN_Registry-downloaded-2021-03-06.pdf")))))

  (= iban-registry-from-online
     iban-registry-from-resource
     (:registry se.jherrlin.iban.registry/data))

  (->> iban-registry-from-online
       :registry
       (vals)
       (map :self-validate))
  )
