Feature: Labcorp Career Search

  Scenario: Verify Job Details
    Given User launches Labcorp website
    When User clicks Careers
    And User searches for "Senior Software Developer - Life-sciences industry"
    And User opens the job posting
    Then Verify Job Title
    And Verify Job Location
    And Verify Job ID
    And Verify Job Responsibilities contains "Internal/External Clients"
    And Verify Preferred Qualifications contains "Certified technical professional in relevant technology - .NET, C#, JAVA"
    And Verify 5th Additional Job Standards bullet equals "This person must be a flexible, self-motivated team player with a positive attitude."
    When User clicks Apply Now
    Then Verify Apply page shows same Job Title
    And Verify Apply page shows same Job ID
    When User clicks Return to Job Search
    Then Verify user is navigated back to Job Search page
