
import com.omegaohm.dashboard.*;
PShape bot;

void setup() {
  size(640, 360);
  // The file "bot1.svg" must be in the data folder
  // of the current sketch to load successfully
  //bot = loadShape("bot1.svg");


  shape.loadShape("bot1.svg").setScalable(50, 200);
  shape.resize(0.5);
  shape.addMenu(menu);

  dash.add(shape);
  dash.add(toggle);
  println(dash.toString());
  println(menu.toString());
  for (Widget widget : dash.getWidgets())
    println(widget.toString());
} 
Widget background = new Widget(this)
.setPosition(0, 0)
.setSize(500, 200)
.setTitle("background");
Slider slider = new Slider(this);

Widget shape = new Widget(this)
.setPosition(0, 0)
.setSize(240, 240)
.setMovable(true)
.setTitle("shape");

Button button = new Button(this)
.setPosition(10, 10)
.setSize(50, 50)
.setColors(0xFFFF0000, 0xFF0000FF, 0xFF00FF00)
.setText("button");

Dashboard menu = new Dashboard(this).setBackground(background)
.add(button)
.add(new Widget(this).setPosition(390, 40).setSize(80, 140).setFixed(true))
.add(new Slider(this).setPosition(445, 100).setSize(11, 50))
.add(new Slider(this).setPosition(435, 100).setSize(11, 50))
.add(new Slider(this).setPosition(405, 100).setSize(11, 50))
.add(new Slider(this).setPosition(420, 20).setSize(50, 11))
.add(new Slider(this).setPosition(420, 40).setSize(50, 11))
.add(new Slider(this).setPosition(420, 60).setSize(50, 11))
.nudge(new PVector(-100, 100))
//.lock()
.setMovable(true);

Button toggle = new Button(this).setPosition(100, 100).setSize(100, 100).setTitle("Toggle");

Dashboard dash = new Dashboard(this);
void draw() {
  background(102);
  //  float zoom = map(mouseX, 0, width, .1, 1.5);
  //  button.resize(zoom);
  dash.draw();

  dash.update(mouseX, mouseY, mousePressed);//  dash.handle();
  if (toggle.isClicked())
    println("Clicked!");
  menu.setMovable(toggle.isOn());
}
void keyPressed() {
  for (Widget widget : dash.getWidgets())
    for (Dashboard menu : widget.getMenus())
        println(menu.toString());
}

