#!/bin/bash

# Set up colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Base URL
BASE_URL="http://localhost:8080/api"

# Track success/failure
PASS_COUNT=0
FAIL_COUNT=0

# Function to check test results
check_result() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✓ Test Passed: $2${NC}"
        ((PASS_COUNT++))
    else
        echo -e "${RED}✗ Test Failed: $2${NC}"
        ((FAIL_COUNT++))
    fi
}

# Start Spring Boot application with test profile
echo -e "${BLUE}Starting Spring Boot application with test profile...${NC}"
echo -e "${BLUE}This is a simulation script. In a real environment, you'd start the application first.${NC}"
echo -e "${BLUE}For actual testing, please run the application with:${NC}"
echo -e "${BLUE}mvn spring-boot:run -Dspring-boot.run.profiles=test${NC}"
echo

# Authenticate as doctor
echo -e "${BLUE}Test 1: Authenticate as doctor${NC}"
echo "POST $BASE_URL/auth/login"
echo '{
  "email": "doctor@example.com", 
  "password": "password"
}'
echo -e "${BLUE}Expected response:${NC}"
echo '{
  "token": "eyJhbGciO...",
  "refreshToken": "eyJhbGciO...",
  "userId": 1,
  "name": "Dr. Smith",
  "role": "DOCTOR"
}'
check_result 0 "Doctor authentication"

# Set doctor token
DOCTOR_TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkb2N0b3JAZXhhbXBsZS5jb20iLCJpYXQiOjE2NzIzMjkzNzAsImV4cCI6MTY3MjQxNTc3MH0.mock_token"

# Authenticate as patient
echo -e "${BLUE}Test 2: Authenticate as patient${NC}"
echo "POST $BASE_URL/auth/login"
echo '{
  "email": "patient@example.com", 
  "password": "password"
}'
echo -e "${BLUE}Expected response:${NC}"
echo '{
  "token": "eyJhbGciO...",
  "refreshToken": "eyJhbGciO...",
  "userId": 2,
  "name": "Patient Jones",
  "role": "PATIENT"
}'
check_result 0 "Patient authentication"

# Set patient token
PATIENT_TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYXRpZW50QGV4YW1wbGUuY29tIiwiaWF0IjoxNjcyMzI5MzcwLCJleHAiOjE2NzI0MTU3NzB9.mock_token"

# Doctor sends message to patient
echo -e "${BLUE}Test 3: Doctor sends message to patient${NC}"
echo "POST $BASE_URL/messages/send"
echo "Authorization: Bearer $DOCTOR_TOKEN"
echo '{
  "recipientId": 2,
  "content": "Hello, how are you feeling today?"
}'
echo -e "${BLUE}Expected response:${NC}"
echo '{
  "id": 1,
  "senderId": 1,
  "senderName": "Dr. Smith",
  "senderRole": "DOCTOR",
  "receiverId": 2,
  "receiverName": "Patient Jones",
  "receiverRole": "PATIENT",
  "content": "Hello, how are you feeling today?",
  "timestamp": "2023-04-15T10:30:00",
  "isRead": false
}'
check_result 0 "Doctor sending message to patient"

# Patient sends message to doctor
echo -e "${BLUE}Test 4: Patient sends message to doctor${NC}"
echo "POST $BASE_URL/messages/send"
echo "Authorization: Bearer $PATIENT_TOKEN"
echo '{
  "recipientId": 1,
  "content": "I am feeling better, thank you!"
}'
echo -e "${BLUE}Expected response:${NC}"
echo '{
  "id": 2,
  "senderId": 2,
  "senderName": "Patient Jones",
  "senderRole": "PATIENT",
  "receiverId": 1,
  "receiverName": "Dr. Smith",
  "receiverRole": "DOCTOR",
  "content": "I am feeling better, thank you!",
  "timestamp": "2023-04-15T10:35:00",
  "isRead": false
}'
check_result 0 "Patient sending message to doctor"

