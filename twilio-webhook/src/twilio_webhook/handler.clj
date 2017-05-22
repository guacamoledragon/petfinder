(ns twilio-webhook.handler
  (:require
    [cheshire.core :refer [generate-stream parse-stream]]
    [clojure.java.io :as io]
    [clojure.walk :refer [keywordize-keys]]
    [ring.util.codec :refer [form-decode]]
    [taoensso.timbre :as timbre :refer [info]]
    [twilio-webhook.lex :refer [lex text-request sms]])
  (:import (java.io InputStream OutputStream)
           (com.amazonaws.services.lambda.runtime Context)
           (com.twilio.twiml Body Message$Builder MessagingResponse$Builder))
  (:gen-class
    :methods [^:static [handler [java.io.InputStream java.io.OutputStream com.amazonaws.services.lambda.runtime.Context] void]]))

(defn ->twiml
  [message]
  (-> (MessagingResponse$Builder.)
      (.message (-> (Message$Builder.)
                    (.body (Body. message))
                    .build))
      .build))

(defn -handler
  [^InputStream input-stream ^OutputStream output-stream ^Context context]
  (info "Executing function:" (.getFunctionName context))

  (with-open [output output-stream
              input input-stream
              writer (io/writer output)
              reader (io/reader input)]

    (info "Incoming message:"
          (-> reader
              (parse-stream true)
              :body
              form-decode
              keywordize-keys
              :Body))

    (generate-stream {:isBase64Encoded false
                      :statusCode      200
                      :headers         {:Content-Type "application/xml"}
                      :body            (.toXml (->twiml "twix!"))}
                     writer)))
