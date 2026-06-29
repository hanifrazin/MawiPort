Feature: Test Step Extraction
  Scenario: Test Given-When-Then with And/But
    Given the user is on the login page
    * the page is fully loaded
    When the user enters username "admin"
    * the user enters password "secret123"
    * the user forgets to check remember me
    Then the dashboard should be displayed
    * the welcome message should say "Hello, admin"
    * the user should be logged in