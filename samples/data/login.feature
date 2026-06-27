Feature: Login Module
  As a user
  I want to log in
  So that I can access the dashboard

  @WEB @P0 @Smoke
  Scenario: Successful login with valid credentials
    Given the user is on the login page
    When the user enters username "admin" and password "secret123"
    Then the dashboard should be displayed
    And the welcome message should say "Hello, admin"