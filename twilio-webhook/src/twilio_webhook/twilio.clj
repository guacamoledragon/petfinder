(ns twilio-webhook.twilio
  (:require [taoensso.timbre :as timbre])
  (:import (com.twilio.twiml Body Message$Builder MessagingResponse$Builder TwiMLException)))

(defn create-sms
  [body]
  (try
    (-> (MessagingResponse$Builder.)
        (.message (-> (Message$Builder.)
                      (.body (Body. body))
                      .build))
        .build
        .toXml)
    (catch TwiMLException ex
      (do
        (timbre/error ex "Failed to create TwiML")
        "EOF"))))
