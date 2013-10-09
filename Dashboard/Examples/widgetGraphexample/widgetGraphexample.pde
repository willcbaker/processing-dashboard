import com.omegaohm.animations.*;
import com.omegaohm.dashboard.*;

void setup(){
  size(500,500);
  dash.add(graph);
  dash.add(button);
}
Dashboard dash = new Dashboard(this);
Graph graph = new Graph(this,"MouseX").setSize(300,150).setPosition(200,200).setColor(0xFFFFFF00);
Button button = new Button(this,"Toggle").setSize(50,50).setPosition(100,100);
void draw(){
  background(125);
  graph.update(float(mouseX)/width,Graph.Mode.AUTO);
  dash.update(new PVector(mouseX,mouseY), mousePressed);
  dash.draw();

  
}
