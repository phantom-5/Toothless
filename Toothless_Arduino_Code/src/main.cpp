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
int echoPin = A5;
int speed=0;
int first_control_forward=0;
int move_control=0;
int scan_control=0;
int angle_loop_func=0;
float cal_dis();
void move_forward();
void move_backward();
void move_left();
void move_right();
void strategic_delay(int value);
void stop();
int scan();

void setup() {
    Serial.begin(9600);
    mbr.setSpeed(200);
    mur.setSpeed(200);
    mbl.setSpeed(200);
    mul.setSpeed(200);
    stest.attach(10);
    pinMode(trigPin,OUTPUT);
    pinMode(echoPin,INPUT);
}

void loop() {
   /**
   float distance=0;
   stest.write(90);
   distance=cal_dis();
   if(distance>45){
       while(distance>45){
       move_forward();
       distance=cal_dis();
       }
       stop();
   }

   else if(distance>10 && distance<25){
       while(distance<25){
           move_backward();
           distance=cal_dis();
       }
       stop();
   }else{
       stop();
   }
   **/
   if(scan_control==0){
   angle_loop_func=scan();
   Serial.println("angle is");
   Serial.println(angle_loop_func);
   }
   else{
       stop();
   }
   
   /**
   if(move_control==0){
   move_left();
   strategic_delay(500);
   }else{
       stop();
   }
   **/
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
void move_left(){
      mbr.run(FORWARD);
      mur.run(FORWARD);
      mbl.run(BACKWARD);
      mul.run(BACKWARD);
     
}
void move_right(){
      mbr.run(BACKWARD);
      mur.run(BACKWARD);
      mbl.run(FORWARD);
      mul.run(FORWARD);
      delay(1000);
      stop();
}
void stop(){
      mbr.run(RELEASE);
      mur.run(RELEASE);
      mbl.run(RELEASE);
      mul.run(RELEASE);
}
int scan(){
      float distance;
      int angle=0;
      //0 degree is left , 180 is right , 90 is center
    for(servoPos=0;servoPos<=180;servoPos+=15){
        distance=cal_dis();
        Serial.println(distance);
        if(distance<=45){
        stest.write(servoPos);
        angle=servoPos;
        distance=cal_dis();
        scan_control=1;
        break;
        }
        stest.write(servoPos);
        delay(200);
        angle++; //move towards right
    }
    if(scan_control==1){
        return angle;
    }
    angle=0;
    for(servoPos=180;servoPos>=0;servoPos-=15){
        distance=cal_dis();
        Serial.println(distance);
        if(distance<=45){
        stest.write(servoPos);
        angle=servoPos;
        distance=cal_dis();
        scan_control=1;
        break;
        }
        stest.write(servoPos);
        delay(200);
        angle--;
    }
    if(scan_control==1){
        return angle;
    }

}
void strategic_delay(int value){
    delay(value);
    move_control=1;
    Serial.println(move_control);
}