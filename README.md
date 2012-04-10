Translation layer to expose the SMART API on the Cerner system at Children's
Hospital Boston.  We expect this implementation may be useful background reading
for others who are exploring ways to run SMART Apps on a Cerner system, although
it hasn't been built with portablity in mind.

There are two primary components:

## MPage to launch an app from inside PowerChart
This component launches an external browser window using Citrix server-to-client
redirection.  The external browser window's URL contains a deep-link to the
proxy layer that will run a specified app in the context of the current patient
record.

## Grails translation layer
This component exposes a subset of the SMART REST API (demographics and
VitalSigns) by wrapping Cerner's Millennium objects API.  It is designed to work
in conjunction with the SMART Reference EMR, which handles OAuth, app
installation, and in-browser UI.