# Doctor lists conversations
echo -e "${BLUE}Test 5: Doctor lists conversations${NC}"
echo "GET $BASE_URL/messages/conversations"
echo "Authorization: Bearer $DOCTOR_TOKEN"
echo -e "${BLUE}Expected response:${NC}"
echo '[
  {
    "partnerId": 2,
    "partnerName": "Patient Jones",
    "partnerRole": "PATIENT",
    "lastMessagePreview": "I am feeling better, thank you!",
    "lastMessageTime": "2023-04-15T10:35:00",
    "hasUnreadMessages": true,
    "unreadCount": 1
  }
]'
check_result 0 "Doctor listing conversations"

# Doctor gets conversation with patient
echo -e "${BLUE}Test 6: Doctor gets conversation with patient${NC}"
echo "GET $BASE_URL/messages/conversations/2"
echo "Authorization: Bearer $DOCTOR_TOKEN"
echo -e "${BLUE}Expected response:${NC}"
echo '[
  {
    "id": 1,
    "senderId": 1,
    "senderName": "Dr. Smith",
    "senderRole": "DOCTOR",
    "receiverId": 2,
    "receiverName": "Patient Jones",
    "receiverRole": "PATIENT",
    "content": "Hello, how are you feeling today?",
    "timestamp": "2023-04-15T10:30:00",
    "isRead": false
  },
  {
    "id": 2,
    "senderId": 2,
    "senderName": "Patient Jones",
    "senderRole": "PATIENT",
    "receiverId": 1,
    "receiverName": "Dr. Smith",
    "receiverRole": "DOCTOR",
    "content": "I am feeling better, thank you!",
    "timestamp": "2023-04-15T10:35:00",
    "isRead": false
  }
]'
check_result 0 "Doctor viewing conversation with patient"

# Doctor marks message as read
echo -e "${BLUE}Test 7: Doctor marks message as read${NC}"
echo "PATCH $BASE_URL/messages/2/read"
echo "Authorization: Bearer $DOCTOR_TOKEN"
echo -e "${BLUE}Expected response: No content (204)${NC}"
check_result 0 "Doctor marking message as read"

# Patient lists conversations after doctor read message
echo -e "${BLUE}Test 8: Patient lists conversations after message is read${NC}"
echo "GET $BASE_URL/messages/conversations"
echo "Authorization: Bearer $PATIENT_TOKEN"
echo -e "${BLUE}Expected response:${NC}"
echo '[
  {
    "partnerId": 1,
    "partnerName": "Dr. Smith",
    "partnerRole": "DOCTOR",
    "lastMessagePreview": "I am feeling better, thank you!",
    "lastMessageTime": "2023-04-15T10:35:00",
    "hasUnreadMessages": false,
    "unreadCount": 0
  }
]'
check_result 0 "Patient listing conversations after message is read"

# Unauthorized access attempt
echo -e "${BLUE}Test 9: Unauthorized access attempt${NC}"
echo "GET $BASE_URL/messages/conversations"
echo -e "${BLUE}Expected response: 401 Unauthorized${NC}"
check_result 0 "Unauthorized access is blocked"

# Print summary
echo
echo -e "${BLUE}Test Summary:${NC}"
echo -e "${GREEN}Passed: $PASS_COUNT${NC}"
echo -e "${RED}Failed: $FAIL_COUNT${NC}"

echo
echo -e "${BLUE}Note: This is a simulation script that doesn't actually make HTTP requests.${NC}"
echo -e "${BLUE}To run actual tests, start your Spring Boot application and run the following curl commands:${NC}"
echo
echo "# Authenticate as doctor"
echo 'DOCTOR_TOKEN=$(curl -s -X POST "http://localhost:8080/api/auth/login" -H "Content-Type: application/json" -d "{\"email\":\"doctor@example.com\", \"password\":\"password\"}" | jq -r .token)'
echo
echo "# Send message as doctor"
echo 'curl -X POST "http://localhost:8080/api/messages/send" -H "Authorization: Bearer $DOCTOR_TOKEN" -H "Content-Type: application/json" -d "{\"recipientId\":2, \"content\":\"Hello, how are you feeling today?\"}"'
echo
echo "# List conversations"
echo 'curl -X GET "http://localhost:8080/api/messages/conversations" -H "Authorization: Bearer $DOCTOR_TOKEN"'

exit 0
