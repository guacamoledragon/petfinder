(ns twilio-webhook.handler
  (:require [cheshire.core :refer [generate-stream parse-stream]]
            [clojure.java.io :as io]
            [clojure.walk :refer [keywordize-keys]]
            [ring.util.codec :refer [form-decode]]
            [taoensso.timbre :as timbre :refer [info]]
            [twilio-webhook.lex :as lex]
            [twilio-webhook.twilio :as twilio]
            [clojure.string :as str])
  (:import (java.io InputStream OutputStream)
           (com.amazonaws.services.lambda.runtime Context))
  (:gen-class
    :methods [^:static [handler [java.io.InputStream java.io.OutputStream com.amazonaws.services.lambda.runtime.Context] void]]))

(defn -handler
  [^InputStream input-stream ^OutputStream output-stream ^Context context]
  (info "Executing function:" (.getFunctionName context))

  (with-open [output output-stream
              input input-stream
              writer (io/writer output)
              reader (io/reader input)]
    (let [request (-> reader
                      (parse-stream true)
                      :body
                      form-decode
                      keywordize-keys)
          message (:Body request)
          from (str/replace (:From request) #"\+" "")]

      (info "Incoming message from:" from message)

      (generate-stream {:isBase64Encoded false
                        :statusCode      200
                        :headers         {:Content-Type "application/xml"}
                        :body            (twilio/create-sms
                                           (.getMessage (lex/send-text-request from message)))}
                       writer))))

(comment
  (DONE isolate the behaviour that is needed to proxy SMS to Lex)
  (DONE move all http-server code to dev)
  (DONE create AWS Service Proxy Lambda handler)

  (date "2017-05-22T00:03:22.331Z")
  (TODO determine when an MMS is received, and save to a bucket))
