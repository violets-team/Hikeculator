# Hiculator

## Discription
An application for planning meals and calculating the nutritional value of products for travel. The application allows you to compose a diet for individual and collective trips, in accordance with individual anthropometric data, categories of difficulty and duration of trips. It simplifies tracking of calories intake rates, as well as the ratio of PFC. Facilitates menu composing. The app allows you to track changes made by other members of the trip in real-time.

## Technology stack
* The code is written with _**Kotlin**_
* _**Clean Architecture**_ + _**MVVM**_
* Access to storage via _**Firestore**_ and _**DataStore**_
* For asynchronous operations used _**Coroutines**_ and _**Coroutine flows**_
* For UI used _**XML**_
* For Ui navigation used _**Navigation component**_
* For networking used _**Retrofit**_
* For dependency injection used _**Koin**_
* For testing used _**Mockk**_
* Single Activity pattern
* _**Theme**_: dark and light

## Features
* Creating of collective trips
* Tracking the status of trips in real time
* Search products via Api
* Adding and removing trip members
* Meal planning and calculating nutritional value of products for trips, taking into account the complexity of the trip and the personal parameters of users
* Changes tracking in real time


## Compatibility
From API level 24 to 31

## Screen samples
![name](https://github.com/violets-team/Hikeculator/blob/main/preview/trip_list_light_theme.png)
![name](https://github.com/violets-team/Hikeculator/blob/main/preview/trip_creation_dark_theme.png)

![name](https://github.com/violets-team/Hikeculator/blob/main/preview/date_picking_dark_theme.png)
![name](https://github.com/violets-team/Hikeculator/blob/main/preview/member_searching_dark_theme.png)

![name](https://github.com/violets-team/Hikeculator/blob/main/preview/trip_details_light_theme.png)
![name](https://github.com/violets-team/Hikeculator/blob/main/preview/trip_details_dark_theme.png)

![name](https://github.com/violets-team/Hikeculator/blob/main/preview/day_details_dark_theme.png)
![name](https://github.com/violets-team/Hikeculator/blob/main/preview/day_details_light_theme.png)

![name](https://github.com/violets-team/Hikeculator/blob/main/preview/food_search_light_theme.png)
![name](https://github.com/violets-team/Hikeculator/blob/main/preview/food_addition_dark_theme.png)

## App working demo
![App preview](preview/app_preview.gif)
