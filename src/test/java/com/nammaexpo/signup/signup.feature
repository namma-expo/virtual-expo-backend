Feature: Test sign up APIs
  Background:
    Given url baseUrl
    Given path '/signup'
    * def input = read('signup.json')

    Scenario: Sign up as EXHIBITOR
      And request input
      And header Accept = 'application/json'
      When method post
      Then status 200
      Then header Content-Type = 'application/json'
      Then match response == { token : '#present'}
      And def authToken = response.token
      
     Scenario: Duplicate user
       And request input
       And header Accept = 'application/json'
       When method post
       Then status 400
       Then header Content-Type = 'application/json'
       And match $.response.code == 'EMAIL_IN_USE'