# Stolker

## Introduction :hand:

Stolker app is named by a combination of the words "stocks" and "stalker" :P. What this app does is to provide an easy way to a user to see details for a stock, crypto or currency. For the moment, these information include the current price, the previous closing price and the percentage difference.

## Technical details :man_technologist:

This app uses Flows, MVVM with UI States, Clean Architecture, Navigation Fragments, Koin for DI, OkHttp for Api and WebSockets communication and consists of the below module:

- **`:data`**
- **`:domain`**
- **`:network`**
- **`:common`**

Also, I intoduced 2 buids types in the build.gradle, where I inject the Base URLs for the beta and mock servers and their auth tokens. 

_**:bulb:Important:** In order to be able to run the app on your own workstation, you have to create a local.properties files and include the required fields that build.gradle tries to retrieve. You can contact me to send you the file for your convinience, but I don't want to commit it and have the API keys available in the repo._

## Videos :video_camera:

[select_product_from_the_list.webm](https://user-images.githubusercontent.com/10981693/204795462-a3842b4e-e529-4ded-8452-004de810cbfa.webm) 

[product_search_and_details.webm](https://user-images.githubusercontent.com/10981693/204796362-23f76c50-b70b-4268-a77c-4bce550b1548.webm)

## Testing :test_tube:

For the Unit testing I used [Mockk](https://mockk.io/) for mocking dependencies, [Turbine](https://github.com/cashapp/turbine) for Flows testing and [kotest](https://github.com/kotest/kotest) for more comprehensive matchers.

For the UI tests, I simply used Espresso and I used the Robot Pattern.
