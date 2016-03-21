# project1
Project 1 Popular Movies

Instructions for running the app.


To run the app, you need to set the MovieDB api key to you own.

 In the app/build.gradle file, replace "Copy your key here" with your own value.
 Note: It must be escaped with quotes.

buildTypes.each {
        it.buildConfigField 'String', 'MOVIEDB_API_KEY', "\"Copy your key here\""
    }

For example:
buildTypes.each {
        it.buildConfigField 'String', 'MOVIEDB_API_KEY', "\"123456778\""
    }
