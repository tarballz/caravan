#CMPS 121 - Mobile Applications - Fall 2017

##Caravan

**Group Members:**

- Gabriel Larwood
- Payton Schwarz
- Qizhi Liu

###Overview

**Goals**

- Whenever a large group of people travel on the road together, 
complications inevitably arise. Even the best made plans may need 
to be adjusted for events like hunger, traffic, or a flat tire. 
Making such changes can be challenging, especially for a group using 
multiple vehicles. To mitigate the impact unexpected events have on 
coordinating a group, our team will attempt to build the Caravan 
Android application. Caravan will allow all members of a group with 
access to an Android phone to easily communicate important updates, 
make route adjustments, and stay organized. 

**Risks**

- Bringing everyone up to speed with the project’s requirements
- Communicating with our remote database
  - Making POST request to our remote database to add the party going on the trip, the cars in this party, and which users are in those cars.
  - Deleting the party from the database once the trip is complete.
    - What happens from the user’s perspective once the database is purged?
- Accurate communication with the Google Maps API
  - Redirecting to the Google Maps app with the party destination already entered at the click of a button
  - Overlaying a Google Maps View within our app with the locations of other vehicles in the party and custom icons for each vehicle (if we have the time)
- Inter-device communication
  - If we need to reroute the destination, how can we update the other phones with the new destination?
  - When a user wants to notify other vehicles of an update, how do we best sent that information to everyone in the party? 
- Finishing on time
  - Our app is fairly complex, with a lot of moving parts coming from external components (Django database, Google Maps API)
- Will the app be too “heavy”?
  - With all the complexities added to our app, and with our limited knowledge of how each component works - can we implement it properly such that the app doesn’t get “bogged-down” from all these features?

**What We Learned** 

- Utilized DynamoDB for our remote server
- Communicating with Dynamo to our application
- Navigation / Implementation of non local APIs such as Google Maps
- "Thread Pools"
- Sending data from one device to multiple others

### Usage





