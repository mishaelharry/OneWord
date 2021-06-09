# OneWord Design

Oneworld test was developed using the MVC design pattern is also known as Model-View-Controller. It is a common architectural pattern that is used to design and create interfaces and the structure of an application.
This pattern divides the application into three parts that are dependent and connected to each other. These designs are used to distinguish the presentation of data from how the data is accepted from the user to the 
data shown. These design patterns have become common in the use of web applications and for developing GUIs

# Setup
To run the application, kindly create a MySQL database name "oneworld_db"

#Swagger
http://localhost:7000/oneworld-service/swagger-ui.html

# Endpoints
1.  Create User
URL: http://127.0.0.1:7000/oneworld-service/api/user<br>
Method: GET <br>
Request:
{
    "title":"Mr",
    "firstName":"Mishael",
    "lastName":"Harry",
    "email":"harry@hotmail.com",
    "mobile":"09039639899",
    "password":"123456",
    "role": "USER"
}

2.  Get User (Paged)
URL: http://127.0.0.1:7000/oneworld-service/api/user?page=0&size=100 <br>
Method: GET <br>

3.  Update User
URL: http://127.0.0.1:7000/oneworld-service/api/user/{id} <br>
Method: PUT <br>

4.  Verify User
URL: http://127.0.0.1:7000/oneworld-service/api/user/verify/{id}  <br>
Method: GET <br>

5.  Deactivate User
URL: http://127.0.0.1:7000/oneworld-service/api/user/{id}  <br>
Method: DELETE <br>


