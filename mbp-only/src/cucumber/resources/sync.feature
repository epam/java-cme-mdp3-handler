@mdpsim
Feature: Incremental Refresh Feed
  Background:
    Given Subscription to Incremental Refresh Feed A of MDP Channel 311 in handler #1
      And Subscription to Snapshot Feed A of MDP Channel 311 in handler #1

  Scenario: Synchronize the given Instrument
     When User application is subscribed to Security 226650 state changes in handler #1
      And Incremental Refresh Feed A of MDP Channel 311 in MDP simulator is started
      And Snapshot Feed A of MDP Channel 311 in MDP simulator is started
     Then Security with id 226650 must be synchronized in 10 secs or less