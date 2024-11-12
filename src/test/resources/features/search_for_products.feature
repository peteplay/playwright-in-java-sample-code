Feature: Search for products

  Scenario: Search for products when there are matching results
    Given Sharon is on the home page
    When she searches for "saw"
    Then she should be presented with the following products:
      | Wood Saw     |
      | Circular Saw |

  Scenario: Search for products when there are no matching results
    Given Sharon is on the home page
    When she searches for "does-not-exist"
    Then she should not see any products