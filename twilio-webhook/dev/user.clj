(ns user
  (:require [clojure.tools.namespace.repl :refer [refresh]]
            [org.httpkit.server :as http]
            [ring.middleware.reload :refer [wrap-reload]]
            [twilio-webhook.handler :refer [app-routes]]))

(defonce server (atom nil))

(defn start []
  (let [app (wrap-reload #'app-routes)]
    (reset! server (http/run-server app {:port 3000}))
    (println (str "Listening on port " 3000 "!"))))

(defn stop []
  (when @server
    (@server :timeout 100)
    (reset! server nil)))

(defn restart []
  (stop)
  (refresh :after 'user/start))
