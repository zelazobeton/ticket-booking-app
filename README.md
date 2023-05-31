# Read Me First

## Build and run
To build and run the application run build_and_run.sh from the main directory. Script does not accept any arguments.
Script requires Java version 11.

## Test endpoints
Sample requests can be send using test_endpoints.sh. Script sends requests to all API endpoints and prints responses.
It contains several reservation requests and requests getting available seats so that the behaviour of seat recommendation 
mechanism is presented. Script can be easily extended using its functions. Script does not accept any arguments.

## Additional requirement
Project implements *Seat recommendation* as additional requirement described in the specification.

## Seat recommendation mechanism
Mechanism is part of /seats endpoint. Auditorium is visualized in the response body of /reservation endpoint using 
symbols: dot - free seat, X - reserved seat, O - recommended seat.
Mechanism never splits reserved seats into multiple rows. If user requested too many seats to be placed in a single row
no recommended seats are returned. 

## Additional assumptions
* I have skipped business requirement number 2: 
    "There cannot be a single place left over in a row between two already reserved places",
    because I found it incomprehensible.

## ADL
Simplified ADL can be found in ADL.md file in main directory.

