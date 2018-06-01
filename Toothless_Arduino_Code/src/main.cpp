#include <Arduino.h>
#include <AFMotor.h>

AF_DCMotor mbl(1);

void setup() {
    mbl.setSpeed(255);
}

void loop() {
    mbl.run(FORWARD);
}