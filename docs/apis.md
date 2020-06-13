## APIs
### signup
URL

    http://localhost:8080/signup
Header

    Content-Type:application/json
Input

    {
	    "name":"name of the userEntity",
	    "password":"password for the userEntity",
	    "email":"email id",
	    "role": "role"
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
	    "username":"name of the userEntity",
	    "password":"userEntity password"
	}
Response

    {
	    "accessToken": "jwt token"
    }

