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

  # Kasus 1: Warisan penuh
  @P1 @S2 @WEB
  Scenario: Inherited scenario
    # Hasil: PRIORITY=P1, SEVERITY=S2, TYPE=WEB

  # Kasus 2: Override parsial
  @P1 @S2 @WEB
  Scenario: Partial override scenario
    # Hasil: PRIORITY=P2, SEVERITY=S2, TYPE=WEB

  # Kasus 3: Tidak ada anotasi
  Scenario: No annotation scenario
    # Hasil: Semua tag null (tidak ditampilkan)

  Scenario: Successful login with valid credentials
    Given the user is on the login page
    When the user enters username "admin" and password "secret123"
    Then the dashboard should be displayed
    And the welcome message should say "Hello, admin"

  # Kasus 1: Warisan penuh
  @P1 @S2 @WEB
  Scenario: Inherited scenario
    # Hasil: PRIORITY=P1, SEVERITY=S2, TYPE=WEB

  # Kasus 2: Override parsial
  @P1 @S2 @WEB
  Scenario: Partial override scenario
    # Hasil: PRIORITY=P2, SEVERITY=S2, TYPE=WEB

  # Kasus 3: Tidak ada anotasi
  Scenario: No annotation scenario
    # Hasil: Semua tag null (tidak ditampilkan)