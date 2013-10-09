import com.leapmotion.leap.*;
import de.iav.gestures.tools.*;
import de.iav.gestures.visualizer.*;
import de.iav.gestures.json.CustomPointable;
import com.omegabyte.dashboard.*;
import com.omegabyte.animations.*;
import processing.opengl.*;

Dashboard dash = new Dashboard(this);
Widget message = new Widget(this).setSize(200, 75).setPosition(200, 100);
Widget ball = new Widget(this).setSize(200, 0).setPosition(300, 300).setShape(Widget.Shape.sphere);
Widget map = new Widget(this)
.setSize(200, 100)
.setPosition(100, 100)
.setMaxSize(500)
.setScalable(true);
void setup() {
  size(800, 800, P3D);
  dash.add(map).add(message).add(ball);
  map.loadImage("map.jpg");
  recon.start();
}
float hudX = 0;
float hudY = 0;
int boundLeft = -150;
int boundRight = 150;
int boundDown = 50;
int boundUp = 450;
float constrainHeight = 190;
color handColor = 0;
boolean grab=false;
GestureRecognizer recon = new GestureRecognizer();
void draw() {
  background(255);
  dash.draw();  
  Frame frame = recon.frameRecorder.getUnconstrainedFrame();
  if (!frame.hands().isEmpty()) {
    Hand hand = frame.hands().get(0);
    hudX = map(constrain(hand.palmPosition().getX(), boundLeft, boundRight), boundLeft, boundRight, 0, width);
    hudY = map(constrain(hand.palmPosition().getY(), boundDown, boundUp), boundUp, boundDown, 0, height);

    float[] stab = stabilize(hudX, hudY);
    hudX = stab[0];
    hudY = stab[1];

    if (hand.fingers().count() > 3)
      handColor = #FF0000;
    else if (hand.fingers().count() < 2) {
      handColor = #00FF00;
      grab = true;
    }
    else { 
      handColor = 100;//noFill();
      stroke(255);
    }
    if (hand.palmPosition().getY() < constrainHeight) {
      handColor = #0000FF;
    }
    Frame lastFrame = recon.controller.frame(5);
    boolean scaling = (frame.scaleProbability(lastFrame)) > 0.4;
    boolean rotate = (frame.rotationProbability(lastFrame)) > 0.4;
    float scale = frame.scaleFactor(lastFrame);
    float rotation = (-frame.rotationAxis(lastFrame).getX()*frame.rotationAngle(lastFrame, frame.rotationAxis(lastFrame)));

    if (mousePressed) {
      grab = true;
      hudX = mouseX;
      hudY = mouseY;
    }
   //dash.update(hudX, hudY, grab, scaling, scale, rotate, rotation);
   dash.update(new PVector(mouseX,mouseY),mousePressed);
   
    pushMatrix();
    translate(hudX, hudY);
    this.hand.setHandColor(handColor);
    this.hand.render(frame);
    popMatrix();
  }
}

HandModel hand = new HandModel(this);
final int stabNum = 5;
float[] stabX = new float[stabNum+1];
float[] stabY = new float[stabNum+1];
float[] stabilize(float x, float y) {
  float[] ans = new float[2];
  for (int n = 0; n < stabNum-1; n++) {
    stabX[n] = stabX[n+1];
    stabY[n] = stabY[n+1];
  }
  stabX[stabNum-1] = x;
  stabY[stabNum-1] = y;
  stabX[stabNum] = 0;
  stabY[stabNum] = 0;
  for (int n = 0; n < stabNum; n++) {
    if (x == stabNum-1 && stabX[stabNum] == 0 && stabY[stabNum] == 0) {
      for (int m = 0; m < stabNum; m++) {
        stabX[m] = x;
        stabY[m] = y;
      }
      ans[0] = x;
      ans[1] = y; 
      return ans;
    }
    stabX[stabNum] += stabX[n];
    stabY[stabNum] += stabY[n];
  }
  ans[0] = stabX[stabNum]/stabNum;
  ans[1] = stabY[stabNum]/stabNum;
  return ans;
}
