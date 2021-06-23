//INCULDES
#include <OneWire.h> 
#include <DallasTemperature.h>
//#include <DHT.h>    // Having issues with this import
#include <Wire.h>
#include <LiquidCrystal_I2C.h>

// Definitions
#define SLAVE_ADDR 9
#define RESPONSE_SIZE 30
#define pumppin 6
#define fanpin1 13
#define fanpin2 A2
#define valvepin 8

#define tecpin0 9
#define tecpin1 10
#define tecpin2 11 
#define tecpin3 5

#define temppin 12
#define flowpin 3
#define temphumidpin 4
#define levelpin A3

#define pb1pin 2
#define pb2pin A1
#define togglepin A0

// VARIABLES
String cmd = "";
//Temperature Sensor
OneWire oneWire(temppin); 
DallasTemperature sensors(&oneWire);

//Temperature Humidity Sensor
//dht DHT;

//LCD Screen
LiquidCrystal_I2C lcd(0x27,16,2);

//Parameters
int motorvalue; 
int onoff = 1;

//Water Level Sensor
int resval = 0;
int leakdetect = 1;
int newleak = 0;

volatile int NbTopsFan;

double settemp = 0;
int newsettemp = false;
int sysrunning = false;
double dewpoint;
double tempuplimit = 30;



int cooleronoff = false;

int testtemp = 24;

unsigned long previousMillis = 0;
const long interval = 2000;
unsigned long previousMillis2 = 0;
const long interval2 = 4500;
unsigned long previousMillis3 = 0;
const long interval3 = 100;
unsigned long previousMillis4 = 0;
const long interval4 = 10;

int state1 = 0;      // the current state of the output pin
int state2 = 0;
int reading1 = 0;           // the current reading from the input pin
int reading2 = 0;
int previous1 = 1;    // the previous reading from the input pin
int previous2= 1;
unsigned long lastdebouncetime = 0;           // the last time the output pin was toggled
unsigned long debounce = 100; 


unsigned long elapsedCTime, CTime, CTimePrev;
float PIDerror;
float PIDP=0;
float PIDI=0;
float PIDD=0;
float PIDvalue=0;

float previouserror;
double kp = 1000;
double ki = 2;
double kd = 0;
void setup() {
  // I2C Comminucation
  Serial.begin(9600);
  Wire.begin(SLAVE_ADDR);
  Wire.onReceive(receiveEvent);
  Wire.onRequest(requestEvent);

  // Pin mode assignments
  pinMode(pumppin, OUTPUT);
  analogWrite(pumppin, 255);
  pinMode(fanpin1, OUTPUT);
  digitalWrite(fanpin1, 1);
  pinMode(fanpin2, OUTPUT);
  digitalWrite(fanpin2, 1);
  pinMode(temppin, INPUT);
  pinMode(flowpin, INPUT);
  pinMode(pb1pin, INPUT);
  pinMode(pb2pin, INPUT);
  pinMode(valvepin, OUTPUT);
  pinMode(tecpin0, OUTPUT);
  pinMode(tecpin1, OUTPUT);
  pinMode(tecpin2, OUTPUT);
  pinMode(tecpin3, OUTPUT);
  analogWrite(tecpin0, 0);
  analogWrite(tecpin1, 0);
  analogWrite(tecpin2, 0);
  analogWrite(tecpin3, 0);

sensors.begin();

  sensors.requestTemperatures();
  double temperature = sensors.getTempCByIndex(0);
  
  settemp = 20;

  attachInterrupt(1, rpm, RISING);
  
  lcd.init();
  lcd.backlight();
  lcd.setCursor(0,0);
  lcd.print("Set up Complete");
  lcd.setCursor(0,1);
  lcd.print("Toggle Switch");

  // I2C communication
}

void loop() {
  //settemplimit();
  startstopsys();
  changesettemp();
  displaytemps();
  //pidcontroller();
  //onoffcontroller();
  //waterlevel();

  unsigned long currentMillis = millis();
  if (currentMillis - previousMillis4 >= interval4) 
  {
    previousMillis4 = currentMillis;
    if (sysrunning)
    {
      Serial.print(millis()  );
      Serial.print("\t");
      Serial.println(  getwatertemp());
    }
  }
}

