
import com.omegaohm.dashboard.*;
import com.omegaohm.animations.*;
PShape bot;


Widget shape = new Widget(this)
.setPosition(200, 200)
.setSize(240, 240)
.setMovable(true)
.setTitle("shape");

Widget background = new Widget(this)
.setPosition(0, 0)
.setSize(500, 200)
.setTitle("background");
Slider slider = new Slider(this);

Button button = new Button(this)
.setPosition(10, 10)
.setSize(50, 50)
.setColors(0xFFFF0000, 0xFF0000FF, 0xFF00FF00)
.setText("button");

Button toggle = new Button(this).setPosition(100, 100).setSize(100, 100).setTitle("Toggle");

Widget sliders = new Widget(this).setPosition(300, 0).setSize(80, 140).setFixed(true).setBackground(true);

Dashboard menu2 = new Dashboard(this)
//.setBackground(new Widget(this).setPosition(-5, 10).setSize(90, 160).setFixed(true))
.add(new Slider(this).setPosition(25, 120).setSize(11, 50))
.add(new Slider(this).setPosition(45, 120).setSize(11, 50))
.add(new Slider(this).setPosition(65, 120).setSize(11, 50))
.add(new Slider(this).setPosition(10, 40).setSize(50, 11))
.add(new Slider(this).setPosition(10, 60).setSize(50, 11))
.add(new Slider(this).setPosition(10, 80).setSize(50, 11))
.move(new PVector(300, 0))
;


Dashboard menu = new Dashboard(this)
.setBackground(background)
.add(button)
.add(sliders)
.nudge(new PVector(-100, 100))
//.lock()
.setMovable(true);

Dashboard dash = new Dashboard(this);
Animation shake = new Animation(menu,100);


void setup() {
  size(640, 360);
  // The file "bot1.svg" must be in the data folder
  // of the current sketch to load successfully
  //bot = loadShape("bot1.svg");


  shape.loadShape("bot1.svg").setScalable(50, 200);
  shape.resize(0.5);
  shape.addMenu(menu);
  shape.addMenu(menu2);
  println(menu2.toString());

  dash.add(shape);
  dash.add(toggle);
  shake.start();
  shake.setShaker(100,2);
  shake.trigger();
  println(dash.toString());
  println(menu.toString());
  for (Widget widget : dash.getWidgets())
    println(widget.toString());
  for (Widget widget : menu.getWidgets())
    println(widget.toString());
} 

void draw() {
  background(102);
  //  float zoom = map(mouseX, 0, width, .1, 1.5);
  //  button.resize(zoom);
  dash.draw();
  dash.update(mouseX, mouseY, mousePressed);//  dash.handle();
  if (button.isClicked()){
    println("Clicked!");
    shake.trigger();
  }
  menu.setMovable(toggle.isOn());
}
void keyPressed() {
  for (Widget widget : dash.getWidgets())
    for (Dashboard menu : widget.getMenus())
      println(menu.toString());
}

