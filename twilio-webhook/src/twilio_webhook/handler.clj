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
    (let [request (-> reader
                      (cheshire/parse-stream true)
                      :body
                      codec/form-decode
                      walk/keywordize-keys)
          message (:Body request)
          from (str/replace (:From request) #"\+" "")]

      (timbre/info "Incoming message from:" from message)

      (cheshire/generate-stream {:isBase64Encoded false
                                 :statusCode      200
                                 :headers         {:Content-Type "application/xml"}
                                 :body            (twilio/create-sms
                                                    (.getMessage (lex/send-text-request from message)))}
                                writer))))

(comment
  (DONE isolate the behaviour that is needed to proxy SMS to Lex)
  (DONE move all http-server code to dev)
  (DONE create AWS Service Proxy Lambda handler)

  (date "2017-05-22T00:03:22.331Z"))
  ;TODO use clj-lambda-utils plugin to update lambda
  ;TODO determine when an MMS is received, and save to a bucket)

