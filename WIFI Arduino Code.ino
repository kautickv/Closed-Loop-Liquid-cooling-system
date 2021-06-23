// IMPORTS
#include <WiFiEsp.h>
#include <WiFiEspClient.h>
#include <WiFiEspServer.h>
#include <WiFiEspUdp.h>
#include <SoftwareSerial.h>
#include <ThingSpeak.h>
#include<Wire.h> // I2C communication between Arduino

// VARIABLES
// Wifi configuration
SoftwareSerial Serial1(10,11); // RX, TX
char ssid[] = "SM-G930W89864";            // your network SSID (name)
char pass[] = "12345678";        // your network password
int status = WL_IDLE_STATUS;     // the Wifi radio's status
const unsigned int writeInterval = 45000; // write interval (in ms)to thingspeak database
unsigned long startTime;
unsigned long endTime;

// thingspeak variables
char apiKeyIn [] = "0E4DKN0J9ALOGI7U"; // API Key of Database
unsigned long channelID = 948045;
WiFiEspClient client;

// Initialize the Ethernet client object
WiFiEspServer server(80);
String response = "";
String wireResponse = "";
unsigned long bufferSize = 0;

// Operational Variables
float currentFlowRate = 0;
float currentTemperature = 20;
boolean valveStatus = true;   // true - open, false - closed

void setup() {
     // initialise the wifi module
      Serial.begin(115200);
      Serial1.begin(9600);
      WiFi.init(&Serial1);
  
       // check for the presence of the shield
    if (WiFi.status() == WL_NO_SHIELD) {
      Serial.println("WiFi shield not present");
      // don't continue
      while (true);
    }
  
    // attempt to connect to WiFi network
    while ( status != WL_CONNECTED) {
      Serial.print("Attempting to connect to WPA SSID: ");
      // Connect to WPA/WPA2 network
      status = WiFi.begin(ssid, pass);
    }
  
    // you're connected now, so print out the data
    Serial.println("You're connected to the network");
    
    printWifiStatus();
    // Start the server
    server.begin();
    ThingSpeak.begin(client);
    startTime = millis();

    // I2C communication
    Wire.begin();
    Wire.onReceive(receiveEvent);

}

void loop() {
  // listen for incoming clients
  client = server.available();
  if (client){
    while (client.connected()) {
      if (client.available()) {
        response = client.readString();
        handleCommand();
        client.flush();
      }
    }
    client.stop();
    Serial.println("Client Disconnected");
  }

   // write to database
  endTime = millis();
  if((endTime - startTime) > writeInterval){
    writetodb(currentTemperature, 2);
    startTime = millis();
  }
}

void writetodb(double temperature, int field)
{
    // write to database
    writeThingSpeak(temperature, field);
}


void printWifiStatus()
{
  // print the SSID of the network you're attached to
  Serial.print("SSID: ");
  Serial.println(WiFi.SSID());

  // print your WiFi shield's IP address
  IPAddress ip = WiFi.localIP();
  Serial.print("IP Address: ");
  Serial.println(ip);

  // print the received signal strength
  long rssi = WiFi.RSSI();
  Serial.print("Signal strength (RSSI):");
  Serial.print(rssi);
  Serial.println(" dBm");
}


void handleCommand(){
  //Serial.println("response: " + response);
  String reply = "";
  float val;
  
  if (checkResponse('G')){
       // reply = converttostring(getwatertemp()) + "|" + getHumidity() + "|" + getWaterLevel() + "|" + getFlowRate();
       Wire.beginTransmission(9);
       Wire.write("GGGGG");
       Wire.endTransmission();
       
  }else if ((checkResponse('S') && checkResponse('T'))){     // set Temperature
        val = getVal(response);        
        reply = "T Set*";
        currentTemperature = val;
  }else if (checkResponse('S') && checkResponse('F')){     // set flow rate
    reply = "F set*";  
    val = getVal(response); 
    currentFlowRate = val;
  }else if(checkResponse('S') && checkResponse('V')){      // change the status of the valve
    // Turn off the pump and close the valve.
    if (valveStatus){
      valveStatus = false;
      reply = "FALSE|OK*";  
    }else{
      valveStatus = false;
      reply = "TRUE|OK*";
      }
  }
  else{
      // do nothing
      reply = "ERROR*";
    }
 // Serial.println("Message sent");
}

boolean checkResponse(char wanted){
    // will return an integer less than 0 if did not pass the test, Otherwise > 0
    char charBuf [response.length()];
    response.toCharArray(charBuf,response.length());
    boolean flag = false;
    for(int i =0; i< response.length();i++){
        if (wanted == charBuf[i]){
            flag = true;
        }
    }
    return flag;
  }

void writeThingSpeak(float val, int field){
   ThingSpeak.writeField(channelID, field, val,apiKeyIn);
}

float getVal(String resp){
    // This function will take the received command and break it into pieces to find the value it encodes.
  char* temp;
  char charBuf [response.length()];
  response.toCharArray(charBuf,response.length());
  char * p = charBuf;
  float val = atof(p);
  return val;
  
  }

void sendToApp(String data){
   data = data + "*";
   size_t len = data.length();
  //Serial.println("Reply: " + reply);
  byte buf [len+1]; 
  data.getBytes(buf,len+1);
  client.write(buf, len+1);   
  }  

String receiveEvent(int bytes){
    wireResponse = Wire.readString();
    Serial.println("Received Response at Master = " + wireResponse);
    sendToApp(wireResponse);
    Serial.println("Master responded to the App's request.");
  }  