//SYSTEM STARTS/STOPS IF SWITCH IS TOGGLED
void startstopsys() //Turns on Fan, Diplays Temperature, Waits 2 Seconds Turns on Pump full power.
{
  if (digitalRead(togglepin) && onoff)   //START System
  {
    lcd.clear();
    lcd.setCursor(0,0);
    lcd.print("Starting System...");
    lcd.setCursor(0,1);
    lcd.print("");
    digitalWrite(fanpin1, 0);
    digitalWrite(fanpin2, 0);
    digitalWrite(valvepin, 1);
    delay(1500);
    analogWrite(pumppin, 0);
    analogWrite(tecpin0, 0);
    analogWrite(tecpin1, 0);
    analogWrite(tecpin2, 0);
    analogWrite(tecpin3, 0);
    lcd.clear();
    lcd.setCursor(1,0);
    lcd.print("System Running");
    lcd.setCursor(0,1);
    lcd.print("");
    delay(1500);
    sysrunning = true;
    onoff = 0;
  }

  if (!digitalRead(togglepin) && !onoff)    //STOP System
  {
    analogWrite(tecpin0, 0); //Shut off TEC
    analogWrite(tecpin1, 0);
    analogWrite(tecpin2, 0);
    analogWrite(tecpin3, 0);
    cooleronoff = false;
    analogWrite(pumppin, 255);
    digitalWrite(valvepin, 0);
    lcd.clear();
    lcd.setCursor(0,0);
    lcd.print("System Stopped");
    lcd.setCursor(0,1);
    lcd.print("Toggle Switch");
    delay(1000);
    digitalWrite(fanpin1, 1);
    digitalWrite(fanpin2, 1);
    sysrunning = false;
    onoff = 1;
  }
}


//PRINTS AVERAGE OF 10 FLOW RATE READINGS
double getflowrate()
{
  int Calc;   
  int avg;
  int sum = 0;
    
  for (int m = 0; m < 10; m++) 
  {
    NbTopsFan = 0;     //Set NbTops to 0 ready for calculations
    sei();            //Enables interrupts
    delay (100);      //Wait 100 millisecond
    cli();            //Disable interrupts
    Calc = (NbTopsFan*60)/4200;//(Pulse frequency x 60  / 7.5Q, = flow rate in L/hour 
    sum = (sum + Calc);
  }
  avg = sum/10;
  lcd.clear();
  lcd.setCursor(0,0);
  lcd.print("Flow Rate:");
  lcd.setCursor(0,1);
  lcd.print(" L/min");
  avg = 0;
  return sum;
}

void rpm()     //This is the function that the interupt calls for flow rate sensor
{ 
 NbTopsFan++;  //This function measures the rising and falling edge of the hall effect sensors signal
}

void waterlevel()
{
  resval = analogRead(levelpin);

  unsigned long currentMillis2 = millis();

  if ((currentMillis2 - previousMillis2 >= interval2)) {
    previousMillis2 = currentMillis2;
    if (sysrunning)
    {
      if (resval<=200 && (leakdetect != newleak) && sysrunning ){
      lcd.clear();
      lcd.setCursor(0,0);
      lcd.print("Water Level Low");
      newleak = leakdetect;
     }

    //digitalWrite(solenoidPin,LOW); //valve opens
    }
  }
  newleak =! leakdetect;
}

double getwatertemp()
{
  double watertemp = 0;
  sensors.requestTemperatures();
  watertemp = sensors.getTempCByIndex(0);
  while ((watertemp < 1) | (watertemp > 75))
  {
    sensors.requestTemperatures();
    watertemp = sensors.getTempCByIndex(0);
  }
  return watertemp;
  
}

void changesettemp()
{
      state1 = digitalRead(pb1pin);

      if (state1 == HIGH) {
         settemp++;
         newsettemp = true;
      }
      state2 = digitalRead(pb2pin);
      // only toggle if the new button state is HIGH
      if (state2 == HIGH) {
         settemp--;
         newsettemp = true;
      }
}


//void settemplimit()
//{
//  int chk = DHT.read11(temphumidpin);
//  dewpoint = DHT.temperature *(DHT.humidity/100);
//  if (settemp > (tempuplimit-1))
//  {
//    settemp--;
//    lcd.clear();
//    lcd.setCursor(0,0);
//    lcd.print("Cannot Increase");
//    lcd.setCursor(0,1);
//    lcd.print("above ");
//    lcd.print(tempuplimit);
//  }  
//  if (settemp < (dewpoint+1))
//  {
//    settemp++;
//    lcd.clear();
//    lcd.setCursor(0,0);
//    lcd.print("Cannot decrease");
//    lcd.setCursor(0,1);
//    lcd.print("below dewppoint");
//  }
//}

void onoffcontroller()
{
  double temperature = getwatertemp();
  unsigned long currentMillis = millis();
  if ((currentMillis - previousMillis3 >= interval3)) 
  {
    previousMillis3 = currentMillis;
    while(sysrunning)
    {
      if((temperature < settemp-0.30) && cooleronoff)
      {
          analogWrite(tecpin0, 0); //Shut off TEC
          analogWrite(tecpin1, 0);
          analogWrite(tecpin2, 0);
          analogWrite(tecpin3, 0);
          cooleronoff = false;
      }
      if((temperature > settemp-0.25) && !cooleronoff)
      {
          analogWrite(tecpin0, 255);
          analogWrite(tecpin1, 255);
          analogWrite(tecpin2, 255);
          analogWrite(tecpin3, 255);
          cooleronoff = true;
        }
        break;
    }  
  }
}


