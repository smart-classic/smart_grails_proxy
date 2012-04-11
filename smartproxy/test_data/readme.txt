This file lists the tests implemented in VitalTest.groovy
#########################################################

DATA
The data in the test files belongs to a dummy patient "Bob Smartfour".
The results and encounters data was created through Cerner Power Chart and is to be used for testing only.
The data is made up and holds no clinical significance.
#########################################################

Test case#1 
File: test_data/Results.xml

The test data contains clinical events information for Bob Smartfour.
All events belong to the same encounter and also have the same parent event id.
The expected result is a single VitalSigns object with encounters and vitals information.
Height and Weight are null

Tests the functionality to
-read results values and codes from the response recieved by making cerner MO API calls.
-accurately map cerner codes to codes the SMART platform uses.
-identify and group blood pressure events.
-identify and group events based on a common parent event id. This creates a single VitalSigns object. 
#########################################################

Test case#2: (test_data/Results-Date.xml)
The test data contains clinical events information for Bob Smartfour.
All events belong to the same encounter and further have the same "event end" and "update" date/time values.
The expected result is a single VitalSigns object with encounters and vitals information.
Height, Weight, bodyPosition are null

Tests the functionality to
-read results values and codes from the response recieved by making cerner MO API calls.
-accurately map cerner codes to codes the SMART platform uses.
-identify and group blood pressure events.
-identify and group events based on "event end" and "update" date/time values. This creates a single VitalSigns object. 
#########################################################

Test case#3: (test_data/ResultsTest.xml)
The test data contains clinical events information for Bob Smartfour.
All VitalSigns objects belong to the same encounter.
Expected result is a set of VitalSigns objects. A set of date properties for all the vitalsigns objects contains 9 elements. 

Tests the functionality to
-assign the correct date to different VitalSigns objects, based on the date/time values of the constituent results (vitals).