# SocialApp - About the application
Social App is made in Java using JavaFX as GUI and PostgreSQL as DB. This app has been made during the "Advanced Programming Methods" course, learning its concepts.

This application lets you register to the platform, log in, follow a user, delete followers or following users, send messages to any user registred on the platform.


[The GUI](#the-gui)

[Key functionalities](#key-functionalities)

[Signing up](#signing-up)

[View messages](#view-messages)

[View requests](#view-requests)

[Settings](#settings)

## The GUI 

Firstly, you are asked to log in into your account or create a new one.

<img src="https://github.com/user-attachments/assets/9fb0dfc9-c186-4a75-8fe0-a043d474ab5a" width="450"/>

If you are unsure, see how you can sign up here -> [signing up](#signing-up)

After successfully logging in with the newly made account (or an existing one), you will be prompted to this beautiful interface

<img src="https://github.com/user-attachments/assets/4d9c6bd9-5a7c-4879-a4e2-6a4df8bd415a" width="450"/>

Here you can see who are you connected as, your followers and who is following you. The two separated lists are showing the names of each user, being shown only two per table page. At the bottom of the window, several functionalities are available:
- [View messages](#view-messages)
- [View requests](#view-requests)
- [Settings](#settings)
- Delete (deletes the currently selected friendship from either the "followers" or "following" table) 
- Available Users (opens a window that lets you send a follow request to a selected user)

## Key functionalities

### Signing up

Just fill out the fields with the required information.
<img src="https://github.com/user-attachments/assets/92e2a405-e762-4726-a43b-5b6d002b9b0d" width="450"/>

### View messages

In this tab, you simply select an existing user on the platform to send messages to each other. If you are lazy scrolling through all users, you can search for an user.

<img src="https://github.com/user-attachments/assets/84d3a42b-fd4a-49be-9b93-7c4e0ead4cda" width="450"/>

### View requests

They are divided in two tabs: **INCOMING** and **OUTGOING** requests, witt their specific status, such as PENDING, ACCEPTED or DECLINED.

<img src="https://github.com/user-attachments/assets/c0c18336-20bc-4913-a18f-b470e41c2768" width="450"/>

To to something with these requests, you can DELETE, DECLINE or ACCEPT them (you can't decline or accept outgoing requests, only delete them because it makes sense).

### Settings

Here you can do basic operations such as log out (prompts you to the log in interface), modify data (name change, or password change, or even both) and delete current account.

<img src="https://github.com/user-attachments/assets/651f7864-8e16-4945-83b0-ba70a482ff24" width="450"/>

