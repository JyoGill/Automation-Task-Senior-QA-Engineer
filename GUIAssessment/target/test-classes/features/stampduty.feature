Feature: Service NSW Stamp duty calculator for motor vehicle

  Scenario: Successful calculate the stamp duty`
    Given User is on service NSW check motor vehicle stamp duty page
    When User click on check online button
    Then User redirected to another site of Revenue NSW calculator
    Then User click on 'Yes' for registration for a passenger vehicle
    And User provide value "45000" in purchase price
    Then User click on Calculate button
    And User verify popup shows passenger "Yes", purchase price "45000", and duty payable "1350"
