#include <Arduino.h>
#include <AFMotor.h>
#include <Servo.h>

Servo stest;
AF_DCMotor mbl(1);
int servoPos=0;
int trigPin = A0;
int echoPin = A1;
float cal_dis();

void setup() {
    Serial.begin(9600);
    mbl.setSpeed(255);
    stest.attach(10);
    pinMode(trigPin,OUTPUT);
    pinMode(echoPin,INPUT);
}

void loop() {
    float distance;
      //0 degree is left , 180 is right , 90 is center
    for(servoPos=0;servoPos<=180;servoPos++){
        distance=cal_dis();
        Serial.println(distance);
        while(distance<=40){
        distance=cal_dis();
        }
        stest.write(servoPos);
    }
    
    for(servoPos=180;servoPos>=0;servoPos--){
        distance=cal_dis();
        Serial.println(distance);
        while(distance<=40){
        distance=cal_dis();
        }
        stest.write(servoPos);
    }
   //stest.write(90);   //stationary state
    
}
float cal_dis(){

        float duration,distance;
        digitalWrite(trigPin,LOW);
        delayMicroseconds(2);
        digitalWrite(trigPin,HIGH);
        delayMicroseconds(10);
        digitalWrite(trigPin,LOW);
        duration=pulseIn(echoPin,HIGH);
        distance=(duration/2)*0.03444;
        return distance;
}