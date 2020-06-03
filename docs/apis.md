## APIs
### signup
URL

    http://localhost:8080/signup
Header

    Content-Type:application/json
Input

    {
	    "username":"name of the user",
	    "password":"password for the user",
	    "email":"email id",
	    "contactNumber":"phone num",
	    "role":[
                {
                    "role":"EXHIBITOR"
                }
		]
	}
Response

    {
	    "message": "registration status"
    }

### Login
URL

    http://localhost:8080/authenticate
Header

    Content-Type:application/json
Input

    {
	    "username":"name of the user",
	    "password":"user password"
	}
Response

    {
	    "accessToken": "jwt token"
    }

