FROM ubuntu:latest
LABEL authors="milli"

ENTRYPOINT ["top", "-b"]