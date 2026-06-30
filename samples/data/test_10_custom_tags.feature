# Test file untuk menguji penanganan 10 custom tag
# File: test_10_custom_tags.feature

@WEB @P0 @Smoke
Feature: Test Custom Tag Positioning

  Scenario: Test dengan 10 custom tag di scenario level
    Given user is on the login page
    When user enters valid credentials
    Then user should be logged in successfully
    And welcome message should be displayed

  @P1 @Regression
  Scenario: Test dengan custom tag di scenario level saja
    Given user is on dashboard
    When user clicks profile button
    Then profile page should be displayed

  Scenario: Test tanpa custom tag
    Given user is on settings page
    When user changes preferences
    Then preferences should be saved