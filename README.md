# Cherry
Cherry is a meal-tracking app designed to make it easy
to enter your meals and track your weekly, monthly, and yearly average calorie intakes. 
This allows its users to more accurately make dietary adjustments
and lose weight, gain muscle, or reach their other fitness goals!

## Live Link
Check out Cherry at https://cherry.joshroundy.dev !!!

## Youtube Overview
[![Josh Roundy on YouTube](https://i.ytimg.com/vi/IligeviHT-M/hqdefault.jpg?sqp=-oaymwEnCNACELwBSFryq4qpAxkIARUAAIhCGAHYAQHiAQoIGBACGAY4AUAB&rs=AOn4CLBSs1LjCeiiuzzGoyFB_rqaKufOyA)](https://www.youtube.com/watch?v=fkjwlnVtwew "Cherry Overview")

## Planning
NOTE: Figma was used as a rough draft and several design changes were made afterward
<br><br>
Figma: https://www.figma.com/design/9na15WcguKcXO18PswOSYC/Untitled?node-id=0-1&t=Pqk8lEKxhM4mqeuK-1

## Backend
Cherry's backend is made with a Spring Boot authentication and data API
that connects with a Microsoft SQL Server database and the OpenAI API. 
<br><br>
The Spring Boot web server is deployed to an AWS EC2 instance and the database uses Postgres SQL RDS.

## Frontend
Cherry's front end consists of a React JS web app that makes HTTP requests
to the back end, getting the authorization and data it needs.
<br><br>
The front-end is deployed to an AWS S3 bucket.

## Conclusion
Cherry was designed to make calorie tracking easy by making food entry simpler. <br> 
Cherry uses AI to let users describe their meals with normal language rather than complicated menus and searches, providing highly accurate estimates of calories and protein content.