void pcontroller()
{
  
  double temperature = getwatertemp();
  unsigned long currentMillis = millis();
  if ((currentMillis - previousMillis3 >= interval3)) 
  {
    previousMillis3 = currentMillis;
    while(sysrunning)
    {
      PIDerror = settemp - temperature;
      //Calculate the P value
      PIDP = kp * PIDerror;
      //We define PWM range between 0 and 255
      if(PIDP < 0)
      {    PIDP = 0;    }
      if(PIDP > 255)  
      {    PIDP = 255;  }
      //Now we can write the PWM signal to the mosfets
      analogWrite(tecpin0,255-PIDP);
      analogWrite(tecpin1,255-PIDP);
      analogWrite(tecpin2,255-PIDP);
      analogWrite(tecpin3,255-PIDP);
      previouserror = PIDerror;     //Remember to store the previous error for next loop.
      break;
    }  
  }
}


//void pidcontroller()
//{
//  
//  double temperature = getwatertemp();
//  unsigned long currentMillis = millis();
//  if ((currentMillis - previousMillis3 >= interval3)) 
//  {
//    previousMillis3 = currentMillis;
//    while(sysrunning)
//    {
//      PIDerror = (currentTemperature - temperature);
//      //Calculate the P value
//      PIDP = kp * PIDerror;
//
//      if (-10 < PIDP < 10)
//      {
//        PIDI = PIDI + (ki*PIDerror);
//      }
//
//      CTimePrev = CTime;
//      CTime = millis();
//      elapsedCTime = (CTime - CTimePrev)/1000;
//      PIDD = kd*((PIDerror - previouserror)/elapsedCTime);
//      
//      PIDvalue = PIDP + PIDI + PIDD;
//      
//      //We define PWM range between 0 and 255
//      if(PIDvalue < 0)
//      {    PIDvalue = 0;    }
//      if(PIDvalue > 255)  
//      {    PIDvalue = 255;  }
//      //Now we can write the PWM signal to the mosfets
//      analogWrite(tecpin0,255-PIDvalue);
//      analogWrite(tecpin1,255-PIDvalue);
//      analogWrite(tecpin2,255-PIDvalue);
//      analogWrite(tecpin3,255-PIDvalue);
//      previouserror = PIDerror;     //Remember to store the previous error for next loop.
//      break;
//    }  
//  }
//}

void displaytemps()
{
  unsigned long currentMillis = millis();
  if ((currentMillis - previousMillis >= interval) | (newsettemp)) {
    previousMillis = currentMillis;
    if (sysrunning)
    {
     //LCD Display
     lcd.clear();
     lcd.setCursor(0,0);
     lcd.print("Set Temp:");
     lcd.print(settemp);
     lcd.print(" C");
     lcd.setCursor(0,1);
     lcd.print("Read Temp:");
     lcd.print(getwatertemp());
     lcd.print(" C");
     newsettemp = false;
    }
  }
}



// ------------------------- I2C communication Methods----------------------
void receiveEvent(){
  cmd = "";
    while(Wire.available()){
        char code = Wire.read();
        cmd += code;
      }
      Wire.flush();
  }
void requestEvent(){
    // this function must return a response to the master
    String resp = getMasterResponse();
    byte arr[resp.length() + 1];
    resp.getBytes(arr,resp.length() +1);
    Wire.write(arr, resp.length() +1);
  }  

String getMasterResponse(){
    // This function will handle the command and prepare a response to 
    // sent to the master
  String reply = "";
  if (checkResponse('G')){
        reply = "125.75|124.96|175.95|745.36|"; 
        Serial.println("Slave responded witht the values");
  }else if ((checkResponse('S') && checkResponse('T'))){     // set Temperature 
        // Update the current temperature 
        String val = getValue(cmd, '|', 1);
        //double temp = atof(val);
        Serial.println("The temp is now: " + val);   
        reply = "OK";
  }else if(checkResponse('S') && checkResponse('V')){      // change the status of the valve
        reply = "OK";
  }
  else{
      // do nothing
      Serial.println("Slave responded with ERROR");
      reply = "ERROR";
    }
    reply = reply + "*";
  while (reply.length() != RESPONSE_SIZE){
        reply = reply + "#";
      }
    return reply;
  }

boolean checkResponse(char wanted){
    // will return an true if the required char is found in response
    char charBuf [cmd.length()];
    cmd.toCharArray(charBuf,cmd.length());
    boolean flag = false;
    for(int i =0; i< cmd.length();i++){
        if (wanted == charBuf[i]){
            flag = true;
        }
    }
    return flag;
  }

String getValue(String data, char separator, int index)
{
    int found = 0;
    int strIndex[] = { 0, -1 };
    int maxIndex = data.length() - 1;

    for (int i = 0; i <= maxIndex && found <= index; i++) {
        if (data.charAt(i) == separator || i == maxIndex) {
            found++;
            strIndex[0] = strIndex[1] + 1;
            strIndex[1] = (i == maxIndex) ? i+1 : i;
        }
    }
    return found > index ? data.substring(strIndex[0], strIndex[1]) : "";
}
