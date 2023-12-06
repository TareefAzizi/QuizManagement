

# Quiz  Application

## Overview

This  is an interactive quiz application designed for educational purposes, enabling teachers to manage student groups and create quizzes with timed MCQs, and allowing students to participate in quizzes and their results.

## Features

- **User Registration**: Distinct roles for Teachers and Students. First time logging in with google will bring a prompt that asks you to pick between student or teacher.
- **Teacher Dashboard**:Teachers can log in to the dashboard and manage their quizzes. Firebase will auto generate a Unique ID for each quiz. The Teacher will be able to copy the Quiz ID to their clipboard. Clicking on the Edit button will allow the users to set the Quiz time limit for each quiz. Clicking on the delete button will ask for a prompt, confirming if the user wants to delete the quiz. Clicking on the item itself will show the questions of the quiz, Teachers are only allowed to view the quiz.
- **Quiz Upload** : Clicking on the create quiz button will bring a dialog asking for the Quiz name, description and the CSV file. Clicking on the upload CSV button will bring you to your downloads. Once a CSV file is picked it will bring update the UI to display the file name. Upon uploading the quiz there will be a toast confirming the quiz.  After successfully submitting, the Latest quiz uploaded will be displayed on the Teacher Dashboard Fragment.
- **Student Dashboard**: Displaying the Quizzes that you have joined and a button to join the quiz. Once the user has joined the quiz  it will update the UI to display the latest quiz that you have joined. 
- **Quiz Participation**: When a Student takes a Quiz there is a timer on the top right. You will be redirected back to the student Dashboard once the timer has ended or you press the Submit button.  Once the timer has ran out, it will mark based on how many questions you have gotten correct. The UI will be changed depending on how much you have scored for the quiz.
- **Results page** : This is the leaderboard. It shows the Quiz name and the score of the correct answers of each students for  the specific quiz.
- **Profile Page**: Shows your username, email, and the button to logout

## Installation

Connect to firebase, setup authentication with email/password and google login. Download the google-services.json.

## User Guide

### User Registration

Implement a user registration system where users can create accounts as teachers or students. Upon Registration you are able to pick between student or teacher.Logging with with google for the first time will bring up a dialog and you will be able to choose the roles.
<img width="488" alt="Screenshot 2023-12-06 at 10 28 09 AM" src="https://github.com/TareefIman/PersonalQuizNew/assets/44467516/7e5f66cd-bf78-46a7-b9ee-e3bba1193e00">
<img width="308" alt="LoginImage" src="https://github.com/TareefIman/PersonalQuizNew/assets/44467516/3c6bee64-629a-4b38-a053-d926fc458186">


###  TEACHER DASHBOARD

Teachers can log in to the dashboard and manage their quizzes. Firebase will auto generate a Unique ID for each quiz. The Teacher will be able to copy the Quiz ID to their clipboard. Clicking on the Edit button will allow the users to set the Quiz time limit for each quiz. Clicking on the delete button will ask for a prompt, confirming if the user wants to delete the quiz. Clicking on the item itself will show the questions of the quiz, Teachers are only allowed to view the quiz.
<img width="332" alt="TeacherDashboard" src="https://github.com/TareefIman/PersonalQuizNew/assets/44467516/70e6a4d4-f55e-4f7d-992f-a4f624a6c19e">
<img width="1125" alt="teacherdashboard2" src="https://github.com/TareefIman/PersonalQuizNew/assets/44467516/174435c9-1a4e-424a-b3a3-687667739106">




### UPLOAD QUIZ PAGE

Clicking on the create quiz button will bring a dialog asking for the Quiz name, description and the CSV file. Clicking on the upload CSV button will bring you to your downloads. Once a CSV file is picked it will  update the UI to display the file name. Upon uploading the quiz there will be a toast confirming the quiz.  After successfully submitting, the Latest quiz uploaded will be displayed on the Teacher Dashboard Fragment.


<img width="395" alt="uploadquiz1" src="https://github.com/TareefIman/PersonalQuizNew/assets/44467516/080afc8e-5ee1-4a59-a18a-5a6f787d06e4">
<img width="1133" alt="uploadquiz2" src="https://github.com/TareefIman/PersonalQuizNew/assets/44467516/a845bc3a-f499-481c-b3a5-044499ff8e67">
<img width="1140" alt="uploadquiz3" src="https://github.com/TareefIman/PersonalQuizNew/assets/44467516/99414eca-924c-4ac9-8ff1-bb4698a39dc8">



### STUDENT DASHBOARD 

Displaying the Quizzes that you have joined and a button to join the quiz. Once the user has joined the quiz  it will update the UI to display the latest quiz that you have joined.When a Student takes a Quiz there is a timer on the top right. You will be redirected back to the student Dashboard once the timer has ended or you press the Submit button.  Once the timer has ran out, it will mark based on how many questions you have gotten correct. The UI will be changed depending on how much you have scored for the quiz.
<img width="1114" alt="studentdashboard1" src="https://github.com/TareefIman/PersonalQuizNew/assets/44467516/92e6e48f-afff-489e-b901-5221c2ebf924">
<img width="1112" alt="studentdashboard2" src="https://github.com/TareefIman/PersonalQuizNew/assets/44467516/c5f17918-f98f-4865-af66-2e96b0718608">





### RESULTS

This is the leaderboard. It shows the Quiz name and the score of the correct answers of each students for  the specific quiz.

<img width="1109" alt="results1" src="https://github.com/TareefIman/PersonalQuizNew/assets/44467516/d60afaa3-7907-49d8-9f1b-8b40099a64bc">



### PROFILE PAGE

Shows your username, email, and the button to logout
<img width="429" alt="profile1" src="https://github.com/TareefIman/PersonalQuizNew/assets/44467516/20035a9f-c1e0-438c-9c0b-f7f0b452c98b">


****







### Teacher's Guide

- How to create quizzes and manage student groups.
- Steps for importing quiz data using CSV files.

### Student's Guide

- Joining quizzes and understanding the home screen features.

## CSV Import
questionId	quizTitle	question	options	correctAnswer
Q1	General Knowledge Quiz	What is the capital of France?	A) Paris, B) Berlin, C) Rome, D) Madrid	A
Q2	General Knowledge Quiz	What is 2 + 2?	A) 3, B) 4	B
Q3	General Knowledge Quiz	Who wrote 'Romeo and Juliet'?	A) William Shakespeare, B) Leo Tolstoy	A

Here is the format your CSV file has to be in for the Quiz CSV import to work.  Just copy and paste it. This works will questions that only have two options to pick from as well.

## Error Handling and Security

- Common error handling for login, register andd UPLOAD CSV file.

Error Handling and Security

## Contributing

Made by Tareef Iman Azizi 

## License

N/A
