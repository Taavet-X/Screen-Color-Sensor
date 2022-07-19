# Screen-Color-Sensor

How to use:

<p>
1. Implement the interface ScreenColorSensorListener and override the method.<br>
<code>
  public class myClass implements ScreenColorSensorListener { 
</code>
<br>
<code>
@Override
</code>
<br>
<code>
  public void onSensorEvent(Color color) { ... 
  }
</code>
<br>
<code>
}
</code>
<br>
<br>
2. Create a new Object of type ScreenColorListener, and add your class as listener.
<br>
<code>
ScreenColorListener scl = new ScreenColorListener("S0");
</code>
<br>
<code>
scl.addSensorListener(this);
</code>
<br>
<code>
scl.setVisible(true);
</code>
<br>
<br>
3. Once the sensor registers an event, the method onSensorEvent wil be called, and the new color will be recieved as parameters.
