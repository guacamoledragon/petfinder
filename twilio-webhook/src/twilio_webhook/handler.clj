(ns twilio-webhook.handler
  (:require
    [cheshire.core :refer [generate-stream parse-stream]]
    [clojure.java.io :as io]
    [clojure.walk :refer [keywordize-keys]]
    [ring.util.codec :refer [form-decode]]
    [taoensso.timbre :as timbre :refer [info]]
    [twilio-webhook.lex :refer [lex text-request sms]]
    [twilio-webhook.twilio :refer [create-sms]])
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
                      :body            (create-sms "twix!")}
                     writer)))
