@mdpsim
Feature: Snapshot Feed
  Background:
    Given Snapshot Feed A of MDP Channel 311 in MDP simulator is started

  Scenario: Subscribe to Snapshot Feed A of MDP Channel
    When I connect to Snapshot Feed A of Channel 311
    Then I receive MDP packets from Snapshot Feed

  Scenario: Subscribe to Snapshot Feed A of MDP Channel and get the given Security Snapshot
    When I connect to Snapshot Feed A of Channel 311
    Then I receive MDP packets from Snapshot Feed
     And I receive Snapshot for the Security 226650 in 10 sec
