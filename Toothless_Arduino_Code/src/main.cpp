#include <Arduino.h>
#include <AFMotor.h>
#include <Servo.h>

Servo stest;
AF_DCMotor mbl(1);
int servoPos=0;


void setup() {
    mbl.setSpeed(255);
    stest.attach(10);
}

void loop() {
    for(servoPos=0;servoPos<=180;servoPos++){
        stest.write(servoPos);
        delay(10);
    }
    for(servoPos=180;servoPos>=0;servoPos--){
        stest.write(servoPos);
        delay(10);
    }
    
}