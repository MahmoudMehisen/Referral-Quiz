Feature: Guest functions should work

    Scenario: Call backend with get quiz metadata
        When the client calls endpoint "/api/guest/getQuizMetadata"
        Then response status code is 200