import com.omegaohm.dashboard.*;

  Widget button = new Widget(this).setPosition(100,100).setSize(20,50).setMovable(true).setTitle("widget");
void setup() {
  size(500, 500);
  dash.add(button);
  dash.add(new Button(this).setPosition(200,100).setSize(50,50).setTitle("button"));
  dash.add(new Slider(this).setPosition(300,100).setSize(50,50).setTitle("slider"));
}

Widget shape = new Widget(this)
.setPosition(240, 240)
.setSize(140, 140)
.setMovable(true)
.setTitle("shape");

MouseHandler mouse = new MouseHandler(this);
Dashboard dash = new Dashboard(this);
void draw() {
  background(100);
  dash.update(mouseX,mouseY,mousePressed);
  dash.draw();
}

void keyPressed(){
  println();
}
