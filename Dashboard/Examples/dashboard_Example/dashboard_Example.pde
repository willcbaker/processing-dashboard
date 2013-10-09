import com.omegabyte.animations.*;
import com.omegabyte.dashboard.*;

Dashboard dash = new Dashboard(this);
Widget message = new Widget(this).setSize(200, 75).setPosition(200, 100);
Widget ball = new Widget(this).setSize(200, 0).setPosition(300, 300).setShape(Widget.Shape.sphere).setAlpha(255);
Widget map = new Widget(this)
.setSize(200, 100)
.setPosition(100,100)
.setMaxSize(500)
.setScalable(true);
void setup() {
  size(800, 800,P3D);
  dash.add(map).add(message);
  dash.add(ball);
  map.loadImage("map.jpg");
}

void draw() {
  lights();
  background(255);
  dash.draw();
  dash.update(new PVector(mouseX, mouseY), mousePressed);
}

