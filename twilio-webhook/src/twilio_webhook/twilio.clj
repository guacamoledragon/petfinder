(ns twilio-webhook.twilio
  (:import (com.twilio.twiml Body Message$Builder MessagingResponse$Builder)))

(defn create-sms
  [body]
  (-> (MessagingResponse$Builder.)
      (.message (-> (Message$Builder.)
                    (.body (Body. body))
                    .build))
      .build
      .toXml))
