# ActiveDirectoryUserCreator
A web application that allows self service request of new AD account based on security groups.

## What It Does
How often do you find yourself creating new users, only to be told that the permissions assigned were incorrect? 
This application aims to solve that by allowing users to see what AD groups people are in, and selecting groups for their new user.

The user puts in the details of the requested account, and searches for similar user. The list of groups they are in are then shown in a table.

![User search](http://i.imgur.com/mJGwY5q.png)

When they click Create User, they are returned their username and password (or an error if the account exists), however the account is disabled in Active Directory.

![User details](http://imgur.com/0ircjfx.png)

An email is sent to the administrator with the details of the account and the requestor, and a link for authorisation. 
By clicking the like included in the email, the account becomes enabled.

![Authorisation email](http://imgur.com/t6MZ7mM.png)

## How To Use
Modify the config.properties to suit your environment and provide a user and password that has permissions to create the users.

