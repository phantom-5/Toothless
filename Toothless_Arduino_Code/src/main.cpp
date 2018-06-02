#include <Arduino.h>
#include <AFMotor.h>
#include <Servo.h>

Servo stest;
AF_DCMotor mbl(1);


void setup() {
    mbl.setSpeed(255);
    stest.attach(10);
}

void loop() {
    stest.write(0);
   // mbl.run(FORWARD);
}