(defproject petfinder/lost-pet-lambda "0.1.0-SNAPSHOT"
  :description "An AWS Lambda function that fulfills the ReportLostPet Intent."
  :url "https://github.com/guacamoledragon/petfinder"
  :min-lein-version "2.0.0"
  :dependencies [[amazonica "0.3.101" :exclusions [com.amazonaws/amazon-kinesis-client
                                                   com.amazonaws/aws-java-sdk
                                                   com.amazonaws/dynamodb-streams-kinesis-adapter]]
                 [cheshire "5.7.1"]
                 [com.amazonaws/aws-java-sdk-core "1.11.132" :exclusions [com.fasterxml.jackson.dataformat/jackson-dataformat-cbor
                                                                          joda-time]]
                 [com.amazonaws/aws-java-sdk-lambda "1.11.132"]
                 [com.amazonaws/aws-java-sdk-s3 "1.11.132"]
                 [com.amazonaws/aws-lambda-java-core "1.1.0"]
                 [com.taoensso/timbre "4.10.0"]
                 [org.clojure/clojure "1.8.0"]]
  :plugins [[lein-clj-lambda "0.10.3"]]
  :lambda {"dev" [{:handler       "lost-pet-lambda.core.handler"
                   :function-name "lost-pet-lambda"
                   :memory-size   512
                   :timeout       15
                   :region        "us-east-1"
                   :s3            {:bucket     "petfinder.com"
                                   :object-key "lambda/lost-pet-lambda-0.1.0-SNAPSHOT.jar"}}]}
  :profiles {:dev     {:dependencies   [[org.clojure/tools.namespace "0.2.11"]]
                       :resource-paths ["resources" "dev-resources"]
                       :source-paths   ["dev"]}
             :uberjar {:aot      [lost-pet-lambda.core]
                       :jvm-opts ["-Xmx1g"]}})
