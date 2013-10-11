import com.omegabyte.dashboard.*;
import com.omegabyte.animations.*;

import com.leapmotion.leap.*;
import de.iav.gestures.tools.*;
import de.iav.gestures.visualizer.*;
import de.iav.gestures.json.CustomPointable;
import com.omegabyte.dashboard.*;
import com.omegabyte.animations.*;
import processing.opengl.*;

PImage mirror;
void setup() {
  size(500, 400, P3D);
  noStroke();
  recon.start();
  ball.setResolution(200);
  ball.loadImage("car4.jpg");
  ball.setShape(Widget.Shape.sphere);
  ball2.setResolution(200);
  ball2.loadImage("mirror.png");
  ball2.setShape(Widget.Shape.sphere);
  
  ball.setCenter(new PVector(200,200));
  ball2.setCenter(new PVector(200,200));
  
  dash.add(ball);
  dash.add(ball2);
}

Sphere ball = new Sphere(this, "ball").setSize(100, 0).setPosition(200, 200).setMovable(true).setRotatable(false).setShape(Widget.Shape.sphere);
Sphere ball2 = new Sphere(this, "ball2").setSize(300, 0).setPosition(200, 200).setMovable(false).setRotatable(true);
Widget test = new Widget(this, "test").setPosition(200, 200).setColor(0xFFFFFF00).setSize(200).setShape(Widget.Shape.circle);
Dashboard dash = new Dashboard(this);//.add(test).add(ball2);//
//Dashboard dash = new Dashboard(this).add(ball).add(test);
HandModel hand = new HandModel(this);
GestureRecognizer recon = new GestureRecognizer();

void keyPressed(){
 if (key == ' ')
  ball.setHidden(!ball.isHidden());
 if (key == 'v')
  ball2.setHidden(!ball2.isHidden());
 if (key == 'b')
  ball2.setRotationZ(ball2.rotationZ+QUARTER_PI/4);
 
}
void mouseDragged(){
  ball2.setRotationY(mouseX*TWO_PI/width);
  ball2.setRotationX(mouseY*TWO_PI/height); 
}
void draw() {
  background(100);
  ellipse(200,200,10,10);
  //lights();
  //  dash.draw();
  // dash.update();
  handleDash();
  fill(0);
  ellipse(200,200,10,10);
}

int boundLeft = -150;
int boundRight = 150;
int boundDown = 50;
int boundUp = 450;
float hudX = 0;
float hudY = 0;

void handleDash() {
  hint(DISABLE_DEPTH_TEST);
  sphereDetail(50);
  dash.draw();
  boolean grab= false;
  int handColor = 0;
  boolean scaling = false;// = (frame.scaleProbability(lastFrame)) > 0.4;
  boolean rotate = false;// = (frame.rotationProbability(lastFrame)) > 0.4;
  float scale = 0;// = frame.scaleFactor(lastFrame);
  float rotation = 0;//

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
    Frame lastFrame = recon.controller.frame(5);
    scaling = (frame.scaleProbability(lastFrame)) > 0.4;
    rotate = (frame.rotationProbability(lastFrame)) > 0.4;
    scale = frame.scaleFactor(lastFrame);
    rotation = (-frame.rotationAxis(lastFrame).getX()*frame.rotationAngle(lastFrame, frame.rotationAxis(lastFrame)));
  }
  if (mousePressed) {
    grab = true;
    hudX = mouseX;
    hudY = mouseY;
  }
  dash.update(hudX, hudY, false, scaling, scale, rotate, rotation);


  pushMatrix();
  translate(hudX, hudY);
  this.hand.setHandColor(handColor);
  this.hand.render(frame);
  popMatrix();
}

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

