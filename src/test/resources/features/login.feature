Feature: Login functionality

  Scenario: Valid login shows secure area
    Given the user is on the login page
    When they log in with username "Admin" and password "admin123"
    Then they should see a Dashboard "Dashboard" panel

  Scenario: Invalid login shows an error
    Given the user is on the login page
    When they log in with username "wronguser" and password "wrongpass"
    Then they should see a message containing "Invalid credentials"