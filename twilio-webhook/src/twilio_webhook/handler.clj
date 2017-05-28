(ns twilio-webhook.handler
  (:require [cheshire.core :as cheshire]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.walk :as walk]
            [ring.util.codec :as codec]
            [taoensso.timbre :as timbre]
            [twilio-webhook.lex :as lex]
            [twilio-webhook.twilio :as twilio])
  (:import (java.io InputStream OutputStream)
           (com.amazonaws.services.lambda.runtime Context))
  (:gen-class
    :methods [^:static [handler [java.io.InputStream java.io.OutputStream com.amazonaws.services.lambda.runtime.Context] void]]))

(defn -handler
  [^InputStream input-stream ^OutputStream output-stream ^Context context]
  (timbre/info "Executing function:" (.getFunctionName context))

  (with-open [output output-stream
              input  input-stream
              writer (io/writer output)
              reader (io/reader input)]
    (let [request        (cheshire/parse-stream reader true)
          twilio-request (-> (get request :body)
                             codec/form-decode
                             walk/keywordize-keys)
          message        (:Body twilio-request)
          from           (str/replace (:From twilio-request) #"\+" "")]

      (timbre/info "Incoming message from:" from message)

      (comment
        "Body can be empty if NumMedia != 0."
        "If NumMedia != 0, then all media files should be downloaded and uploaded to s3-bucket to mms/<from>/<date>.png")

      (cheshire/generate-stream {:isBase64Encoded false
                                 :statusCode      200
                                 :headers         {:Content-Type "application/xml"}
                                 :body            (twilio/create-sms
                                                    (.getMessage (lex/send-text-request from message)))}
                                writer))))

(comment
  ; Micro-level changelog
  (DONE isolate the behaviour that is needed to proxy SMS to Lex)
  (DONE move all http-server code to dev)
  (DONE create AWS Service Proxy Lambda handler)
  (date "2017-05-22T00:03:22.331Z")

  (DONE use clj-lambda-utils plugin to update lambda))
  ;TODO determine when an MMS is received, and save to a bucket)

