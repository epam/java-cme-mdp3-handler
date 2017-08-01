@mdpsim
Feature: Instrument Feed
  Background:
    Given Instrument Feed A of MDP Channel 311 in MDP simulator

  Scenario: Subscribe to Instrument Feed A of MDP Channel
    When I connect to Instrument Feed A of Channel 311
    Then I receive MDP packets from Instrument Feed

  Scenario: Subscribe to Instrument Feed A of MDP Channel and get the given Security Definition
    When I connect to Instrument Feed A of Channel 311
    Then I receive MDP packets from Instrument Feed
     And I receive Security Definition with Symbol 'EWG5 C1560' in 10 sec
