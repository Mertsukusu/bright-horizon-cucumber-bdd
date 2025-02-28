Feature: Bright Horizons Website Testing
    As a user
    I want to verify the functionality of the Bright Horizons website
    So that I can ensure it works correctly for parents and employers

    Background:
        Given I am on the Bright Horizons homepage

    Scenario: Verify footer section titles have sufficient length
        When I scroll to the footer section
        Then each footer section should contain a title with at least 15 characters
        When I click on the search icon
        And I search for "Employee Education in 2018: Strategies to Watch"
        Then the first search result should exactly match "Employee Education in 2018: Strategies to Watch"
        And each footer section should contain a title with at least 15 characters