(ns twilio-webhook.twilio
  (:require [taoensso.timbre :refer [error]])
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
        (error ex "Failed to create TwiML")
        "EOF"))))
