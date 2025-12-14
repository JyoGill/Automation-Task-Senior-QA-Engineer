
Feature: Validate Open Library Author fields

  # Non-Revenue Automation Framework compliant: public Open Library API.
  # Target: https://openlibrary.org/authors/OL1A.json

  Scenario: Validate author details for OL1A
    Given the Open Library Authors API
    When I GET "/authors/OL1A.json"
    Then the response status code should be 200
    And the field "personal_name" should be "Sachi Rautroy"
    And the array "alternate_names" should contain "Yugashrashta Sachi Routray"
