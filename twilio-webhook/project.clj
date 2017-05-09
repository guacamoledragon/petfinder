(defproject petfinder/twilio-webhook "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[compojure "1.5.1"]
                 [org.clojure/clojure "1.8.0"]
                 [ring/ring-defaults "0.2.1"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler twilio-webhook.handler/app}
  :profiles
  {:dev {:dependencies [[http-kit "2.2.0"]
                        [javax.servlet/servlet-api "2.5"]
                        [org.clojure/tools.namespace "0.2.11"]
                        [ring/ring-devel "1.6.0"]
                        [ring/ring-mock "0.3.0"]]
         :source-paths ["dev"]}})
