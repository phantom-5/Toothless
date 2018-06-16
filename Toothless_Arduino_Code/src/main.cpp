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
int Bluetooth_Override_Active=0;
int Transit_Mode_Active=0;
int Left_Angle_Count=0;
int Right_Angle_Count=0;
float cal_dis();
void move_forward();
void move_backward();
void move_left();
void move_right();
void strategic_delay(int value);
void stop();
int scan(int dis);

void setup() {
    //Serial.begin(9600);
    mbr.setSpeed(200);
    mur.setSpeed(200);
    mbl.setSpeed(200);
    mul.setSpeed(200);
    stest.attach(10);
    pinMode(trigPin,OUTPUT);
    pinMode(echoPin,INPUT);
}

void loop() {
    stest.write(90);
    if (Bluetooth_Override_Active == 0){
        if (Transit_Mode_Active == 0){
           int gotcha = scan(45);
          // Serial.println("gotcha");
          // Serial.println(gotcha);
           if(gotcha>0){
               //move left
               while(move_control==0){
                move_right();
                strategic_delay((((7-gotcha)*350)));
               }stop();
           }else if(gotcha<0){
               //move right
               while(move_control==0){
                   move_left();
                   strategic_delay(((7-((-1)*gotcha))*350));
               }stop();
               
           }
           move_control=0;
           scan_control=0;
           Transit_Mode_Active=1;
        }else{
            if(cal_dis()<=60 && cal_dis()>25){
                while(cal_dis() <= 40){
                       move_forward();
                }
                stop();
            }else if(cal_dis()>=0 && cal_dis()<=25){
                while(cal_dis()>=40){
                    move_backward();
                }
                stop();
            }else{
                Transit_Mode_Active=0;
            }
            
        }
    }//this ends autonomous code
    
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
      Transit_Mode_Active=0;

}

void move_backward(){
      mbr.run(BACKWARD);
      mur.run(BACKWARD);
      mbl.run(BACKWARD);
      mul.run(BACKWARD);
      Transit_Mode_Active=0;
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

}
void stop(){
      mbr.run(RELEASE);
      mur.run(RELEASE);
      mbl.run(RELEASE);
      mul.run(RELEASE);
}
int scan(int dis){
    while(scan_control==0){
      float distance;
      int angle=0;
      Left_Angle_Count=0;
      Right_Angle_Count=0;
      //0 degree is left , 180 is right , 90 is center
       stest.write(90);
       if(cal_dis()<=dis){
           return 0;
       }
    for(servoPos=0;servoPos<=90;servoPos+=15){
        distance=cal_dis();
      //  Serial.println(distance);
        if(distance<=dis){
        stest.write(servoPos);
        //angle=servoPos;
        distance=cal_dis();
        scan_control=1;
        break;
        }
        stest.write(servoPos);
        delay(200);
        //angle++; //move towards right
        Left_Angle_Count++;
    }
    if(cal_dis()<=dis){
           return 0;
    }
    if(scan_control==1){
        if (Left_Angle_Count==6){stest.write(90);}
        return Left_Angle_Count;
    }
    for(servoPos=180;servoPos>=90;servoPos-=15){
        distance=cal_dis();
      //  Serial.println(distance);
        if(distance<=dis){
        stest.write(servoPos);
        //angle=servoPos;
        distance=cal_dis();
        scan_control=1;
        break;
        }
        stest.write(servoPos);
        delay(200);
        //angle--; //moves towards left
        Right_Angle_Count--;
    }
    if(scan_control==1){
        if (Right_Angle_Count==6){stest.write(90);}
        return Right_Angle_Count;
    }
    }

}
void strategic_delay(int value){
    delay(value);
    move_control=1;
    Serial.println(move_control);
}