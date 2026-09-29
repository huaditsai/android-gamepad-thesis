#include <RFduinoBLE.h>

int led = 1;
int button = 4;
int x_axis= 5;
int y_axis= 6;

int a = 0;

void setup() {
  //Serial.begin(9600);
  // led turned on/off from the iPhone app
  pinMode(led, OUTPUT);

  // button press will be shown on the iPhone app)
  pinMode(button, INPUT);
  pinMode(x_axis, INPUT);
  pinMode(y_axis, INPUT);

  // this is the data we want to appear in the advertisement
  // (the deviceName length plus the advertisement length must be <= 18 bytes
  RFduinoBLE.advertisementData = "gamepad";
  
  // start the BLE stack
  RFduinoBLE.begin();
}

void loop() 
{ 
  a = analogRead(button)*1000*1000 + analogRead(x_axis) * 1000 + analogRead(y_axis);
 // Serial.println(a);
  RFduinoBLE.sendInt(a); 
  //RFduinoBLE.sendFloat(16.49);
    
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