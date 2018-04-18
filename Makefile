.PHONY: compile all install clean check
.NOTPARALLEL: all install

CC = mvn

all: compile

compile:
	${CC} package


install:
	${CC} install


check:
	${CC} test


clean:
	${CC} clean

