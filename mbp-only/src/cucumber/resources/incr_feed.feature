@mdpsim
Feature: Incremental Refresh Feed
  Background:
    Given Incremental Refresh Feed A of MDP Channel 311 in MDP simulator is started

  Scenario: Subscribe to Incremental Refresh Feed A of MDP Channel
    When I connect to Incremental Refresh Feed A of Channel 311
    Then I receive MDP packets from Incremental Refresh Feed in 5 sec

  Scenario: Subscribe to Incremental Refresh Feed A of MDP Channel and get the given Security Updates
    When I connect to Incremental Refresh Feed A of Channel 311
    Then I receive Incremental Refresh packets for the Security 226650 in 10 secs
