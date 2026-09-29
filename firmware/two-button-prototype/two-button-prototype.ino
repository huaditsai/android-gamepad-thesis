#include <RFduinoBLE.h>

int led = 3;
int buttonL = 4;
int buttonR = 5;


int buttonStateL = 0; 
int buttonStateR = 0;

void setup() {
  //Serial.begin(9600);
  // led turned on/off from the iPhone app
  pinMode(led, OUTPUT);

  // button press will be shown on the iPhone app)
  pinMode(buttonL, INPUT);
  pinMode(buttonR, INPUT);

  // this is the data we want to appear in the advertisement
  // (the deviceName length plus the advertisement length must be <= 18 bytes
  RFduinoBLE.advertisementData = "ledbtn";
  
  // start the BLE stack
  RFduinoBLE.begin();
}

void loop() 
{
  buttonStateL = digitalRead(buttonL);
  buttonStateR = digitalRead(buttonR);
  
  //Serial.println(buttonStateR);
  
  if (buttonStateL == HIGH) 
    RFduinoBLE.sendByte(1);
    
  if (buttonStateR == HIGH) 
    RFduinoBLE.sendByte(2);

  if(buttonStateL == LOW && buttonStateL == LOW)
    RFduinoBLE.sendByte(0); 
    
    delay(10);

}
void RFduinoBLE_onConnect()
{
  digitalWrite(led, HIGH);
}
void RFduinoBLE_onDisconnect()
{
  // don't leave the led on if they disconnect
  digitalWrite(led, LOW);
}

void RFduinoBLE_onReceive(char *data, int len)
{
  // if the first byte is 0x01 / on / true
  if (data[0])
    digitalWrite(led, HIGH);
  else
    digitalWrite(led, LOW);
}