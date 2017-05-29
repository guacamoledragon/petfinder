(ns twilio-webhook.handler
  (:require [cheshire.core :as cheshire]
            [clojure.java.io :as io]
            [ring.util.codec :as codec]
            [taoensso.timbre :as timbre]
            [twilio-webhook.lex :as webhook.lex]
            [twilio-webhook.s3 :as webhook.s3]
            [twilio-webhook.twilio :as webhook.twilio]
            [clojure.string :as str])
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
                             webhook.twilio/sanitize)
          message        (:body twilio-request)
          media-count    (:num-media twilio-request)
          from           (str/replace (:from twilio-request) #"\+" "")]

      (timbre/info "Incoming message from:" from message)

      (when (pos? media-count)
        (timbre/info "Found MMS from: " from)
        (webhook.s3/send-mms-s3 from (:media-url-0 twilio-request)))

      (let [response    {:isBase64Encoded false
                         :statusCode      200
                         :headers         {:Content-Type "application/xml"}}
            text-result (webhook.lex/send-text-request from message)
            message     (str (if (pos? media-count) "Thanks for the picture!\n")
                             (if (some? text-result) (.getMessage text-result)))
            sms         (webhook.twilio/create-sms message)]
        (cheshire/generate-stream (assoc response :body sms) writer)))))

(comment
  ; Micro-level changelog
  (DONE isolate the behaviour that is needed to proxy SMS to Lex)
  (DONE move all http-server code to dev)
  (DONE create AWS Service Proxy Lambda handler)
  (date "2017-05-22T00:03:22.331Z")

  (DONE use clj-lambda-utils plugin to update lambda)
  (DONE determine when an MMS is received)
  (date "2017-05-28T00:11:04.229Z"))
  ; TODO save to a bucket)
