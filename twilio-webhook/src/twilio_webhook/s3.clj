(ns twilio-webhook.s3
  (:require [amazonica.aws.s3 :as s3]
            [org.httpkit.client :as http]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [taoensso.timbre :as timbre]))

(def bucket-name "petfinder.com")

(def key-prefix "mms")


(defn uuid
  "Create UUID"
  []
  (str (java.util.UUID/randomUUID)))


(defn get-filename
  "Extracts the filename from a content disposition string."
  [content-disposition]
  (when-some [[_ extension] (re-find #"filename=\".*(\..*)\"" content-disposition)]
    (str (uuid) extension)))


(defn download-mms
  "Simple wrapper over http-kit to make a get request for the MMS url."
  [mms-url]
  @(http/get mms-url {:as :byte-array}))


(defn send-mms-s3
  [from mms-url]
  (let [mms-response (download-mms mms-url)]
    (if (= 200 (:status mms-response))
      (with-open [input-stream (io/input-stream (:body mms-response))]
        (let [filename (get-filename (get-in mms-response [:headers :content-disposition]))
              key (str/join "/" [key-prefix from filename])
              content-length (Integer/parseInt (get-in mms-response [:headers :content-length]))]
          (timbre/info "Sending MMS to S3:" key)
          (s3/put-object :bucket-name bucket-name
                         :key key
                         :input-stream input-stream
                         :metadata {:content-length content-length})
          filename))
      (timbre/error "Could not retrieve MMS\n"
                    (String. ^bytes (:body mms-response))))))
