(ns twilio-webhook.twilio
  (:require [camel-snake-kebab.core :as csk]
            [taoensso.timbre :as timbre])
  (:import (com.twilio.twiml Body Message$Builder MessagingResponse$Builder TwiMLException)))

(defn- numberify
  [m]
  (let [f (fn [[k v]] (try [k (Integer/parseInt v)]
                           (catch NumberFormatException _ [k v])))]
    (into {} (map f m))))

(defn- kebabize-keys
  "Transforms all map keys from strings to kebabified keywords."
  [m]
  (let [f (fn [[^String k v]] [(csk/->kebab-case-keyword k) v])]
    (into {} (map f m))))

(defn sanitize
  [twilio-request]
  (-> twilio-request
      kebabize-keys
      numberify))

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
