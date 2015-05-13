#!/bin/bash

java -jar MABED.jar -split tweets.csv 60
java -Xmx7000M -jar MABED.jar -run

