This file lists the tests implemented in VitalTest.groovy

The data in the test files is from the Cerner "Build" domain and belongs to the dummy patient "Bob Smartfour".
The results and encounters data was created through Power Chart (running against the BUILD domain).

Test case#1: (test_data/Results.xml)
The test data contains clinical events information for Bob Smartfour.
All events belong to the same encounter and further have the same parent event id.
The expected result is a single VitalSigns object with encounters and vitals information.
Height and Weight are null

Tests the functionality to
-read results values and codes from the cerner response.
-accurately map cerner codes to codes the SMART platform expects.
-identify and group together blood pressure events.
-identify and group together events based on a common parent event id. This creates a VitalSigns object. 

Test case#2: (test_data/Results-Date.xml)
The test data contains clinical events information for Bob Smartfour.
All events belong to the same encounter and further have the same "event end" and "update" date/time values.
The expected result is a single VitalSigns object with encounters and vitals information.
Height, Weight, bodyPosition are null

Tests the functionality to
-read results values and codes from the cerner response.
-accurately map cerner codes to codes the SMART platform expects.
-identify and group together blood pressure events.
-identify and group together events based on a same "event end" and "update" date/time values. This creates a VitalSigns object. 

Test case#3: (test_data/ResultsTest.xml)
The test data contains clinical events information for Bob Smartfour.
All VitalSigns objects belong to the same encounter.
Expected result is a set of VitalSigns objects with 9 distinct date values among each other.

Tests the functionality to
-assign the correct date to different VitalSigns object, based on the date/time values of the constituent vitals.