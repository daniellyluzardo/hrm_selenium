Feature: Candidate functionality

  Background:
    Given the user is on the login page
    When they log in with username "Admin" and password "admin123"
    Then they should see a Dashboard "Dashboard" panel

  Scenario: Validate Dashboard panel widgets
    Then they should see widget "Time at Work"
    And they should see widget "My Actions"
    And they should see widget "Quick Launch"
    And they should see widget "Buzz Latest Posts"
    And they should see widget "Employees on Leave Today"
    And they should see widget "Employee Distribution by Sub Unit"
    And they should see widget "Employee Distribution by Location"
