#!/bin/bash
GIT_COMMIT?=$(shell git rev-parse --short HEAD)
ENV?=dev
PACTICIPANT := OrderService
PACTICIPANT_REPORT := ${PWD}/target/pacts/OrderService-BeerService.json
MYDIR := ${PWD}/target/pacts
PACT_CLI="docker run --rm -w ${PWD} -v ${PWD}:${PWD} -e PACT_BROKER_BASE_URL -e PACT_BROKER_TOKEN pactfoundation/pact-cli:latest"

build: pacticipant test publish deploy_to_env can_i_deploy

test:
	mvn clean test

pacticipant:
	@"${PACT_CLI}" pact-broker create-or-update-pacticipant --name=${PACTICIPANT}

publish:
	@"${PACT_CLI}" publish ${PACTICIPANT_REPORT} --consumer-app-version=${GIT_COMMIT} --branch=${ENV}

deploy_to_env:
	@"${PACT_CLI}" pact-broker record-deployment --pacticipant=${PACTICIPANT} --version=${GIT_COMMIT} --environment=${ENV}

can_i_deploy:
	@"${PACT_CLI}" pact-broker can-i-deploy --pacticipant=${PACTICIPANT} --to-environment=${ENV} --version=${GIT_COMMIT}

echo:
	echo @"${PACT_CLI}" pact-broker can-i-deploy --pacticipant=${PACTICIPANT} --to-environment=${ENV} --version=${GIT_COMMIT}