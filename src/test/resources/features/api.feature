@api
Feature: To validate the api

  Scenario: Verification of sample open public api
    Given hit the 'https://jsonplaceholder.typicode.com/todos/1' uri
    Then the response should be 200