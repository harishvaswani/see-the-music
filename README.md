# SeeTheMusic

SeeTheMusic is an app that uses IBM Watson's Natural Language Classifier API to demonstrate retrieving relevant images for a song based on it's lyrics.

It uses the following APIs:
- IBM Watson Natural Language Classifier API
- Spotify API
- ChartLyrics API
- Flickr API

The app takes an input of a song and artist from the user and sends it to the web application's "STM" wrapper service. This is a componsite service that calls the above listed APIs. First, it retrieves the lyrics from the ChartLyrics API and feeds that to Watson's Natural Language Classifier API to get the mood of the song based on the lyrics. The classifier has been trained to interpret natural language text into mood classifiers like happiness, sadness, inspirational and love. Once the mood is retrieved, that is sent to the Flickr API to retrieve photos based on that mood (tag).

The user views these images in the app while enjoying a 30 second clip of the song (which is played using Spotify's API).

# Download and deployment

Feel free to clone the repo and build the war file. The steps:

1. Execute 'gradle war' in the root directory. 
2. The war file (seethemusic.war) will be in ~/see-the-music/build/dist.
2. Deploy the war file in the app server of choice (tested on Tomcat 7) 

Note: This app is for demo and non-commercial purposes only

