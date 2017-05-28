(ns user
  (:require [clojure.java.io :as io]
            [clojure.tools.namespace.repl :refer [refresh]]
            [org.httpkit.server :as http]
            [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
            [ring.middleware.reload :refer [wrap-reload]]
            [twilio-webhook.handler :as webhook])
  (:import (java.io ByteArrayOutputStream)
           (com.amazonaws.services.lambda.runtime Context)))

(def server nil)

(defn start [])

(defn stop [])

(defn restart []
  (stop)
  (refresh :after 'user/start))

(def sample-input-stream
  (io/input-stream "resources/api-gateway-proxy.json"))

(def dummy-context
  (reify
    Context
    (getFunctionName [this] "dummy")))

(defn test-handler
  []
  (with-open [input sample-input-stream
              output (ByteArrayOutputStream.)]
    (webhook/-handler input output dummy-context)
    (-> output
        .toByteArray
        String.)))
