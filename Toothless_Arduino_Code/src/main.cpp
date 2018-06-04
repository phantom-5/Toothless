#include <Arduino.h>
#include <AFMotor.h>
#include <Servo.h>

Servo stest;
AF_DCMotor mur(1);
AF_DCMotor mbr(2);
AF_DCMotor mbl(4);
AF_DCMotor mul(3);
int servoPos=0;
int trigPin = A0;
int echoPin = A1;
float cal_dis();
void move_forward();
void move_backward();

void setup() {
    Serial.begin(9600);
    mbr.setSpeed(255);
    mur.setSpeed(255);
    mbl.setSpeed(255);
    mul.setSpeed(255);
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
        delay(5);
        while(distance<=40){
        stest.write(servoPos);
        distance=cal_dis();
        }
        stest.write(servoPos);
        //move_forward();
      //move_backward();
    }
    
    for(servoPos=180;servoPos>=0;servoPos--){
        distance=cal_dis();
        Serial.println(distance);
        delay(5);
        while(distance<=40){
        stest.write(servoPos);
        distance=cal_dis();
        }
        stest.write(servoPos);
    
      

       
    }
   //stest.write(90);   //stationary state
}

float cal_dis(){
//returns distance in cm
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

void move_forward(){
      mbr.run(FORWARD);
      mur.run(FORWARD);
      mbl.run(FORWARD);
      mul.run(FORWARD);
}

void move_backward(){
      mbr.run(BACKWARD);
      mur.run(BACKWARD);
      mbl.run(BACKWARD);
      mul.run(BACKWARD);
}