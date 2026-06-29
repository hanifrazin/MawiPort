Feature: Test Step Extraction
  Scenario: Test Given-When-Then with And/But
    Given the user is on the login page
    And the page is fully loaded
    When the user enters username "admin"
    And the user enters password "secret123"
    But the user forgets to check remember me
    Then the dashboard should be displayed
    And the welcome message should say "Hello, admin"
    And the user should be logged in