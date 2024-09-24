# Eatzy
<br>
<p align="center">
I participated in the Jetpack Compose Bootcamp program organized by Kasım Adalan in collaboration with Techcareer.net and Turkcell. 
In this process, each student was tasked with creating a food delivery application. 
The project primarily illustrates the use of Coroutines, Flows, MVI combined with MVVM, Firebase and related technologies.
</p> <br>

<p align="center">
<a href="https://opensource.org/licenses/Apache-2.0"><img src="https://img.shields.io/badge/License-Apache%202.0-red.svg"></a>
<a href="https://android-arsenal.com/api?level=23"><img src="https://img.shields.io/badge/API-23%2B-brightgreen.svg?style=flat"></a>
<a href="https://github.com/ErenMlg"><img src="https://img.shields.io/badge/github-ErenMlg-blue"></a>
</p>

 ## Project Screens
<img width=100% src="https://github.com/user-attachments/assets/39d4c98a-4e4e-4846-8df0-98cc476efc26"/>

## Project Tech Stack
<ul>
 <li>This project developed with %100 with <a href="https://developer.android.com/kotlin?hl=tr">Kotlin</a></li>
 <li>Made with <a href="https://developer.android.com/topic/architecture?hl=tr">Android Architecture Components</a> for the Collection of libraries that help you design robust, testable, and maintainable apps.</li>
 <li><a href="https://developer.android.com/topic/libraries/architecture/viewmodel?hl=tr">ViewModel</a>: The ViewModel class is a business logic or screen level state holder. It exposes state to the UI and encapsulates related business logic. Its principal advantage is that it caches state and persists it through configuration changes. This means that your UI doesn’t have to fetch data again when navigating between activities, or following configuration changes, such as when rotating the screen.</li>
  <li><a href="https://developer.android.com/kotlin/coroutines"">Kotlin Coroutine:</a> On Android, coroutines help to manage long-running tasks that might otherwise block the main thread and cause your app to become unresponsive. Over 50% of professional developers who use coroutines have reported seeing increased productivity. This topic describes how you can use Kotlin coroutines to address these problems, enabling you to write cleaner and more concise app code.</li>
  <li><a href="https://developer.android.com/training/dependency-injection/hilt-android">Dependency Injection with Hilt</a>: Hilt is a dependency injection library for Android that reduces the boilerplate of doing manual dependency injection in your project. Doing manual dependency injection requires you to construct every class and its dependencies by hand, and to use containers to reuse and manage dependencies.</li>
  <li><a href="https://developer.android.com/guide/navigation">Navigation</a>: Navigation refers to the interactions that allow users to navigate across, into, and back out from the different pieces of content within your app. Android Jetpack's Navigation component helps you implement navigation, from simple button clicks to more complex patterns, such as app bars and the navigation drawer.</li>
  <li><a href="https://square.github.io/retrofit/">Retrofit</a>: Retrofit is the class through which your API interfaces are turned into callable objects. By default, Retrofit will give you sane defaults for your platform but it allows for customization.</li>
  <li><a href="https://kotlinlang.org/docs/ksp-overview.html">Kotlin KSP</a>: Kotlin Symbol Processing (KSP) is an API that you can use to develop lightweight compiler plugins</li>
  <li><a href="https://square.github.io/okhttp/">OkHttp</a>: OkHttp is an open-source HTTP client developed by Square and is widely used for handling network operations in Android apps.</li>
  <li><a href="https://developer.android.com/topic/architecture/data-layer">Repository</a>: This located in data layer that contains application data and business logic. </li>
  <li><a href="https://coil-kt.github.io/coil/compose/">Coil</a>: Coil for show pictures on network.</li>
  <li><a href="https://coil-kt.github.io/coil/compose/">Firebase Auth</a>: Utilized for user login and registration functionalities.</li>
  <li><a href="https://coil-kt.github.io/coil/compose/">Firebase Firestore</a>: Used for storing user information, locations, payments, and promotion codes. (Payments etc. musn't hide here but this is prototype</li>
    <li><a href="https://developer.android.com/jetpack/androidx/releases/room?hl=tr">Room</a>: Used for storing user' favorite foods.</li>
</ul>

## Architecture
<p>
  This app uses <a href="https://developer.android.com/topic/architecture?hl=tr#recommended-app-arch"> MVVM (Model View View-Model)</a> 
and <a href="https://medium.com/@mohammedkhudair57/mvi-architecture-pattern-in-android-0046bf9b8a2e"> MVI (Model View Intent)</a> architecture structure as combined.
</p>
<img src="https://miro.medium.com/v2/resize:fit:1400/1*k3G2EYx8lCPYHbMH8x1tcg.png">


## Project Graph
This app uses <a href="https://developer.android.com/topic/architecture?hl=tr#recommended-app-arch">MVVM (Model View View-Model)</a> combined with <a href="https://medium.com/@mohammedkhudair57/mvi-architecture-pattern-in-android-0046bf9b8a2e">
MVI (Model View Intent)</a> architecture<br>
Core layer have 5 sub layer as common, data, di, domain, navigation, presentation;
<table>
  <tr>
    <td>
      <img src="https://github.com/user-attachments/assets/c055d30a-85bc-4b5d-9518-8b4d305182ab">
    </td>
    <td>
      <ul>
        <li>Extension: Hold Map extensions for the response to entity, other extension for the view, network response, modifier etc.</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/4412973a-836d-4a6a-bac4-eec6437446f0"></td>
    <td>
      <ul>
        <li>Dto: Hold database objects for the response</li>
        <li>Local: Hold api services</li>
        <li>Remote: Hold dao's and database file</li>
        <li>Repository: Hold repository for the application data and business logic.</li>
        <li>Source: For the remote data source to listen result and convert flow.</li>
      </ul>
    </td>
  </tr>
    <tr>
    <td><img src="https://github.com/user-attachments/assets/239d7e03-8cc3-4188-9c9a-71cdcfccf363"></td>
    <td>
      <ul>
        <li>Di: Hold di objects and works</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/a03e8ce7-4d28-4fb1-aef0-521c76bd666f"></td>
    <td>
      <ul>
        <li>Model: Hold used models on screens.</li>
        <li>Repository: Hold repository interfaces for the clean view.</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/6b246acc-b3ea-46a8-ad53-de383e76acbf"></td>
    <td>
      <ul>
        <li>Navigation: Hold navhost, route objects and bottom app bar</li>
      </ul>
    </td>
  </tr>
     <tr>
  <td><img src="https://github.com/user-attachments/assets/a061c071-05c2-4711-ab82-b866886a167a"></td>
 <td>
   <ul>
        <li>ViewModel: Communicate with data layer and send response to Contract for the state.</li>
     <li>Contract: Hold user actions and incoming data responses for the send UI</li>
     <li>Route: Its hold Screen(UI) and route for the navigation</li>
   </ul>
 </td>
 </tr>
 <tr>
  <td><img src="https://github.com/user-attachments/assets/f8f86f32-4ec0-4eb3-8ee9-134ef2bc6a31"></td>
 <td>
  Presentation layer have 13 sub layer as components, address, cart, favorites, introduce, login, main, payment, productDetail, promotions, register, setting, splash theme.
They hold own fragments, viewmodels and detail pages.
 </td>
 </tr>
</table>


## End Note
I may have mistakes, you can contact me for your feedback. 👉 📫 **eren.mollaoglu@outlook.com**<br>

## License
<pre>
Designed and developed by 2024 ErenMlg (Eren Mollaoğlu)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</pre>
