#!/bin/bash

host="http://localhost:8080"
endpoint_screening="/screening"
endpoint_seats="/seats"
endpoint_reservation="/reservation"

function callUrlWithGetAndPresentResponse() {
	# Call the URL using curl and store the response in a variable
	response=$(curl -s -w "\n%{http_code}" "$host$1?$2")

	# Extract the response body and status code from the variable
	body=$(echo "$response" | head -n -1)
	status_code=$(echo "$response" | tail -n 1)

	printf "\n\n\n"
	echo "Calling: "$1" with parameters: "$2""
	echo "Response Body:"
	echo "$body" | python -m json.tool
	echo "Status Code: $status_code"
}

function callReservationUrlAndPresentResponse() {
	reservation_request_body="{\"client\":{\"name\":\"Jan\",\"surname\":\"Kowalski\"},\"screening\":{\"title\":\"Eyes wide open\",\"time\":\"2023-07-01T20:40\"},\"seats\":[{\"row\":$1,\"number\":$2,\"type\":\"ADULT\"},{\"row\":$3,\"number\":$4,\"type\":\"CHILD\"},{\"row\":$5,\"number\":$6,\"type\":\"STUDENT\"},{\"row\":$7,\"number\":$8,\"type\":\"STUDENT\"}]}"

	response=$(curl -s -X POST -H "Content-Type: application/json" -d "$reservation_request_body" "$host$endpoint_reservation")
	prettified_response=$(echo "$response" | python -m json.tool)

	printf "\n\n\n"
	echo "Calling: $endpoint_reservation"
	echo "Reserving seats: {row: $1, seat: $2}, {row: $3, seat: $4}, {row: $5, seat: $6}"
	echo "Response Body:"
	echo "$prettified_response"
}


screening_parameters="date=2023-07-01&time=20:00"
callUrlWithGetAndPresentResponse $endpoint_screening $screening_parameters

seats_parameters="title=Eyes+wide+open&date=2023-07-01&time=20:40&seats="
seats=4
callUrlWithGetAndPresentResponse "$endpoint_seats" "$seats_parameters$seats"

callReservationUrlAndPresentResponse 9 5 9 6 9 7 9 8

seats=6
callUrlWithGetAndPresentResponse "$endpoint_seats" "$seats_parameters$seats"

callReservationUrlAndPresentResponse 8 3 8 4 8 5 8 6

seats=2
callUrlWithGetAndPresentResponse "$endpoint_seats" "$seats_parameters$seats"

callReservationUrlAndPresentResponse 8 7 8 8 8 9 8 10
callReservationUrlAndPresentResponse 7 6 7 7 7 10 7 11

seats=5
callUrlWithGetAndPresentResponse "$endpoint_seats" "$seats_parameters$seats"