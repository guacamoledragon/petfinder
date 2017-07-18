# Pet Finder
> A service used to report a lost pet.

![petfinder](./logo/petfinder.png)

## Motivation

In my community, there are several lost pet reports. They all occur on different mediums Craigslist, Nextdoor, Facebook, etc. In order for someone to be able to post their pet, they have to log into these services and post the information there.

What this service aims to do, is to provide a bot that the user can interact with via SMS. That way as soon as they see that their little one is missing, the owner can text a number and fill in the information required. Then when someone hears about the missing pet, they'll get a notification. No need to install apps or go to websites.

## Usage

To use the bot, the user can send an SMS to (505) 207-3377 letting them know that they've lost their pet.

![petfinder-demo](./petfinder-demo.gif)

## Technical Description

For this implementation, we really wanted to user to be able to upload a picture of their fur baby. So we created a Twilio webhook that would intercept messages between Lex and the user. If an MMS was detected, the picture attached would be given a UUID and then stored into S3. That UUID would be used to satisfy the Lex slot.

Unfortunately, we weren't able to complete the rest of the service, but the idea was that the image would then be given to Rekognition to further extract details and provide more metadata about the picture provided.

All of the pieces were written in Clojure.

## License

Copyright © 2017 Guacamole Dragon, LLC

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

## Attributions

- [puppy](https://thenounproject.com/search/?q=dog&i=875858) by Alina Oleynik from the [Noun Project](https://thenounproject.com)
- [Search](https://thenounproject.com/search/?q=magnyfing%20glass&i=158234) by Gregor Cresnar from the [Noun Project](https://thenounproject.com)
