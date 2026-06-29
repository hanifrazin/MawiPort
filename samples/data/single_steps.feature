Feature: Single Steps Test
  Scenario: Single steps without And
    Given the user is logged in
    When the user clicks logout
    Then the user is redirected to login page