// the setup routine runs once when you press reset:
void setup() {
  // initialize serial communication at 9600 bits per second:
  Serial.begin(9600);
}

// the loop routine runs over and over again forever:
void loop() {
  // read the input on analog pin 5:
  Serial.print(analogRead(4));
  Serial.print(", ");
  Serial.print(analogRead(5));
  Serial.print(", ");
  Serial.println(analogRead(6));
  delay(10);
}
