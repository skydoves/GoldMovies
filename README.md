# GoldMovies
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16) <a href="https://github.com/skydoves"><img alt="License" src="https://img.shields.io/static/v1?label=GitHub&message=skydoves&color=C51162"/></a>

GoldMovies is based on Kotlin, MVVM architecture, coroutines, dagger, koin, and material designs & animations.

## Previews
<p align="center">
<img src="/preview/preview0.gif" width="32%"/>
<img src="/preview/preview1.gif" width="32%"/>
<img src="/preview/preview2.gif" width="32%"/>
</p>

## What Open API Used?
[The Movies Database](https://developers.themoviedb.org/3/getting-started/introduction) (TMDB) is a community built movie and TV database.
Every piece of data has been added by our amazing community dating back to 2008.
TMDb's strong international focus and breadth of data is largely unmatched and something we're incredibly proud of.
Put simply, we live and breathe community and that's precisely what makes us different.

## How to build on your environment
Add your [The Movie DB](https://www.themoviedb.org)'s API key in your `local.properties` file.
```xml
tmdb_api_key=YOUR_API_KEY
```

## Code style formatting & Lint check
Ideally, [Spotless](https://github.com/diffplug/spotless) is a code formatter can do more than just find formatting errors - it should fix them as well. Such a formatter is really just a Function<String, String>, which returns a formatted version of its potentially unformatted input.

## Module structure
The module structure is designed to try several different architectures.

<img src="/preview/structure.png" width="100%"/>

## Entity module
[Entity module](/entity) composed of entity models for persisting in database and response models for fetching data from network requests. 

### Dependencies
- Room Persistence - constructing database (An abstraction layer over SQLite).
- [Gson converter](https://github.com/square/retrofit/tree/master/retrofit-converters/gson) - a converter which uses Gson for serialization to and from JSON.
- [Threetenabp](https://github.com/JakeWharton/ThreeTenABP) - an adaptation of the JSR-310 backport for Android.

### Unit Test
[Unit Tests](/entity/src/test/java/com/skydoves/entity) to construct database and migration to newer versions using the Room.
<br><br>
<img src="/preview/unitTest0.png" width="100%"/>

## Network module
[Network module](/network) composed of abstractions for RESTful requests. 
And `ApiResponseModel` for standardizing a raw request model. 
An Interceptor for requesting every time with a query parameter `api_key`.

### Dependencies
- [Retrofit2](https://github.com/square/retrofit) - constructing the REST API.
- [Logging Interceptor](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor) - logs HTTP request and response data.
- [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver) - constructing a web server for testing HTTP clients.

### Unit Test
[Unit Tests](/network/src/test) to verify RESTful requests abstractions via a testing web server and mocked data.
<br><br>
<img src="/preview/unitTest1.png" width="100%"/>

## Common-ui module
[Common-ui module](/common-ui) composed of adapters and viewholders for composing recyclerview's item via databinding. And some factories and extensions related to custom views.

### Dependencies
- [Google-Material](https://github.com/material-components/material-components-android) - material Components for Android (MDC-Android) help developers execute Material Design.
- [Glide](https://github.com/bumptech/glide) - loading image.
- [GlidePalette](https://github.com/florent37/GlidePalette) - compatible with glide, extracting a primary color from an image.
- [BaseRecyclerViewAdapter](https://github.com/skydoves/BaseRecyclerViewAdapter) - fast way to binding RecyclerView adapter and ViewHolder for implementing clean sections.
- [WhatIf](https://github.com/skydoves/whatif) - fluent Kotlin expressions for a single if-else statement, nullable and boolean.
- [Flourish](https://github.com/skydoves/Flourish) - a polished and dynamic way to show up layouts.
- [AndroidRibbon](https://github.com/skydoves/androidribbon) - beautiful and the simplest ribbon view with shimmering effect.
- [ExpandableTextView](https://github.com/Manabu-GT/ExpandableTextView) - allows developers to easily create an TextView which can expand/collapse.

## Mvvm module
[Mvvm module](/mvvm) is the implementation of user interfaces on the application. 
Based on mvvm architecture (view-databinding-viewmodel-model) with the repository pattern.

- JetPack
  - LiveData - notify domain layer data to views.
  - Lifecycle - dispose observing data when lifecycle state changes.
  - ViewModel - UI related data holder, lifecycle aware.
  - Room Persistence - construct database.
- [Dagger2](https://github.com/google/dagger) - constructing dependency injection framework based on compile-time. 
- [Timber](https://github.com/JakeWharton/timber) - this is a logger with a small, extensible API.

### Unit Test
[Unit Tests](/mvvm/src/test/java/com/skydoves/mvvm) verify the interactions of viewmodels between repositories and dao & REST api requests.
<br><br>
<img src="/preview/unitTest2.png" width="100%"/>

## Mvvm-coroutines module
[Mvvm-coroutines module](/mvvm-coroutines) almost same as the Mvvm module. Implementation of user interfaces on the application. 
Based on mvvm architecture and coroutines.

- [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) - kotlin side(at the language level) supports for asynchronous programming.
- [Koin](https://github.com/InsertKoinIO/koin) - A pragmatic lightweight dependency injection, actually service locator.
- [Timber](https://github.com/JakeWharton/timber) - this is a logger with a small, extensible API.

### Unit Test
[Unit Tests](/mvvm-coroutines/src/test/java/com/skydoves/mvvm_coroutines) verify the interactions of viewmodels between repositories and DAO & REST api requests.
<br><br>
<img src="/preview/unitTest3.png" width="100%"/>

## Unit Testing Framework
- [Robolectric](https://github.com/robolectric/robolectric) - Robolectric is the industry-standard unit testing framework for Android.
- [Mockito-Kotlin](https://github.com/nhaarman/mockito-kotlin) - a small library that provides helper functions to work with Mockito in Kotlin.

## User Interface Design
Based on `Material` design & animations.

- Google Material Design.
- Ripple Effect.
- Shared Element Transition.

## Find this repository useful? :heart:
Support it by joining __[stargazers](https://github.com/skydoves/GoldMovies/stargazers)__ for this repository. :star: <br>
And __[follow](https://github.com/skydoves)__ me for my next creations! 🤩

## Supports :coffee:
If you feel like support me a coffee for my efforts, I would greatly appreciate it. <br><br>
<a href="https://www.buymeacoffee.com/skydoves" target="_blank"><img src="https://www.buymeacoffee.com/assets/img/custom_images/purple_img.png" alt="Buy Me A Coffee" style="height: auto !important;width: auto !important;" ></a>

# License
```xml
Designed and developed by 2019 skydoves (Jaewoong Eum)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
