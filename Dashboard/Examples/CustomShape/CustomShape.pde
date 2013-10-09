
import com.omegaohm.dashboard.*;
PShape bot;

void setup() {
  size(640, 360);
  // The file "bot1.svg" must be in the data folder
  // of the current sketch to load successfully
  //bot = loadShape("bot1.svg");


  shape.loadShape("bot1.svg").setScalable(50, 200);
  shape.resize(0.5);
  
  dash.setBackground(background)
  .add(button)
  .add(shape)
  .add(new Widget(this).setPosition(180,20).setSize(80,150).setFixed(true))
  .add(new Slider(this).setPosition(200,50).setSize(10,50))
  .add(new Slider(this).setPosition(180,50).setSize(10,50))
  .add(new Slider(this).setPosition(160,50).setSize(10,50))
  .add(new Slider(this).setPosition(180,0).setSize(50,10))
  .add(new Slider(this).setPosition(180,-20).setSize(50,10))
  .add(new Slider(this).setPosition(180,-40).setSize(50,10))
  .nudge(new PVector(300, 200))
  .setMovable(true).lock();
} 
Widget background = new Widget(this)
.setPosition(0, 0)
.setSize(500, 200)
.setTitle("background");
Slider slider = new Slider(this);

Widget shape = new Widget(this)
.setPosition(10, 10)
.setSize(140, 140)
.setMovable(true)
.setTitle("shape");

Button button = new Button(this)
.setPosition(-200, -50)
.setSize(50, 50)
.setColors(0xFFFF0000,0xFF0000FF,0xFF00FF00)
.setText("button");

Dashboard dash = new Dashboard(this);
void draw() {
  background(102);
  float zoom = map(mouseX, 0, width, .1, 1.5);
  button.resize(zoom);
  dash.draw();

  dash.handle(mouseX,mouseY,mousePressed);//  dash.handle();
}

